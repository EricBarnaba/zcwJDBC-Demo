package com.zipcodewilmington.jdbc.tools.database.connection;

import com.zipcodewilmington.jdbc.tools.general.functional.ExceptionalSupplier;
import com.zipcodewilmington.jdbc.tools.general.exception.SQLeonError;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


/**
 * Created by leon on 3/13/18.
 */
public class ConnectionWrapper {
    enum ConnectionProperty {
        IS_CLOSED,
        METADATA,
        CATALOG,
        CATALOGS;
    }

    private final Connection connection;

    public ConnectionWrapper(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isClosed() {
        return getProperty(connection::isClosed, ConnectionProperty.IS_CLOSED);
    }

    public DatabaseMetaData getMetaData() {
        return getProperty(connection::getMetaData, ConnectionProperty.METADATA);
    }

    public String getCatalog() {
        return getProperty(connection::getCatalog, ConnectionProperty.CATALOG);
    }

    public ResultSetHandler getCatalogs() {
        return getProperty(() -> {
            ResultSet rs = getMetaData().getCatalogs();
            ResultSetHandler rsh = new ResultSetHandler(rs);
            return rsh;
        }, ConnectionProperty.CATALOGS);
    }

    private <E> E getProperty(ExceptionalSupplier<E> setMethod, ConnectionProperty property) {
        E valueRetrieved = null;
        try {
            valueRetrieved = setMethod.get(); // invoke getter
        } catch (Throwable throwable) {
            String error = "Failed to get property `%s`";
            String errorMessage = String.format(error, property.name());
            throw new SQLeonError(throwable, errorMessage);
        }
        return valueRetrieved;
    }

    public String[] getSchemaNames() {
        ResultSetHandler rsh = getCatalogs();
        String schemaColumn = rsh.getColumnName(1);
        String[] schemaNames = rsh.getRows(schemaColumn);
        return schemaNames;
    }


    public Boolean hasDatabase(String name) {
        List<String> schemaNames = Arrays.asList(getSchemaNames());
        return schemaNames.contains(name);
    }
}
