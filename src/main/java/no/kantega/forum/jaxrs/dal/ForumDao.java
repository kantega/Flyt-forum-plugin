package no.kantega.forum.jaxrs.dal;

import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.bol.ForumBo;
import no.kantega.forum.jaxrs.dal.jdbc.MsSqlRows;
import no.kantega.forum.jaxrs.dal.jdbc.Rows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-08
 */
public class ForumDao {

    public Map<Long,List<ForumBo>> readByCategories(Connection connection, Long... categoryIds) {
        String in = Arrays.asList(categoryIds).stream().map(String::valueOf).collect(Collectors.joining(","));
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT forumId, forumCategoryId, name, description, numThreads, createdDate, owner, attachmentsAllowed, approvalRequired, anonymousPostAllowed, moderator FROM forum_forum WHERE forumCategoryId IN ("+ in + ") ORDER BY forumCategory")) {
            try (Rows rows = new MsSqlRows(preparedStatement.executeQuery())) {
                List<ForumBo> forumBos = rows.mapAll(row -> new ForumBo(row.getLong(1), row.getLong(2), row.getString(3), row.getString(4), row.getInteger(5), row.getDate(6), row.getString(7), row.getBoolean(8), row.getBoolean(9), row.getBoolean(10), row.getString(11)));
                return forumBos.stream().collect(Collectors.groupingBy(ForumBo::getCategoryId));
            }

        } catch (SQLException cause) {
            throw new Fault(500, String.format("Could not read forums by categories: %s", categoryIds) , cause);
        }
    }

    public List<ForumBo> readByCategory(Connection connection, Long categoryId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT forumId, forumCategoryId, name, description, numThreads, createdDate, owner, attachmentsAllowed, approvalRequired, anonymousPostAllowed, moderator FROM forum_forum WHERE forumCategoryId = ?")) {
            preparedStatement.setLong(1, categoryId);
            try (Rows rows = new MsSqlRows(preparedStatement.executeQuery())) {
                return rows.mapAll(row -> new ForumBo(row.getLong(1), row.getLong(2), row.getString(3), row.getString(4), row.getInteger(5), row.getDate(6), row.getString(7), row.getBoolean(8), row.getBoolean(9), row.getBoolean(10), row.getString(11)));
            }

        } catch (SQLException cause) {
            throw new Fault(500, String.format("Could not read forums by category: %s", categoryId) , cause);
        }
    }
}
