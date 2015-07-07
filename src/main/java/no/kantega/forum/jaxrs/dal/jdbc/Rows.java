package no.kantega.forum.jaxrs.dal.jdbc;

import java.sql.SQLException;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-07
 */
public interface Rows extends Iterable<Row>, AutoCloseable {

    void close() throws SQLException;
}
