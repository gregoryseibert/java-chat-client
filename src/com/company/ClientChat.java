package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientChat {
    private PrintWriter writer;
    private ClientReader clientReader;
    private ClientGui clientGui;
    private Socket server;

    private TrayIcon trayIcon;
    private Image imageNeutral, imageAttention;
    private boolean imageIsNeutral;
    private final String defaultIpAddress = "127.0.0.1";

    public ClientChat() {
        try {
            if (!SystemTray.isSupported()) {
                System.out.println("SystemTray is not supported");
                return;
            }

            SystemTray tray = SystemTray.getSystemTray();

            imageNeutral = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));
            imageAttention = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon-2.png"));

            trayIcon = new TrayIcon(imageNeutral, "Pure Wa'a Chat");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("Pure Wa'a Chat");

            imageIsNeutral = true;

            tray.add(trayIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTrayImageNeutral() {
        if(!imageIsNeutral) {
            imageIsNeutral = true;
            trayIcon.setImage(imageNeutral);
        }
    }

    public void setTrayImageAttention() {
        if(imageIsNeutral && !clientGui.isInFocus()) {
            imageIsNeutral = false;
            trayIcon.setImage(imageAttention);

            if(clientGui.notificationSoundEnabled()) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    public void start() {
        clientGui = new ClientGui(this);

        String serverAddress = JOptionPane.showInputDialog("Server IP address: ", defaultIpAddress);

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

    public void sendNotification() {
        trayIcon.displayMessage("Pure wa'a chat", "New messages available", TrayIcon.MessageType.INFO);
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
                        clientChat.setTrayImageAttention();
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
