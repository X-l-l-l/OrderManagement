package bll;

import dao.OrderDAO;
import model.Client;
import model.Orders;
import model.Product;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class containing all the operations on orders
 * @author Rares Apreutesei
 */
public class OrderBLL {
    private OrderDAO orderDAO;

    public OrderBLL() {
        orderDAO = new OrderDAO();
    }

    /**
     * Finds an order by id
     * @param id
     * @return
     */
    public Orders findOrderById(int id) {
        Orders st = orderDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The student with id =" + id + " was not found!");
        }
        return st;
    }

    /**
     * Returns all the orders from the database
     * @return
     */
    public List<Orders> findOrders() {
        List<Orders> Cl = orderDAO.findAll();
        return Cl;
    }

    /**
     * Inserts an order into the database
     * @param c
     */
    public void insert(Orders c) {
        orderDAO.insert(c);
    }

    /**
     * Updates an order from the database
     * @param c
     * @return
     */
    public Orders update(Orders c) {
        return orderDAO.update(c);
    }

    /**
     * Returns the rezult of a join select on the orders table
     * @return
     */
    public List<String> joinOrders() {
        return orderDAO.joinOrders();
    }

    /**
     * Deletes an order from the database
     * @param o
     */
    public void delete(Orders o) {
        orderDAO.delete(o);
    }

    /**
     * Creates a pdf file containing the Client and Product data of an order
     * @param c
     * @param p
     * @throws IOException
     */
    public void createBill(Client c, Product p) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream cont = new PDPageContentStream(document, page);

        cont.beginText();

        cont.setFont(PDType1Font.TIMES_ROMAN, 20);
        cont.setLeading(14.5f);
        cont.newLineAtOffset(25, 700);

        cont.showText("Bill");
        cont.newLine();

        cont.setFont(PDType1Font.TIMES_ROMAN, 12);

        String line1 = "Client: " + c.getName() + ", age " + c.getAge() + ", address " + c.getAddress() + ", email " + c.getEmail();
        String line2 = "Product: " + p.getName() + ", price " + p.getPrice();
        cont.showText(line1);


        cont.newLine();
        cont.showText(line2);
        cont.endText();

        cont.close();

        String title = "Bill "+c.getName() + " " + p.getName();

        document.save("bills/"+title+".pdf");
        document.close();
    }

}
