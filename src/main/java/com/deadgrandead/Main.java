package com.deadgrandead;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<String> messages = new ArrayList<>(); // хранилище сообщений
    public static void main(String[] args) {
        final var server = new Server(64, 9999);

        server.addHandler("GET", "/messages", (request, out) -> {
            try {
                // Конвертируем список сообщений в строку и отправляем обратно
                String response = String.join("\n", messages);
                out.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: text/plain\r\n" +
                                "Content-Length: " + response.getBytes().length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n" +
                                response
                ).getBytes());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        server.addHandler("POST", "/messages", (request, out) -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getBody()))) {
                // Прочитать сообщение из тела запроса
                String newMessage = reader.readLine();
                messages.add(newMessage); // добавляем в хранилище

                // Отправляем ответ об успешном добавлении
                out.write((
                        "HTTP/1.1 201 Created\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        server.start();
    }
}