package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

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

    private BooleanExpression weatherCondition(String weather) {
        return Objects.isNull(weather) ? null : QTodo.todo.weather.eq(weather);
    }

    private BooleanExpression updatedAfterCondition(LocalDateTime updatedAfter) {
        return Objects.isNull(updatedAfter) ? null : QTodo.todo.modifiedAt.goe(updatedAfter);
    }

    private BooleanExpression updatedBeforeCondition(LocalDateTime updatedBefore) {
        return Objects.isNull(updatedBefore) ? null : QTodo.todo.modifiedAt.loe(updatedBefore);
    }

}
