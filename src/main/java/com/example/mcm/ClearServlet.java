import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.util.Properties;

@WebServlet("/clearChat")
public class ClearServlet extends HttpServlet {
    
    private Properties loadDbConfig() {
        Properties props = new Properties();
        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/classes/dbconfig.properties")) {
            if (input == null) {
                props.setProperty("db.url", "jdbc:mysql://localhost:3306/chatterbox_db");
                props.setProperty("db.username", "root");
                props.setProperty("db.password", "mysql");
            } else {
                props.load(input);
            }
        } catch (IOException e) {
            props.setProperty("db.url", "jdbc:mysql://localhost:3306/chatterbox_db");
            props.setProperty("db.username", "root");
            props.setProperty("db.password", "mysql");
        }
        return props;
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        String tabId = request.getParameter("tabId"); // Frontend kadun tabId ghetla
        
        String userFullName = null;

        if (session != null) {
            // Tab-specific user fetch kara
            userFullName = (String) session.getAttribute("user_" + tabId);
            
            // Jar tab-specific nasel tar global check kara
            if (userFullName == null) {
                userFullName = (String) session.getAttribute("user");
            }
        }

        if (userFullName == null) {
            response.sendRedirect("chatdome.html");
            return;
        }

        try {
            Properties dbConfig = loadDbConfig();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbConfig.getProperty("db.url"), dbConfig.getProperty("db.username"), dbConfig.getProperty("db.password"));

            // Logic: Fakt login aslelya user che messages delete kara
            String sql = "DELETE FROM messages WHERE LOWER(TRIM(sender)) = LOWER(TRIM(?))"; 
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, userFullName); 
            
            pst.executeUpdate();
            con.close();

            // Redirect kartana tabId maintain kara
            response.sendRedirect("chatdome.html?tabId=" + (tabId != null ? tabId : ""));

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("chatdome.html");
        }
    }
}