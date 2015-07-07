package no.kantega.forum.jaxrs.dal.jdbc;

import no.kantega.forum.jaxrs.bol.Fault;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Optional;

import static no.kantega.utilities.Objects.nonNull;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-06
 */
public class OracleRows implements Rows, Row, AutoCloseable, Iterator<Row> {

    private ResultSet resultSet;
    private boolean hasNext = false;

    public OracleRows(ResultSet resultSet) {
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
        return hasNext() ? this : null;
    }

    @Override
    public Boolean getBoolean(int columnIndex) throws SQLException {
        BigDecimal value = getNumber(columnIndex);
        return nonNull(value) ? value.equals(BigDecimal.ONE) : null;
    }

    @Override
    public Byte getByte(int columnIndex) throws SQLException {
        BigDecimal value = getNumber(columnIndex);
        return nonNull(value) ? value.byteValue() : null;
    }

    @Override
    public Short getShort(int columnIndex) throws SQLException {
        BigDecimal value = getNumber(columnIndex);
        return nonNull(value) ? value.shortValue() : null;
    }

    @Override
    public Integer getInteger(int columnIndex) throws SQLException {
        BigDecimal value = getNumber(columnIndex);
        return nonNull(value) ? value.intValue() : null;
    }

    @Override
    public Long getLong(int columnIndex) throws SQLException {
        BigDecimal value = getNumber(columnIndex);
        return nonNull(value) ? value.longValue() : null;
    }

    @Override
    public BigInteger getBigInteger(int columnIndex) throws SQLException {
        BigDecimal value = getNumber(columnIndex);
        return nonNull(value) ? value.toBigInteger() : null;
    }

    @Override
    public Boolean getBoolean(String columnLabel) throws SQLException {
        BigDecimal value = getNumber(columnLabel);
        return nonNull(value) ? value.equals(BigDecimal.ONE) : null;
    }

    @Override
    public Byte getByte(String columnLabel) throws SQLException {
        BigDecimal value = getNumber(columnLabel);
        return nonNull(value) ? value.byteValue() : null;
    }

    @Override
    public Short getShort(String columnLabel) throws SQLException {
        BigDecimal value = getNumber(columnLabel);
        return nonNull(value) ? value.shortValue() : null;
    }

    @Override
    public Integer getInteger(String columnLabel) throws SQLException {
        BigDecimal value = getNumber(columnLabel);
        return nonNull(value) ? value.intValue() : null;
    }

    @Override
    public Long getLong(String columnLabel) throws SQLException {
        BigDecimal value = getNumber(columnLabel);
        return nonNull(value) ? value.longValue() : null;
    }

    @Override
    public BigInteger getBigInteger(String columnLabel) throws SQLException {
        BigDecimal value = getNumber(columnLabel);
        return nonNull(value) ? value.toBigInteger() : null;
    }

    @Override
    public Float getFloat(int columnIndex) throws SQLException {
        BigDecimal value = getNumber(columnIndex);
        return nonNull(value) ? value.floatValue() : null;
    }

    @Override
    public Double getDouble(int columnIndex) throws SQLException {
        BigDecimal value = getNumber(columnIndex);
        return nonNull(value) ? value.doubleValue() : null;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        BigDecimal value = getNumber(columnIndex);
        return nonNull(value) ? value : null;
    }

    @Override
    public Float getFloat(String columnLabel) throws SQLException {
        BigDecimal value = getNumber(columnLabel);
        return nonNull(value) ? value.floatValue() : null;
    }

    @Override
    public Double getDouble(String columnLabel) throws SQLException {
        BigDecimal value = getNumber(columnLabel);
        return nonNull(value) ? value.doubleValue() : null;
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        BigDecimal value = getNumber(columnLabel);
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

    @Override
    public <T> Optional<T> getOptional(T value) {
        return nonNull(value) ? Optional.of(value) : Optional.empty();
    }

    private BigDecimal getNumber(int columnIndex) throws SQLException {
        BigDecimal value = resultSet.getBigDecimal(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    private BigDecimal getNumber(String columnLabel) throws SQLException {
        BigDecimal value = resultSet.getBigDecimal(columnLabel);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
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
