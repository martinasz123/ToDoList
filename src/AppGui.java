import javax.swing.*;

public class AppGui extends JFrame{
    public AppGui(String user) {
        super(user + "'s to do list");
        setSize(450,650);

        JPanel newTask = taskSection("laundry", 11.05);
        add(newTask);
        add(new JSeparator(SwingConstants.HORIZONTAL));

    }

    public JPanel taskSection(String taskName, double completionDate){
        //Creating the panel for the task
        JPanel task = new JPanel();

        //Creating the task fields
        JLabel name = new JLabel("Task name: " + taskName);
        JLabel date = new JLabel("Completion date: " + String.valueOf(completionDate));
        JLabel completionName = new JLabel("Completed:");
        JCheckBox completed = new JCheckBox();

        //adding the fields
        task.add(name);
        task.add(date);
        task.add(completionName);
        task.add(completed);

        return task;
    }
}
