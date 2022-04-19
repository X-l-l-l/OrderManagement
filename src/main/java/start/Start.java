package start;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import bll.ClientBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import model.Client;
import model.Product;
import presentation.Controller;

/**
 * Starts the app
 */
public class Start {
    protected static final Logger LOGGER = Logger.getLogger(Start.class.getName());

    public static void main(String[] args) {

        Controller.main(null);

    }

}
