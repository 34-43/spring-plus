package org.example.expert.domain.comment.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor
import org.example.expert.domain.common.entity.Timestamped
import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.user.entity.User

@Entity
@NoArgsConstructor
@Table(name = "comments")
class Comment(
    contents: String,
    user: User,
    todo: Todo
) : Timestamped() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Column(nullable = false)
    private lateinit var contents: String

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private lateinit var user : User

    @JoinColumn(name = "todo_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private lateinit var todo : Todo

    fun getId() = this.id
    fun getContents() = this.contents
    fun getUser() = this.user
}
