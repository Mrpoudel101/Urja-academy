package urja_academy;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Showmarks extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private String userName;

    // JDBC connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/login";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private JLabel lblMarksheetOfStudent;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Showmarks frame = new Showmarks();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public Showmarks() {
    	setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 976, 845);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Create the table
        table = new JTable();

        // Create a JScrollPane and add the table to it
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 125, 940, 671);
        contentPane.add(scrollPane);
        
        lblMarksheetOfStudent = new JLabel("MARKSHEET OF STUDENT\r\n");
        lblMarksheetOfStudent.setForeground(new Color(54, 82, 173));
        lblMarksheetOfStudent.setFont(new Font("Times New Roman", Font.BOLD, 42));
        lblMarksheetOfStudent.setBounds(148, 48, 611, 50);
        contentPane.add(lblMarksheetOfStudent);

        // Fetch data and populate the table
        fetchAndPopulateData();
    }

    private void fetchAndPopulateData() {
        try {
            // Establish JDBC connection
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Check if the user exists in the 'grade' table
            String checkUserSql = "SELECT COUNT(*) AS count FROM grade WHERE nameofstd = ?";
            try (PreparedStatement checkUserStatement = connection.prepareStatement(checkUserSql)) {
                checkUserStatement.setString(1, userName);
                try (ResultSet userCheckResultSet = checkUserStatement.executeQuery()) {
                    // Retrieve the count
                    userCheckResultSet.next();
                    int userCount = userCheckResultSet.getInt("count");

                    if (userCount > 0) {
                        // User exists, fetch data from the 'grade' table for the specific user
                        String sql = "SELECT nameofstd AS Name, subject AS Course, markofstd AS Marks, passorfail AS Result FROM grade WHERE nameofstd = ?";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setString(1, userName);
                            try (ResultSet resultSet = statement.executeQuery()) {
                                // Create a DefaultTableModel to hold the data
                                DefaultTableModel model = new DefaultTableModel();
                                model.addColumn("Name");
                                model.addColumn("Course");
                                model.addColumn("Marks");
                                model.addColumn("Result");

                                // Populate the model with data from the result set
                                while (resultSet.next()) {
                                    String name = resultSet.getString("Name");
                                    String course = resultSet.getString("Course");
                                    int marks = resultSet.getInt("Marks");
                                    String result = resultSet.getString("Result");

                                    model.addRow(new Object[]{name, course, marks, result});
                                }

                                // Set the model to the JTable
                                table.setModel(model);
                            }
                        }
                    } else {
                        // User does not exist, you can show a message or take appropriate action
                        System.out.println("User does not exist in the database.");
                    }
                }
            }

            // Close the JDBC connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setUserName(String receivedUserName) {
        this.userName = receivedUserName;
        System.out.println("UserName set: " + this.userName); // Add this line
        // Fetch data and populate the table when userName is set
        fetchAndPopulateData();
    }
}
