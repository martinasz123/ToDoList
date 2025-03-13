import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class sqlLogic {
    private static final String url = "jdbc:sqlite:" + System.getProperty("user.home") + "/Documents/ToDoList/toDo.db";
    public static void createTable(){
        var sql = "CREATE TABLE IF NOT EXISTS tasks ("
                +" name TEXT,"
                +" date INTEGER NOT NULL,"
                +" notes TEXT"
                + ");";
        try (var conn = DriverManager.getConnection(url);
            var stmt = conn.createStatement()){

            stmt.execute(sql);

        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
    public static void createDB(){
        File dbDir = new File(System.getProperty("user.home") + "/Documents/ToDoList");
        if(!dbDir.exists()){
            dbDir.mkdirs();
            System.out.println("File was created");
        }
        try( var conn = DriverManager.getConnection(url)){
            if (conn != null){
                var meta  = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new DB has been created");
                createTable();
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
    public static void insertTask(String taskName, Date completionDate){
        String sql = "INSERT INTO tasks(name,date) VALUES(?,?)";
        try (var conn  = DriverManager.getConnection(url);
        var pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,taskName);
            pstmt.setLong(2,completionDate.getTime() / 1000);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
    public static void populateTasks(){
        var sql = "SELECT name, date, notes FROM tasks";
        try(var conn = DriverManager.getConnection(url);
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery(sql)){
            while (rs.next()){
                AppGui.taskCard(rs.getString("name"),new Date(rs.getLong("date")*1000),rs.getString("notes"));
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
    public static void sortData(String sortType){
        var sql = "";
        switch (sortType){
            case "Sort By":
                break;
            case "Date Asc.":
                sql = "SELECT * FROM tasks ORDER BY date ASC";
                break;
            case "Date Desc.":
                sql = "SELECT * FROM tasks ORDER BY date DESC";
                break;
            case "Name Asc.":
                sql = "SELECT * FROM tasks ORDER BY name ASC";
                break;
            case "Name Desc.":
                sql = "SELECT * FROM tasks ORDER BY name DESC";
                break;
        }
        try(var conn = DriverManager.getConnection(url);
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(sql)){
            while (rs.next()){
                AppGui.taskCard(rs.getString("name"),new Date(rs.getLong("date")*1000),rs.getString("notes"));
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
    public static void clearTable(){
        var sql = "DELETE FROM tasks";
        try(var conn = DriverManager.getConnection(url);
        var pstmt = conn.prepareStatement(sql)){
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
    public static void clearTask(String name, Date CompletionDate){
        var sql = "DELETE FROM tasks WHERE name = ? AND date = ?";
        try(var conn = DriverManager.getConnection(url);
            var pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,name);
            pstmt.setLong(2,CompletionDate.getTime()/1000);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }

    public static void addNoteDB(String taskName, Date completionDate, String note){
        String sql = "UPDATE tasks SET notes = ? WHERE name = ? AND date = ?";
        try (var conn  = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,note);
            pstmt.setString(2,taskName);
            pstmt.setLong(3,completionDate.getTime()/1000);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
}
