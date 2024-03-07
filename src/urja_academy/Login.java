package urja_academy;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Rectangle;

public class Login extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;
    private JComboBox<String> comboBox; // Declare comboBox at the class level

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login() {
    	setBounds(new Rectangle(100, 100, 1362, 800));
    	setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1404, 861);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(128, 128, 128));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(54, 82, 173));
        panel.setBounds(0, 0, 1388, 833);
        contentPane.add(panel);
        panel.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setForeground(new Color(54, 82, 173));
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 2));
        panel_1.setBackground(Color.WHITE);
        panel_1.setBounds(0, 0, 688, 833);
        panel.add(panel_1);
        panel_1.setLayout(null);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(54, 82, 173));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBounds(161, 508, 140, 38);
        panel_1.add(btnLogin);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userName = textField.getText();
                @SuppressWarnings("deprecation")
                String password = passwordField.getText();
                String userRole = comboBox.getSelectedItem().toString();

                // Check if username, password, and userRole match a record in the database
                if (authenticateUser(userName, password, userRole)) {
                    // Open the Panel frame if authentication is successful
                	if(userRole=="Student") {
                		insertUsername(userName);
                	}
                    openPanel(userRole , userName);
                } else {
                    // Show an error message in a pop-up dialog
                    JOptionPane.showMessageDialog(Login.this, "Authentication failed. Please check your credentials.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }

			private void insertUsername(String userName) {
			
			}
        });
        btnLogin.setFont(new Font("Sanskrit Text", Font.BOLD, 20));

        JButton btnNewButton = new JButton("Sign Up");
        btnNewButton.setForeground(Color.WHITE);
        btnNewButton.setBackground(new Color(54, 82, 173));
        btnNewButton.setBounds(499, 594, 127, 38);
        panel_1.add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSignUp();
            }
        });
        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String userName = textField.getText();
                @SuppressWarnings("deprecation")
                String password = passwordField.getText();
//                System.out.println(userName + "\n" + "\n" + password);
            }
        });
        btnNewButton.setFont(new Font("Sanskrit Text", Font.BOLD, 20));

        JLabel lblNotRegistered = new JLabel("Not Registered?");
        lblNotRegistered.setForeground(new Color(54, 82, 173));
        lblNotRegistered.setBounds(461, 509, 165, 49);
        panel_1.add(lblNotRegistered);
        lblNotRegistered.setFont(new Font("Sanskrit Text", Font.BOLD, 25));

        JLabel lblWelcomeToCourse = new JLabel("LOGIN PAGE");
        lblWelcomeToCourse.setBounds(161, 31, 362, 99);
        panel_1.add(lblWelcomeToCourse);
        lblWelcomeToCourse.setForeground(new Color(54, 82, 173));
        lblWelcomeToCourse.setFont(new Font("Sanskrit Text", Font.BOLD, 50));

        JLabel lblNewLabel = new JLabel("Username");
        lblNewLabel.setForeground(new Color(54, 82, 173));
        lblNewLabel.setBounds(161, 184, 154, 38);
        panel_1.add(lblNewLabel);
        lblNewLabel.setFont(new Font("Sanskrit Text", Font.BOLD, 20));
        lblNewLabel.setLabelFor(this);

        textField = new JTextField();
        textField.setBounds(161, 219, 215, 33);
        panel_1.add(textField);
        textField.setForeground(new Color(0, 0, 0));
        textField.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        textField.setColumns(10);

        JLabel lblPassword = new JLabel("Password ");
        lblPassword.setForeground(new Color(54, 82, 173));
        lblPassword.setBounds(161, 279, 114, 38);
        panel_1.add(lblPassword);
        lblPassword.setFont(new Font("Sanskrit Text", Font.BOLD, 20));

        passwordField = new JPasswordField();
        passwordField.setBounds(161, 308, 215, 33);
        panel_1.add(passwordField);

        comboBox = new JComboBox<>();
        comboBox.setModel(new DefaultComboBoxModel<>(new String[] {"", "Student", "Admin", "Instructor"}));
        comboBox.setBounds(161, 394, 140, 27);
        panel_1.add(comboBox);

        JLabel lblIAm = new JLabel("CHOOSE");
        lblIAm.setForeground(new Color(54, 82, 173));
        lblIAm.setFont(new Font("Sanskrit Text", Font.BOLD, 17));
        lblIAm.setBounds(173, 372, 102, 27);
        panel_1.add(lblIAm);
        
        JLabel lblNewLabel_3 = new JLabel("URJA ACADEMY");
        lblNewLabel_3.setForeground(Color.WHITE);
        lblNewLabel_3.setFont(new Font("Sanskrit Text", Font.BOLD, 65));
        lblNewLabel_3.setBounds(749, 283, 629, 130);
        panel.add(lblNewLabel_3);
        
        JLabel lblNewLabel_2 = new JLabel("WELCOME  TO");
        lblNewLabel_2.setForeground(Color.WHITE);
        lblNewLabel_2.setFont(new Font("Sanskrit Text", Font.BOLD, 57));
        lblNewLabel_2.setBounds(739, 225, 549, 105);
        panel.add(lblNewLabel_2);

    }

    private boolean authenticateUser(String username, String password, String userRole) {
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "");

            // Create a PreparedStatement for checking if the user exists
            String query = "SELECT * FROM logs WHERE name=? AND pass=? AND ufor=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, userRole);

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                // Check if any record matches the provided credentials
                if (resultSet.next()) {
                    // User authenticated successfully
                    connection.close();
                    System.out.println("Connected");
                    return true;
                }
            }

            // Close the database connection
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Object message = "Error authenticating user";
			JOptionPane.showMessageDialog(this, message , "Error", JOptionPane.ERROR_MESSAGE);        }
 
        return false;
    }

    private void openPanel(String userRole, String userName) {
        Panel panelFrame = new Panel();
        // Pass the username to the Chosecourse frame
        panelFrame.setUserName(userName);
        panelFrame.setUserRole(userRole);
        panelFrame.setVisible(true);
        dispose();
    }

    private void openSignUp() {
        Signup signupFrame = new Signup();
        signupFrame.setVisible(true);
        dispose();
    }
}