package com.culottes.mazegen;

import java.io.File;

import net.morbz.minecraft.blocks.CustomBlock;
import net.morbz.minecraft.blocks.Material;
import net.morbz.minecraft.level.FlatGenerator;
import net.morbz.minecraft.level.GameType;
import net.morbz.minecraft.level.IGenerator;
import net.morbz.minecraft.level.Level;
import net.morbz.minecraft.world.DefaultLayers;
import net.morbz.minecraft.world.World;

import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Maze {

    private byte[][] matrix;
    private int height;
    private int width;
    private Stack<Point> stack;

    public Maze(int height, int width) {
        if (height <= 0 || width <= 0) {
            throw new AssertionError();
        }
        matrix = new byte[width][height];
        this.height = height;
        this.width = width;
        stack = new Stack<Point>();
    }

    private Point getUnvisitedNeighbour(Point p) {
        if (p == null || p.x < 0 || p.x >= width || p.y < 0 || p.y >= height) {
            throw new AssertionError();
        }
        Point[] pl = new Point[]{
                new Point(p.x - 2, p.y),
                new Point(p.x + 2, p.y),
                new Point(p.x, p.y + 2),
                new Point(p.x, p.y - 2)
        };
        pl = Arrays
                .stream(pl)
                .filter(pp -> pp.x >= 0 && pp.x < width && pp.y >= 0 && pp.y < height && matrix[pp.x][pp.y] == 0)
                .toArray(Point[]::new);
        if (pl.length == 0) {
            return null;
        }
        return pl[(new Random()).nextInt(pl.length)];
    }

    private void setVisited(Point a) {
        if (a == null || a.x < 0 || a.x >= width || a.y < 0 || a.y >= height) {
            throw new AssertionError();
        }
        matrix[a.x][a.y] = 1;
    }

    private void drawXLine(Point a, int x) {
        if (a == null || a.x < 0 || a.x >= width || a.y < 0 || a.y >= height) {
            throw new AssertionError();
        }
        int ax = a.x + 1;
        while (x > 0) {
            matrix[ax][a.y] = 1;
            ++ax;
            --x;
        }
    }

    private void drawYLine(Point a, int y) {
        if (a == null || a.x < 0 || a.x >= width || a.y < 0 || a.y >= height) {
            throw new AssertionError();
        }
        int ay = a.y + 1;
        while (y > 0) {
            matrix[a.x][ay] = 1;
            ++ay;
            --y;
        }
    }

    private void genMaze() {
        while (stack.size() != 0) {
            Point a = stack.pop();
            Point b = getUnvisitedNeighbour(a);
            System.out.println(b);
            if (b != null) {
                stack.push(a);
                setVisited(b);
                stack.push(b);
                if (a.x > b.x) {
                    drawXLine(b, a.x - b.x);
                }else if (a.x < b.x) {
                    drawXLine(a, b.x - a.x);
                }else if (a.y > b.y) {
                    drawYLine(b, a.y - b.y);
                }else  {
                    drawYLine(a, b.y - a.y);
                }
            }
        }
    }

    public void run() {
        Point i = new Point(0, 0);
        setVisited(i);
        stack.push(i);
        genMaze();
    }

    public BufferedImage mazeToBuffredImage() {
        int expand = 5;
        BufferedImage image = new BufferedImage(expand * width, expand * height, BufferedImage.TYPE_BYTE_INDEXED);
        for(int i = 0; i < expand * width; ++i) {
            for(int j = 0; j < expand * height; ++j) {
                Color newColor;
                if (matrix[i / expand][j / expand] == 0) {
                    newColor = new Color(0, 0, 0);
                } else {
                    newColor = new Color(255, 255, 255);
                }
                image.setRGB(i, j, newColor.getRGB());
            }
        }
        try {
            File output = new File("GrayScale.png");
            ImageIO.write(image, "png", output);
        } catch(Exception e) {
            System.out.println("Error:");
        }
        return image;
    }

    public void mazeToMinecraftWorld() throws IOException {
        DefaultLayers layers = new DefaultLayers();
        layers.setLayer(0, Material.STONE);

        IGenerator generator = new FlatGenerator(layers);

        Level level = new Level("MazeWorld", generator);
        level.setMapFeatures(false);
        level.setGameType(GameType.CREATIVE);
        level.setSpawnPoint(0, 5, 0);

        World world = new World(level, layers);

        for(int x = 0; x < width; ++x) {
            for(int z = 0; z < height; ++z) {
                if (matrix[x][z] == 0) {
                    for(int y = 1; y < 4; ++y) {
                        world.setBlock(x, y, z, new CustomBlock(Material.WOOL.getValue(), 0, 0));
                    }
                }
            }
        }

        world.save();
    }
}