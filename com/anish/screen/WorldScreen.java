package com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// import com.anish.sprites.BubbleSorter;
import com.anish.sprites.QuickSorter;
import com.anish.sprites.Sprite;
import com.anish.sprites.World;

import asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {

    private World world;
    private SpritesCollection sprites;
    String[] sortSteps;

    public WorldScreen() {
        try {
            // init(3);
            init(16);
        } catch(IOException e) {
            ;
        }
    }

    private void init(int size) throws IOException {
        world = new World();
        sprites = new SpritesCollection(size, size);

        File file = new File("resources/c256.png");
        BufferedImage bufImage = ImageIO.read(file);

        int rgb = 0;
        int rdm = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                rgb = bufImage.getRGB((int)(j * 35.75 + 13.375), (int)(i * 26.75 + 13.375));
                rdm = sprites.getRandom();
                sprites.set(new Sprite(new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF), i * size + j, world), rdm / size, rdm % size);
                world.put(sprites.get(rdm / size, rdm % size), rdm % size * 2 + 4, rdm / size * 2 + 4);
            }
        }

        QuickSorter<Sprite> qs = new QuickSorter<>();
        qs.load(sprites.toArray());
        qs.sort();

        sortSteps = this.parsePlan(qs.getPlan());
    }

    private String[] parsePlan(String plan) {
        return plan.split("\n");
    }

    private void execute(Sprite[] sprites, String step) {
        String[] couple = step.split("<->");
        getSpriteByRank(sprites, Integer.parseInt(couple[0])).swap(getSpriteByRank(sprites, Integer.parseInt(couple[1])));
    }

    private Sprite getSpriteByRank(Sprite[] sprites, int rank) {
        for (Sprite sprite : sprites) {
            if (sprite.getRank() == rank) {
                return sprite;
            }
        }
        return null;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
    }

    int i = 0;

    @Override
    public Screen respondToUserInput(KeyEvent key) {

        if (i < this.sortSteps.length) {
            this.execute(sprites.toArray(), sortSteps[i]);
            i++;
        }

        return this;
    }

    private class SpritesCollection {
        
        private Sprite[][] sprites;
        int row;
        int col;
        private List<Integer> randomList;
        private int index;

        SpritesCollection(int row, int col) {
            sprites = new Sprite[row][col];
            this.row = row;
            this.col = col;

            index = 0;
            randomList = IntStream.rangeClosed(0, row * col - 1).boxed().collect(Collectors.toList());
            Collections.shuffle(randomList);
        }

        public void set(Sprite sprite, int r, int c) {
            sprites[r][c] = sprite;
        }

        public Sprite get(int r, int c) {
            return sprites[r][c];
        }

        public Sprite[] toArray() {
            Sprite[] ss = new Sprite[row * col];

            int i = 0;
            for(Sprite[] l : sprites) {
                for(Sprite s : l) {
                    ss[i] = s;
                    i++;
                }
            }

            return ss;
        }

        public int getRandom() {
            index = (index + 1) % (row * col);
            return (int)randomList.get(index);
        }
    }
}
