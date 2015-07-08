package no.kantega.forum.jaxrs.dal.jdbc;

import no.kantega.forum.jaxrs.bol.Fault;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

import static no.kantega.utilities.Objects.nonNull;

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

    public static <Output> Output transactional(DataSource dataSource, Function<Connection, Output> action) {
        return transactional(dataSource, null, action);
    }

    public static <Output> Output transactional(DataSource dataSource, TransactionIsolation transactionIsolation, Function<Connection, Output> action) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            if(nonNull(transactionIsolation)) {
                connection.setTransactionIsolation(transactionIsolation.getJdbcCode());
            }
            Output output = action.apply(connection);
            connection.commit();
            return output;

        } catch (SQLException cause) {
            throw new Fault(500, cause);
        }
    }

    public enum TransactionIsolation {
        NONE(Connection.TRANSACTION_NONE),
        READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
        READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
        REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
        TRANSACTION_SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

        private int jdbcCode;

        TransactionIsolation(int jdbcCode) {
            this.jdbcCode = jdbcCode;
        }

        public int getJdbcCode() {
            return jdbcCode;
        }
    }
}
