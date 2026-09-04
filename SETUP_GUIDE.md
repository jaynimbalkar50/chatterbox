# ChatterBox Project Setup Guide

## Prerequisites
- Java Development Kit (JDK) 8 or higher
- Apache Tomcat 8.5 or higher
- MySQL 5.7+ or MariaDB 10.3+

## Database Setup

### 1. Create Database
```bash
mysql -u root -p < chatterbox_db.sql
```

### 2. Configure Database Credentials
Edit `webapps/mcm_project/WEB-INF/classes/dbconfig.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/chatterbox_db
db.username=your_mysql_username
db.password=your_mysql_password
```

### 3. Enable MySQL Event Scheduler (Required for Auto-Delete Feature)
The project includes an auto-delete feature that removes messages older than 3 minutes. This requires MySQL event scheduler to be enabled.

#### Check Current Status
```sql
SHOW VARIABLES LIKE 'event_scheduler';
```

#### Enable Event Scheduler
**Temporary (until MySQL restart):**
```sql
SET GLOBAL event_scheduler = ON;
```

**Permanent (add to MySQL configuration):**
- Windows: Add `event_scheduler=ON` to `my.ini` file under `[mysqld]` section
- Linux/Mac: Add `event_scheduler=ON` to `my.cnf` file under `[mysqld]` section

#### Restart MySQL after permanent configuration change.

## Application Deployment

### Option 1: Using Tomcat
1. Copy `webapps/mcm_project` folder to Tomcat's `webapps` directory
2. Start Tomcat:
   - Windows: `bin/startup.bat`
   - Linux/Mac: `bin/startup.sh`
3. Access application: `http://localhost:8080/mcm_project`

### Option 2: Using IntelliJ IDEA
1. Open project in IntelliJ
2. Configure Tomcat server in Run/Debug Configurations
3. Deploy the `mcm_project` webapp
4. Run the Tomcat configuration

## Recompiling Servlets

After making changes to servlet source files, you need to recompile them:

### Manual Compilation
```bash
cd src/java/com/example/mcm
javac -cp "../../../..;../../../../../webapps/mcm_project/WEB-INF/lib/mysql-connector-j-8.0.33.jar;../../../../../webapps/mcm_project/WEB-INF/lib/servlet-api.jar" -d ../../../../webapps/mcm_project/WEB-INF/classes *.java
```

### Using IDE
1. Open the project in IntelliJ IDEA or Eclipse
2. Build the project (Build > Build Project)
3. Ensure the compiled classes are copied to `webapps/mcm_project/WEB-INF/classes`

## Default Users
The database comes with pre-configured test users:
- admin / admin@chatterbox.com / whyadmin (Admin access)
- jay / jay@xyz.com / bhendi
- anuj / xyz@gmail.com / anju
- rohit / rohit@chatterbox.com / balya

## Features
- User registration and login
- Real-time chat with emoji support
- Multi-tab session support
- Auto-delete messages (3 minutes)
- Message backup functionality
- Admin control panel for managing auto-delete feature
- Password recovery

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running
- Check credentials in `dbconfig.properties`
- Ensure database `chatterbox_db` exists
- Test connection: `mysql -u username -p chatterbox_db`

### Servlet Not Found Errors
- Ensure servlets are compiled and in `WEB-INF/classes`
- Check `web.xml` has proper servlet mappings
- Verify Tomcat is properly configured

### Auto-Delete Not Working
- Verify MySQL event scheduler is enabled: `SHOW VARIABLES LIKE 'event_scheduler'`
- Check if the event exists: `SHOW EVENTS FROM chatterbox_db;`
- Ensure event scheduler is permanently enabled in MySQL config
