import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppGui extends JFrame implements ItemListener {
    //public static Object taskSection;
    private static JPanel taskPanel;
    static JComboBox<String> sortMenu;
    private static int width;

    public AppGui(String user) {
        //Setting up the main frame by setting the size and layout features
        super(user + "'s to do list");
        width = 960;
        int height = 660;
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        setLocationRelativeTo(null);


        //Creating a panel to hold the buttons at the top
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setMaximumSize(new Dimension(getWidth(), 2));
        header.setBorder(new LineBorder(Color.black));

        //Adding buttons to the header
        //Task button
        JButton taskBtn = taskBtn();
        //Database button
        JButton dbBtn = createDbBtn();
        //Sort menu
        String[] sortType = {"Sort By","Date Asc.","Date Desc.", "Name Asc.", "Name Desc."};
        sortMenu = new JComboBox<>(sortType);
        sortMenu.addItemListener(this);
        //Clear tasks button
        JButton clearTaskBtn = clearTasks();
        //Adding them to header
        header.add(taskBtn);
        header.add(sortMenu);
        header.add(clearTaskBtn);
        header.add(dbBtn);
        add(header);

        //Creating and adding a new panel which will hold the tasks
        taskPanel = new JPanel();
        taskPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        add(taskPanel);
    }

    //Dialog box to hold task entry fields
    public void addTask(){
        //Initialising the dialog box and setting style features
        JDialog taskDetails = new JDialog(this, "Add Task", true);
        taskDetails.setSize(500,100);
        taskDetails.setLayout(new FlowLayout());
        taskDetails.setLocationRelativeTo(null);

        //Creating fields for the dialog box
        JTextField taskNameField = new JTextField("Task name: ");
        JLabel completion = new JLabel("Completion Date: ");

        //Setting up the date selection field
        Date today = new Date();
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel(today, null, null, Calendar.MONTH));
        JSpinner.DateEditor date = new JSpinner.DateEditor(dateSpinner,"dd/MM/yy");
        dateSpinner.setEditor(date);

        //Calling a button method to handle the data entry to the task panel and DB
        JButton addTaskBtn = addTaskBtn(taskNameField,dateSpinner);

        //Adding the elements to the dialog box
        taskDetails.add(taskNameField);
        taskDetails.add(completion);
        taskDetails.add(dateSpinner);
        taskDetails.add(addTaskBtn);

        //Setting the panel to be visible
        taskDetails.setVisible(true);
    }


    public static void taskCard(String taskName, Date completionDate, String notes){
        //Creating the panel for the fieldsPanel and setting layout
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel,BoxLayout.Y_AXIS));
        cardPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
        cardPanel.setPreferredSize(new Dimension(width,150));

        //Creating a panel for the notes
        JPanel notesPanel = new JPanel(new BorderLayout());
        notesPanel.setPreferredSize(new Dimension(width,100));
        notesPanel.setBorder(BorderFactory.createTitledBorder("Notes:"));


        //Creating a panel for the task details fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setPreferredSize(new Dimension(width,50));
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.X_AXIS));

        //Creating a date formatter to properly display the passed date
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

        //Creating fields for the fieldsPanel details
        JLabel name = new JLabel("<html><b>Task: </b> " + taskName + " </html>");
        JLabel date = new JLabel("<html><b>Completion date:</b> " + formatDate.format(completionDate) + "</html>");


        //creating and styling the notes field
        JTextArea notesArea = notesAreaCreator(notesPanel,taskName,completionDate,notes);


        //Calling a method to handle fieldsPanel completion
        JCheckBox completed = completedCheck(cardPanel,taskName,completionDate);

        //adding the fields to the fieldsPanel card
        fieldsPanel.add(name);
        fieldsPanel.add(date);
        fieldsPanel.add(completed);

        //Adding the notes to the notes panel
        notesPanel.add(notesArea);

        //Adding the fields and the notes panel to the card panel
        cardPanel.add(fieldsPanel);
        cardPanel.add(notesPanel, BorderLayout.CENTER);

        //adding the card Panel card to the task panel and revalidating it
        AppGui.taskPanel.add(cardPanel);
        AppGui.taskPanel.revalidate();
    }

    /*
    Creating a button to add the task
    This will send the task details to the task creator and
    Call a DB method to create an entry for the new task
    */
    private JButton addTaskBtn(JTextField taskNameField, JSpinner dateSpinner){
        JButton addTaskBtn = new JButton("Add Task");
        addTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Getting the data from the input fields
                String notes = "";
                String taskName = taskNameField.getText();
                Date completionDate = (Date) dateSpinner.getValue();

                //Calling methods in both classes to add the tasks
                taskCard(taskName,completionDate,notes);
                sqlLogic.insertTask(taskName,completionDate);
                revalidate();
            }
        });
        return addTaskBtn;
    }

    private JButton taskBtn(){
        JButton addTaskBtn = new JButton("New Task");
        addTaskBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addTaskBtn.setLayout(null);
        addTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });
        return addTaskBtn;
    }
    private JButton createDbBtn(){
        JButton addTaskBtn = new JButton("Create DB");
        addTaskBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addTaskBtn.setLayout(null);
        addTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sqlLogic.createDB();
            }
        });
        return addTaskBtn;
    }
    public void itemStateChanged(ItemEvent e){
        if (e.getSource() == sortMenu){
            taskPanel.removeAll();
            sqlLogic.sortData(String.valueOf(sortMenu.getSelectedItem()));
        }
    }
    public JButton clearTasks(){
        JButton clearTasksBtn = new JButton("Clear Tasks");
        clearTasksBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearTasksBtn.setLayout(null);
        clearTasksBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sqlLogic.clearTable();
                taskPanel.removeAll();
                taskPanel.repaint();
            }
        });
        return clearTasksBtn;
    }
    public static JCheckBox completedCheck(JPanel task, String taskName, Date completionDate){
        JCheckBox completeBtn = new JCheckBox("Completed: ");
        completeBtn.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        completeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskPanel.remove(task);
                sqlLogic.clearTask(taskName,completionDate);
                taskPanel.repaint();
            }
        });
        return completeBtn;
    }

    public static JTextArea notesAreaCreator(JPanel notesPanel, String taskName, Date completionDate, String notesFromDB){
        JTextArea notes = new JTextArea();
        notes.setPreferredSize(new Dimension(notesPanel.getWidth() - 20, notesPanel.getHeight()-10));
        notes.setBorder(BorderFactory.createEmptyBorder(0,5,5,5));
        notes.append(notesFromDB);


        notes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                notes.setBackground(Color.white);
                sqlLogic.addNoteDB(taskName,completionDate,notes.getText());
                notes.transferFocus();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                notes.setBackground(Color.lightGray);
            }
        });

        return notes;
    }

}
