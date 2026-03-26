/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.csx370.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uga.menik.csx370.models.Post;
import uga.menik.csx370.models.User;


/**
 * This service contains people related functions.
 */
@Service
public class HomeService {

    // dataSource enables talking to the database.
    private final DataSource dataSource;
    

    @Autowired
    public HomeService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Post> getFollowingPosts(String userIdToSearch) throws SQLException {
        final String sql = """
                select
                    p.postId,
                    p.userId,
                    p.content,
                    p.createdDate,
                    count(distinct c.commentId) as numComments,
                    count(distinct l.userId) as numLikes,
                    exists(select 1 from likes l2 where l2.userId = ? and l2.postId = p.postId) as userLiked,
                    exists(select 1 from bookmarks b2 where b2.userId = ? and b2.postId = p.postId) as userBookmarked,
                    u.firstName,
                    u.lastName
                from posts p
                join user u on u.userId = p.userId
                left join comments c on c.postId = p.postId
                left join likes l on l.postId = p.postId
                where
                    p.userId = ?
                    or p.userId in (
                        select f.followingId
                        from follows f
                        where f.followerId = ?
                    )
                group by p.postId, p.userId, p.content, p.createdDate, u.firstName, u.lastName
                order by p.createdDate desc
                """;
        List<Post> posts = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following line replaces the first place holder with username.
            int uid = Integer.parseInt(userIdToSearch);
            pstmt.setInt(1, uid);
            pstmt.setInt(2, uid);
            pstmt.setInt(3, uid);
            pstmt.setInt(4, uid);


            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                // Note: This specific while loop will only run at most once 
                // since username is unique.
                while (rs.next()) {

                    String userId = rs.getString("userId");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    String postId = rs.getString("postId");
                    String content = rs.getString("content");
                    String createdDate = rs.getString("createdDate");
                    int numComments = rs.getInt("numComments");
                    int numLikes = rs.getInt("numLikes");
                    boolean userLiked = rs.getBoolean("userLiked");
                    boolean userBookmarked = rs.getBoolean("userBookmarked");
                    
                    posts.add(new Post(postId, 
                        content, 
                        createdDate, 
                        new User(userId, firstName, lastName),
                        numLikes,
                        numComments, 
                        userLiked,
                        userBookmarked));
                    // add posts to be returned
                }
            }
        }
        return posts;
    }



    

}
