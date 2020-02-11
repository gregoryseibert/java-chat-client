package com.company;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class ClientGui {
    private final ClientChat clientChat;

    private JPanel panel;
    private JTextPane chatContent;
    private JTextField chatInput;
    private JButton buttonSend;
    private JScrollPane chatScrollPane;

    public ClientGui(ClientChat clientChat) {
        this.clientChat = clientChat;

        buttonSend.addActionListener(button -> sendMessage(chatInput.getText()));

        chatInput.addActionListener(input -> sendMessage(chatInput.getText()));
    }

    public void show() {
        JFrame frame = new JFrame("Pure wa chat");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    private void sendMessage(String message) {
        clientChat.sendMessage(message);
        chatInput.setText("");
    }

    public void addTextToChatContent(String newContent) {
        String existingText = chatContent.getText();
        if(existingText.length() > 0) {
            existingText += "\n";
        }

        chatContent.setText(existingText + newContent);

        chatContent.setCaretPosition(chatContent.getDocument().getLength());
    }
}
