package no.kantega.forum.jaxrs.dal.jdbc;

import no.kantega.forum.jaxrs.bol.Fault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

import static java.util.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-06
 */
public class Jdbc {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jdbc.class);

    private Jdbc() {}

    public static <Output> Output readOnly(DataSource dataSource, Function<Connection, Output> action) {
        Connection connection = null;
        Boolean _readOnly = null;
        try {
            connection = dataSource.getConnection();
            _readOnly = connection.isReadOnly();
            connection.setReadOnly(true);
            return action.apply(connection);
        } catch (SQLException cause) {
            throw new Fault(500, cause);
        } finally {
            if (nonNull(connection)) {
                if (nonNull(_readOnly)) {
                    try {
                        connection.setReadOnly(_readOnly);
                    } catch (Throwable cause) {
                        LOGGER.error("Could not set read only to " + _readOnly + " on connection", cause);
                    }
                }
                try {
                    connection.close();
                } catch (Throwable cause) {
                    LOGGER.error("Could not close connection", cause);
                    throw new Fault(500, cause);
                }
            }
        }
    }
}
