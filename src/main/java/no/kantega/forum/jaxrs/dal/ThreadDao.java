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
public class ThreadDao {

    public List<Long> getThreadIdsForForumIds(Connection connection, List<Long> forumIds) {
        if (forumIds.isEmpty()) {
            return new ArrayList<>();
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(new StringBuilder("SELECT threadId FROM forum_thread WHERE forumId IN (")
                .append(forumIds.stream().map(String::valueOf).collect(Collectors.joining(", ")))
                .append(")").toString())) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Long> threadIds = new ArrayList<>();
                while (resultSet.next()) {
                    threadIds.add(resultSet.getLong(1));
                }
                return threadIds;
            }
        } catch (SQLException cause) {
            throw new Fault(500, cause);
        }
    }

}
