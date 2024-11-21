package org.example.expert.domain.user.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor
import org.example.expert.domain.common.dto.AuthUser
import org.example.expert.domain.common.entity.Timestamped
import org.example.expert.domain.user.enums.UserRole

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
class User : Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null

    @Column(unique = true)
    private lateinit var email: String

    @Column
    private lateinit var password: String

    @Column
    private lateinit var nickname: String

    @Enumerated(EnumType.STRING)
    private lateinit var userRole: UserRole

    constructor(email: String, password: String, nickname: String, userRole: UserRole) {
        this.email = email
        this.password = password
        this.nickname = nickname
        this.userRole = userRole
    }

    private constructor(id: Long, email: String, nickname: String, userRole: UserRole) {
        this.id = id
        this.email = email
        this.nickname = nickname
        this.userRole = userRole
    }

    // getter
    fun getId() = this.id
    fun getEmail() = this.email
    fun getNickname() = this.nickname
    fun getPassword() = this.password
    fun getUserRole() = this.userRole

    fun changePassword(password: String) {
        this.password = password
    }

    fun updateRole(userRole: UserRole) {
        this.userRole = userRole
    }

    companion object {
        @JvmStatic
        fun fromAuthUser(authUser: AuthUser): User {
            return User(authUser.id, authUser.email, authUser.nickname, authUser.userRole)
        }
    }
}
