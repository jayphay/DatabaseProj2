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

import uga.menik.csx370.models.FollowableUser;


/**
 * This service contains people related functions.
 */
@Service
public class PeopleService {

    // dataSource enables talking to the database.
    private final DataSource dataSource;
    

    @Autowired
    public PeopleService(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /**
     * This function should query and return all users that 
     * are followable. The list should not contain the user 
     * with id userIdToExclude.
     */
    public List<FollowableUser> getFollowableUsers(String userIdToExclude) throws SQLException{
        final String sql = """
                select
                    u.userId,
                    u.firstName,
                    u.lastName,
                    max(p.createdDate) as lastPostTime,
                    exists(
                        select 1
                        from follows f
                        where f.followerId = ? and f.followingId = u.userId
                    ) as isFollowed
                from user u
                left join posts p on p.userId = u.userId
                where u.userId <> ?
                group by u.userId, u.firstName, u.lastName
                order by u.firstName, u.lastName
                """;
        List<FollowableUser> users = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userIdToExclude);
            pstmt.setString(2, userIdToExclude);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {

                        String userId = rs.getString("userId");
                        String firstName = rs.getString("firstName");
                        String lastName = rs.getString("lastName");
                        String postTime = rs.getString("lastPostTime");
                        if (postTime == null) {
                            postTime = "No posts yet";
                        }
                        boolean isFollowed = rs.getBoolean("isFollowed");
        

                        users.add(new FollowableUser(
                            userId, 
                            firstName, 
                            lastName,
                            isFollowed,
                            postTime
                        ));
                    }
                }
            }
        
        return users;
    }

    public List<FollowableUser> searchUsers(String query, String loggedInUserId) throws SQLException {
        String q = query == null ? "" : query.trim();
        if (q.isEmpty()) {
            return getFollowableUsers(loggedInUserId);
        }

        final String sql = """
                select
                    u.userId,
                    u.firstName,
                    u.lastName,
                    max(p.createdDate) as lastPostTime,
                    exists(
                        select 1
                        from follows f
                        where f.followerId = ? and f.followingId = u.userId
                    ) as isFollowed
                from user u
                left join posts p on p.userId = u.userId
                where u.userId <> ?
                  and (
                      u.username like ?
                      or u.firstName like ?
                      or u.lastName like ?
                      or concat(u.firstName, ' ', u.lastName) like ?
                  )
                group by u.userId, u.firstName, u.lastName
                order by u.firstName, u.lastName
                """;

        List<FollowableUser> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int uid = Integer.parseInt(loggedInUserId);
            pstmt.setInt(1, uid);
            pstmt.setInt(2, uid);
            String like = "%" + q + "%";
            pstmt.setString(3, like);
            pstmt.setString(4, like);
            pstmt.setString(5, like);
            pstmt.setString(6, like);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String userId = rs.getString("userId");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    String postTime = rs.getString("lastPostTime");
                    boolean isFollowed = rs.getBoolean("isFollowed");

                    users.add(new FollowableUser(userId, firstName, lastName, isFollowed, postTime));
                }
            }
        }
        return users;
    }

    public void follow(String followerId, String followingId) throws SQLException {
        final String sql = "insert ignore into follows (followerId, followingId) values (?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(followerId));
            pstmt.setInt(2, Integer.parseInt(followingId));
            pstmt.executeUpdate();
        }
    }

    public void unfollow(String followerId, String followingId) throws SQLException {
        final String sql = "delete from follows where followerId = ? and followingId = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(followerId));
            pstmt.setInt(2, Integer.parseInt(followingId));
            pstmt.executeUpdate();
        }
    }

}
