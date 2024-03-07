package urja_academy;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;

public class Optionalcourse extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable optionalCoursesTable;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Optionalcourse frame = new Optionalcourse();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Optionalcourse() {
    	setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(54, 82, 173));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Data for the table
        Object[][] data = {
                {false, "Quantum Mechanics", "Richard Feynman"},
                {false, "C.Math", "Aatiz Ghimire"},
                {false, "Astrophysics", "Stephen Hawking"}
        };

        // Column names
        String[] columnNames = {"Select", "Module", "Teacher"};

        // Create a table model with the data and column names
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }
        };

        // Create the JTable with the table model
        optionalCoursesTable = new JTable(tableModel);
        optionalCoursesTable.getColumnModel().getColumn(0).setMinWidth(50);
        optionalCoursesTable.getColumnModel().getColumn(1).setMinWidth(100);
        optionalCoursesTable.getColumnModel().getColumn(2).setMinWidth(150);

        // Enable selection for each module
        optionalCoursesTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(optionalCoursesTable);
        scrollPane.setBounds(10, 61, 414, 190);
        contentPane.add(scrollPane);
        
        JLabel lblNewLabel = new JLabel("OPTIONAL COURSES");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Sanskrit Text", Font.BOLD, 25));
        lblNewLabel.setBounds(52, 10, 304, 52);
        contentPane.add(lblNewLabel);
    }
}
