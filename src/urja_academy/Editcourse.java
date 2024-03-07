package urja_academy;

import java.awt.EventQueue;
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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;

public class Editcourse extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3; // Text field for current course name
    private JTextField textField_4; // Text field for new course name

    // JDBC database connection details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/login";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Editcourse frame = new Editcourse();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Editcourse() {
    	setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 695, 593);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(54, 82, 173));
        panel.setBounds(0, 0, 691, 556);
        contentPane.add(panel);
        panel.setLayout(null);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(68, 309, 121, 33);
        panel.add(textField_1);

        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(68, 392, 121, 33);
        panel.add(textField_2);

        textField_3 = new JTextField();
        textField_3.setBounds(70, 133, 197, 33);
        panel.add(textField_3);
        textField_3.setColumns(10);

        textField_4 = new JTextField();
        textField_4.setBounds(70, 221, 185, 33);
        panel.add(textField_4);
        textField_4.setColumns(10);

        JLabel lblYears = new JLabel("Years");
        lblYears.setForeground(Color.WHITE);
        lblYears.setFont(new Font("Times New Roman", Font.BOLD, 22));
        lblYears.setBounds(68, 274, 101, 44);
        panel.add(lblYears);

        JLabel lblTeacher = new JLabel("Teacher");
        lblTeacher.setForeground(Color.WHITE);
        lblTeacher.setFont(new Font("Times New Roman", Font.BOLD, 25));
        lblTeacher.setBounds(68, 359, 101, 23);
        panel.add(lblTeacher);

        JLabel lblCurrentCourse = new JLabel("Current Course");
        lblCurrentCourse.setForeground(Color.WHITE);
        lblCurrentCourse.setFont(new Font("Times New Roman", Font.BOLD, 25));
        lblCurrentCourse.setBounds(70, 93, 222, 44);
        panel.add(lblCurrentCourse);

        JLabel lblNewCourse = new JLabel("New Course");
        lblNewCourse.setForeground(Color.WHITE);
        lblNewCourse.setFont(new Font("Times New Roman", Font.BOLD, 25));
        lblNewCourse.setBounds(70, 188, 167, 33);
        panel.add(lblNewCourse);

        JButton btnNewButton = new JButton("Change Course's info");
        btnNewButton.setForeground(new Color(54, 82, 173));
        btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 24));
        btnNewButton.setBounds(289, 406, 302, 52);
        panel.add(btnNewButton);

        JLabel lblNewLabel_1 = new JLabel("Edit Course");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setBackground(new Color(54, 82, 173));
        lblNewLabel_1.setBounds(274, 30, 230, 77);
        panel.add(lblNewLabel_1);
        lblNewLabel_1.setFont(new Font("Sanskrit Text", Font.BOLD, 36));
        
        JButton btnNewButton_1 = new JButton("Go back");
        btnNewButton_1.setForeground(new Color(54, 82, 173));
        btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 23));
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        	}
        });
        btnNewButton_1.setBounds(458, 480, 133, 44);
        panel.add(btnNewButton_1);

        // Action listener for the button
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call the method to update the database
                updateCourseInfo();
            }
        });
    }

    // Method to update course information in the database
    private void updateCourseInfo() {
        // Get values from text fields
        String currentCourseName = textField_3.getText();
        String newCourseName = textField_4.getText();
        int years = Integer.parseInt(textField_1.getText());
        String teacher = textField_2.getText();

        // JDBC variables
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Check if the current course exists
            String checkQuery = "SELECT * FROM courses WHERE courselist = ?";
            preparedStatement = connection.prepareStatement(checkQuery);
            preparedStatement.setString(1, currentCourseName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Course exists, update its information
                String updateQuery = "UPDATE courses SET courselist=?, years=?, teacher=? WHERE courselist=?";
                preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setString(1, newCourseName);
                preparedStatement.setInt(2, years);
                preparedStatement.setString(3, teacher);
                preparedStatement.setString(4, currentCourseName);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Course information updated successfully.");
                } else {
                    System.out.println("Failed to update course information.");
                }
            } else {
                // Current course does not exist
                System.out.println("Current course does not exist.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Close the JDBC resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
