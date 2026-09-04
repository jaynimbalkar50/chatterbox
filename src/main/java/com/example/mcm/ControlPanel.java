import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.util.Properties;

@WebServlet("/ControlPanel")
public class ControlPanel extends HttpServlet {
    
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
        // [F1, F2, F3] 
        String action = request.getParameter("action");
        
        
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        
        StringBuilder resultData = new StringBuilder();

        try {
            Properties dbConfig = loadDbConfig();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbConfig.getProperty("db.url"), dbConfig.getProperty("db.username"), dbConfig.getProperty("db.password"));
            Statement stmt = con.createStatement();

            // २. [F1] TOGGLE BOT (MySQL Event on/off)
            if ("toggleBot".equals(action)) {
                
                ResultSet rs = stmt.executeQuery("SHOW VARIABLES LIKE 'event_scheduler'");
                if (rs.next()) {
                    String currentStatus = rs.getString("Value");
                    if ("ON".equalsIgnoreCase(currentStatus)) {
                        stmt.execute("SET GLOBAL event_scheduler='OFF'");
                        resultData.append("SYSTEM ALERT: AUTO-DELETE BOT HAS BEEN DISABLED.\n");
                    } else {
                        stmt.execute("SET GLOBAL event_scheduler='ON'");
                        resultData.append("SYSTEM ALERT: AUTO-DELETE BOT IS NOW ACTIVE.\n");
                    }
                }
            }

            // ३. [F2] FETCH DATA (backup_messages)
            else if ("fetchData".equals(action)) {
                ResultSet rs = stmt.executeQuery("SELECT sender, msg, msg_time,deleted_at FROM messages_backup ORDER BY msg_time DESC");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    resultData.append("[").append(rs.getString("msg_time")).append("] ")
				.append("[").append(rs.getString("deleted_at")).append("] ")
                              .append(rs.getString("sender")).append(": ")
                              .append(rs.getString("msg")).append("\n");
				
                }
                if (!found) {
                    resultData.append("--- NO DELETED LOGS FOUND IN DATABASE ---");
                }
            }

            // ४. [F3] MANUALLY PURGE 
            else if ("purgeData".equals(action)) {
                stmt.executeUpdate("TRUNCATE TABLE messages_backup");
                resultData.append("SYSTEM NOTIFICATION: BACKUP DATABASE PURGED SUCCESSFULLY.");
            }

            con.close();
        } catch (Exception e) {
            resultData.append("CRITICAL SYSTEM ERROR: ").append(e.getMessage());
        }

        
        out.print(resultData.toString());
        out.flush();
    }
}