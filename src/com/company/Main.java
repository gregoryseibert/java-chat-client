package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        Socket server = null;

        try {
            server = new Socket("127.0.0.1", 5555);

            BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            ClientReader clientReader = new ClientReader(reader);
            clientReader.start();

            PrintWriter writer = new PrintWriter(server.getOutputStream());

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String input = consoleReader.readLine();

                writer.println(input);
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class ClientReader extends Thread {
        private BufferedReader reader;

        public ClientReader(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    System.out.println(reader.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
