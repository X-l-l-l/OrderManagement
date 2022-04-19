package model;

/**
 * Class that implements the order
 * <p>
 *     Only contains constructors, getters, setters and toString
 * </p>
 * @author Rares Apreutesei
 */
public class Orders {
    private int id;
    private int id_client;
    private int id_product;
    private int quantity;

    public Orders() {

    }

    public Orders(int id, int id_client, int id_product, int quantity) {
        this.id = id;
        this.id_client = id_client;
        this.id_product = id_product;
        this.quantity = quantity;
    }

    public Orders(int id_client, int id_product, int quantity) {
        this.id_client = id_client;
        this.id_product = id_product;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order[" +
                "id=" + id +
                ", id_client=" + id_client +
                ", id_product=" + id_product +
                ", quantity=" + quantity +
                ']';
    }
}
