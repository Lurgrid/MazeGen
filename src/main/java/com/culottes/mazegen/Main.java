package com.culottes.mazegen;

public class Main {

    public static void main(String[] args) {
        MazeGen maze = new MazeGen();
        GUI gui = new GUI(0, 999999, maze);
        maze.setGui(gui);
    }
}
