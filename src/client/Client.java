package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost",8188);
            System.out.println("Подключился");
            DataInputStream  in =new DataInputStream(socket.getInputStream());
            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
            String response = in.readUTF(); // Ждём сообщение от сервера
            System.out.println("Ответ от сервера: "+response);
            Scanner scanner = new Scanner(System.in);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try {
                            String response = in.readUTF();
                            System.out.println(response);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
            while(true){
                String request = scanner.nextLine(); // Читам пользовательский ввод
                out.writeUTF(request); // Отправляем на сервер текст с коносли
            }


        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
