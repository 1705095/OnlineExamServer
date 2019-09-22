package Connectivity;

import java.sql.Connection;
import java.sql.DriverManager;

public class QuestionDBConnector {
    public static Connection connection;

    public static Connection getConnection() {
        String dbName = "projectQuestion";
        String username = "root";
        String password = "";
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName, username, password);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return connection;

    }
}
