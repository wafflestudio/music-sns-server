package com.wafflestudio.msns.domain.post.model

import com.wafflestudio.msns.domain.model.BaseTimeEntity
import com.wafflestudio.msns.domain.playlist.model.Playlist
import com.wafflestudio.msns.domain.post.dto.PostResponse
import com.wafflestudio.msns.domain.user.model.Like
import com.wafflestudio.msns.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.ColumnResult
import javax.persistence.ConstructorResult
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.NamedNativeQueries
import javax.persistence.NamedNativeQuery
import javax.persistence.OneToMany
import javax.persistence.SqlResultSetMapping
import javax.persistence.SqlResultSetMappings

@Entity
@Table(name = "post")
@NamedNativeQueries(
    NamedNativeQuery(
        name = "Post.findAllWithUserId",
        query = "SELECT p.id AS post_id, p.title AS title, p.content AS content, p.created_at AS created_at, " +
            "pl.playlist_id AS playlist_id, pl.thumbnail AS thumbnail, " +
            "COUNT(l.id) AS likes " +
            "FROM post p " +
            "INNER JOIN playlist pl ON p.playlist_id = pl.id " +
            "LEFT OUTER JOIN likes l ON p.id = l.post_id " +
            "WHERE p.user_id = :userId " +
            "GROUP BY p.id, p.title, p.content, p.created_at, pl.id, pl.thumbnail",
        resultSetMapping = "PreviewResponse"
    ),
    NamedNativeQuery(
        name = "Post.findAllWithMyId",
        query = "SELECT p.id AS id, p.title AS title, p.created_at AS created_at, " +
            "pl.thumbnail AS thumbnail " +
            "FROM post p " +
            "INNER JOIN playlist pl ON p.playlist_id = pl.id " +
            "WHERE p.user_id = :myId",
        resultSetMapping = "UserPageResponse"
    ),
    NamedNativeQuery(
        name = "Post.findFeed",
        query = "SELECT p.id AS id, p.title AS title, p.content AS content, p.created_at AS created_at, " +
            "u.username AS username, u.profileImageUrl AS profileImageUrl," +
            "CONCAT(LPAD(DATE_FORMAT(p.created_at, '%Y%m%d%H%i%s'), 20, '0'), LPAD()) " +
            "FROM post p " +
            "INNER JOIN user u ON p.user_id = u.id " +
            "WHERE p.user_id = :myId",
        resultSetMapping = "UserPageResponse"
    )
)
@SqlResultSetMappings(
    SqlResultSetMapping(
        name = "PreviewResponse",
        classes = [
            ConstructorResult(
                targetClass = PostResponse.PreviewResponse::class,
                columns = arrayOf(
                    ColumnResult(name = "post_id", type = Long::class),
                    ColumnResult(name = "title", type = String::class),
                    ColumnResult(name = "content", type = String::class),
                    ColumnResult(name = "created_at", type = LocalDateTime::class),
                    ColumnResult(name = "playlist_id", type = UUID::class),
                    ColumnResult(name = "thumbnail", type = String::class),
                    ColumnResult(name = "likes", type = Int::class)
                )
            )
        ]
    ),
    SqlResultSetMapping(
        name = "UserPageResponse",
        classes = [
            ConstructorResult(
                targetClass = PostResponse.UserPageResponse::class,
                columns = arrayOf(
                    ColumnResult(name = "id", type = Long::class),
                    ColumnResult(name = "title", type = String::class),
                    ColumnResult(name = "created_at", type = LocalDateTime::class),
                    ColumnResult(name = "thumbnail", type = String::class),
                )
            )
        ]
    )
)
class Post(
    @ManyToOne(fetch = FetchType.LAZY, cascade = [])
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User,

    var title: String,

    var content: String,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [])
    @JoinColumn(name = "playlist_id", referencedColumnName = "id")
    val playlist: Playlist,

    @OneToMany(mappedBy = "post")
    val likes: MutableList<Like> = mutableListOf(),

) : BaseTimeEntity()
