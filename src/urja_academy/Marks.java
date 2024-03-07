package urja_academy;

import java.awt.EventQueue;
import java.awt.Font;
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

public class Marks extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;

    // Database connection details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/login";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Marks frame = new Marks();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Marks() {
    	setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Chicken\\Downloads\\heraldinverted.png"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 412, 546);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(54, 82, 173));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Add marks for Student");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Sanskrit Text", Font.BOLD, 30));
        lblNewLabel.setBounds(10, 25, 378, 69);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Student name:");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setFont(new Font("Sanskrit Text", Font.PLAIN, 20));
        lblNewLabel_1.setBounds(10, 127, 161, 36);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel("Marks");
        lblNewLabel_1_1.setForeground(Color.WHITE);
        lblNewLabel_1_1.setFont(new Font("Sanskrit Text", Font.PLAIN, 20));
        lblNewLabel_1_1.setBounds(10, 336, 157, 36);
        contentPane.add(lblNewLabel_1_1);

        JLabel lblNewLabel_1_1_1 = new JLabel("Subject");
        lblNewLabel_1_1_1.setForeground(Color.WHITE);
        lblNewLabel_1_1_1.setFont(new Font("Sanskrit Text", Font.PLAIN, 20));
        lblNewLabel_1_1_1.setBounds(10, 239, 127, 36);
        contentPane.add(lblNewLabel_1_1_1);

        textField = new JTextField();
        textField.setBounds(10, 158, 226, 36);
        contentPane.add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(10, 270, 232, 36);
        contentPane.add(textField_1);

        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(10, 366, 200, 36);
        contentPane.add(textField_2);

        JButton btnNewButton = new JButton("Add marks");
        btnNewButton.setForeground(new Color(54, 82, 173));
        btnNewButton.setBackground(Color.WHITE);
        btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the values from text fields
                String studentName = textField.getText();
                String subject = textField_1.getText();
                String marksText = textField_2.getText();

                try {
                    // Parse the marks as a float
                    float marks = Float.parseFloat(marksText);

                    // Determine pass or fail
                    String passOrFail = (marks >= 40) ? "Pass" : "Fail";

                    // Connect to the database
                    try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                        // Check if the student and subject exist in the student table
                        String checkStudentQuery = "SELECT * FROM student WHERE stdname = ? AND stdsub = ?";
                        try (PreparedStatement checkStudentStatement = connection.prepareStatement(checkStudentQuery)) {
                            checkStudentStatement.setString(1, studentName);
                            checkStudentStatement.setString(2, subject);
                            ResultSet resultSet = checkStudentStatement.executeQuery();

                            if (resultSet.next()) {
                                // Student and subject exist, add marks to the grade table
                                String addMarksQuery = "INSERT INTO grade (nameofstd, subject, markofstd, passorfail) VALUES (?, ?, ?, ?)";
                                try (PreparedStatement addMarksStatement = connection.prepareStatement(addMarksQuery)) {
                                    addMarksStatement.setString(1, studentName);
                                    addMarksStatement.setString(2, subject);
                                    addMarksStatement.setFloat(3, marks);
                                    addMarksStatement.setString(4, passOrFail);

                                    // Execute the query
                                    int rowsAffected = addMarksStatement.executeUpdate();

                                    if (rowsAffected > 0) {
                                        showSuccess("Data inserted successfully.");
                                    } else {
                                        showError("Failed to insert data.");
                                    }
                                }
                            } else {
                                showError("Student or subject does not exist.");
                            }
                        }
                    }
                } catch (NumberFormatException ex) {
                    // Handle the case where the input for marks is not a valid float
                    showError("Invalid marks input. Please enter a valid float.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showError("Error connecting to the database.");
                }
            }
        });
        btnNewButton.setBounds(26, 427, 141, 39);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Go back");
        btnNewButton_1.setForeground(new Color(54, 82, 173));
        btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnNewButton_1.setBackground(Color.WHITE);
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnNewButton_1.setBounds(206, 427, 121, 39);
        contentPane.add(btnNewButton_1);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
