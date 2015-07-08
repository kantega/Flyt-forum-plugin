package no.kantega.forum.jaxrs.dal.jdbc;

import no.kantega.forum.jaxrs.bol.Fault;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-06
 */
public class Jdbc {

    private Jdbc() {}

    public static <Output> Output readOnly(DataSource dataSource, Function<Connection, Output> action) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setReadOnly(true);
            return action.apply(connection);
        } catch (SQLException cause) {
            throw new Fault(500, cause);
        }
    }
}
