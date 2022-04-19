package presentation;

import bll.ClientBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import bll.validator.EmailValidator;
import bll.validator.Validator;
import model.Client;
import model.Orders;
import model.Product;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * Enum that helps with remembering the previous operation executed
 *
 * @author Rares Apreutesei
 */
enum Operation {
    INSERT, UPDATE, DELETE
}

/**
 * The controller for the GUI
 */
public class Controller {
    static Operation op = Operation.INSERT;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }
        });
    }

    /**
     * Determines what the GUI looks like and handles all the actions on buttons and other input components
     */
    private static void createGUI() {
        View ui = new View();
        JPanel root = ui.getMainPanel();
        JFrame frame = new JFrame();
        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //CREATE MENU
        ui.getTabelComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ui.getTabelComboBox().getSelectedItem().toString().equals("Clients")) {
                    ui.createClientsTable();
                    if (op == Operation.INSERT)
                        ui.createInsertMenu();
                    else if (op == Operation.DELETE)
                        ui.createDeleteMenu();
                    else if (op == Operation.UPDATE)
                        ui.createUpdateMenu();
                } else if (ui.getTabelComboBox().getSelectedItem().toString().equals("Products")) {
                    ui.createProductsTable();
                    if (op == Operation.INSERT)
                        ui.createInsertMenu();
                    else if (op == Operation.DELETE)
                        ui.createDeleteMenu();
                    else if (op == Operation.UPDATE)
                        ui.createUpdateMenu();
                } else if (ui.getTabelComboBox().getSelectedItem().toString().equals("Orders")) {
                    ui.createOrdersTable();
                    if (op == Operation.INSERT)
                        ui.createInsertMenu();
                    else if (op == Operation.DELETE)
                        ui.createDeleteMenu();
                    else if (op == Operation.UPDATE)
                        ui.createUpdateMenu();
                }
            }
        });


        //DELETE BUTTON
        ui.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.createDeleteMenu();
                op = Operation.DELETE;
            }
        });

        //INSERT BUTTON
        ui.getInsertButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.createInsertMenu();
                op = Operation.INSERT;
            }
        });

        //UPDATE BUTTON
        ui.getUpdateButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.createUpdateMenu();
                op = Operation.UPDATE;
            }
        });

        //EXECUTION BUTTON
        ui.getExecuteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (op == Operation.INSERT) {

                    if (ui.getTabelComboBox().getSelectedItem().toString().equals("Clients")) {
                        ClientBLL bll = new ClientBLL();
                        Validator validator = new EmailValidator();
                        if (!validator.validate(ui.getTextField3().getText()))
                            JOptionPane.showMessageDialog(null, "Invalid email!", "Error", JOptionPane.INFORMATION_MESSAGE);
                        else {
                            if (Integer.parseInt(ui.getTextField4().getText()) < 0)
                                JOptionPane.showMessageDialog(null, "Invalid age!", "Error", JOptionPane.INFORMATION_MESSAGE);
                            else {
                                bll.insert(new Client(ui.getTextField1().getText(), ui.getTextField2().getText(), ui.getTextField3().getText(), Integer.parseInt(ui.getTextField4().getText())));
                                ui.createClientsTable();
                                ui.getTextField1().setText("");
                                ui.getTextField2().setText("");
                                ui.getTextField3().setText("");
                                ui.getTextField4().setText("");
                            }
                        }
                    } else if (ui.getTabelComboBox().getSelectedItem().toString().equals("Products")) {
                        ui.createProductsTable();
                        ProductBLL bll = new ProductBLL();
                        if (Integer.parseInt(ui.getTextField2().getText()) < 0)
                            JOptionPane.showMessageDialog(null, "Invalid price!", "Error", JOptionPane.INFORMATION_MESSAGE);
                        else {
                            if (Integer.parseInt(ui.getTextField3().getText()) < 0)
                                JOptionPane.showMessageDialog(null, "Invalid stock!", "Error", JOptionPane.INFORMATION_MESSAGE);
                            else {
                                bll.insert(new Product(ui.getTextField1().getText(), Integer.parseInt(ui.getTextField2().getText()), Integer.parseInt(ui.getTextField3().getText())));
                                ui.createProductsTable();
                                ui.getTextField1().setText("");
                                ui.getTextField2().setText("");
                                ui.getTextField3().setText("");
                            }
                        }
                    } else if (ui.getTabelComboBox().getSelectedItem().toString().equals("Orders")) {
                        ui.createOrdersTable();
                        OrderBLL obll = new OrderBLL();
                        ProductBLL pbll = new ProductBLL();
                        ClientBLL cbll = new ClientBLL();
                        List<Client> cList = cbll.findClients();
                        List<Product> pList = pbll.findProducts();
                        if (Integer.parseInt(ui.getTextField1().getText()) < 0) {
                            JOptionPane.showMessageDialog(null, "Invalid input!", "Error", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            if (pList.get(ui.getComboBox2().getSelectedIndex()).getStock() > Integer.parseInt(ui.getTextField1().getText())) {
                                obll.insert(new Orders(cList.get(ui.getComboBox1().getSelectedIndex()).getId(), pList.get(ui.getComboBox2().getSelectedIndex()).getId(), Integer.parseInt(ui.getTextField1().getText())));
                                pbll.update(new Product(pList.get(ui.getComboBox2().getSelectedIndex()).getId(), pList.get(ui.getComboBox2().getSelectedIndex()).getName(), pList.get(ui.getComboBox2().getSelectedIndex()).getPrice(), pList.get(ui.getComboBox2().getSelectedIndex()).getStock() - Integer.parseInt(ui.getTextField1().getText())));
                                try {

                                    obll.createBill(cList.get(ui.getComboBox1().getSelectedIndex()), pList.get(ui.getComboBox2().getSelectedIndex()));
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            } else
                                JOptionPane.showMessageDialog(null, "Insuficient stock!", "Error", JOptionPane.INFORMATION_MESSAGE);
                            ui.createOrdersTable();
                            ui.getTextField1().setText("");
                        }
                    }
                } else if (op == Operation.DELETE) {
                    if (ui.getTabelComboBox().getSelectedItem().toString().equals("Clients")) {
                        ClientBLL bll = new ClientBLL();
                        List<Client> cList = bll.findClients();
                        bll.delete(cList.get(ui.getTable().getSelectedRow()));
                        ui.createClientsTable();

                    } else if (ui.getTabelComboBox().getSelectedItem().toString().equals("Products")) {
                        ProductBLL bll = new ProductBLL();
                        List<Product> pList = bll.findProducts();
                        bll.delete(pList.get(ui.getTable().getSelectedRow()));
                        ui.createProductsTable();
                    } else if (ui.getTabelComboBox().getSelectedItem().toString().equals("Orders")) {
                        OrderBLL bll = new OrderBLL();
                        List<Orders> oList = bll.findOrders();
                        bll.delete(oList.get(ui.getTable().getSelectedRow()));
                        ui.createOrdersTable();
                    }
                } else if (op == Operation.UPDATE) {
                    Validator validator = new EmailValidator();
                    if (ui.getTabelComboBox().getSelectedItem().toString().equals("Clients")) {
                        ClientBLL bll = new ClientBLL();
                        List<Client> cList = bll.findClients();
                        String name, address, email, age;

                        if (!ui.getTextField1().getText().equals(""))
                            name = ui.getTextField1().getText();
                        else
                            name = cList.get(ui.getTable().getSelectedRow()).getName();

                        if (!ui.getTextField2().getText().equals(""))
                            address = ui.getTextField2().getText();
                        else
                            address = cList.get(ui.getTable().getSelectedRow()).getAddress();

                        if (!ui.getTextField3().getText().equals(""))
                            email = ui.getTextField3().getText();
                        else
                            email = cList.get(ui.getTable().getSelectedRow()).getEmail();

                        if (!ui.getTextField4().getText().equals(""))
                            age = ui.getTextField4().getText();
                        else
                            age = String.valueOf(cList.get(ui.getTable().getSelectedRow()).getAge());

                        if (!validator.validate(email))
                            JOptionPane.showMessageDialog(null, "Invalid email!", "Error", JOptionPane.INFORMATION_MESSAGE);
                        else {
                            if (Integer.parseInt(age) < 0)
                                JOptionPane.showMessageDialog(null, "Invalid age!", "Error", JOptionPane.INFORMATION_MESSAGE);
                            else {
                                bll.update(new Client(cList.get(ui.getTable().getSelectedRow()).getId(), name, address, email, Integer.parseInt(age)));
                                ui.createClientsTable();
                            }
                        }
                    } else if (ui.getTabelComboBox().getSelectedItem().toString().equals("Products")) {
                        ProductBLL bll = new ProductBLL();
                        List<Product> pList = bll.findProducts();
                        String name, price, stock;

                        if (!ui.getTextField1().getText().equals(""))
                            name = ui.getTextField1().getText();
                        else
                            name = pList.get(ui.getTable().getSelectedRow()).getName();

                        if (!ui.getTextField2().getText().equals(""))
                            price = ui.getTextField2().getText();
                        else
                            price = String.valueOf(pList.get(ui.getTable().getSelectedRow()).getPrice());

                        if (!ui.getTextField3().getText().equals(""))
                            stock = ui.getTextField3().getText();
                        else
                            stock = String.valueOf(pList.get(ui.getTable().getSelectedRow()).getStock());

                        if (Integer.parseInt(ui.getTextField2().getText()) < 0)
                            JOptionPane.showMessageDialog(null, "Invalid price!", "Error", JOptionPane.INFORMATION_MESSAGE);
                        else {
                            if (Integer.parseInt(ui.getTextField3().getText()) < 0)
                                JOptionPane.showMessageDialog(null, "Invalid stock!", "Error", JOptionPane.INFORMATION_MESSAGE);
                            else {
                                bll.update(new Product(pList.get(ui.getTable().getSelectedRow()).getId(), name, Integer.parseInt(price), Integer.parseInt(stock)));
                                ui.createProductsTable();
                            }
                        }
                    } else if (ui.getTabelComboBox().getSelectedItem().toString().equals("Orders")) {
                        OrderBLL obll = new OrderBLL();
                        ProductBLL pbll = new ProductBLL();
                        ClientBLL cbll = new ClientBLL();
                        List<Client> cList = cbll.findClients();
                        List<Product> pList = pbll.findProducts();
                        List<Orders> oList = obll.findOrders();
                        obll.update(new Orders(oList.get(ui.getTable().getSelectedRow()).getId(), cList.get(ui.getComboBox1().getSelectedIndex()).getId(), pList.get(ui.getComboBox2().getSelectedIndex()).getId(), Integer.parseInt(ui.getTextField1().getText())));
                        ui.createOrdersTable();
                        ui.getTextField1().setText("");
                    }
                }
            }
        });
    }
}

