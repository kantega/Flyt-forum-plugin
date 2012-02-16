package no.kantega.forum.util;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 *
 */
public class ImageHelper {
    /**
     * Tar bilde fra en ByteArrayOutputStream og returnerer en ny ByteArrayOutputStream
     * med krympet bilde
     *
     * @param bos         - Stream med orginalt bilde
     * @param width       - Ny bredde på bilde
     * @param height      - Ny høyde på bilde
     * @param imageFormat - Bildeformat jpg / png
     * @param quality     - Kvalitet for jpg bilder
     * @return - Resultatbilde
     * @throws InterruptedException
     * @throws IOException
     */

    public static ByteArrayOutputStream resizeImage(ByteArrayOutputStream bos, int width, int height, String imageFormat, int quality) throws InterruptedException, IOException {
        Image image = Toolkit.getDefaultToolkit().createImage(bos.toByteArray());

        ResizedImage img = resizeImage(image, width, height);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        ImageWriter writer = null;
        Iterator iter = ImageIO.getImageWritersByFormatName(imageFormat);
        if (iter.hasNext()) {
            writer = (ImageWriter) iter.next();
        }

        ImageOutputStream ios = ImageIO.createImageOutputStream(bout);
        writer.setOutput(ios);

        ImageWriteParam iwparam = null;
        if (imageFormat.equalsIgnoreCase("jpg")) {
            iwparam = new JPEGImageWriteParam(Locale.getDefault());
            iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            float q = ((float) quality) / ((float) 100);
            iwparam.setCompressionQuality(q);
        }
        writer.write(null, new IIOImage(img.getBufferedImage(), null, null), iwparam);

        ios.flush();
        writer.dispose();
        return bout;
    }

    public static ResizedImage resizeImage(Image img, int targetWidth, int targetHeight) throws InterruptedException {
        boolean higherQuality = true;
        int type = BufferedImage.TYPE_INT_RGB;

        MediaTracker mediaTracker = new MediaTracker(new Container());
        mediaTracker.addImage(img, 0);
        mediaTracker.waitForID(0);

        BufferedImage ret = null;

        // get size of src-image
        int w = img.getWidth(null);
        int h = img.getHeight(null);

        // make sure target-size is valid
        if (targetWidth == -1 || targetWidth > w) {
            targetWidth = w;
        }

        if (targetHeight == -1 || targetHeight > h) {
            targetHeight = h;
        }

        // make sure target is smaller that src-image
        if (w < targetWidth || h < targetHeight) {
            if (w < targetWidth) {
                w = targetWidth;
            }

            if (h < targetHeight) {
                h = targetHeight;
            }
        }

        // hints for rendering
        Map map = new HashMap();
        map.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // keep AR
        double thumbRatio = (double) targetWidth / (double) targetHeight;
        double imageRatio = (double) w / (double) h;
        if (thumbRatio < imageRatio) {
            targetHeight = (int) (targetWidth / imageRatio);
        } else {
            targetWidth = (int) (targetHeight * imageRatio);
        }


        if (!higherQuality) {
            w = targetWidth;
            h = targetHeight;
        }

        // do the resize
        int iter = 0;
        do {
            if (higherQuality && w > targetWidth) {
                w >>= 1;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h >>= 1;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);

            if (iter == 0) {
                Graphics g = tmp.createGraphics();
                g.drawImage(img, 0, 0, w, h, null);
                g.dispose();
            } else {
                Graphics2D g2 = tmp.createGraphics();
                g2.setRenderingHints(map);
                g2.drawImage(ret, 0, 0, w, h, null);
                g2.dispose();
            }

            ret = tmp;
            iter++;
        } while (w != targetWidth || h != targetHeight);

        // return new image
        return new ResizedImage(ret, w, h);
    }

    public static class ResizedImage {
        private BufferedImage bufferedImage;
        private int width;
        private int height;

        public ResizedImage(BufferedImage bufferedImage, int width, int height) {
            this.bufferedImage = bufferedImage;
            this.width = width;
            this.height = height;
        }

        public BufferedImage getBufferedImage() {
            return bufferedImage;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
