package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientChat {
    private PrintWriter writer;
    private ClientReader clientReader;
    private ClientGui clientGui;
    private Socket server;

    public ClientChat() {

    }

    public void start() {
        clientGui = new ClientGui(this);
        clientGui.show();

        try {
            server = new Socket("127.0.0.1", 5555);

            BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));

            clientReader = new ClientReader(this, reader);
            clientReader.start();

            writer = new PrintWriter(server.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            if(clientReader != null) {
                clientReader.stopTask();
            }
        }
    }

    public void stop() {
        //server.close();
        //reader.close();
        //writer.close();
    }

    public void sendMessage(String message) {
        if(writer != null && message.length() > 0) {
            writer.println(message);
            writer.flush();
        }
    }

    ClientGui getClientGui() {
        return clientGui;
    }

    static class ClientReader extends Thread {
        private ClientChat clientChat;

        private BufferedReader reader;
        private boolean isRunning;

        public ClientReader(ClientChat clientChat, BufferedReader reader) {
            this.clientChat = clientChat;
            this.reader = reader;
            isRunning = true;
        }

        @Override
        public void run() {
            while(isRunning) {
                try {
                    String input = reader.readLine();
                    if (input == null) {
                        isRunning = false;
                    } else {
                        clientChat.getClientGui().addTextToChatContent(input);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    isRunning = false;
                }
            }
        }

        public void stopTask() {
            isRunning = false;
        }

        public boolean isRunning() {
            return isRunning;
        }
    }
}
