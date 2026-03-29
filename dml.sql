// It is accessed by the home (http://localhost:8081/) page and the profile page (http://localhost:8081/profile/{userId}) when the bookmark button is clicked
        final String sql = "INSERT IGNORE INTO bookmarks (userId, postId) VALUES (?, ?)";

// It is accessed by the home (http://localhost:8081/) page and the profile page (http://localhost:8081/profile/{userId}) when the unbookmark button is clicked
        final String sql = "DELETE FROM bookmarks WHERE userId = ? AND postId = ?";

// It is accessed by http://localhost:8081/ when the home page is loaded
        final String sql = """

// It is accessed by the home (http://localhost:8081/) page and the profile page (http://localhost:8081/profile/{userId}) when the like button is clicked.
        final String sql = "INSERT IGNORE INTO likes (userId, postId) VALUES (?, ?)";

// It is accessed by the home (http://localhost:8081/) page and the profile page (http://localhost:8081/profile/{userId}) when the unlike button is clicked.
        final String sql = "DELETE FROM likes WHERE userId = ? AND postId = ?";

// It is accessed by http://localhost:8081/usersearch?users={query}
        final String sql = """

// It is accessed by the http://localhost:8081/people page, and the http://localhost:8081/usersearch?users={query} page
        final String sql = "insert ignore into follows (followerId, followingId) values (?, ?)";

// It is accessed by the http://localhost:8081/people page, and the http://localhost:8081/usersearch?users={query} page
        final String sql = "delete from follows where followerId = ? and followingId = ?";

// It is accessed by http://localhost:8081/register via POST request with username, password, firstName and lastName in the body.
        final String registerSql = "insert into user (username, password, firstName, lastName) values (?, ?, ?, ?)";

// It is accessed by http://localhost:8081
        final String sql = "insert into posts (userId, content) values (?, ?)";

// It is accessed by http://localhost:8081/bookmarks
        final String sql = """

// It is accessed by http://localhost:8081/user/{userIdToView}
        final String sql = """

// It is accessed by http://localhost:8081/hashtagsearch?hashtags={hashtagquery}
        final String sql = """

// It is accessed by http://localhost:8081/post/{postIdToFind}
        final String postInfoSQL = """

// It is also accessed by http://localhost:8081/post/{postIdToFind}
        final String commentsSQL = """

// It is accessed by http://localhost:8081/post/{postId}/comment via a POST request.
        final String sql = "insert into comments (userId, postId, content) values (?, ?, ?)";