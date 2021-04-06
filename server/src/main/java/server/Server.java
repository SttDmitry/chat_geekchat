package server;

import commands.Command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.*;

public class Server {
    private ServerSocket server;
    private Socket socket;
    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthService authService;
    private BufferedWriter writer;
    private static final Logger logger = Logger.getLogger(Server.class.getName());


    public Server() {

        clients = new CopyOnWriteArrayList<>();
//        authService = new DataBaseAuthService();
        authService = new SimpleAuthService();


        try {
            server = new ServerSocket(PORT);
//            System.out.println("Server started");
            logger.info("Server started");

            while (true) {
                socket = server.accept();
//                System.out.println("Client connected");
                logger.info("Client connected");
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(ClientHandler clientHandler, String msg) throws IOException {
        String message = String.format("[ %s ]: %s", clientHandler.getNickname(), msg);
        File file;
        for (ClientHandler c : clients) {
            logMessage(c.getLogin(), message);
            c.sendMsg(message);
        }
    }

    public void privateMsg(ClientHandler sender, String receiver, String msg) throws IOException {
        String message = String.format("[ %s ] to [ %s ]: %s", sender.getNickname(), receiver, msg);
        logger.info(message);
        for (ClientHandler c : clients) {
            if (c.getNickname().equals(receiver)) {
                logMessage(c.getLogin(), message);
                c.sendMsg(message);
                if (!c.equals(sender)) {
                    logMessage(sender.getLogin(), message);
                    sender.sendMsg(message);
                }
                return;
            }
        }
        sender.sendMsg(String.format("User %s not found", receiver));
    }

    void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientList();
    }

    void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isLoginAuthenticated(String login) {
        for (ClientHandler c : clients) {
            if (c.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public void broadcastClientList() {
        StringBuilder sb = new StringBuilder(Command.CLIENT_LIST);

        for (ClientHandler c : clients) {
            sb.append(" ").append(c.getNickname());
        }

        String msg = sb.toString();
        logger.info(msg);

        for (ClientHandler c : clients) {
            c.sendMsg(msg);
        }
    }

    public void logMessage (String login, String text) throws IOException {
        writer = new BufferedWriter(new FileWriter("logs/history_"+login+".txt", true));
        writer.write(text);
        writer.newLine();
        writer.flush();
        writer.close();
    }
}
