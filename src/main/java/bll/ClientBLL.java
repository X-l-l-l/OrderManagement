package bll;

import dao.ClientDAO;
import model.Client;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class containing all the database operations for the client class
 * @author Rares Apreutesei
 */
public class ClientBLL {
    private ClientDAO clientDAO;

    public ClientBLL() {
        clientDAO = new ClientDAO();
    }

    /**
     * Finds client by id
     * @param id
     * @return
     */

    public Client findClientById(int id) {
        Client st = clientDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return st;
    }

    /**
     * Returns all the clients from the database
     * @return
     */
    public List<Client> findClients() {
        List<Client> Cl = clientDAO.findAll();
        return Cl;
    }

    /**
     * Inserts a client into the database
     * @param c
     */
    public void insert(Client c) {
        clientDAO.insert(c);
    }

    /**
     * Updates a client from the database
     * @param c
     * @return
     */
    public Client update(Client c) {
        return clientDAO.update(c);
    }

    /**
     * Deletes a client from the database
     * @param c
     */
    public void delete(Client c) {
        clientDAO.delete(c);
    }
}
