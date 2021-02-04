package server;


import java.sql.*;

public class DataBaseAuthService implements AuthService {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement prepStmt;
    private static PreparedStatement prepStmtReg;


    private static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:main.db");
        stmt = connection.createStatement();
    }

    private static void disconnect () {
        try {
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void prepareAllStatements () throws SQLException {
        prepStmt = connection.prepareStatement("SELECT login, password, nickname FROM auth WHERE login = ? AND password = ? ;");
        prepStmtReg = connection.prepareStatement("INSERT INTO auth (login, password, nickname) VALUES ( ? , ? , ?);");
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        ResultSet rs;
        try {
            connect();
            prepareAllStatements();
            prepStmt.setString(1,login);
            prepStmt.setString(2,password);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                return rs.getString("nickname");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        ResultSet rs;
        try {
            connect();
            prepareAllStatements();
            prepStmt.setString(1,login);
            prepStmt.setString(2,password);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                return false;
            }
            prepStmtReg.setString(1,login);
            prepStmtReg.setString(2,password);
            prepStmtReg.setString(3,nickname);
            prepStmtReg.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
//        for (SimpleAuthService.UserData user : users) {
//            if(user.login.equals(login) || user.nickname.equals(nickname)){
//                return false;
//            }
//        }
//        users.add(new SimpleAuthService.UserData(login, password, nickname));
        return true;
    }
}
