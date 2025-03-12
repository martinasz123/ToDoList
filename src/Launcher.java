public class Launcher {
    public static void main(String[] args)
    {
        String user = "Martin";
        sqlLogic.connect();
        new AppGui(user).setVisible(true);
        sqlLogic.populateTasks();
    }
}