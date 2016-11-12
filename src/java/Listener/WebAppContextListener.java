
package Listener;

import DataBase.DBManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Luca
 */
public class WebAppContextListener implements ServletContextListener{

    
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        String dburl = sce.getServletContext().getInitParameter("dburl");
        String contextPath = sce.getServletContext().getInitParameter("contextPath");
        String completePath = sce.getServletContext().getInitParameter("completePath");
        try {
            DBManager manager = new DBManager(dburl,contextPath,completePath);
            sce.getServletContext().setAttribute("dbmanager", manager);
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).severe(ex.toString());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            DBManager.shutdown();
            //niente da fare
        } catch (Throwable ex) {
            System.out.println("Failed to stop DataBase: " + ex.toString());
        }
    }
    
}