package com.kang.lab.utils.db;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Date;

public class NamedParameterStatementUtil {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * The statement this object is wrapping.
     */
    private final PreparedStatement statement;

    /**
     * Maps parameter names to arrays of ints which are the parameter indices.
     */
    private final Map<String, int[]> indexMap;

    /**
     * Creates a NamedParameterStatement. Wraps a call to c.
     * {@link Connection#prepareStatement(java.lang.String) prepareStatement}.
     *
     * @param connection the database connection
     * @param query      the parameterized query
     * @throws SQLException if the statement could not be created
     */
    public NamedParameterStatementUtil(Connection connection, String query)
            throws SQLException {
        indexMap = new HashMap<String, int[]>();
        String parsedQuery = parse(query, indexMap);
        statement = connection.prepareStatement(parsedQuery);
    }

    public void setParameterStatementValue(HashMap<String, Object> parameters) throws SQLException {
        if (parameters != null) {
            Iterator<Map.Entry<String, Object>> iterator = parameters
                    .entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                String name = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof Long) {
                    setLong(name, (Long) value);
                } else if (value instanceof Integer) {
                    setInt(name, (Integer) value);
                } else if (value instanceof String) {
                    setString(name, (String) value);
                } else if (value instanceof java.util.Date) {
                    setString(name, simpleDateFormat.format((java.util.Date) value));
                } else {
                    setObject(name, value);
                }
            }
        }
    }

    /**
     * Parses a query with named parameters. The parameter-index mappings are
     * put into the map, and the parsed query is returned. DO NOT CALL FROM
     * CLIENT CODE. This method is non-private so JUnit code can test it.
     *
     * @param query    query to parse
     * @param paramMap map to hold parameter-index mappings
     * @return the parsed query
     */
    static final String parse(String query, Map<String, int[]> paramMap) {
        // I was originally using regular expressions, but they didn't work well
        // for ignoring parameter-like strings inside quotes.
        Map<String, List<Integer>> paramMapAux = new HashMap<String, List<Integer>>();
        int length = query.length();
        StringBuffer parsedQuery = new StringBuffer(length);
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        int index = 1;

        for (int i = 0; i < length; i++) {
            char c = query.charAt(i);
            if (inSingleQuote) {
                if (c == '\'') {
                    inSingleQuote = false;
                }
            } else if (inDoubleQuote) {
                if (c == '"') {
                    inDoubleQuote = false;
                }
            } else {
                if (c == '\'') {
                    inSingleQuote = true;
                } else if (c == '"') {
                    inDoubleQuote = true;
                } else if ((c == ':' || c == '@') && i + 1 < length
                        && Character.isJavaIdentifierStart(query.charAt(i + 1))) {
                    int j = i + 2;
                    while (j < length
                            && Character.isJavaIdentifierPart(query.charAt(j))) {
                        j++;
                    }
                    String name = query.substring(i + 1, j);
                    c = '?'; // replace the parameter with a question mark
                    i += name.length(); // skip past the end if the parameter

                    List<Integer> indexList = paramMapAux.get(name);
                    if (indexList == null) {
                        indexList = new LinkedList<Integer>();
                        paramMapAux.put(name, indexList);
                    }
                    indexList.add(index);

                    index++;
                }
            }
            parsedQuery.append(c);
        }

        // replace the lists of Integer objects with arrays of ints
        for (Map.Entry<String, List<Integer>> entry : paramMapAux.entrySet()) {
            List<Integer> list = entry.getValue();
            int[] indexes = new int[list.size()];
            int i = 0;
            for (Integer x : list) {
                indexes[i++] = x;
            }
            paramMap.put(entry.getKey(), indexes);
        }

        return parsedQuery.toString();
    }

    /**
     * Returns the indexes for a parameter.
     *
     * @param name parameter name
     * @return parameter indexes
     * @throws IllegalArgumentException if the parameter does not exist
     */
    private int[] getIndexes(String name) {
        int[] indexes = indexMap.get(name);
        if (indexes == null) {
            throw new IllegalArgumentException("Parameter not found: " + name);
        }
        return indexes;
    }

    /**
     * Sets a parameter.
     *
     * @param name  parameter name
     * @param value parameter value
     * @throws SQLException             if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setObject(int, java.lang.Object)
     */
    public void setObject(String name, Object value) throws SQLException {
        int[] indexes = getIndexes(name);
        for (int i = 0; i < indexes.length; i++) {
            statement.setObject(indexes[i], value);
        }
    }

    /**
     * Sets a parameter.
     *
     * @param name  parameter name
     * @param value parameter value
     * @throws SQLException             if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setString(int, java.lang.String)
     */
    public void setString(String name, String value) throws SQLException {
        int[] indexes = getIndexes(name);
        for (int i = 0; i < indexes.length; i++) {
            statement.setString(indexes[i], value);
        }
    }

    /**
     * Sets a parameter.
     *
     * @param name  parameter name
     * @param value parameter value
     * @throws SQLException             if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setInt(int, int)
     */
    public void setInt(String name, int value) throws SQLException {
        int[] indexes = getIndexes(name);
        for (int i = 0; i < indexes.length; i++) {
            statement.setInt(indexes[i], value);
        }
    }

    /**
     * Sets a parameter.
     *
     * @param name  parameter name
     * @param value parameter value
     * @throws SQLException             if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setInt(int, int)
     */
    public void setLong(String name, long value) throws SQLException {
        int[] indexes = getIndexes(name);
        for (int i = 0; i < indexes.length; i++) {
            statement.setLong(indexes[i], value);
        }
    }

    /**
     * Sets a parameter.
     *
     * @param name  parameter name
     * @param value parameter value
     * @throws SQLException             if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp(String name, Timestamp value) throws SQLException {
        int[] indexes = getIndexes(name);
        for (int i = 0; i < indexes.length; i++) {
            // TODO: Con setTimestamp contra Oracle en algunos casos los queries
            // se quedaban colgados ...
            // statement.setTimestamp(indexes[i], value);
            Date date = new Date(value.getTime());
            statement.setDate(indexes[i], date);
        }
    }

    /**
     * Returns the underlying statement.
     *
     * @return the statement
     */
    public PreparedStatement getStatement() {
        return statement;
    }

    /**
     * Executes the statement.
     *
     * @return true if the first result is a {@link ResultSet}
     * @throws SQLException if an error occurred
     * @see PreparedStatement#execute()
     */
    public boolean execute() throws SQLException {
        return statement.execute();
    }

    /**
     * Executes the statement, which must be a query.
     *
     * @return the query results
     * @throws SQLException if an error occurred
     * @see PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery() throws SQLException {
        statement.setFetchSize(1000);
        return statement.executeQuery();
    }

    /**
     * Executes the statement, which must be an SQL INSERT, UPDATE or DELETE
     * statement; or an SQL statement that returns nothing, such as a DDL
     * statement.
     *
     * @return number of rows affected
     * @throws SQLException if an error occurred
     * @see PreparedStatement#executeUpdate()
     */
    public int executeUpdate() throws SQLException {
        return statement.executeUpdate();
    }

    /**
     * Closes the statement.
     *
     * @throws SQLException if an error occurred
     * @see Statement#close()
     */
    public void close() throws SQLException {
        statement.close();
    }

    /**
     * Adds the current set of parameters as a batch entry.
     *
     * @throws SQLException if something went wrong
     */
    public void addBatch() throws SQLException {
        statement.addBatch();
    }

    /**
     * Executes all of the batched statements.
     * <p>
     * See {@link Statement#executeBatch()} for details.
     *
     * @return update counts for each statement
     * @throws SQLException if something went wrong
     */
    public int[] executeBatch() throws SQLException {
        return statement.executeBatch();
    }
}
