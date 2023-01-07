package com.wafflestudio.msns.domain.user.dto

import com.wafflestudio.msns.domain.user.model.User
import java.time.LocalDate
import java.util.UUID

class UserResponse {
    data class SimpleUserInfo(
        val id: Long,
        val email: String,
        val username: String,
        val phoneNumber: String,
    ) {
        constructor(user: User) : this(
            id = user.id,
            email = user.email,
            username = user.username,
            phoneNumber = user.phoneNumber,
        )
    }

    data class DetailResponse(
        val id: Long,
        val email: String,
        val username: String,
        val phoneNumber: String,
        val name: String,
        val birthDate: LocalDate
    )

    data class MyProfileResponse(
        val id: Long,
        val username: String,
        val name: String,
        val profileImageUrl: String?,
        val introduction: String?,
        val countPosts: Long,
        val countFollowers: Long,
        val countFollowings: Long
    )

    data class ProfileResponse(
        val id: Long,
        val username: String,
        val name: String,
        val profileImageUrl: String?,
        val introduction: String?,
        val countPosts: Long,
        val countFollowers: Long,
        val countFollowings: Long,
        val isFollowing: Boolean
    )

    data class PreviewResponse(
        val id: Long,
        val username: String,
        val profileImageUrl: String?
    ) {
        constructor(user: User) : this(
            id = user.id,
            username = user.username,
            profileImageUrl = user.profileImageUrl
        )
    }

    data class PostAPIDto(
        val id: UUID,
        val username: String
    )

    data class LikeResponse(
        val id: Long,
        val username: String,
        val profileImageUrl: String?
    ) {
        constructor(user: User) : this(
            id = user.id,
            username = user.username,
            profileImageUrl = user.profileImageUrl
        )
    }

    data class FollowListResponse(
        val id: Long,
        val username: String,
        val profileImageUrl: String?
    )
}
