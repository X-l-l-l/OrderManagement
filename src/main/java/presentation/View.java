package presentation;

import bll.ClientBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import model.Client;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Class that creates the graphical interface
 * @author Rares Apreutesei
 */
public class View {
    private JFrame frame;
    private JPanel mainPanel;
    private JTable table;
    private JPanel tablePanel;
    private JPanel titlePanel;
    private JLabel title;
    private JButton insertButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JComboBox tabelComboBox;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton executeButton;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JLabel label5;
    private JLabel label6;

    public JComboBox getTabelComboBox() {
        return tabelComboBox;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTextField getTextField1() {
        return textField1;
    }

    public JTextField getTextField2() {
        return textField2;
    }

    public JTextField getTextField3() {
        return textField3;
    }

    public JTextField getTextField4() {
        return textField4;
    }

    public JButton getExecuteButton() {
        return executeButton;
    }
    public JFrame getFrame() {
        return frame;
    }

    public JTable getTable() {
        return table;
    }

    public JPanel getTablePanel() {
        return tablePanel;
    }

    public JPanel getTitlePanel() {
        return titlePanel;
    }

    public JLabel getTitle() {
        return title;
    }

    public JButton getInsertButton() {
        return insertButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JLabel getLabel1() {
        return label1;
    }

    public JLabel getLabel2() {
        return label2;
    }

    public JLabel getLabel3() {
        return label3;
    }

    public JLabel getLabel4() {
        return label4;
    }

    public JComboBox<String> getComboBox1() {
        return comboBox1;
    }

    public JComboBox<String> getComboBox2() {
        return comboBox2;
    }

    public JLabel getLabel5() {
        return label5;
    }

    public JLabel getLabel6() {
        return label6;
    }

    public View() {
        createClientsTable();
        createInsertMenu();

    }

    /**
     * Creates the client table
     */
    public void createClientsTable() {
        title.setText("Clients");
        ClientBLL cb = new ClientBLL();
        List<Client> clientList = cb.findClients();

        String[][] data = new String[clientList.size()][4];
        int i = 0, j = 0;
        for (Client c :
                clientList) {
            j = 0;
            data[i][j++] = c.getName();
            data[i][j++] = c.getAddress();
            data[i][j++] = c.getEmail();
            data[i][j] = String.valueOf(c.getAge());
            i++;
        }
        String[] columnNames = {"Name", "Address", "Email", "Age"};

        table.setModel(new DefaultTableModel(
                data, columnNames
        ));
    }

    /**
     * Creates the product table
     */
    public void createProductsTable() {
        title.setText("Products");
        ProductBLL cb = new ProductBLL();
        List<Product> productList = cb.findProducts();

        String[][] data = new String[productList.size()][3];
        int i = 0, j = 0;
        for (Product p :
                productList) {
            j = 0;
            data[i][j++] = p.getName();
            data[i][j++] = String.valueOf(p.getPrice());
            data[i][j] = String.valueOf(p.getStock());
            i++;
        }
        String[] columnNames = {"Name", "Price", "Stock"};

        table.setModel(new DefaultTableModel(
                data, columnNames
        ));
    }

    /**
     * Create the orders table
     */
    public void createOrdersTable() {
        title.setText("Orders");
        OrderBLL cb = new OrderBLL();
        List<String> list = cb.joinOrders();

        String[][] data = new String[list.size() / 4][3];
        int j = 1;
        for (int i = 0; i < list.size() / 4; i++) {
            data[i][0] = list.get(i + (j++));
            data[i][1] = list.get(i + (j++));
            data[i][2] = list.get(i + (j++));
        }
        String[] columnNames = {"Client", "Product", "Quantity"};

        table.setModel(new DefaultTableModel(
                data, columnNames
        ));
    }

    /**
     * Creates the insert menu based on the selected table
     */
    public void createInsertMenu() {
        if (tabelComboBox.getSelectedItem().toString().equals("Clients")) {
            label1.setVisible(true);
            label2.setVisible(true);
            label3.setVisible(true);
            label4.setVisible(true);
            label5.setVisible(false);
            label6.setVisible(false);
            textField1.setVisible(true);
            textField2.setVisible(true);
            textField3.setVisible(true);
            textField4.setVisible(true);
            comboBox1.setVisible(false);
            comboBox2.setVisible(false);
            label1.setText("Name");
            label2.setText("Address");
            label3.setText("Email");
            label4.setText("Age");
        } else if (tabelComboBox.getSelectedItem().toString().equals("Products")) {
            label1.setVisible(true);
            label2.setVisible(true);
            label3.setVisible(true);
            label4.setVisible(false);
            label5.setVisible(false);
            label6.setVisible(false);
            textField1.setVisible(true);
            textField2.setVisible(true);
            textField3.setVisible(true);
            textField4.setVisible(false);
            comboBox1.setVisible(false);
            comboBox2.setVisible(false);
            label1.setText("Name");
            label2.setText("Price");
            label3.setText("Stock");
        } else if (tabelComboBox.getSelectedItem().toString().equals("Orders")) {
            comboBox1.removeAllItems();
            comboBox2.removeAllItems();
            label1.setVisible(true);
            label2.setVisible(false);
            label3.setVisible(false);
            label4.setVisible(false);
            label5.setVisible(true);
            label6.setVisible(true);
            textField1.setVisible(true);
            textField2.setVisible(false);
            textField3.setVisible(false);
            textField4.setVisible(false);
            comboBox1.setVisible(true);
            comboBox2.setVisible(true);
            label1.setText("Quantity");
            label5.setText("Client");
            label6.setText("Product");
            ClientBLL cbll = new ClientBLL();
            List<Client> clist = cbll.findClients();
            for (Client c : clist) {
                comboBox1.addItem(c.getName());
            }
            ProductBLL pbll = new ProductBLL();
            List<Product> plist = pbll.findProducts();
            for (Product p : plist) {
                comboBox2.addItem(p.getName());
            }
        }
        executeButton.setText("Insert");
    }

    /**
     * Creates the update menu based on the selected table
     */
    public void createUpdateMenu() {
        if (tabelComboBox.getSelectedItem().toString().equals("Clients")) {
            label1.setVisible(true);
            label2.setVisible(true);
            label3.setVisible(true);
            label4.setVisible(true);
            label5.setVisible(false);
            label6.setVisible(false);
            textField1.setVisible(true);
            textField2.setVisible(true);
            textField3.setVisible(true);
            textField4.setVisible(true);
            comboBox1.setVisible(false);
            comboBox2.setVisible(false);
            label1.setText("Name");
            label2.setText("Address");
            label3.setText("Email");
            label4.setText("Age");
        } else if (tabelComboBox.getSelectedItem().toString().equals("Products")) {
            label1.setVisible(true);
            label2.setVisible(true);
            label3.setVisible(true);
            label4.setVisible(false);
            label5.setVisible(false);
            label6.setVisible(false);
            textField1.setVisible(true);
            textField2.setVisible(true);
            textField3.setVisible(true);
            textField4.setVisible(false);
            comboBox1.setVisible(false);
            comboBox2.setVisible(false);
            label1.setText("Name");
            label2.setText("Price");
            label3.setText("Stock");
        } else if (tabelComboBox.getSelectedItem().toString().equals("Orders")) {
            comboBox1.removeAllItems();
            comboBox2.removeAllItems();
            label1.setVisible(true);
            label2.setVisible(false);
            label3.setVisible(false);
            label4.setVisible(false);
            label5.setVisible(true);
            label6.setVisible(true);
            textField1.setVisible(true);
            textField2.setVisible(false);
            textField3.setVisible(false);
            textField4.setVisible(false);
            comboBox1.setVisible(true);
            comboBox2.setVisible(true);
            label1.setText("Quantity");
            label5.setText("Client");
            label6.setText("Product");
            ClientBLL cbll = new ClientBLL();
            List<Client> clist = cbll.findClients();
            for (Client c : clist) {
                comboBox1.addItem(c.getName());
            }
            ProductBLL pbll = new ProductBLL();
            List<Product> plist = pbll.findProducts();
            for (Product p : plist) {
                comboBox2.addItem(p.getName());
            }
        }
        executeButton.setText("Update");
    }

    /**
     * Creates the delete menu based on the selected table
     */
    public void createDeleteMenu() {
        label1.setVisible(false);
        label2.setVisible(false);
        label3.setVisible(false);
        label4.setVisible(false);
        label5.setVisible(false);
        label6.setVisible(false);
        textField1.setVisible(false);
        textField2.setVisible(false);
        textField3.setVisible(false);
        textField4.setVisible(false);
        comboBox1.setVisible(false);
        comboBox2.setVisible(false);
        executeButton.setText("Delete");
    }
}
