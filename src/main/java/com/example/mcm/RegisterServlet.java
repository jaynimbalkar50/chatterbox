import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.util.Properties;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private Properties loadDbConfig() {
        Properties props = new Properties();
        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/classes/dbconfig.properties")) {
            if (input == null) {
                // Fallback to default values if file not found
                props.setProperty("db.url", "jdbc:mysql://localhost:3306/chatterbox_db");
                props.setProperty("db.username", "root");
                props.setProperty("db.password", "mysql");
            } else {
                props.load(input);
            }
        } catch (IOException e) {
            // Fallback to default values on error
            props.setProperty("db.url", "jdbc:mysql://localhost:3306/chatterbox_db");
            props.setProperty("db.username", "root");
            props.setProperty("db.password", "mysql");
        }
        return props;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Form madhun data ghene
        String fullname = request.getParameter("fullname");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // Database connection
            Properties dbConfig = loadDbConfig();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbConfig.getProperty("db.url"), dbConfig.getProperty("db.username"), dbConfig.getProperty("db.password"));
            
            // Query prepare karne
            PreparedStatement ps = con.prepareStatement("INSERT INTO users(fullname, username, email, password) VALUES(?,?,?,?)");
            ps.setString(1, fullname);
            ps.setString(2, username);
            ps.setString(3, email);
            ps.setString(4, password);
            
            int status = ps.executeUpdate();
            if(status > 0) {
                // Success jhale ki login page (chatterbox.html) var pathva
                response.sendRedirect("chatterbox.html");
            }
            con.close();
        } catch(Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}