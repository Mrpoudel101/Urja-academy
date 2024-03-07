package urja_academy;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.Color;

public class Addcourse extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField teacherTextField; // New text field for entering teacher's name

    // JDBC database URL, username, and password
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/login";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // SQL query to insert data into the courses table
    private static final String INSERT_QUERY = "INSERT INTO courses (courselist, years, teacher) VALUES (?, ?, ?)";
    private static final String INSERT_TUTOR_QUERY = "INSERT INTO tutor (tutrname, tutrclas, module) VALUES (?, ?, ?)";
    private JTextField textField_2;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Addcourse frame = new Addcourse();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Addcourse() {
    	setResizable(false);
    	setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Chicken\\Downloads\\heraldinverted.png"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 738, 522);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Add a new Course");
        lblNewLabel.setForeground(new Color(54, 82, 173));
        lblNewLabel.setFont(new Font("Sanskrit Text", Font.BOLD, 30));
        lblNewLabel.setBounds(214, 11, 289, 78);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Course's Name:");
        lblNewLabel_1.setForeground(new Color(54, 82, 173));
        lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 17));
        lblNewLabel_1.setBounds(143, 76, 192, 58);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel("Years:");
        lblNewLabel_1_1.setForeground(new Color(54, 82, 173));
        lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.BOLD, 17));
        lblNewLabel_1_1.setBounds(143, 251, 93, 38);
        contentPane.add(lblNewLabel_1_1);

        // New label and text field for entering teacher's name
        JLabel lblNewLabel_1_2 = new JLabel("Teacher's Name:");
        lblNewLabel_1_2.setForeground(new Color(54, 82, 173));
        lblNewLabel_1_2.setFont(new Font("Times New Roman", Font.BOLD, 17));
        lblNewLabel_1_2.setBounds(143, 325, 132, 38);
        contentPane.add(lblNewLabel_1_2);

        teacherTextField = new JTextField();
        teacherTextField.setColumns(10);
        teacherTextField.setBounds(143, 353, 265, 32);
        contentPane.add(teacherTextField);

        textField = new JTextField();
        textField.setBounds(143, 129, 229, 32);
        contentPane.add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(143, 280, 265, 32);
        contentPane.add(textField_1);

        JButton btnNewButton = new JButton("Add Course");
        btnNewButton.setForeground(new Color(54, 82, 173));
        btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnNewButton.setBounds(143, 417, 168, 58);
        contentPane.add(btnNewButton);

        JButton btnGoBack = new JButton("Go Back to Panel");
        btnGoBack.setForeground(new Color(54, 82, 173));
        btnGoBack.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnGoBack.setBounds(418, 417, 192, 58);
        contentPane.add(btnGoBack);
        
        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(143, 207, 230, 32);
        contentPane.add(textField_2);
        
        JLabel lblNewLabel_1_3 = new JLabel("Module Name:");
        lblNewLabel_1_3.setForeground(new Color(54, 82, 173));
        lblNewLabel_1_3.setFont(new Font("Times New Roman", Font.BOLD, 17));
        lblNewLabel_1_3.setBounds(143, 174, 132, 38);
        contentPane.add(lblNewLabel_1_3);

        // Add ActionListener to the "Add Course" button for database insertion
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertDataToDatabase();
                insertDataToTutor();
            }
        });

        // Add ActionListener to the "Go Back to Panel" button
        btnGoBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Dispose of the current frame and open Panel.java
                dispose();
            }
        });
    }

    private void insertDataToDatabase() {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Get the data from text fields
            String courseName = textField.getText();
            String years = textField_1.getText();
            String teacherName = teacherTextField.getText();

            // Check if the instructor is authorized to create a course
            if (isUserAuthorized(teacherName, connection)) { {
                // Prepare the SQL statement
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
                preparedStatement.setString(1, courseName);
                preparedStatement.setString(2, years);
                preparedStatement.setString(3, teacherName);

                // Execute the SQL statement to insert data
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    showSuccess("Data inserted Successfully");
                    // Create a column in the "subs" table based on the course name
                    createColumnInSubsTable(connection, courseName);
                } else {
                    showError("Failed to insert Data");
                }

                // Close the connection and statement
                preparedStatement.close();}}
           else {
                showError("No teacher of such name exists in the Database");
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void createColumnInSubsTable(Connection connection, String courseName) {
//    	try {
//            // Prepare the SQL statement to create a column in the "subs" table
//            String createColumnSQL = String.format(CREATE_COLUMN_QUERY, courseName);
//            PreparedStatement createColumnStatement = connection.prepareStatement(createColumnSQL);
//
//            // Execute the SQL statement to create a column
//            createColumnStatement.executeUpdate();
//
//            System.out.println("Column created in the 'subs' table: " + courseName);
//
//            // Close the statement
//            createColumnStatement.close();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
	}

	private void insertDataToTutor() {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            String teacherName = teacherTextField.getText();
            String teacherClass = textField.getText();
            String module = textField_2.getText();

            // Use the correct INSERT_TUTOR_QUERY for the tutor table
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TUTOR_QUERY);
            preparedStatement.setString(1, teacherName);
            preparedStatement.setString(2, teacherClass);
            preparedStatement.setString(3, module);
            if (isUserAuthorized(teacherName, connection)) { {
            int rowsAffected = preparedStatement.executeUpdate();

//            if (rowsAffected > 0) {
//            	showSuccess("Data inserted Successfully");
//            } else {
//            	showError("Failed to insert Data");
//            }
            }
            preparedStatement.close();
            connection.close();
            }} catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
	private boolean isUserAuthorized(String userName, Connection connection) throws SQLException {
	    // SQL query to check if the user exists in the logs table and has the role 'Instructor'
	    String checkAuthorizationQuery = "SELECT * FROM logs WHERE name = ? AND ufor = 'Instructor'";
	    PreparedStatement authorizationStatement = connection.prepareStatement(checkAuthorizationQuery);
	    authorizationStatement.setString(1, userName);

	    // Execute the query
	    ResultSet resultSet = authorizationStatement.executeQuery();

	    // Check if there is a matching row
	    boolean isAuthorized = resultSet.next();

	    // Close the statement and result set
	    authorizationStatement.close();
	    resultSet.close();

	    return isAuthorized;
	}
	private void showError(String message) {
	    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	private void showSuccess(String message) {
	    JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
	}

}