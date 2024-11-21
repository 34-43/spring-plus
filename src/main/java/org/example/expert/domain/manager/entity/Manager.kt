package org.example.expert.domain.manager.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor
import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.user.entity.User

@Entity
@NoArgsConstructor
@Table(name = "managers")
class Manager(user: User, todo: Todo) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private lateinit var user: User

    @JoinColumn(name = "todo_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private lateinit var todo: Todo

    // getter
    fun getId() = this.id
    fun getUser() = this.user
    fun getTodo() = this.todo
}
