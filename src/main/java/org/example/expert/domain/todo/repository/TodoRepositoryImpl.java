package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Todo> findAllWithParamsDsl(
            String weather,
            LocalDateTime updatedAfter,
            LocalDateTime updatedBefore,
            Pageable pageable) {

        QTodo todo = QTodo.todo;

        JPAQuery<Todo> results = jpaQueryFactory
                .selectFrom(todo)
                .leftJoin(todo.user)
                .fetchJoin()
                .where(
                        weatherCondition(weather),
                        updatedAfterCondition(updatedAfter),
                        updatedBeforeCondition(updatedBefore)
                )
                .orderBy(todo.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        Long count = jpaQueryFactory
                .select(todo.count())
                .from(todo)
                .where(
                        weatherCondition(weather),
                        updatedAfterCondition(updatedAfter),
                        updatedBeforeCondition(updatedBefore)
                )
                .fetchOne();

        return new PageImpl<>(results.fetch(),pageable, Objects.isNull(count) ? 0 : count);
    }

    @Override
    public Page<TodoSearchResponse> searchTodosWithParams(
            String search,
            String searchBy,
            LocalDateTime updatedAfter,
            LocalDateTime updatedBefore,
            Pageable pageable) {

        QTodo todo = QTodo.todo;
        QManager manager = QManager.manager;
        QComment comment = QComment.comment;

        JPAQuery<TodoSearchResponse> query = jpaQueryFactory
                .select(Projections.constructor(
                        TodoSearchResponse.class,
                        todo.title,
                        manager.countDistinct().as("managerCount"),
                        comment.countDistinct().as("commentCount")
                ))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .groupBy(todo)
                .where(
                        searchCondition(search,searchBy),
                        updatedAfterCondition(updatedAfter),
                        updatedBeforeCondition(updatedBefore)
                );

        Long count = jpaQueryFactory
                .select(todo.count())
                .from(todo)
                .where(
                        searchCondition(search,searchBy),
                        updatedAfterCondition(updatedAfter),
                        updatedBeforeCondition(updatedBefore)
                ).fetchOne();

        List<TodoSearchResponse> results = query.fetch();

        return new PageImpl<>(results,pageable,Objects.isNull(count) ? 0 : count);
    }




    private BooleanExpression weatherCondition(String weather) {
        return Objects.isNull(weather) ? null : QTodo.todo.weather.eq(weather);
    }

    private BooleanExpression searchCondition(String search, String searchBy) {
        if (Objects.isNull(search)) {
            return null;
        }
        return switch (searchBy) {
            case "title" -> QTodo.todo.title.contains(search);
            case "nickname" -> QTodo.todo.user.nickname.contains(search);
            default -> QTodo.todo.title.contains(search).and(
                    QTodo.todo.user.nickname.contains(search)
            );
        };
    }

    private BooleanExpression updatedAfterCondition(LocalDateTime updatedAfter) {
        return Objects.isNull(updatedAfter) ? null : QTodo.todo.modifiedAt.goe(updatedAfter);
    }

    private BooleanExpression updatedBeforeCondition(LocalDateTime updatedBefore) {
        return Objects.isNull(updatedBefore) ? null : QTodo.todo.modifiedAt.loe(updatedBefore);
    }

}
