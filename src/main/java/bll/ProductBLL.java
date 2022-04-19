package bll;

import dao.ProductDAO;
import model.Product;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class containing all the operations on products
 */
public class ProductBLL {
    private ProductDAO productDAO;

    public ProductBLL() {
        productDAO = new ProductDAO();
    }

    /**
     * Finds a product by id
     * @param id
     * @return
     */
    public Product findProductById(int id) {
        Product st = productDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The product with id =" + id + " was not found!");
        }
        return st;
    }

    /**
     * Returns all the products from the database
     * @return
     */
    public List<Product> findProducts() {
        List<Product> Cl = productDAO.findAll();
        return Cl;
    }

    /**
     * Inserts a product into the database
     * @param p
     */
    public void insert(Product p) {
        productDAO.insert(p);
    }

    /**
     * Updates a product in the database
     *
     * @param p
     */
    public void update(Product p) {
        productDAO.update(p);
    }

    /**
     * Deletes a product from the database
     * @param p
     */
    public void delete(Product p) {
        productDAO.delete(p);
    }
}
