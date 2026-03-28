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
public class BookmarkService {

    private final DataSource dataSource;

    @Autowired
    public BookmarkService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Adds a bookmark for a user on a specific post.
     */
    public void addBookmark(int userId, int postId) throws SQLException {
        final String sql = "INSERT IGNORE INTO bookmarks (userId, postId) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, postId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Removes a bookmark for a user on a specific post.
     */

    public void removeBookmark(int userId, int postId) throws SQLException {
        // The query remains a DELETE regardless of ID types
        final String sql = "DELETE FROM bookmarks WHERE userId = ? AND postId = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // You just ensure these match your table's data type (setInt for INT)
            pstmt.setInt(1, userId);
            pstmt.setInt(2, postId);

            pstmt.executeUpdate();
        }
    }
}
