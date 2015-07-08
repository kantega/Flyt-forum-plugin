package no.kantega.forum.jaxrs.dal.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static no.kantega.utilities.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-07
 */
public interface Rows extends Iterable<Row>, AutoCloseable {

    void close() throws SQLException;

    default <T> T mapOne(SqlFunction<Row, T> mapper) throws SQLException {
        Row row = this.iterator().next();
        return nonNull(row) ? mapper.apply(row) : null;
    }

    default <T> List<T> mapAll(SqlFunction<Row, T> mapper) throws SQLException {
        List<T> out = new ArrayList<>();
        for(Row row : this){
            out.add(mapper.apply(row));
        }
        return out;
    }

    interface SqlFunction<Input,Output> {
        Output apply(Input input) throws SQLException;
    }
}
