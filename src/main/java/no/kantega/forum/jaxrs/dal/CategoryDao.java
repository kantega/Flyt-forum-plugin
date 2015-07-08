package no.kantega.forum.jaxrs.dal;

import no.kantega.forum.jaxrs.bol.CategoryBo;
import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.dal.jdbc.MsSqlRows;
import no.kantega.forum.jaxrs.dal.jdbc.Rows;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static no.kantega.utilities.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-08
 */
public class CategoryDao {

    public CategoryBo create(Connection connection, CategoryBo categoryBo) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO forum_forumcategory (name, description, numForums, createdDate, owner) OUTPUT Inserted.forumCategoryId, Inserted.name, Inserted.description, Inserted.numForums, Inserted.createdDate, Inserted.owner,  VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, categoryBo.getName());
            preparedStatement.setString(2, categoryBo.getDescription());
            preparedStatement.setInt(3, categoryBo.getNumForums());
            preparedStatement.setDate(4, nonNull(categoryBo.getCreatedDate()) ? new Date(categoryBo.getCreatedDate().toEpochMilli()) : null);
            preparedStatement.setString(5, categoryBo.getName());
            try (Rows rows = new MsSqlRows(preparedStatement.executeQuery())) {
                return rows.mapOne(row -> new CategoryBo(row.getLong(1), row.getString(2), row.getString(3), row.getInteger(4), row.getDate(5), row.getString(6)));
            }
        } catch (SQLException cause) {
            throw new Fault(500, "Could not create category", cause);
        }
    }

    public CategoryBo read(Connection connection, Long categoryId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT forumCategoryId, name, description, numForums, createdDate, owner FROM forum_forumcategory WHERE forumCategoryId = ?")) {
            preparedStatement.setLong(1, categoryId);
            try (Rows rows = new MsSqlRows(preparedStatement.executeQuery())) {
                return rows.mapOne(row -> new CategoryBo(row.getLong(1), row.getString(2), row.getString(3), row.getInteger(4), row.getDate(5), row.getString(6)));
            }
        } catch (SQLException cause) {
            throw new Fault(500, "Could not read category: " + categoryId, cause);
        }
    }

    public CategoryBo update(Connection connection, CategoryBo categoryBo) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE forum_forumcategory SET (name = ?, description = ?, numForums = ?, createdDate = ?, owner = ?) WHERE forumCategoryId = ?")) {
            preparedStatement.setString(1, categoryBo.getName());
            preparedStatement.setString(2, categoryBo.getDescription());
            preparedStatement.setInt(3, categoryBo.getNumForums());
            preparedStatement.setDate(4, nonNull(categoryBo.getCreatedDate()) ? new Date(categoryBo.getCreatedDate().toEpochMilli()) : null);
            preparedStatement.setString(5, categoryBo.getName());
            preparedStatement.setLong(6, categoryBo.getId());
            return categoryBo;
        } catch (SQLException cause) {
            throw new Fault(500, "Could not update category", cause);
        }
    }

    public void delete(Connection connection, Long categoryId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FORM forum_forumcategory WHERE forumCategoryId = ?")) {
            preparedStatement.setLong(1, categoryId);
            preparedStatement.executeUpdate();
        } catch (SQLException cause) {
            throw new Fault(500, "Could not delete category: " + categoryId, cause);
        }
    }

    public List<CategoryBo> read(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT forumCategoryId, name, description, numForums, createdDate, owner FROM forum_forumcategory")) {
            try (Rows rows = new MsSqlRows(preparedStatement.executeQuery())) {
                return rows.mapAll(row -> new CategoryBo(row.getLong(1), row.getString(2), row.getString(3), row.getInteger(4), row.getDate(5), row.getString(6)));
            }
        } catch (SQLException cause) {
            throw new Fault(500, "Could not read categories", cause);
        }
    }
}
