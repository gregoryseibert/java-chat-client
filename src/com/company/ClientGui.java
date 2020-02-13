package com.company;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;

public class ClientGui {
    private final ClientChat clientChat;

    private JPanel panel;
    private JTextField chatInput;
    private JButton buttonSend;
    private JScrollPane chatScrollPane;

    private JTextPane chatContent;
    private HTMLEditorKit editorKit;
    private HTMLDocument document;

    public ClientGui(ClientChat clientChat) {
        this.clientChat = clientChat;

        buttonSend.addActionListener(button -> sendMessage(chatInput.getText()));

        chatInput.addActionListener(input -> sendMessage(chatInput.getText()));

        editorKit = new HTMLEditorKit();
        document = new HTMLDocument();
        chatContent.setEditorKit(editorKit);
        chatContent.setDocument(document);
    }

    public void show() {
        JFrame frame = new JFrame("Pure wa'a chat");
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
        System.out.println(newContent);

        try {
            editorKit.insertHTML(document, document.getLength(), "<p style=\"margin-top: 0\">" + newContent + "</p>", 0, 0, null);
        } catch (BadLocationException | IOException e) {
            e.printStackTrace();
        }

        chatContent.setCaretPosition(chatContent.getDocument().getLength());
    }
}
