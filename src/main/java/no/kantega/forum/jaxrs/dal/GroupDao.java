package no.kantega.forum.jaxrs.dal;

import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.bol.GroupBo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-06
 */
public class GroupDao {

    public List<GroupBo> getAllGroups(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT forumId, groupId FROM forum_forum_groups ORDER by groupId")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<GroupBo> groups = new ArrayList<>();
                while (resultSet.next()) {
                    groups.add(new GroupBo(
                            resultSet.getLong(1),
                            resultSet.getString(2)));
                }
                return groups;
            }
        } catch (SQLException cause) {
            throw new Fault(500, cause);
        }
    }

    public List<GroupBo> getGroups(Connection connection, Long forumId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT forumId, groupId FROM forum_forum_groups WHERE forumId = ?")) {
            preparedStatement.setLong(1, forumId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<GroupBo> groups = new ArrayList<>();
                while (resultSet.next()) {
                    groups.add(new GroupBo(
                            resultSet.getLong(1),
                            resultSet.getString(2)
                    ));
                }
                return groups;
            }
        } catch (SQLException cause) {
            throw new Fault(500, cause);
        }
    }

    public List<GroupBo> getGroupsByRoles(Connection connection, List<String> roles) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT forumId, groupId FROM forum_forum_groups WHERE groupId IN (?)")) {
            preparedStatement.setString(1, roles.stream().collect(Collectors.joining(", ")));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<GroupBo> groups = new ArrayList<>();
                while (resultSet.next()) {
                    groups.add(new GroupBo(
                            resultSet.getLong(1),
                            resultSet.getString(2)
                    ));
                }
                return groups;
            }
        } catch (SQLException cause) {
            throw new Fault(500, cause);
        }
    }
}
