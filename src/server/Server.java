package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        ArrayList<Socket> users = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(8188);
            while (true){
                Socket socket = serverSocket.accept(); //Ожидаем клиента
                System.out.println("Клиент подключился");
                users.add(socket);
                Thread thread = new Thread(new Runnable() { // Создаёи поток для подключившегося клиента
                    @Override
                    public void run() {
                        try {
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            DataInputStream in = new DataInputStream(socket.getInputStream());
                            out.writeUTF("Добро пожаловать на сервер!");
                            while (true){
                                System.out.println("Ожидаем сообщение от пользователя");
                                String request = in.readUTF(); // Ожидаем сообщение от клиента
                                for (Socket user:users) {
                                    DataOutputStream userOut = new DataOutputStream(user.getOutputStream());
                                    userOut.writeUTF(request);
                                }
                            }
                        } catch (IOException exception) {
                            exception.printStackTrace();
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
