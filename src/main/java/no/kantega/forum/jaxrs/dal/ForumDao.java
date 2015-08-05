package no.kantega.forum.jaxrs.dal;

import no.kantega.forum.jaxrs.bol.Fault;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kristian Myrhaug
 * @since 2015-08-04
 */
public class ForumDao {

    public List<Long> getForumIdsForGroups(Connection connection, List<String> groups) {
        ;

        try (PreparedStatement preparedStatement = connection.prepareStatement(new StringBuilder("SELECT forumId FROM forum_forum_groups WHERE groupId IN (")
                .append(groups.stream().map(group -> String.format("'%s'", group)).collect(Collectors.joining(", ")))
                .append(")")
                .toString())) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Long> forumIds = new ArrayList<>();
                while (resultSet.next()) {
                    forumIds.add(resultSet.getLong(1));
                }
                return forumIds;
            }
        } catch (SQLException cause) {
            throw new Fault(500, cause);
        }
    }

    public List<Long> getForumIdsWithoutGroups(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT f.forumId FROM forum_forum f LEFT JOIN forum_forum_groups g ON f.forumId = g.forumId WHERE g.forumId IS NULL")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Long> forumIds = new ArrayList<>();
                while (resultSet.next()) {
                    forumIds.add(resultSet.getLong(1));
                }
                return forumIds;
            }
        } catch (SQLException cause) {
            throw new Fault(500, cause);
        }
    }
}
