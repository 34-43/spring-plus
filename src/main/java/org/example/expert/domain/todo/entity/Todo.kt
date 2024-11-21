package org.example.expert.domain.todo.entity

import jakarta.persistence.*
import lombok.NoArgsConstructor
import org.example.expert.domain.comment.entity.Comment
import org.example.expert.domain.common.entity.Timestamped
import org.example.expert.domain.manager.entity.Manager
import org.example.expert.domain.user.entity.User

@Entity
@Table(name = "todos")
@NoArgsConstructor
class Todo() : Timestamped() {
    constructor(title: String, contents: String, weather: String, user: User) : this() {
        this.title = title
        this.contents = contents
        this.weather = weather
        this.user = user
        this.appendManager()
    }

    private final fun appendManager() {
        this.managers.add(Manager(this.user, this))
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id : Long = 0L

    @Column(nullable = false)
    private lateinit var title : String

    @Column(nullable = false)
    private lateinit var contents : String

    @Column(nullable = false)
    private lateinit var weather : String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private lateinit var user : User

    @OneToMany(mappedBy = "todo", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    private var comments : MutableList<Comment> = mutableListOf()

    @OneToMany(mappedBy = "todo", cascade = [CascadeType.ALL], orphanRemoval = true)
    private var managers : MutableList<Manager> = mutableListOf()

    // getter
    fun getId() = this.id
    fun getTitle() = this.title
    fun getContents() = this.contents
    fun getWeather() = this.weather
    fun getUser() = this.user
}