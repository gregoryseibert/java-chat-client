package com.company;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientChat {
    private PrintWriter writer;
    private ClientReader clientReader;
    private ClientGui clientGui;
    private Socket server;

    public ClientChat() {

    }

    public void start() {
        clientGui = new ClientGui(this);

        String serverAddress = JOptionPane.showInputDialog("Server IP address: ", "127.0.0.1");

        clientGui.show();

        try {
            server = new Socket(serverAddress, 5555);

            BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream(), StandardCharsets.UTF_8));

            clientReader = new ClientReader(this, reader);
            clientReader.start();

            writer = new PrintWriter(new OutputStreamWriter(server.getOutputStream(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
            if(clientReader != null) {
                clientReader.stopTask();
            }
            clientGui.addTextToChatContent("An error occurred. Is the server running and accessable?");
        }
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
                    clientChat.getClientGui().addTextToChatContent("An error occurred. Is the server running and accessable?\nPlease restart this client.");
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
