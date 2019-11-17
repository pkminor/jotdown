import dao.Sql2oJoteDao;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static spark.Spark.get;

public class App {
    public static void main(String[] args) {

        String connectionStr="jdbc:postgresql://localhost:5432/jotdown";
        Sql2o sql2o = new Sql2o(connectionStr,"pkminor","password");

        Sql2oJoteDao joteDao = new Sql2oJoteDao(sql2o);

        get("/", (req,res)->"Hello spark world");


    }
}
