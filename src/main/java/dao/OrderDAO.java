package dao;

import connection.ConnectionFactory;
import model.Orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Order specific methods
 * @author Rares Apreutesei
 */
public class OrderDAO extends AbstractDAO<Orders> {

    //SELECT orders.id, client.name, product.name FROM orders join client join product where id_client=client.id and id_product=product.id;

    /**
     * Gets the data rezulted from the join select statement
     * @param resultSet
     * @return
     */
    private List<String> getData(ResultSet resultSet) {
        List<String> list = new ArrayList<>();

        while (true) {
            try {
                if (!resultSet.next()) break;
                for (int i = 0; i < 4; i++)
                    list.add(resultSet.getString(i + 1));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return list;
    }

    /**
     * Executes the join select statement
     * @return
     */
    public List<String> joinOrders() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT orders.id, client.name, product.name, orders.quantity FROM orders join client join product where id_client=client.id and id_product=product.id";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return getData(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "OrderDAO:joinOrders " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

}
