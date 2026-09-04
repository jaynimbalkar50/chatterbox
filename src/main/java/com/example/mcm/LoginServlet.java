import java.io.*;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.util.Properties;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    
    // Multi-tab support sathi static Map
    private static final Map<String, String> tabUserMap = new ConcurrentHashMap<>();

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        String tabId = request.getParameter("tabId"); // Frontend kadun yenara Unique ID

        try {
            Properties dbConfig = loadDbConfig();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbConfig.getProperty("db.url"), dbConfig.getProperty("db.username"), dbConfig.getProperty("db.password")); 
            
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            ps.setString(1, user);
            ps.setString(2, pass);
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                String fullNameFromDB = rs.getString("fullname");
                HttpSession session = request.getSession();
                
                // 1. Global user set kela (Existing Logic)
                session.setAttribute("user", fullNameFromDB);
                
                // 2. Tab-specific user set kela (Guest Issue Solution)
                if (tabId != null && !tabId.isEmpty()) {
                    session.setAttribute("user_" + tabId, fullNameFromDB);
                }

                // Redirect logic based on Role
                if(user.equalsIgnoreCase("admin")){
                    response.sendRedirect("adminutility.html");
                } else {    
                    // TabId sobat redirect 
                    response.sendRedirect("chatdome.html?tabId=" + (tabId != null ? tabId : ""));
                }
            } else {
                response.sendRedirect("chatterbox.html?error=1");
            }
            con.close();
        } catch(Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}