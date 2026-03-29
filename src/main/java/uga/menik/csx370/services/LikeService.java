package uga.menik.csx370.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class LikeService {

    private final DataSource dataSource;

    @Autowired
    public LikeService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Adds a like for a user on a specific post.
     */
    public void addLike(int userId, int postId) throws SQLException {
        // The query uses INSERT IGNORE to prevent duplicate likes from the same user on the same post.
        // It is accessed by the home (http://localhost:8081/) page and the profile page (http://localhost:8081/profile/{userId}) when the like button is clicked.
        final String sql = "INSERT IGNORE INTO likes (userId, postId) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, postId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Removes a like for a user on a specific post.
     */

    public void removeLike(int userId, int postId) throws SQLException {
        // The query remains a DELETE regardless of ID types
        // It removes the like from the database for the given userId and postId.
        // It is accessed by the home (http://localhost:8081/) page and the profile page (http://localhost:8081/profile/{userId}) when the unlike button is clicked.
        final String sql = "DELETE FROM likes WHERE userId = ? AND postId = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // You just ensure these match your table's data type (setInt for INT)
            pstmt.setInt(1, userId);
            pstmt.setInt(2, postId);

            pstmt.executeUpdate();
        }
    }
}
