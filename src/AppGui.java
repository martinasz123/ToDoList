import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppGui extends JFrame{
    //public static Object taskSection;
    private static JPanel taskPanel;
    public AppGui(String user) {
        super(user + "'s to do list");
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        setLocationRelativeTo(null);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setMaximumSize(new Dimension(getWidth(), 2));

        header.setBorder(new LineBorder(Color.black));
        JButton taskBtn = taskBtn();
        JButton dbBtn = createDbBtn();
        header.add(dbBtn);
        header.add(taskBtn);
        add(header);

        taskPanel = new JPanel();
        taskPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        add(taskPanel);
    }

    public JDialog addTask(){
        JDialog taskDetails = new JDialog(this, "Add Task", true);
        taskDetails.setSize(500,100);
        taskDetails.setLayout(new FlowLayout());
        taskDetails.setLocationRelativeTo(null);

        JTextField taskNameField = new JTextField("Task name: ");
        taskDetails.add(taskNameField);

        JLabel completion = new JLabel("Completion Date: ");

        Date today = new Date();
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel(today, null, null, Calendar.MONTH));
        JSpinner.DateEditor date = new JSpinner.DateEditor(dateSpinner,"dd/MM/yy");
        dateSpinner.setEditor(date);

        JButton addTaskBtn = new JButton("Add Task");
        addTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String taskName = taskNameField.getText();
                Date completionDate = (Date) dateSpinner.getValue();
                taskSection(taskName,completionDate);
                sqlLogic.insertTask(taskName,completionDate);
                revalidate();
            }
        });

        taskDetails.add(completion);
        taskDetails.add(dateSpinner);
        taskDetails.add(addTaskBtn);

        return taskDetails;
    }

    public static void taskSection(String taskName, Date completionDate){
        //Creating the panel for the task
        JPanel task = new JPanel();
        task.setPreferredSize(new Dimension(500,50));
        task.setLayout(new BoxLayout(task, BoxLayout.X_AXIS));
        task.setBorder(new BevelBorder(BevelBorder.RAISED));
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

        //Creating the task fields
        JLabel name = new JLabel("<html><b>Task name:</b> " + taskName + " </html>");
        JLabel date = new JLabel("<html><b>Completion date:</b> " + String.valueOf(formatDate.format(completionDate)) + "</html>");
        JLabel completionName = new JLabel("<html><b>Completed:</b></html>");
        JCheckBox completed = new JCheckBox();

        //adding the fields
        task.add(name);
        task.add(date);
        task.add(completionName);
        task.add(completed);

        taskPanel.add(task);
        taskPanel.revalidate();
    }

    private JButton taskBtn(){
        JButton addTaskBtn = new JButton("New Task");
        addTaskBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addTaskBtn.setLayout(null);

        addTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog newTask = addTask();
                newTask.setVisible(true);
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
}
