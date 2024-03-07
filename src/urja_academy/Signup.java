package urja_academy;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.sql.*;


public class Signup extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JPasswordField passwordField;
    private JPasswordField passwordField_1;
    private JComboBox<String> comboBox;

    private String userfor;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Signup frame = new Signup();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public Signup() {
    	setBounds(new Rectangle(100, 100, 1362, 800));
    	
    	setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1370, 852);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        panel.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
        panel.setBackground(new Color(54, 82, 173));
        panel.setBounds(0, 0, 668, 833);
        contentPane.add(panel);
        panel.setLayout(null);

        Label label_1 = new Label("Username");
        label_1.setForeground(Color.WHITE);
        label_1.setFont(new Font("Sanskrit Text", Font.BOLD, 17));
        label_1.setBounds(99, 149, 126, 28);
        panel.add(label_1);

        Label label_2 = new Label("Email");
        label_2.setForeground(Color.WHITE);
        label_2.setFont(new Font("Sanskrit Text", Font.BOLD, 17));
        label_2.setBounds(99, 217, 109, 35);
        panel.add(label_2);

        Label label_3 = new Label("Password");
        label_3.setForeground(Color.WHITE);
        label_3.setFont(new Font("Sanskrit Text", Font.BOLD, 17));
        label_3.setBounds(99, 292, 126, 37);
        panel.add(label_3);

        Label label_4 = new Label("Confirm Password");
        label_4.setForeground(Color.WHITE);
        label_4.setFont(new Font("Sanskrit Text", Font.BOLD, 17));
        label_4.setBounds(99, 380, 225, 23);
        panel.add(label_4);

        Label label_1_1 = new Label("SIGN UP");
        label_1_1.setForeground(Color.WHITE);
        label_1_1.setBounds(155, 0, 336, 120);
        panel.add(label_1_1);
        label_1_1.setFont(new Font("Sanskrit Text", Font.BOLD, 60));

        textField = new JTextField();
        textField.setBounds(98, 183, 226, 28);
        panel.add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(99, 258, 225, 28);
        panel.add(textField_1);

        passwordField = new JPasswordField();
        passwordField.setBounds(99, 335, 225, 28);
        panel.add(passwordField);

        passwordField_1 = new JPasswordField();
        passwordField_1.setBounds(99, 409, 225, 28);
        panel.add(passwordField_1);

        JButton btnNewButton = new JButton("Sign Up");
        btnNewButton.setBackground(Color.WHITE);
        btnNewButton.setForeground(new Color(54, 82, 173));
        btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 25));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = textField.getText();
                String email = textField_1.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(passwordField_1.getPassword());

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || userfor == null) {
                    showError("Please fill in all fields");
                } else if (!isValidEmail(email)) {
                    showError("Invalid email address. Please use @gmail.com or @yahoo.com domains.");
                } else if (password.equals(confirmPassword)) {
                    if (isUserExists(username, email)) {
                        showError("User already exists");
                    } else {
                        try {
                            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "");
                            String query = "INSERT INTO logs (name, email, pass, ufor) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                                preparedStatement.setString(1, username);
                                preparedStatement.setString(2, email);
                                preparedStatement.setString(3, password);
                                preparedStatement.setString(4, userfor);

                                preparedStatement.executeUpdate();
                            }

                            connection.close();

                            showSuccess("User Created\nName: " + username + "\nEmail: " + email + "\nUser For: " + userfor);
                            openLogin();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            showError("Error creating user. Please check your database connection.");
                        }
                    }
                } else {
                    showError("Passwords do not match");
                }
            }
        });

        btnNewButton.setBounds(95, 590, 163, 34);
        panel.add(btnNewButton);

        JLabel lblNewLabel = new JLabel("Already register?");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Sanskrit Text", Font.BOLD, 20));
        lblNewLabel.setBounds(449, 561, 181, 35);
        panel.add(lblNewLabel);

        JButton btnNewButton_1 = new JButton("Login");
        btnNewButton_1.setBackground(Color.WHITE);
        btnNewButton_1.setForeground(new Color(54, 82, 173));
        btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 25));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openLogin();
            }
        });
        btnNewButton_1.setBounds(500, 593, 104, 28);
        panel.add(btnNewButton_1);

        comboBox = new JComboBox<String>();
        comboBox.setForeground(new Color(54, 82, 173));
        comboBox.setBackground(Color.WHITE);
        comboBox.setFont(new Font("Times New Roman", Font.BOLD, 25));
        comboBox.setModel(new DefaultComboBoxModel(new String[] {"Student", "Admin", "Instructor"}));
        comboBox.setBounds(265, 522, 158, 41);
        panel.add(comboBox);

        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userfor = (String) comboBox.getSelectedItem();
            }
        });

        JLabel lblNewLabel_1 = new JLabel("Account Types");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setFont(new Font("Sanskrit Text", Font.BOLD, 15));
        lblNewLabel_1.setBounds(287, 494, 136, 28);
        panel.add(lblNewLabel_1);
        
        JLabel lblNewLabel_2 = new JLabel("WELCOME  TO");
        lblNewLabel_2.setFont(new Font("Sanskrit Text", Font.BOLD, 57));
        lblNewLabel_2.setForeground(new Color(54, 82, 173));
        lblNewLabel_2.setBounds(707, 185, 549, 105);
        contentPane.add(lblNewLabel_2);
        
        JLabel lblNewLabel_3 = new JLabel("URJA ACADEMY");
        lblNewLabel_3.setFont(new Font("Sanskrit Text", Font.BOLD, 65));
        lblNewLabel_3.setForeground(new Color(54, 82, 173));
        lblNewLabel_3.setBounds(717, 243, 629, 130);
        contentPane.add(lblNewLabel_3);
    }

    private boolean isUserExists(String username, String email) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "");
            String query = "SELECT * FROM logs WHERE name = ? OR email = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, email);

                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showError("Error checking user existence. Please check your database connection.");
            return false;
        }
    }

    private void openLogin() {
        Login loginFrame = new Login();
        loginFrame.setVisible(true);
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    private boolean isValidEmail(String email) {
        String[] allowedDomains = {"@gmail.com", "@yahoo.com"};
        for (String domain : allowedDomains) {
            if (email.endsWith(domain)) {
                return true;
            }
        }
        return false;
    }
}
