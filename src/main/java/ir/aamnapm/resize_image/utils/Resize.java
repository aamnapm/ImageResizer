package ir.aamnapm.resize_image.utils;

import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Resize {

    private int width;
    private int height;
    private BufferedImage bufferedImage;

    public Resize(int width, int height, BufferedImage bufferedImage) {
        this.width = width;
        this.height = height;
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage resize() {
        Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        final Graphics2D graphics2D = resizedBufferedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();

        return resizedBufferedImage;
    }

}
