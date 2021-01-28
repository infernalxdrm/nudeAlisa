package core.services.video;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImagePr {
    public static BufferedImage addText(BufferedImage im, String line, int x, int y, Color color, int text_size) {
        Graphics2D g = im.createGraphics();
        /**Set Antialias Rendering**/
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        /**
         * Draw background image at location (0,0)
         * You can change the (x,y) value as required
         */
        g.drawImage(im, 0, 0, null);
        Font font = new Font("Arial", Font.BOLD, text_size);
        g.setFont(font);
        g.setColor(color);
        g.drawString(line, x, y);
        g.dispose();
        return im;
    }

    public static BufferedImage overlayImages(BufferedImage bgImage,
                                              BufferedImage fgImage, int x, int y) {

        /**
         * Doing some preliminary validations.
         * Foreground image height cannot be greater than background image height.
         * Foreground image width cannot be greater than background image width.
         *
         * returning a null value if such condition exists.
         */
        if (fgImage.getHeight() > bgImage.getHeight()
                || fgImage.getWidth() > fgImage.getWidth()) {
            System.err.print("Foregorund image is bigger than bg, you fool");
            return null;
        }

        /**Create a Graphics  from the background image**/
        Graphics2D g = bgImage.createGraphics();
        /**Set Antialias Rendering**/
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        /**
         * Draw background image at location (0,0)
         * You can change the (x,y) value as required
         */
        g.drawImage(bgImage, 0, 0, null);

        /**
         * Draw foreground image at location (0,0)
         * Change (x,y) value as required.
         */
        g.drawImage(fgImage, x, y, null);


        g.dispose();
        return bgImage;
    }

    /**
     * This method reads an image from the file
     *
     * @param fileLocation -- > eg. "C:/testImage.jpg"
     * @return BufferedImage of the file read
     */
    @SneakyThrows
    public static BufferedImage readImage(String fileLocation) {
        BufferedImage image = null;
        final URL url = new URL(fileLocation);
        final HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
        connection.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
        image = ImageIO.read(connection.getInputStream());


        return image;
    }

    /**
     * This method writes a buffered image to a file
     *
     * @param img          -- > BufferedImage
     * @param fileLocation --> e.g. "C:/testImage.jpg"
     * @param extension    --> e.g. "jpg","gif","png"
     */
    public static void writeImage(BufferedImage img, String fileLocation,
                                  String extension) {
        try {
            BufferedImage bi = img;
            File outputfile = new File(fileLocation);
            ImageIO.write(bi, extension, outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage changeResolution(BufferedImage i, int x, int y) {
        try {
            return TV.resizeImage(i, x, y);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
