package com.company;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ClientGui {
    private final ClientChat clientChat;

    private JPanel panel;
    private JTextField chatInput;
    private JButton buttonSend;
    private JScrollPane chatScrollPane;

    private JTextPane chatContent;
    private JCheckBox checkBoxNotificationSound;
    private HTMLEditorKit editorKit;
    private HTMLDocument document;

    private boolean isInFocus;

    public ClientGui(ClientChat clientChat) {
        this.clientChat = clientChat;

        buttonSend.addActionListener(button -> sendMessage(chatInput.getText()));

        chatInput.addActionListener(input -> sendMessage(chatInput.getText()));

        //checkBoxNotificationSound.setSelected(true);

        editorKit = new HTMLEditorKit();
        document = new HTMLDocument();
        chatContent.setEditorKit(editorKit);
        chatContent.setDocument(document);
    }

    public void show() {
        JFrame frame = new JFrame("Pure Wa'a Chat");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

        frame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                super.windowGainedFocus(e);
                isInFocus = true;
                clientChat.setTrayImageNeutral();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                super.windowLostFocus(e);
                isInFocus = false;
                clientChat.setTrayImageNeutral();
            }
        });
    }

    public boolean isInFocus() {
        return isInFocus;
    }

    public boolean notificationSoundEnabled() {
        return checkBoxNotificationSound.isSelected();
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
