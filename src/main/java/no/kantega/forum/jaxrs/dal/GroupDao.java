package no.kantega.forum.jaxrs.dal;

import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.bol.GroupDo;
import no.kantega.forum.jaxrs.dal.jdbc.Jdbc;
import no.kantega.forum.jaxrs.dal.jdbc.MsSqlRows;
import no.kantega.forum.jaxrs.dal.jdbc.Row;
import no.kantega.forum.jaxrs.dal.jdbc.Rows;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-06
 */
public class GroupDao {

    public List<GroupDo> getAllGroups(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT groupId,forumId FROM forum_forum_groups ORDER by groupId")) {
            try (Rows rows = new MsSqlRows(preparedStatement.executeQuery())) {
                return rows.mapAll(row -> new GroupDo(row.getString(1), row.getLong(2)));
            }
        } catch (SQLException cause) {
            throw new Fault(500, "Could not read groups", cause);
        }
    }

    public List<GroupDo> readByForum(Connection connection, Long forumId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT groupId,forumId FROM forum_forum_groups WHERE forumId = ?")) {
            try (Rows rows = new MsSqlRows(preparedStatement.executeQuery())) {
                return rows.mapAll(row -> new GroupDo(row.getString(1), row.getLong(2)));
            }
        } catch (SQLException cause) {
            throw new Fault(500, String.format("Could not read groups by forum: %s", forumId), cause);
        }
    }

    public Map<Long,List<GroupDo>> readByForums(Connection connection, List<Long> forumIds) {
        String in = forumIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT groupId,forumId FROM forum_forum_groups WHERE forumId IN (" + in + ") ORDER BY forumId")) {
            try (Rows rows = new MsSqlRows(preparedStatement.executeQuery())) {
                return rows.mapAll(row -> new GroupDo(row.getString(1), row.getLong(2))).stream().collect(Collectors.groupingBy(GroupDo::getForumId));
            }
        } catch (SQLException cause) {
            throw new Fault(500, String.format("Could not read groups by forums: %s", forumIds), cause);
        }
    }

    public Map<Long,List<GroupDo>> readByForums(Connection connection, Long... forumIds) {
        return readByForums(connection, Arrays.asList(forumIds));
    }

}
