# ChatterBox

A modern, ephemeral chat application built with Java Servlets and MySQL. Messages automatically disappear after 3 minutes, ensuring privacy and reducing digital clutter.

![Java](https://img.shields.io/badge/Java-17-orange)
![Servlet](https://img.shields.io/badge/Servlet-4.0-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Tomcat](https://img.shields.io/badge/Tomcat-9.0-red)

## Features

- **Ephemeral Messaging**: Messages auto-delete after 3 minutes
- **User Authentication**: Registration, login, and password recovery
- **Real-time Chat**: Live messaging with emoji support
- **Multi-tab Support**: Handle multiple browser tabs simultaneously
- **Admin Panel**: Control auto-delete bot and manage message backups
- **Message Backup**: Deleted messages are automatically backed up
- **Responsive Design**: Modern UI that works on all devices
- **Session Management**: Secure user sessions with timeout handling

## Tech Stack

- **Backend**: Java Servlets 4.0, JDBC
- **Frontend**: HTML5, CSS3, JavaScript
- **Database**: MySQL 8.0 / MariaDB
- **Server**: Apache Tomcat 9.0
- **Build Tool**: Maven

## Prerequisites

- JDK 8 or higher
- Apache Tomcat 9.0 or higher
- MySQL 8.0+ or MariaDB 10.3+
- Maven 3.6+ (for building)

## Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/chatterbox.git
cd chatterbox
```

### 2. Build with Maven
```bash
mvn clean package
```

### 3. Setup Database
```bash
mysql -u root -p < database/chatterbox_db.sql
```

### 4. Configure Database
Edit `src/main/webapp/WEB-INF/classes/dbconfig.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/chatterbox_db
db.username=root
db.password=your_password
```

### 5. Enable MySQL Event Scheduler
```sql
SET GLOBAL event_scheduler = ON;
```

### 6. Deploy to Tomcat
Copy the generated WAR file to Tomcat's webapps directory:
```bash
cp target/chatterbox.war $TOMCAT_HOME/webapps/
```

### 7. Start Tomcat
```bash
$TOMCAT_HOME/bin/startup.sh  # Linux/Mac
$TOMCAT_HOME/bin/startup.bat # Windows
```

### 8. Access the Application
Open your browser and navigate to:
```
http://localhost:8080/chatterbox/
```

## Default Users

The database comes with pre-configured test users:

| Username | Email | Password | Role |
|----------|-------|----------|------|
| admin | admin@chatterbox.com | whyadmin | Admin |
| jay | jay@xyz.com | bhendi | User |
| anuj | xyz@gmail.com | anju | User |
| rohit | rohit@chatterbox.com | balya | User |

## Project Structure

```
chatterbox/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── mcm/
│       │               ├── ChatServlet.java
│       │               ├── ClearServlet.java
│       │               ├── ControlPanel.java
│       │               ├── ForgotServlet.java
│       │               ├── GetMessagesServlet.java
│       │               ├── LoginServlet.java
│       │               └── RegisterServlet.java
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── web.xml
│           │   ├── classes/
│           │   │   └── dbconfig.properties
│           │   └── lib/
│           ├── adminutility.html
│           ├── chatdome.html
│           ├── chatterbox.html
│           ├── forgot.html
│           └── signup.html
├── database/
│   └── chatterbox_db.sql
├── docs/
│   └── SETUP_GUIDE.md
├── pom.xml
├── .gitignore
└── README.md
```

## API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/RegisterServlet` | POST | User registration |
| `/LoginServlet` | POST | User authentication |
| `/ChatServlet` | POST | Send chat message |
| `/GetMessagesServlet` | GET | Retrieve chat messages |
| `/clearChat` | GET | Clear user's messages |
| `/ForgotServlet` | POST | Password recovery |
| `/ControlPanel` | POST | Admin control panel |

## Configuration

### Database Configuration
Update `src/main/webapp/WEB-INF/classes/dbconfig.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/chatterbox_db
db.username=your_mysql_username
db.password=your_mysql_password
```

### Auto-Delete Configuration
The auto-delete feature uses MySQL events. To modify the deletion interval:
```sql
ALTER EVENT auto_delete_history ON SCHEDULE EVERY 1 MINUTE
```

## Development

### Running in Development Mode
1. Configure Tomcat in your IDE (IntelliJ IDEA/Eclipse)
2. Add the project as a web application
3. Run the Tomcat server

### Building from Source
```bash
mvn clean install
```

### Running Tests
```bash
mvn test
```

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running
- Check credentials in `dbconfig.properties`
- Ensure database `chatterbox_db` exists

### Auto-Delete Not Working
- Enable MySQL event scheduler: `SET GLOBAL event_scheduler = ON;`
- Check event status: `SHOW EVENTS FROM chatterbox_db;`

### Servlet Not Found Errors
- Ensure WAR file is properly deployed
- Check Tomcat logs for errors
- Verify `web.xml` configuration

## Security Considerations

- Passwords are stored in plaintext (should be hashed in production)
- Database credentials are in properties file (use environment variables in production)
- No HTTPS support (configure SSL in production)
- Basic session management (consider JWT for production)

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- MySQL Connector/J for database connectivity
- Apache Tomcat for servlet container
- Font Awesome for icons
- Confetti.js for celebration effects

## Contact

For questions or support, please open an issue on GitHub or contact [jaynimbalkar50@gmail.com].

---

**Note**: This is a demonstration project. For production use, implement proper security measures including password hashing, SSL/TLS, and input validation.
