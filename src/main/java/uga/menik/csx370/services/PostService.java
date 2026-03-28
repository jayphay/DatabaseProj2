/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.csx370.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uga.menik.csx370.models.Post;
import uga.menik.csx370.models.User;

@Service
public class PostService {
    private final DataSource dataSource;

    @Autowired
    public PostService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createPost(String userId, String content) throws SQLException {
        final String sql = "insert into posts (userId, content) values (?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(userId));
            pstmt.setString(2, content);
            pstmt.executeUpdate();
        }
    }

    public List<Post> getBookmarkedPosts(String userId) throws SQLException {
        final String sql = """
            select
                p.postId, 
                p.content, 
                p.createdDate,
                u.userId, 
                u.firstName, 
                u.lastName,
                (select count(*) from comments c where c.postId = p.postId) as numComments,
                (select count(*) from likes l where l.postId = p.postId) as numLikes,
                exists(select 1 from likes l where l.userId = ? and l.postId = p.postId) as viewerLiked,
                true as viewerBookmarked -- Since we are in the Bookmarks tab, these are all true
            from posts p
            join bookmarks b on p.postId = b.postId
            join user u on u.userId = p.userId
            where b.userId = ?
            order by p.createdDate desc
            """;

        List<Post> posts = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int uId = Integer.parseInt(userId);
            pstmt.setInt(1, uId);
            pstmt.setInt(2, uId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(new Post(
                            rs.getString("postId"),
                            rs.getString("content"),
                            rs.getString("createdDate"),
                            new User(rs.getString("userId"), rs.getString("firstName"), rs.getString("lastName")),
                            rs.getInt("numLikes"),
                            rs.getInt("numComments"),
                            rs.getBoolean("viewerLiked"),
                            rs.getBoolean("viewerBookmarked")
                    ));
                }
            }
        }
        return posts;
    }

    public List<Post> getPostsByUser(String userIdToView, String viewerUserId) throws SQLException {
        final String sql = """
                select
                    p.postId,
                    p.content,
                    p.createdDate,
                    u.userId,
                    u.firstName,
                    u.lastName,
                    (select count(*) from comments c where c.postId = p.postId) as numComments,
                    (select count(*) from likes l where l.postId = p.postId) as numLikes,
                    exists(select 1 from likes l where l.userId = ? and l.postId = p.postId) as viewerLiked,
                    exists(select 1 from bookmarks b where b.userId = ? and b.postId = p.postId) as viewerBookmarked
                from posts p
                join user u on u.userId = p.userId
                where p.userId = ?
                order by p.createdDate desc
                """;

        List<Post> posts = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int viewerId = Integer.parseInt(viewerUserId);
            pstmt.setInt(1, viewerId);
            pstmt.setInt(2, viewerId);
            pstmt.setInt(3, Integer.parseInt(userIdToView));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String postId = rs.getString("postId");
                    String content = rs.getString("content");
                    String createdDate = rs.getString("createdDate");

                    String postUserId = rs.getString("userId");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");

                    int numComments = rs.getInt("numComments");
                    int numLikes = rs.getInt("numLikes");
                    boolean viewerLiked = rs.getBoolean("viewerLiked");
                    boolean viewerBookmarked = rs.getBoolean("viewerBookmarked");

                    posts.add(new Post(
                            postId,
                            content,
                            createdDate,
                            new User(postUserId, firstName, lastName),
                            numLikes,
                            numComments,
                            viewerLiked,
                            viewerBookmarked));
                }
            }
        }
        return posts;
    }

    public List<Post> searchPostsByHashtags(String hashtagsQuery, String viewerUserId) throws SQLException {
        List<String> tags = parseTags(hashtagsQuery);
        if (tags.isEmpty()) {
            return List.of();
        }

        StringBuilder where = new StringBuilder();
        for (int i = 0; i < tags.size(); i++) {
            if (i > 0) {
                where.append(" and ");
            }
            where.append("p.content like ?");
        }

        final String sql = """
                select
                    p.postId,
                    p.content,
                    p.createdDate,
                    u.userId,
                    u.firstName,
                    u.lastName,
                    (select count(*) from comments c where c.postId = p.postId) as numComments,
                    (select count(*) from likes l where l.postId = p.postId) as numLikes,
                    exists(select 1 from likes l where l.userId = ? and l.postId = p.postId) as viewerLiked,
                    exists(select 1 from bookmarks b where b.userId = ? and b.postId = p.postId) as viewerBookmarked
                from posts p
                join user u on u.userId = p.userId
                where
                """ + where + """
                order by p.createdDate desc
                """;

        List<Post> posts = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int viewerId = Integer.parseInt(viewerUserId);
            pstmt.setInt(1, viewerId);
            pstmt.setInt(2, viewerId);

            int paramIdx = 3;
            for (String tag : tags) {
                pstmt.setString(paramIdx++, "%" + tag + "%");
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String postId = rs.getString("postId");
                    String content = rs.getString("content");
                    String createdDate = rs.getString("createdDate");

                    String postUserId = rs.getString("userId");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");

                    int numComments = rs.getInt("numComments");
                    int numLikes = rs.getInt("numLikes");
                    boolean viewerLiked = rs.getBoolean("viewerLiked");
                    boolean viewerBookmarked = rs.getBoolean("viewerBookmarked");

                    posts.add(new Post(
                            postId,
                            content,
                            createdDate,
                            new User(postUserId, firstName, lastName),
                            numLikes,
                            numComments,
                            viewerLiked,
                            viewerBookmarked));
                }
            }
        }
        return posts;
    }

    private static List<String> parseTags(String raw) {
        if (raw == null) {
            return List.of();
        }
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            return List.of();
        }

        String[] parts = trimmed.split("\\s+");
        List<String> tags = new ArrayList<>();
        for (String p : parts) {
            String tag = p.trim();
            if (tag.isEmpty()) {
                continue;
            }
            if (!tag.startsWith("#")) {
                tag = "#" + tag;
            }
            tags.add(tag);
        }
        return tags;
    }
}

