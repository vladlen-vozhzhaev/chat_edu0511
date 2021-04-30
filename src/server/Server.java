package server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();
        System.out.println("Сервер запущен");
        try {
            ServerSocket serverSocket = new ServerSocket(8188);
            while (true){
                Socket socket = serverSocket.accept(); //Ожидаем клиента
                System.out.println("Клиент подключился");
                User currentUser = new User(socket);
                users.add(currentUser);
                Thread thread = new Thread(new Runnable() { // Создаёи поток для подключившегося клиента
                    @Override
                    public void run() {
                        try {
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            DataInputStream in = new DataInputStream(socket.getInputStream());
                            out.writeUTF("Введите имя:");
                            String userName = in.readUTF();
                            currentUser.setUserName(userName);
                            out.writeUTF(currentUser.getUserName()+" добро пожаловать на сервер!");
                            while (true){
                                System.out.println("Ожидаем сообщение от пользователя");
                                String request = in.readUTF(); // Ожидаем сообщение от клиента
                                System.out.println(currentUser.getUserName()+": "+request);
                                for (User user:users) {
                                    if(currentUser.equals(user)) continue;
                                    DataOutputStream userOut = new DataOutputStream(user.getSocket().getOutputStream());
                                    userOut.writeUTF(userName+": "+request);
                                }
                            }
                        } catch (IOException exception) {
                            users.remove(currentUser);
                            for (User user:users) {
                                try {
                                    DataOutputStream userOut = new DataOutputStream(user.getSocket().getOutputStream());
                                    userOut.writeUTF(currentUser.getUserName()+" покинул чат");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                });
                thread.start();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
