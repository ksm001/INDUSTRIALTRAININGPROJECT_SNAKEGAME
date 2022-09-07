package SnakeGame;

import java.awt.Color;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setBounds(10,10,905,700);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        GamePanel panel = new GamePanel();
        panel.setBackground(Color.BLACK);
        frame.add(panel);
        frame.setVisible(true);
    }
}