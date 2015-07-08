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
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-06
 */
public class GroupDao {

    private DataSource dataSource;


    @Inject
    public GroupDao(@Named("aksessDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<GroupDo> getAllGroups() {
        return Jdbc.readOnly(dataSource, (Connection connection) -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT groupId,forumId FROM forum_forum_groups ORDER by groupId")) {
                try (ResultSet rows = preparedStatement.executeQuery()) {
                    List<GroupDo> groups = new ArrayList<>();
                    while (rows.next()) {
                        groups.add(new GroupDo(rows.getString(1), rows.getLong(2)));
                    }
                    return groups;
                }
            } catch (SQLException cause) {
                throw new Fault(500, cause);
            }
        });
    }

    public static GroupDo toGroupDo(Row row) {
        try {
            return new GroupDo(row.getString(1), row.getLong(2));
        } catch (SQLException cause) {
            throw new Fault(500, cause);
        }
    }

}
