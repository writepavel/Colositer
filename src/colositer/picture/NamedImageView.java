/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package colositer.picture;

import colositer.ColositerController;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import colositer.utils.Filename;
import static colositer.utils.SimpleFileIMConverter.convertFile;
//import javax.xml.bind.Element;

/**
 * 1) Replaces initiation of ObservableHashMap by standard ObservableList 2)
 * Contains methods for saving pictures - original and colored.
 *
 * @author pavel
 */
class NamedImageView extends javafx.scene.image.ImageView {

    private String fileAbsolutePath;

    NamedImageView(String url, String filepath) {
        super(url);
        this.fileAbsolutePath = filepath;
        this.setEffect(ColositerController.colorEffect);
    }
    
    String getFileName(){
        Filename name = new Filename(fileAbsolutePath);
        return name.filename()+"."+name.extension();
    }

    NamedImageView(Image image, String filename) {
        super(image);
        this.fileAbsolutePath = filename;
        this.setEffect(ColositerController.colorEffect);
    }

    String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    void saveToFile(File file, String format) {
//        System.out.println("FORMAT = " + format);

        int w = (int) getImage().getWidth();
        int h = (int) getImage().getHeight();
        WritableImage writableImage = new WritableImage(w, h);
        this.snapshot(null, writableImage);
        try {
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
//            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
//            if ("jpg".equals(format.toLowerCase()) || "jpeg".equals(format.toLowerCase())) {
//                System.out.println("SAVING JPEG TO "+ file.getAbsolutePath());
//                //IMJpegSaver.bufferedImageToJpeg(bufferedImage, file.getAbsolutePath());
//                file.createNewFile();
//                saveAsJPEG(format, convertRenderedImage(renderedImage), 0.9f , (FileOutputStream) Files.newOutputStream(file.toPath(), null));
//            } else 
            {
            Filename destname = new Filename(file.getAbsolutePath());
            File tmppng = new File(destname.path()+File.separator+destname.filename()+".tmp.png");
                ImageIO.write(
                        renderedImage,
                        "png",
                        tmppng);
                convertFile(tmppng, file, format);
                tmppng.delete();
            }
        } catch (IOException ex) {
        }

    }

//    /**
//     * Code from here:
//     * http://blog.idrsolutions.com/2012/05/replacing-the-deprecated-java-jpeg-classes-for-java-7/
//     *
//     * @param jpgFlag
//     * @param image_to_save
//     * @param JPEGcompression
//     * @param fos
//     * @throws IOException
//     */
//    public static void saveAsJPEG(String jpgFlag, BufferedImage image_to_save, float JPEGcompression, FileOutputStream fos) throws IOException {
//
//        //useful documentation at http://docs.oracle.com/javase/7/docs/api/javax/imageio/metadata/doc-files/jpeg_metadata.html
//        //useful example program at http://johnbokma.com/java/obtaining-image-metadata.html to output JPEG data
//
//        //old jpeg class
//        //com.sun.image.codec.jpeg.JPEGImageEncoder jpegEncoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(fos);
//        //com.sun.image.codec.jpeg.JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(image_to_save);
//
//        // Image writer
//        JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpeg").next();
//        ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
//        imageWriter.setOutput(ios);
//
//        //and metadata
//        IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image_to_save), null);
//
//        if (jpgFlag != null) {
//
//            int dpi = 96;
//
//            try {
//                dpi = Integer.parseInt(jpgFlag);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            //old metadata
//            //jpegEncodeParam.setDensityUnit(com.sun.image.codec.jpeg.JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);
//            //jpegEncodeParam.setXDensity(dpi);
//            //jpegEncodeParam.setYDensity(dpi);
//
//            //new metadata
//            Element tree = (Element) imageMetaData.getAsTree("javax_imageio_jpeg_image_1.0?");
//            Element jfif = (Element) tree.getElementsByTagName("app0JFIF").item(0);
//            jfif.setAttribute("Xdensity", Integer.toString(dpi));
//            jfif.setAttribute("Ydensity", Integer.toString(dpi));
//
//        }
//
//        if (JPEGcompression >= 0 && JPEGcompression <= 1f) {
//
//            //old compression
//            //jpegEncodeParam.setQuality(JPEGcompression,false);
//
//            // new Compression
//            JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
//            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
//            jpegParams.setCompressionQuality(JPEGcompression);
//
//        }
//
//        //old write and clean
//        //jpegEncoder.encode(image_to_save, jpegEncodeParam);
//
//        //new Write and clean up
//        imageWriter.write(imageMetaData, new IIOImage(image_to_save, null, null), null);
//        ios.close();
//        imageWriter.dispose();
//
//    }
//    
//    /**
//     * Took code from here: http://www.jguru.com/faq/view.jsp?EID=114602
//     * @param img
//     * @return 
//     */
//    public BufferedImage convertRenderedImage(RenderedImage img) {
//		if (img instanceof BufferedImage) {
//			return (BufferedImage)img;	
//		}	
//		ColorModel cm = img.getColorModel();
//		int width = img.getWidth();
//		int height = img.getHeight();
//		WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
//		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
//		Hashtable properties = new Hashtable();
//		String[] keys = img.getPropertyNames();
//		if (keys!=null) {
//			for (int i = 0; i < keys.length; i++) {
//				properties.put(keys[i], img.getProperty(keys[i]));
//			}
//		}
//		BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
//		img.copyData(raster);
//		return result;
//	}
    
}
