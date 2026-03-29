package uga.menik.csx370.utility;

import java.util.ArrayList;
import java.util.List;

import uga.menik.csx370.models.Comment;
import uga.menik.csx370.models.ExpandedPost;
import uga.menik.csx370.models.FollowableUser;
import uga.menik.csx370.models.Post;
import uga.menik.csx370.models.User;

public class Utility {

    // Format: Mar 07, 2026, 10:54 PM
    private static final java.time.format.DateTimeFormatter DISPLAY_DATE_FORMATTER =
            java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy, hh:mm a");

    /**
     * Formats a java.sql.Timestamp or ISO string to the display format.
     * Accepts either a java.sql.Timestamp, java.util.Date, or ISO string.
     */
    public static String formatDisplayDate(Object dateObj) {
        if (dateObj == null) return "";
        java.time.LocalDateTime ldt = null;
        if (dateObj instanceof java.sql.Timestamp) {
            ldt = ((java.sql.Timestamp) dateObj).toLocalDateTime();
        } else if (dateObj instanceof java.util.Date) {
            ldt = ((java.util.Date) dateObj).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        } else if (dateObj instanceof String) {
            String s = (String) dateObj;
            try {
                ldt = java.time.LocalDateTime.parse(s, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception e) {
                try {
                    ldt = java.time.LocalDateTime.parse(s, java.time.format.DateTimeFormatter.ISO_DATE_TIME);
                } catch (Exception ex) {
                    return s; // fallback: return as-is
                }
            }
        }
        if (ldt == null) return dateObj.toString();
        return ldt.format(DISPLAY_DATE_FORMATTER);
    }

    public static List<FollowableUser> createSampleFollowableUserList() {
        List<FollowableUser> followableUsers = new ArrayList<>();
        followableUsers.add(new FollowableUser("1", "John", "Smith",
            true, formatDisplayDate("2024-03-07 22:54:00")));
        followableUsers.add(new FollowableUser("2", "Josh", "Did",
            false, formatDisplayDate("2024-03-05 11:00:00")));
        followableUsers.add(new FollowableUser("3", "Milk", "Man",
            true, formatDisplayDate("2024-03-06 09:30:00")));
        followableUsers.add(new FollowableUser("4", "Bob", "Yellow",
            false, formatDisplayDate("2024-03-02 20:15:00")));
        return followableUsers;
    }

    public static List<Post> createSamplePostsListWithoutComments() {
        User user1 = new User("1", "John", "Doe");
        User user2 = new User("2", "Jane", "Doe");
        User user3 = new User("3", "Alice", "Smith");
        User user4 = new User("4", "Bob", "Brown");
        User user5 = new User("5", "Charlie", "Green");
        List<Post> postsWithoutComments = new ArrayList<>();
        postsWithoutComments.add(new Post("1", "Exploring Spring Boot features",
            formatDisplayDate("2024-03-07 22:54:00"), user1, 10, 4, false, false));
        postsWithoutComments.add(new Post("2", "Introduction to Microservices",
            formatDisplayDate("2024-03-08 11:00:00"), user2, 15, 6, true, true));
        postsWithoutComments.add(new Post("3", "Basics of Reactive Programming",
            formatDisplayDate("2024-03-09 09:30:00"), user3, 20, 3, true, false));
        return postsWithoutComments;
    }

    public static List<ExpandedPost> createSampleExpandedPostWithComments() {
        User user1 = new User("1", "John", "Doe");
        User user2 = new User("2", "Jane", "Doe");
        User user3 = new User("3", "Alice", "Smith");
        User user4 = new User("4", "Bob", "Brown");
        User user5 = new User("5", "Charlie", "Green");
        List<Comment> commentsForPost = new ArrayList<>();

        commentsForPost.add(new Comment("1", "Great insights, thanks for sharing!", 
            formatDisplayDate("2024-03-07 22:54:00"), user2));
        commentsForPost.add(new Comment("2", "I'm looking forward to trying this out.", 
            formatDisplayDate("2024-03-08 11:00:00"), user4));
        commentsForPost.add(new Comment("3", "Can you provide more examples in your next post?", 
            formatDisplayDate("2024-03-09 09:30:00"), user5));
        ExpandedPost postWithComments = new ExpandedPost("4", "Advanced Techniques " + 
            "in Spring Security", formatDisplayDate("2024-03-10 20:15:00"), user1, 25, 
            commentsForPost.size(), false, true, commentsForPost);
        return List.of(postWithComments);
    }

}
