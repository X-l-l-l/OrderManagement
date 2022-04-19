package dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;

/**
 * Class that implements the operations on database through reflection
 * @param <T>
 * @author Rares Apreutesei
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Creates a select query for a specific field
     * @param field
     * @return
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    /**
     * Creates a SELECT ALL query
     * @return
     */
    private String createSelectQueryAll() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        return sb.toString();
    }

    /**
     * Executes the SELECT ALL query
     * @return
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQueryAll();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Executes a select by id
     * @param id
     * @return
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Gets the rezult of the select query and creates a list of objects
     * @param resultSet
     * @return
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Creates an insert statement not specifying the colums
     * @param table
     * @param values
     * @return
     */
    private String createInsertQuery(String table, ArrayList<String> values) {
        //INSERT INTO table_name
        //VALUES (value1, value2, value3, ...);
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(table);
        sb.append(" VALUES (");
        for (String v : values) {
            sb.append("'").append(v).append("'").append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    /**
     * Creates an insert statement specifying the colums
     * @param table
     * @param values
     * @return
     */
    private String createInsertIdQuery(String table, ArrayList<String> values) {
        //INSERT INTO table_name
        //VALUES (value1, value2, value3, ...);
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(table);
        sb.append(" (");
        for (Field f : type.getDeclaredFields()) {
            if (!f.getName().equals("id"))
                sb.append("`").append(f.getName()).append("`,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") VALUES (");
        for (int i = 1; i < values.size(); i++) {

            sb.append("'").append(values.get(i)).append("'").append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    /**
     * Creates an update statement
     * @param t
     * @return
     */
    private String createUpdateQuery(T t) {
        //INSERT INTO table_name
        //VALUES (value1, value2, value3, ...);
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(type.getSimpleName());
        sb.append(" SET ");
        String id = "";
        try {
            Field f = type.getDeclaredField("id");
            f.setAccessible(true);
            id = f.get(t).toString();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        for (Field f : type.getDeclaredFields()) {
            f.setAccessible(true);
            try {
                sb.append(f.getName()).append("='").append(f.get(t)).append("'").append(",");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" WHERE id=");
        sb.append(id);
        return sb.toString();
    }

    /**
     * Executes the insert statement
     * @param t
     * @return
     */
    public T insert(T t) {
        // TODO:
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<String> vals = new ArrayList<>();

        for (Field f : type.getDeclaredFields()) {
            f.setAccessible(true);
            try {
                vals.add(f.get(t).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String query = createInsertIdQuery(type.getSimpleName(), vals);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Executes the update statement
     * @param t
     * @return
     */
    public T update(T t) {
        // TODO:
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = createUpdateQuery(t);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

    /**
     * Creates a delete statement
     * @param table
     * @param id
     * @return
     */
    private String createDeleteQuery(String table, String id) {
        //DELETE FROM `ordermanagement`.`client` WHERE (`id` = '8');
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(table);
        sb.append(" WHERE id = '");
        sb.append(id).append("'");
        return sb.toString();
    }

    /**
     * Executes the delete statement
     * @param t
     * @return
     */
    public T delete(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = null;
        try {
            Field field = type.getDeclaredField("id");
            field.setAccessible(true);
            query = createDeleteQuery(type.getSimpleName(), field.get(t).toString());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

}
