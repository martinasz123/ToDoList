public class Launcher {
    public static void main(String[] args)
    {
        String user = System.getProperty("user.name");
        new AppGui(user).setVisible(true);
        sqlLogic.populateTasks();
    }
}