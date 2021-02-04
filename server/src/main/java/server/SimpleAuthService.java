package server;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleAuthService implements AuthService {
    private class UserData {
        String login;
        String password;
        String nickname;

        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }

    private List<UserData> users;

    public SimpleAuthService() {
        users = new ArrayList<>();
        users.add(new UserData("qwe", "qwe", "qwe"));
        users.add(new UserData("asd", "asd", "asd"));
        users.add(new UserData("zxc", "zxc", "zxc"));
        for (int i = 1; i < 10; i++) {
            users.add(new UserData("user" + i, "pass" + i, "nick" + i));
        }
        createFiles();
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password)  {
        for (UserData user : users) {
            if(user.login.equals(login) && user.password.equals(password)){
                return user.nickname;
            }
        }
        return null;
    }

    public void createFiles () {
        for (UserData user : users) {
            File file = new File("logs/history_"+user.login+".txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getMessageForNick (String nick) {
        StringBuilder sb = new StringBuilder();
        String[] strArr = null;
        File file = null;
        for (UserData user : users) {
            if(user.nickname.equals(nick)){
                file = new File ("logs/history_"+user.login+".txt");
            }
        }

        if (file!=null) {
            try {
                Object[] objectArray =  Files.readAllLines(file.toPath()).toArray();
                strArr =  Arrays.copyOf(objectArray, objectArray.length, String[].class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }

        for (int i = 0; i < strArr.length; i++) {
            sb.append(strArr[i]+"\n");
        }

        return sb.toString();
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        for (UserData user : users) {
            if(user.login.equals(login) || user.nickname.equals(nickname)){
                return false;
            }
        }
        users.add(new UserData(login, password, nickname));
        createFiles();
        return true;
    }
}
