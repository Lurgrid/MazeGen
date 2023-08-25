package com.culottes.mazegen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class GUI {
    private JFrame frame;
    private JButton valid;
    private JSpinner sizeHeight;
    private JSpinner sizeWidth;
    private JPanel result;

    public GUI(int defaultSize, int maxSize, ActionListener action) {
        if (defaultSize < 0 || maxSize < 0) {
            throw new AssertionError();
        }
        frame = new JFrame("Maze Generator");
        SpinnerModel model = new SpinnerNumberModel(defaultSize, 0, maxSize, 1);
        SpinnerModel model2 = new SpinnerNumberModel(defaultSize, 0, maxSize, 1);
        sizeHeight = new JSpinner(model);
        sizeWidth = new JSpinner(model2);
        valid = new JButton("Gen");

        valid.addActionListener(action);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30,30, 30));

        panel.add(sizeHeight);
        panel.add(sizeWidth);
        panel.add(valid);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Integer getSizeHeight() {
        return (Integer) sizeHeight.getValue();
    }

    public Integer getSizeWidth() {
        return (Integer) sizeWidth.getValue();
    }

    public void addImageIcon(BufferedImage image) {
        result = new JPanel();
        result.setBorder(BorderFactory.createEmptyBorder(100, 150,100, 100));
        result.add(new JLabel(new ImageIcon(image)));
        frame.add(result);
        frame.pack();
    }

    public void clearImageIcon() {
        if (result == null) {
            return;
        }
        frame.remove(result);
        result = null;
        frame.pack();
    }
}
