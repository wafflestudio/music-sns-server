package com.wafflestudio.msns.domain.user.service

import com.wafflestudio.msns.domain.playlist.repository.PlaylistRepository
import com.wafflestudio.msns.domain.post.dto.PostResponse
import com.wafflestudio.msns.domain.post.repository.PostRepository
import com.wafflestudio.msns.domain.user.dto.UserRequest
import com.wafflestudio.msns.domain.user.dto.UserResponse
import com.wafflestudio.msns.domain.user.exception.UserNotFoundException
import com.wafflestudio.msns.domain.user.model.User
import com.wafflestudio.msns.domain.user.repository.FollowRepository
import com.wafflestudio.msns.domain.user.repository.LikeRepository
import com.wafflestudio.msns.domain.user.repository.UserRepository
import com.wafflestudio.msns.global.auth.exception.ForbiddenUsernameException
import com.wafflestudio.msns.global.auth.repository.VerificationTokenRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val verificationTokenRepository: VerificationTokenRepository,
    private val followRepository: FollowRepository,
    private val likeRepository: LikeRepository,
    private val playlistRepository: PlaylistRepository
) {
    fun getMyself(user: User): UserResponse.DetailResponse = UserResponse.DetailResponse(user)

    fun getMyProfile(userId: Long): UserResponse.ProfileResponse =
        userRepository.findByIdOrNull(userId)
            ?.let { user -> UserResponse.ProfileResponse(user) }
            ?: throw UserNotFoundException("user is not found with the userId.")

    fun putMyProfile(user: User, putRequest: UserRequest.PutProfile): UserResponse.ProfileResponse =
        user.apply {
            this.username = putRequest.username.also {
                if ((userRepository.findByUsername(it)?.id ?: user.id) != user.id)
                    throw ForbiddenUsernameException("There is another user using this name.")
            }
            this.profileImageUrl = putRequest.profileImageUrl
            this.introduction = putRequest.introduction
        }
            .let { userRepository.save(it) }
            .let { UserResponse.ProfileResponse(it) }

    fun getMyPosts(pageable: Pageable, myId: Long): Page<PostResponse.UserPageResponse> =
        postRepository.findAllWithMyId(pageable, myId)

    fun withdrawUser(user: User) =
        run {
            followRepository.deleteFollowsByFromUser_Id(user.id)
            followRepository.deleteFollowsByToUser_Id(user.id)
            likeRepository.deleteMappingByUserId(user.id)
            likeRepository.deleteMappingByUserIdOfPost(user.id)

            postRepository.deleteAllUserPosts(user.id)
            playlistRepository.deleteMappingByUserId(user.id)
            verificationTokenRepository.deleteVerificationTokenByEmailOrPhoneNumber(user.email, user.phoneNumber)

            userRepository.deleteUserById(user.id)
        }
}
