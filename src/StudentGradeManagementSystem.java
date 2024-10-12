import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StudentGradeManagementSystem extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_grade_management";
    private static final String USER = "root";
    private static final String PASSWORD = "host";
    private Connection connection;

    // UI Components
    private JTextField studentIdField, nameField, javaMarksField, dbmsMarksField, dsaMarksField, mathsMarksField, webTechMarksField;
    private JTextArea outputArea;

    // Constructor to set up the GUI and database connection
    public StudentGradeManagementSystem() {
        // Set title and size of the window
        setTitle("Student Grade Management System");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Initialize input fields and buttons
        studentIdField = new JTextField(10);
        nameField = new JTextField(10);
        javaMarksField = new JTextField(5);
        dbmsMarksField = new JTextField(5);
        dsaMarksField = new JTextField(5);
        mathsMarksField = new JTextField(5);
        webTechMarksField = new JTextField(5);
        
        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update Student");
        JButton deleteButton = new JButton("Delete Student");
        JButton displayButton = new JButton("Display Marksheet");

        // Output area to display messages
        outputArea = new JTextArea(15, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(460, 300));

        // Add components to the frame
        add(new JLabel("Student ID:"));
        add(studentIdField);
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Java Marks:"));
        add(javaMarksField);
        add(new JLabel("DBMS Marks:"));
        add(dbmsMarksField);
        add(new JLabel("DSA Marks:"));
        add(dsaMarksField);
        add(new JLabel("Maths Marks:"));
        add(mathsMarksField);
        add(new JLabel("Web Tech Marks:"));
        add(webTechMarksField);
        add(addButton);
        add(updateButton);
        add(deleteButton);
        add(displayButton);
        add(scrollPane);

        // Attach action listeners to buttons
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        displayButton.addActionListener(e -> displayMarksheet());

        // Establish database connection
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            outputArea.setText("Error connecting to database: " + e.getMessage());
        }
    }

    // Method to add a new student to the database
    private void addStudent() {
        try {
            String studentId = studentIdField.getText();
            String name = nameField.getText();
            float javaMarks = Float.parseFloat(javaMarksField.getText());
            float dbmsMarks = Float.parseFloat(dbmsMarksField.getText());
            float dsaMarks = Float.parseFloat(dsaMarksField.getText());
            float mathsMarks = Float.parseFloat(mathsMarksField.getText());
            float webTechMarks = Float.parseFloat(webTechMarksField.getText());

            // SQL query to insert a new student
            String query = "INSERT INTO students (student_id, name, java_marks, dbms_marks, dsa_marks, maths_marks, web_tech_marks) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, studentId);
                pstmt.setString(2, name);
                pstmt.setFloat(3, javaMarks);
                pstmt.setFloat(4, dbmsMarks);
                pstmt.setFloat(5, dsaMarks);
                pstmt.setFloat(6, mathsMarks);
                pstmt.setFloat(7, webTechMarks);
                pstmt.executeUpdate(); // Execute the insert operation
                outputArea.setText("Student added successfully!"); // Confirmation message
            }
        } catch (SQLException | NumberFormatException e) {
            outputArea.setText("Error adding student: " + e.getMessage());
        }
    }

    // Method to update an existing student's marks
    private void updateStudent() {
        try {
            String studentId = studentIdField.getText();
            float javaMarks = Float.parseFloat(javaMarksField.getText());
            float dbmsMarks = Float.parseFloat(dbmsMarksField.getText());
            float dsaMarks = Float.parseFloat(dsaMarksField.getText());
            float mathsMarks = Float.parseFloat(mathsMarksField.getText());
            float webTechMarks = Float.parseFloat(webTechMarksField.getText());

            // SQL query to update student marks
            String query = "UPDATE students SET java_marks = ?, dbms_marks = ?, dsa_marks = ?, maths_marks = ?, web_tech_marks = ? WHERE student_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setFloat(1, javaMarks);
                pstmt.setFloat(2, dbmsMarks);
                pstmt.setFloat(3, dsaMarks);
                pstmt.setFloat(4, mathsMarks);
                pstmt.setFloat(5, webTechMarks);
                pstmt.setString(6, studentId);
                int rowsAffected = pstmt.executeUpdate(); // Execute the update operation
                outputArea.setText(rowsAffected > 0 ? "Student updated successfully!" : "No student found with ID: " + studentId);
            }
        } catch (SQLException | NumberFormatException e) {
            outputArea.setText("Error updating student: " + e.getMessage());
        }
    }

    // Method to delete a student from the database
    private void deleteStudent() {
        try {
            String studentId = studentIdField.getText(); // Get the student ID for deletion

            // SQL query to delete the student
            String query = "DELETE FROM students WHERE student_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, studentId); // Set the student ID
                int rowsAffected = pstmt.executeUpdate(); // Execute the delete operation
                outputArea.setText(rowsAffected > 0 ? "Student deleted successfully!" : "No student found with ID: " + studentId);
            }
        } catch (SQLException e) {
            outputArea.setText("Error deleting student: " + e.getMessage());
        }
    }

    // Method to display the marksheet for a specific student
    private void displayMarksheet() {
        if (connection == null) {
            outputArea.setText("Database connection is not established.");
            return; // Exit the method if connection is null
        }
    
        try {
            String studentId = studentIdField.getText(); // Get the student ID for displaying marksheet
    
            // SQL query to retrieve student details
            String query = "SELECT * FROM students WHERE student_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, studentId); // Set the student ID
                ResultSet rs = pstmt.executeQuery(); // Execute the query
    
                // Check if the student exists
                if (rs.next()) {
                    // Retrieve student information
                    String name = rs.getString("name");
                    float javaMarks = rs.getFloat("java_marks");
                    float dbmsMarks = rs.getFloat("dbms_marks");
                    float dsaMarks = rs.getFloat("dsa_marks");
                    float mathsMarks = rs.getFloat("maths_marks");
                    float webTechMarks = rs.getFloat("web_tech_marks");
    
                    // Calculate total and percentage
                    float totalMarks = javaMarks + dbmsMarks + dsaMarks + mathsMarks + webTechMarks;
                    float percentage = (totalMarks / 500) * 100;
    
                    // Format the marksheet output
                    String marksheet = "----------------------------------------\n" +
                                       "              CHARUSAT UNIVERSITY              \n" +
                                       "                Semester: 3rd                    \n" +
                                       "----------------------------------------\n" +
                                       "Name: " + name + "\n" +
                                       "Roll No: " + studentId + "\n" +
                                       "----------------------------------------\n" +
                                       "Subject            Marks\n" +
                                       "----------------------------------------\n" +
                                       "Java: " + javaMarks + "\n" +
                                       "DBMS: " + dbmsMarks + "\n" +
                                       "DSA: " + dsaMarks + "\n" +
                                       "Mathematics: " + mathsMarks + "\n" +
                                       "Web Technology: " + webTechMarks + "\n" +
                                       "----------------------------------------\n" +
                                       "Total:            " + totalMarks + "\n" +
                                       "Percentage:       " + percentage + "%\n" +
                                       "----------------------------------------\n";
                    outputArea.setText(marksheet); // Display the formatted marksheet
                } else {
                    outputArea.setText("No student found with ID: " + studentId); // Message if student not found
                }
            }
        } catch (SQLException e) {
            outputArea.setText("Error displaying marksheet: " + e.getMessage());
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentGradeManagementSystem app = new StudentGradeManagementSystem();
            app.setVisible(true); // Make the GUI visible
        });
    }
}
