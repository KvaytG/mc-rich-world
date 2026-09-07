package ru.kvaytg.coremc;

import javax.swing.*;
import java.awt.*;

public class App {

    private static final String MESSAGE = String.format("This JAR is not a standalone program.\n" +
            "It's a plugin for a Minecraft server running Paper or its forks (v. %s)", ProjectInfo.SERVER_VERSION);
    private static final Font MESSAGE_FONT = new Font("Arial", Font.BOLD, 14);

    public static void main(String[] args) {
        System.err.println(MESSAGE);
        UIManager.put("OptionPane.messageFont", MESSAGE_FONT);
        JOptionPane.showMessageDialog(null, MESSAGE, ProjectInfo.NAME, JOptionPane.PLAIN_MESSAGE);
    }

}