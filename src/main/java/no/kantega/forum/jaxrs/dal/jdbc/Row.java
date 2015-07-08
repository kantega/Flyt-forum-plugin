package no.kantega.forum.jaxrs.dal.jdbc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-06
 */
public interface Row {

    Boolean getBoolean(int columnIndex) throws SQLException;
    Byte getByte(int columnIndex) throws SQLException;
    Short getShort(int columnIndex) throws SQLException;
    Integer getInteger(int columnIndex) throws SQLException;
    Long getLong(int columnIndex) throws SQLException;
    BigInteger getBigInteger(int columnIndex) throws SQLException;

    Boolean getBoolean(String columnLabel) throws SQLException;
    Byte getByte(String columnLabel) throws SQLException;
    Short getShort(String columnLabel) throws SQLException;
    Integer getInteger(String columnLabel) throws SQLException;
    Long getLong(String columnLabel) throws SQLException;
    BigInteger getBigInteger(String columnLabel) throws SQLException;

    Float getFloat(int columnIndex) throws SQLException;
    Double getDouble(int columnIndex) throws SQLException;
    BigDecimal getBigDecimal(int columnIndex) throws SQLException;

    Float getFloat(String columnLabel) throws SQLException;
    Double getDouble(String columnLabel) throws SQLException;
    BigDecimal getBigDecimal(String columnLabel) throws SQLException;

    String getString(int columnIndex) throws SQLException;
    String getString(String columnLabel) throws SQLException;

    <T> Optional<T> getOptional(T value);
    void close() throws SQLException;
}
