import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.servlet.annotation.WebServlet;
import java.util.Properties;

@WebServlet("/ForgotServlet")
public class ForgotServlet extends HttpServlet {
    
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
        
        String email = request.getParameter("email");
        String newPass = request.getParameter("newpass");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Properties dbConfig = loadDbConfig();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbConfig.getProperty("db.url"), dbConfig.getProperty("db.username"), dbConfig.getProperty("db.password"));

            // Practical sathi simple update query
            PreparedStatement ps = con.prepareStatement("UPDATE users SET password=? WHERE email=?");
            ps.setString(1, newPass);
            ps.setString(2, email);

            int result = ps.executeUpdate();

            if(result > 0) {
                out.println("<script>alert('Password Reset Success!'); window.location='chatterbox.html';</script>");
            } else {
                out.println("<script>alert('Email not found!'); window.location='forgot.html';</script>");
            }
            con.close();
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}