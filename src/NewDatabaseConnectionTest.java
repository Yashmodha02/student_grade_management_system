import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException;

public class NewDatabaseConnectionTest {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_grade_management"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "host"; 

    public static void main(String[] args) {
        testDatabaseConnection();
    }

    private static void testDatabaseConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            if (connection != null) {
                System.out.println("Database connection successful!");
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
