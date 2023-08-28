package com.culottes.mazegen;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MazeGen implements ActionListener {

    GUI gui;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gui == null || gui.getSizeWidth() == 0 || gui.getSizeHeight() == 0) {
            return;
        }
        gui.clearImageIcon();
        Maze maze = new Maze(gui.getSizeHeight(), gui.getSizeWidth());
        maze.run();
        gui.addImageIcon(maze.mazeToBuffredImage());
        try {
            maze.mazeToMinecraftWorld();
        } catch (IOException ex) {
            System.out.println("Error: Cannot save the minecraft world");
        }
    }
}
