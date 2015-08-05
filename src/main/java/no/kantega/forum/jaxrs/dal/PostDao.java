package no.kantega.forum.jaxrs.dal;

import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.bol.PostBo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static no.kantega.utilities.Objects.requireNonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-08-05
 */
public class PostDao {

    public List<PostBo> getPostsByPostIds(Connection connection, List<Long> postIds) {
        postIds = requireNonNull(postIds, "May not be null: postId");
        if (postIds.isEmpty()) {
            return new ArrayList<>();
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(new StringBuilder(
                "SELECT postId, threadId, replyToId, author, subject, body, postDate, owner, approved, ratingScore, numberOfRatings, modifiedDate, embed FROM forum_post WHERE postId IN (")
                .append(postIds.stream().map(String::valueOf).collect(Collectors.joining(", ")))
                .append(")").toString())) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<PostBo> posts = new ArrayList<>();
                while (resultSet.next()) {
                    posts.add(new PostBo(
                            resultSet.getLong(1),
                            resultSet.getLong(2),
                            resultSet.getLong(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getString(6),
                            getTimestamp(resultSet, 7),
                            resultSet.getString(8),
                            resultSet.getBoolean(9),
                            resultSet.getFloat(10),
                            getInteger(resultSet, 11),
                            getTimestamp(resultSet, 12),
                            resultSet.getString(13)
                    ));
                }
                return posts;
            }
        } catch (SQLException cause) {
            throw new Fault(500, cause);
        }
    }

    public static Integer getInteger(ResultSet resultSet, int columnIndex) throws SQLException {
        Integer integer = resultSet.getInt(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        return integer;
    }

    public static Instant getTimestamp(ResultSet resultSet, int columnIndex) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        return timestamp != null ? timestamp.toInstant() : null;
    }
}
