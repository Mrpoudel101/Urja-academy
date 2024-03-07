package urja_academy;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;

public class Panel extends JFrame {
    private JTabbedPane tabbedPane;
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String receivedUserRole;
    private String receivedUserName;
    private JTable coursesTable;
    private DefaultTableModel coursesTableModel;
    private JTable studentTable;
    private DefaultTableModel studentTableModel;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/login";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private Connection connection;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblStudentsCount;
    private JLabel lblTutorsCount;
    private JLabel lblCoursesCount;
    private JTable table_1;
    private JTextField usernameField;
    private JTable tutorTable;
    private DefaultTableModel tutorTableModel;
    private int previousTabIndex = 0;


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Panel frame = new Panel();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Panel() {
    	setBounds(new Rectangle(100, 100, 1362, 800));
    	setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1360, 846);
        contentPane = new JPanel();
        contentPane.setBackground(UIManager.getColor("Button.light"));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Initialize the database connection
        initializeDatabaseConnection();

        JPanel panel = new JPanel();
        panel.setBackground(new Color(54, 82, 173));
        panel.setBounds(0, 0, 274, 805);
        contentPane.add(panel);
        panel.setLayout(null);

        JButton btnDashboard = createButton("Dashboard", 51, 78, 167, 77);
        btnDashboard.addActionListener(e -> {
            tabbedPane.setSelectedIndex(0);
            // Load data into the table when the "Dashboard" button is clicked
            loadTableData();
            updateStudentAndInstructorCounts();
        });
        panel.add(btnDashboard);

        JButton btnStudent = createButton("Students", 51, 217, 167, 77);
        btnStudent.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        panel.add(btnStudent);

        JButton btnTutors = createButton("Tutors", 51, 356, 167, 77);
        btnTutors.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        panel.add(btnTutors);

        JButton btnCourses = createButton("Courses", 51, 506, 167, 77);
        btnCourses.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        panel.add(btnCourses);

        JButton btnLogout = createButton("Logout", 51, 647, 167, 77);
        btnLogout.addActionListener(e -> {
            openLogin();
        });
        panel.add(btnLogout);
        
        usernameField = new JTextField();
        usernameField.setForeground(Color.WHITE);
        usernameField.setEditable(false);
        usernameField.setFont(new Font("Sanskrit Text", Font.BOLD, 20));
        usernameField.setBorder(new EmptyBorder(0, 0, 0, 0));
        usernameField.setBackground(new Color(54, 82, 173));
        usernameField.setBounds(51, 35, 156, 32);
        panel.add(usernameField);
        usernameField.setColumns(10);
        
