package urja_academy;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Chosecourse extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private List<Course> coursesList;
    private DefaultTableModel selectedCoursesTableModel;
    private String userName;
    private String userRole;
    private JComboBox<String> semesterComboBox;
    private JButton btnAddExtraCourse;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Chosecourse frame = new Chosecourse();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressWarnings("serial")
    public Chosecourse() {
    	setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Chicken\\Downloads\\heraldinverted.png"));
        this.userName = userName;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 837, 573);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Choose a Course");
        lblNewLabel.setForeground(new Color(54, 82, 173));
        lblNewLabel.setBounds(5, 5, 765, 66);
        lblNewLabel.setFont(new Font("Sanskrit Text", Font.BOLD, 30));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblNewLabel);

        // Initialize the list of courses and selected courses table model
        coursesList = new ArrayList<>();
        selectedCoursesTableModel = new DefaultTableModel(new Object[]{"Select", "Course Name", "Duration (Years)", "Tutor/Teacher"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : super.getColumnClass(columnIndex);
            }
        };

        // Connect to the database and fetch courses
        fetchCoursesFromDatabase();

        // Add a JTable to display selected courses
        JTable selectedCoursesTable = new JTable(selectedCoursesTableModel);
        selectedCoursesTable.getColumnModel().getColumn(0).setMinWidth(50);
        selectedCoursesTable.getColumnModel().getColumn(0).setMaxWidth(50);
        selectedCoursesTable.getColumnModel().getColumn(1).setMinWidth(150);
        selectedCoursesTable.getColumnModel().getColumn(2).setMinWidth(100);
        selectedCoursesTable.getColumnModel().getColumn(3).setMinWidth(150);

        // Use custom renderers to display a tick mark for the "Select" column
        selectedCoursesTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if ((boolean) value) {
                    ((JLabel) rendererComponent).setText("\u2713"); // Unicode for check mark
                } else {
                    ((JLabel) rendererComponent).setText("");
                }
                return rendererComponent;
            }
        });

        JScrollPane scrollPane = new JScrollPane(selectedCoursesTable);
        scrollPane.setBounds(5, 67, 765, 402);
        contentPane.add(scrollPane);

        JButton btnSave = new JButton("Save Selected Courses");
        btnSave.setForeground(new Color(54, 82, 173));
        btnSave.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        btnSave.setBounds(5, 502, 384, 23);
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveSelectedCourses();
            }
        });
        contentPane.add(btnSave);

        JButton btnGoBack = new JButton("Go back");
        btnGoBack.setForeground(new Color(54, 82, 173));
        btnGoBack.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        btnGoBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnGoBack.setBounds(386, 502, 384, 23);
        contentPane.add(btnGoBack);

        JLabel lblSemester = new JLabel("Select Semester:");
        lblSemester.setFont(new Font("Minecraftia", Font.PLAIN, 16));
        lblSemester.setBounds(553, 468, 177, 23);
        contentPane.add(lblSemester);

        // Add semester options to the combo box
        semesterComboBox = new JComboBox<>(new String[]{"Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6"}); // Add more semesters as needed
        semesterComboBox.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6"}));
        semesterComboBox.setFont(new Font("Minecraftia", Font.PLAIN, 12));
        semesterComboBox.setBounds(731, 468, 39, 23);
        semesterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkSemesterSelection();
            }
        });
        contentPane.add(semesterComboBox);

        // Create checkboxes for each course and add them to the table
        for (Course course : coursesList) {
            selectedCoursesTableModel.addRow(new Object[]{false, course.getName(), course.getYears(), course.getTutor()});
        }

        // Initialize and add button to add extra course
        btnAddExtraCourse = new JButton("Select Extra Course");
        btnAddExtraCourse.setEnabled(false);
        btnAddExtraCourse.setFont(new Font("Minecraftia", Font.PLAIN, 12));
        btnAddExtraCourse.setBounds(5, 468, 182, 23);
        btnAddExtraCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
                addExtraCourse();
            }
        });
        contentPane.add(btnAddExtraCourse);
    }

    private void fetchCoursesFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/login";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT courselist, years, teacher FROM courses";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String courseName = resultSet.getString("courselist");
                        int years = resultSet.getInt("years");
                        String tutor = resultSet.getString("teacher");
                        coursesList.add(new Course(courseName, years, tutor));
                    }
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private void saveSelectedCourses() {
        String url = "jdbc:mysql://localhost:3306/login";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            for (int i = 0; i < selectedCoursesTableModel.getRowCount(); i++) {
                boolean selected = (boolean) selectedCoursesTableModel.getValueAt(i, 0);
                if (selected) {
                    String courseName = (String) selectedCoursesTableModel.getValueAt(i, 1);

                    // Check if the student is already assigned to the course and semester
                    if (isStudentAssigned(connection, userName, courseName)) {
                        showError("Student is already assigned to the course: " + courseName);
                        return; // Stop processing further courses
                    }

                    String selectedSemester = (String) semesterComboBox.getSelectedItem();

                    String insertSql = "INSERT INTO student (stdname, stdsub, sem) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                        insertStatement.setString(1, userName);
                        insertStatement.setString(2, courseName);
                        insertStatement.setString(3, selectedSemester);
                        insertStatement.executeUpdate();
                    } catch (SQLException e) {
                        handleSQLException(e);
                    }

                    // Print selected courses information
                    int years = (int) selectedCoursesTableModel.getValueAt(i, 2);
                    String tutor = (String) selectedCoursesTableModel.getValueAt(i, 3);
                    showSuccess("Selected Course: " + courseName + " (" + years + " years) - Tutor/Teacher: " + tutor +
                            " - Semester: " + selectedSemester);
                }
            }
            showSuccess("Courses Saved Successfully");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private void addExtraCourse() {
    	openOptionalcourse();

    }

    private boolean isStudentAssigned(Connection connection, String studentName, String courseName) throws SQLException {
        String query = "SELECT COUNT(*) FROM student WHERE stdname = ? AND stdsub = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, studentName);
            preparedStatement.setString(2, courseName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // If count is greater than 0, the student is already assigned
                }
            }
        }
        return false; // Default to false in case of an exception or unexpected behavior
    }

    private void checkSemesterSelection() {
        String selectedSemester = (String) semesterComboBox.getSelectedItem();
        // Enable or disable the button based on semester selection
        btnAddExtraCourse.setEnabled("5".equals(selectedSemester) || "6".equals(selectedSemester));
        btnAddExtraCourse.setEnabled(true);
    }

    private void handleSQLException(SQLException e) {
        System.err.println("SQLException: " + e.getMessage());
        System.err.println("SQLState: " + e.getSQLState());
        System.err.println("VendorError: " + e.getErrorCode());
        e.printStackTrace();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private static class Course {
        private String name;
        private int years;
        private String tutor;

        public Course(String name, int years, String tutor) {
            this.name = name;
            this.years = years;
            this.tutor = tutor;
        }

        public String getName() {
            return name;
        }

        public int getYears() {
            return years;
        }

        public String getTutor() {
            return tutor;
        }
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    private void openOptionalcourse() {
    	Optionalcourse optionalcourseFrame = new Optionalcourse();
    	optionalcourseFrame.setVisible(true);
    }
}
