import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class sqlLogic {

    public static void connect(){
        var url = "jdbc:sqlite:toDo.db";

        var sql = "CREATE TABLE IF NOT EXISTS tasks ("
                +" name TEXT PRIMARY KEY, "
                +" date INTEGER NOT NULL"
                + ");";

        try (var conn = DriverManager.getConnection(url);
            var stmt = conn.createStatement()){

            stmt.execute(sql);

        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }

    public static void createDB(){
        String url = "jdbc:sqlite:toDo.db";
        try( var conn = DriverManager.getConnection(url)){
            if (conn != null){
                var meta  = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new DB has been created");
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }

    public static void insertTask(String taskName, Date completionDate ){
        String url = "jdbc:sqlite:toDo.db";

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
        var url = "jdbc:sqlite:toDo.db";
        var sql = "SELECT name, date FROM tasks";

        try(var conn = DriverManager.getConnection(url);
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery(sql)){
            while (rs.next()){
                AppGui.taskSection(rs.getString("name"),new Date(rs.getLong("date")*1000));
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
}