        JLabel lblNewLabel_1 = new JLabel("Logged in as");
        lblNewLabel_1.setForeground(Color.WHITE);
        lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblNewLabel_1.setBounds(77, 10, 130, 26);
        panel.add(lblNewLabel_1);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBackground(new Color(128, 255, 255));
        tabbedPane.setBounds(274, -26, 1070, 831);
        contentPane.add(tabbedPane);

        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 3) { // Assuming "Courses" tab has index 3
                    loadTableData();
                }
            }
        });

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(255, 255, 255));
        panel_1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        tabbedPane.addTab("Dashboard", null, panel_1, null);

        // Initialize the table model
        String[] columnNames = {"                 Username", "                   Role", "                 Email"};
        tableModel = new DefaultTableModel(columnNames, 0);
        panel_1.setLayout(null);

        // Create the JTable with the initialized table model
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(new Color(54, 82, 173));
        scrollPane.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 2), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
        scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        scrollPane.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        scrollPane.setBounds(315, 271, 452, 427);
        panel_1.add(scrollPane);

        JPanel pnlstudents = new JPanel();
        pnlstudents.setForeground(Color.WHITE);
        pnlstudents.setFont(new Font("Times New Roman", Font.BOLD, 20));
        pnlstudents.setBorder(new EmptyBorder(0, 0, 0, 0));
        pnlstudents.setBackground(new Color(54, 82, 173));
        pnlstudents.setBounds(59, 137, 234, 76);
        panel_1.add(pnlstudents);

        JPanel pnltutors = new JPanel();
        pnltutors.setBorder(new EmptyBorder(0, 0, 0, 0));
        pnltutors.setBackground(new Color(54, 82, 173));
        pnltutors.setBounds(375, 137, 261, 76);
        panel_1.add(pnltutors);

        JPanel pnlcourses = new JPanel();
        pnlcourses.setBorder(new EmptyBorder(0, 0, 0, 0));
        pnlcourses.setBackground(new Color(54, 82, 173));
        pnlcourses.setBounds(729, 144, 277, 69);
        panel_1.add(pnlcourses);
        pnlstudents.setLayout(null);

        lblStudentsCount = new JLabel("Number of Students: ");
        lblStudentsCount.setForeground(Color.WHITE);
        lblStudentsCount.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblStudentsCount.setBounds(18, 31, 216, 14);
        pnlstudents.add(lblStudentsCount);
        pnltutors.setLayout(null);

        lblTutorsCount = new JLabel("Number of Instructors: ");
        lblTutorsCount.setForeground(Color.WHITE);
        lblTutorsCount.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblTutorsCount.setBounds(10, 28, 247, 23);
        pnltutors.add(lblTutorsCount);
        pnlcourses.setLayout(null);

        lblCoursesCount = new JLabel("Number of Courses: ");
        lblCoursesCount.setForeground(Color.WHITE);
        lblCoursesCount.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblCoursesCount.setBounds(10, 17, 212, 42);
        pnlcourses.add(lblCoursesCount);
        
        JLabel lblDashboard = new JLabel("Dashboard");
        lblDashboard.setForeground(new Color(54, 82, 173));
        lblDashboard.setFont(new Font("Sanskrit Text", Font.BOLD, 60));
        lblDashboard.setBounds(326, 10, 362, 117);
        panel_1.add(lblDashboard);

        loadTableData();
        updateStudentAndInstructorCounts();
        updateCoursesCount();

        JPanel panel_4 = new JPanel();
        panel_4.setBackground(new Color(255, 255, 255));
        tabbedPane.addTab("Students", null, panel_4, null);
        panel_4.setLayout(null);

        // Initialize the table model for students
        String[] studentColumnNames = {"                 Student Name","        Course", "                                   Module","             Teacher"};
        studentTableModel = new DefaultTableModel(studentColumnNames, 0);

        // Create the JTable for students with the initialized table model
        studentTable = new JTable(studentTableModel);
        JScrollPane studentScrollPane = new JScrollPane(studentTable);
        studentScrollPane.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 2), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
        studentScrollPane.setBounds(169, 223, 731, 453); 
        panel_4.add(studentScrollPane);
        
        JLabel lblStudentTab = new JLabel("Student's Tab");
        lblStudentTab.setForeground(new Color(54, 82, 173));
        lblStudentTab.setFont(new Font("Sanskrit Text", Font.BOLD, 53));
        lblStudentTab.setBounds(279, 11, 390, 128);
        panel_4.add(lblStudentTab);
        
        JButton btnNewButton_2 = new JButton("Show marks");
        btnNewButton_2.setForeground(new Color(54, 82, 173));
        btnNewButton_2.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 2), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
        btnNewButton_2.setBackground(Color.WHITE);
        btnNewButton_2.setFont(new Font("Times New Roman", Font.BOLD, 15));
        btnNewButton_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		openshowmarks();
        	}
        });
        btnNewButton_2.setBounds(383, 149, 130, 38);
        panel_4.add(btnNewButton_2);

        // Add this line to load data into the students table when the "Students" tab is selected
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 1) { // Assuming "Students" tab has index 1
                	loadStudentTableData();
                }
            }
        });

        JPanel panel_3 = new JPanel();
        panel_3.setBackground(new Color(255, 255, 255));
        tabbedPane.addTab("Tutors", null, panel_3, null);
        panel_3.setLayout(null);

        JPanel panel_2 = new JPanel();
        panel_2.setBackground(new Color(255, 255, 255));
        tabbedPane.addTab("Courses", null, panel_2, null);
        panel_2.setLayout(null);

        String[] tutorColumnNames = {"                                             Module" ,"                                        Subject","                 Student"};
        tutorTableModel = new DefaultTableModel(tutorColumnNames, 0);
        
        tutorTable = new JTable(tutorTableModel);
        JScrollPane tutorScrollPane = new JScrollPane(tutorTable);
        tutorScrollPane.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 2), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
        tutorScrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        tutorScrollPane.setBackground(new Color(124, 174, 187));
        tutorScrollPane.setBounds(157, 215, 720, 451);
        panel_3.add(tutorScrollPane);
        
        JButton btnNewButton_1 = new JButton("Add marks");
        btnNewButton_1.setForeground(new Color(54, 82, 173));
        btnNewButton_1.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 2), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
        btnNewButton_1.setBackground(new Color(255, 255, 255));
        btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 15));
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(receivedUserRole != "Student") {
        		openMarks();
        	}}
        });
        btnNewButton_1.setBounds(387, 150, 119, 38);
        panel_3.add(btnNewButton_1);
        
        JLabel lblTutorsTab = new JLabel("Tutor's Tab");
        lblTutorsTab.setForeground(new Color(54, 82, 173));
        lblTutorsTab.setFont(new Font("Sanskrit Text", Font.BOLD, 51));
        lblTutorsTab.setBounds(327, 0, 325, 128);
        panel_3.add(lblTutorsTab);
        
        String[] coursesColumnNames = {"                            Course List", "                         Years", "                    Teacher"};
        coursesTableModel = new DefaultTableModel(coursesColumnNames, 0);

        coursesTable = new JTable(coursesTableModel);
        JScrollPane coursesScrollPane = new JScrollPane(coursesTable);
        coursesScrollPane.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 2), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
        coursesScrollPane.setBounds(233, 249, 597, 473);
        panel_2.add(coursesScrollPane);

        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 2) { // Assuming "Tutors" tab has index 2
                	if(receivedUserRole != "Student") {
                    loadTutorsTableData(receivedUserName);
                }else{
                    JOptionPane.showMessageDialog(contentPane, "Sorry, you cannot access this tab.", "Access Denied", JOptionPane.ERROR_MESSAGE);

                    // Set the selected tab to the previous one (you may customize this behavior)
                    tabbedPane.setSelectedIndex(previousTabIndex);
                }}
            }
        });

        table_1 = new JTable();
        table_1.setBounds(532, 5, 0, 0);
        panel_2.add(table_1);

        JButton btnNewButton = new JButton("Add Course");
        btnNewButton.setForeground(new Color(54, 82, 173));
        btnNewButton.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 2), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
        btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNewButton.setBackground(new Color(255, 255, 255));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (receivedUserRole == "Admin") {
                    openAddcourse();
                } else {
                    JLabel label = new JLabel("You are not allowed to Add a new course");
                    label.setFont(new Font("Arial", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(label, "Permission Denied");
                }
            }
        });
        btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnNewButton.setBounds(185, 158, 134, 38);
        panel_2.add(btnNewButton);

        JButton btnDeleteCourse = new JButton("Delete Course");
        btnDeleteCourse.setForeground(new Color(54, 82, 173));
        btnDeleteCourse.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 2), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
        btnDeleteCourse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDeleteCourse.setBackground(new Color(255, 255, 255));
        btnDeleteCourse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (receivedUserRole == "Admin") {
                    openDelcourse();
                } else {
                    JLabel label = new JLabel("You are not allowed to Delete a course");
                    label.setFont(new Font("Arial", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(label, "Permission Denied");
                }
            }
        });
        btnDeleteCourse.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnDeleteCourse.setBounds(343, 158, 161, 38);
        panel_2.add(btnDeleteCourse);

        JButton btnChooseCourse = new JButton("Choose Course");
        btnChooseCourse.setForeground(new Color(54, 82, 173));
        btnChooseCourse.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 2), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
        btnChooseCourse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnChooseCourse.setBackground(new Color(255, 255, 255));
        btnChooseCourse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (receivedUserRole == "Student") {
                    openchosecourse(receivedUserName);
                } else {
                    JLabel label = new JLabel("You are not allowed to Choose a course");
                    label.setFont(new Font("Arial", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(label, "Permission Denied");
                }
            }
        });
        btnChooseCourse.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnChooseCourse.setBounds(532, 158, 180, 38);
        panel_2.add(btnChooseCourse);
        
        JLabel lblCourses = new JLabel("Courses");
        lblCourses.setForeground(new Color(54, 82, 173));
        lblCourses.setFont(new Font("Sanskrit Text", Font.BOLD, 78));
        lblCourses.setBounds(314, 15, 417, 133);
        panel_2.add(lblCourses);
        
        JButton btnEditCourse = new JButton("Edit Course");
        btnEditCourse.setForeground(new Color(54, 82, 173));
        btnEditCourse.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(receivedUserRole == "Admin") {
        		openEditcourse();
        	}else{
        		showError("You are not Authorized");
        	}}
        }); 
        btnEditCourse.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnEditCourse.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 2), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
        btnEditCourse.setBackground(Color.WHITE);
        btnEditCourse.setBounds(736, 158, 161, 38);
        panel_2.add(btnEditCourse);
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 3) { // Assuming "Courses" tab has index 3
                    loadCoursesTableData(); // Add this line to fetch data for the "Courses" tab
                }
            }
        });

    }

    private void initializeDatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            if (connection != null) {
//                System.out.println("Database connection established");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setForeground(new Color(54, 82, 173));
        button.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 2, true), new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0))));
        button.setBackground(Color.WHITE);
        button.setFont(new Font("Sanskrit Text", Font.BOLD, 25));
        button.setBounds(x, y, width, height);
        return button;
    }

    private void loadTableData() {
        try {
            String query = "SELECT name, ufor, email FROM logs";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                tableModel.setRowCount(0);

                while (resultSet.next()) {
                    String stdName = resultSet.getString("name");
                    String userfor = resultSet.getString("ufor");
                    String email = resultSet.getString("email");

                    tableModel.addRow(new Object[]{stdName, userfor, email});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCoursesTableData() {
        try {
            String coursesQuery = "SELECT courselist, years, teacher FROM courses";
            try (PreparedStatement preparedStatement = connection.prepareStatement(coursesQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                coursesTableModel.setRowCount(0);

                while (resultSet.next()) {
                    String courseName = resultSet.getString("courselist");
                    int years = resultSet.getInt("years");
                    String teacher = resultSet.getString("teacher");

                    coursesTableModel.addRow(new Object[]{courseName, years, teacher});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadTutorsTableData(String userName) {
        try {
            String tutorQuery = "SELECT tutrname, tutrclas, module FROM tutor";
            try (PreparedStatement tutorStatement = connection.prepareStatement(tutorQuery);
                 ResultSet tutorResultSet = tutorStatement.executeQuery()) {

                tutorTableModel.setRowCount(0);

                // Create a map to store subject-student mappings
                Map<String, Map<String, StringBuilder>> tutorMap = new HashMap<>();

                while (tutorResultSet.next()) {
                    String tutorName = tutorResultSet.getString("tutrname");
                    String tutorClass = tutorResultSet.getString("tutrclas");
                    String moduleName = tutorResultSet.getString("module");

                    if (tutorName.equals(userName)) {
                        // Check if there is a match with student table
                        String studentQuery = "SELECT stdname FROM student WHERE stdsub = ?";
                        try (PreparedStatement studentStatement = connection.prepareStatement(studentQuery)) {
                            studentStatement.setString(1, tutorClass);
                            ResultSet studentResultSet = studentStatement.executeQuery();

                            StringBuilder students = new StringBuilder();
                            boolean hasStudents = false;

                            while (studentResultSet.next()) {
                                hasStudents = true;
                                String studentName = studentResultSet.getString("stdname");
                                students.append(studentName).append(", ");
                            }

                            if (hasStudents) {
                                students.delete(students.length() - 2, students.length()); // Remove the last comma

                                Map<String, StringBuilder> subjectStudentMap = tutorMap.computeIfAbsent(moduleName, k -> new HashMap<>());
                                subjectStudentMap.put(tutorClass, students);
                            }
                        }
                    }
                }

                // Populate the tutor table with subject-student mappings
                for (Map.Entry<String, Map<String, StringBuilder>> entry : tutorMap.entrySet()) {
                    for (Map.Entry<String, StringBuilder> subEntry : entry.getValue().entrySet()) {
                        tutorTableModel.addRow(new Object[]{entry.getKey(), subEntry.getKey(), subEntry.getValue().toString()});
                    }
                }

                // If no classes found for the tutor, display a message
                if (tutorMap.isEmpty()) {
                    tutorTableModel.addRow(new Object[]{"No classes", "", "You don't have any classes"});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadStudentTableData() {
        try {
            String studentQuery = "SELECT stdname, stdsub FROM student";
            String tutorQuery = "SELECT tutrname, tutrclas, module FROM tutor WHERE tutrclas = ?";

            try (PreparedStatement studentStatement = connection.prepareStatement(studentQuery)) {
                ResultSet studentResultSet = studentStatement.executeQuery();

                studentTableModel.setRowCount(0);

                while (studentResultSet.next()) {
                    String stdName = studentResultSet.getString("stdname");
                    String stdSub = studentResultSet.getString("stdsub");

                    // Check if there is a match with tutor table
                    try (PreparedStatement tutorStatement = connection.prepareStatement(tutorQuery)) {
                        tutorStatement.setString(1, stdSub);
                        ResultSet tutorResultSet = tutorStatement.executeQuery();

                        while (tutorResultSet.next()) {
                            String tutorName = tutorResultSet.getString("tutrname");
                            String tutorClass = tutorResultSet.getString("tutrclas");
                            String moduleName = tutorResultSet.getString("module");

                            if (stdSub.equals(tutorClass)) {
                                studentTableModel.addRow(new Object[]{stdName, stdSub, moduleName, tutorName});
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCoursesCount() {
        try {
            String coursesQuery = "SELECT COUNT(*) FROM courses";
            try (PreparedStatement coursesStatement = connection.prepareStatement(coursesQuery);
                 ResultSet coursesResultSet = coursesStatement.executeQuery()) {
                if (coursesResultSet.next()) {
                    int coursesCount = coursesResultSet.getInt(1);
                    lblCoursesCount.setText("Number of Courses: " + coursesCount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateStudentAndInstructorCounts() {
        try {
            String studentsQuery = "SELECT COUNT(*) FROM logs WHERE ufor = 'Student'";
            try (PreparedStatement studentsStatement = connection.prepareStatement(studentsQuery);
                 ResultSet studentsResultSet = studentsStatement.executeQuery()) {
                if (studentsResultSet.next()) {
                    int studentsCount = studentsResultSet.getInt(1);
                    lblStudentsCount.setText("Number of Students: " + studentsCount);
                }
            }
            String instructorsQuery = "SELECT COUNT(*) FROM logs WHERE ufor = 'Instructor'";
            try (PreparedStatement instructorsStatement = connection.prepareStatement(instructorsQuery);
                 ResultSet instructorsResultSet = instructorsStatement.executeQuery()) {
                if (instructorsResultSet.next()) {
                    int instructorsCount = instructorsResultSet.getInt(1);
                    lblTutorsCount.setText("Number of Instructors: " + instructorsCount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setDefaultCloseOperation(int operation) {
        closeDatabaseConnection();
        super.setDefaultCloseOperation(operation);
    }
    private void closeDatabaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
//                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setUserRole(String userRole) {
        this.receivedUserRole = userRole;
    }
    private void openAddcourse() {
        Addcourse addcourseFrame = new Addcourse();
        addcourseFrame.setVisible(true);
    }
    private void openDelcourse() {
        Delcourse delcourseFrame = new Delcourse();
        delcourseFrame.setVisible(true);
    }
    private void openchosecourse(String userName) {
        Chosecourse chosecourseFrame = new Chosecourse();
        chosecourseFrame.setUserName(userName);
        chosecourseFrame.setVisible(true);
    }
    private void openLogin() {
        Login loginFrame = new Login();
        loginFrame.setVisible(true);
        dispose();
    }
    public void setUserName(String userName) {
        this.receivedUserName = userName;
        if (usernameField != null) {
            usernameField.setText(userName);
        }
    }	
    private void openMarks() {
    	Marks marksFrame = new Marks();
    	marksFrame.setVisible(true);
    }
    private void openshowmarks() {
        Showmarks showmarksFrame = new Showmarks();
        if(receivedUserRole == "Student") {
        showmarksFrame.setUserName(receivedUserName);
        showmarksFrame.setVisible(true);
        }else {
        	showmarksFrame.setVisible(true);
        }
        
    }
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    private void openEditcourse() {
    	Editcourse editcourseFrame = new Editcourse();
    	editcourseFrame.setVisible(true);
    	}
}