package no.kantega.forum.jaxrs.dal.jdbc;

import no.kantega.forum.jaxrs.bol.Fault;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.Iterator;
import java.util.Optional;

import static no.kantega.utilities.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-06
 */
public class MsSqlRows implements Rows, Row, AutoCloseable, Iterator<Row> {

    private ResultSet resultSet;
    private boolean hasNext = false;

    public MsSqlRows(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public boolean hasNext() {
        if (!hasNext) {
            try {
                hasNext = resultSet.next();
            } catch (SQLException cause) {
                throw new Fault(500, cause);
            }
        }
        return hasNext;
    }

    @Override
    public Row next() {
        if(hasNext()) {
            hasNext = false;
            return this;
        }
        hasNext = false;
        return null;
    }

    @Override
    public Boolean getBoolean(int columnIndex) throws SQLException {
        boolean value = resultSet.getBoolean(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    @Override
    public Byte getByte(int columnIndex) throws SQLException {
        short value = resultSet.getShort(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        return (byte) value;
    }

    @Override
    public Short getShort(int columnIndex) throws SQLException {
        short value = resultSet.getShort(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    @Override
    public Integer getInteger(int columnIndex) throws SQLException {
        int value = resultSet.getInt(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    @Override
    public Long getLong(int columnIndex) throws SQLException {
        long value = resultSet.getLong(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    @Override
    public BigInteger getBigInteger(int columnIndex) throws SQLException {
        BigDecimal value = resultSet.getBigDecimal(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        return nonNull(value) ? value.toBigInteger() : null;
    }

    @Override
    public Boolean getBoolean(String columnLabel) throws SQLException {
        boolean value = resultSet.getBoolean(columnLabel);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    @Override
    public Byte getByte(String columnLabel) throws SQLException {
        short value = resultSet.getShort(columnLabel);
        if (resultSet.wasNull()) {
            return null;
        }
        return (byte) value;
    }

    @Override
    public Short getShort(String columnLabel) throws SQLException {
        short value = resultSet.getShort(columnLabel);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    @Override
    public Integer getInteger(String columnLabel) throws SQLException {
        int value = resultSet.getInt(columnLabel);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    @Override
    public Long getLong(String columnLabel) throws SQLException {
        long value = resultSet.getLong(columnLabel);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    @Override
    public BigInteger getBigInteger(String columnLabel) throws SQLException {
        BigDecimal value = resultSet.getBigDecimal(columnLabel);
        if (resultSet.wasNull()) {
            return null;
        }
        return nonNull(value) ? value.toBigInteger() : null;
    }

    @Override
    public Float getFloat(int columnIndex) throws SQLException {
        float value = resultSet.getFloat(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    @Override
    public Double getDouble(int columnIndex) throws SQLException {
        double value = resultSet.getFloat(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        BigDecimal value = resultSet.getBigDecimal(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        return nonNull(value) ? value : null;
    }

    @Override
    public Float getFloat(String columnLabel) throws SQLException {
        float value = resultSet.getFloat(columnLabel);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    @Override
    public Double getDouble(String columnLabel) throws SQLException {
        double value = resultSet.getFloat(columnLabel);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        BigDecimal value = resultSet.getBigDecimal(columnLabel);
        if (resultSet.wasNull()) {
            return null;
        }
        return nonNull(value) ? value : null;
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        String value = resultSet.getString(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        String value = resultSet.getString(columnLabel);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    public Object getObject(String columnLabel) throws SQLException {
        Object value = resultSet.getObject(columnLabel);
        if(resultSet.wasNull()){
            return null;
        }
        return value;
    }

    @Override
    public <T> Optional<T> getOptional(T value) {
        return nonNull(value) ? Optional.of(value) : Optional.empty();
    }

    @Override
    public void close() throws SQLException {
        if (nonNull(resultSet)) {
            resultSet.close();
        }
    }

    @Override
    public Iterator<Row> iterator() {
        return this;
    }
}
