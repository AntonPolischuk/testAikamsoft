package util;



import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionDB {

    private static Connection connection;

    private ConnectionDB(){
    }

    public static Connection getConnection() throws IOException, SQLException {

        Properties props = new Properties();

        try(InputStream in = Files.newInputStream(Paths.get("src/main/resources/database.properties"))){
            props.load(in);
        } catch (IOException e) {
            ExceptionHandler.getInstance().createErrorToOutputFile("Не найден файл с настройками доступа к базе данных");
            throw e;
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException throwables) {
            ExceptionHandler.getInstance().createErrorToOutputFile("Не корректные настройки доступа к базе данных");
            throw throwables;
        }
        return connection;
    }


    public static void closeConnection(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            //TODO логировать
            throwables.printStackTrace();
        }
    }
}
