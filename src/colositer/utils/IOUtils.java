/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package colositer.utils;

//import colositer.picture.IMimageView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author pavel
 */
public class IOUtils {
    
    public static InputStream createISfromBI(BufferedImage bi) {
        ByteArrayOutputStream os = new ByteArrayOutputStream() {
            @Override
            public synchronized byte[] toByteArray() {
                return this.buf;
            }
        };
        try {
            ImageIO.write(bi, "png", os);
        } catch (IOException ex) {
            Logger.getLogger(IOUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ByteArrayInputStream(os.toByteArray(), 0, os.size());
    }
    
  
}
