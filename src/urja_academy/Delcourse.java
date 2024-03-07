package urja_academy;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

public class Delcourse extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/login";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String DELETE_QUERY = "DELETE FROM courses WHERE courselist = ?";
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Delcourse frame = new Delcourse();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Delcourse() {
    	setResizable(false);
    	setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Chicken\\Downloads\\heraldinverted.png"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 752, 624);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Delete a Course");
        lblNewLabel.setForeground(new Color(54, 82, 173));
        lblNewLabel.setFont(new Font("Sanskrit Text", Font.BOLD, 48));
        lblNewLabel.setBounds(214, 39, 412, 116);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Course's Name:");
        lblNewLabel_1.setForeground(new Color(54, 82, 173));
        lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 34));
        lblNewLabel_1.setBounds(238, 177, 312, 74);
        contentPane.add(lblNewLabel_1);

        textField = new JTextField();
        textField.setFont(new Font("Times New Roman", Font.PLAIN, 19));
        textField.setBounds(238, 236, 250, 45);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton("Delete Course");
        btnNewButton.setForeground(new Color(54, 82, 173));
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnNewButton.setBounds(250, 338, 204, 53);
        contentPane.add(btnNewButton);
        
        JButton btnNewButton_1 = new JButton("Goback");
        btnNewButton_1.setForeground(new Color(54, 82, 173));
        btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 25));
        btnNewButton_1.setBounds(296, 417, 123, 39);
        contentPane.add(btnNewButton_1);

        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteDataFromDatabase();
            }
        });
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Dispose of the current frame and open Panel.java
                dispose();
            }
        });
    }

    private void deleteDataFromDatabase() {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            String courseName = textField.getText();

            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setString(1, courseName);

            int rowsAffected = preparedStatement.executeUpdate();
            deleteColumnInSubsTable(connection, courseName);
            if (rowsAffected > 0) {
                showSuccess("Course Deleted Successfully");
            } else {
                showError("Failed to Delete course");
            }

            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void deleteColumnInSubsTable(Connection connection, String courseName) {
//        try {
//            // Prepare the SQL statement to drop a column in the "subs" table
//            String deleteColumnSQL = String.format(DELETE_COLUMN_QUERY, courseName);
//            PreparedStatement deleteColumnStatement = connection.prepareStatement(deleteColumnSQL);
//
//            // Execute the SQL statement to drop a column
//            deleteColumnStatement.executeUpdate();
//
//            System.out.println("Column Deleted in the 'subs' table: " + courseName);
//
//            // Close the statement
//            deleteColumnStatement.close();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
    }
	private void showError(String message) {
	    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	private void showSuccess(String message) {
	    JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
	}
}
