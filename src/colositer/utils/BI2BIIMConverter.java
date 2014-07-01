/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package colositer.utils;

//import colositer.picture.IMimageView;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Stream2BufferedImage;
import org.im4java.process.Pipe;

/**
 * Converts images from BufferedImage to BufferedImage by ImageMagic
 * @author pavel
 */
abstract class BI2BIIMConverter {
    private static ConvertCmd convert = new ConvertCmd();
    
    BufferedImage inputBuf;
    BufferedImage outputBuf;

    public BI2BIIMConverter(BufferedImage input, BufferedImage output){
        inputBuf = input;
        outputBuf = output;
    }
    
    abstract IMOperation operation(Object[] args);
    
    
    abstract void customConversion(IMOperation op, Object[] args);
    
    private IMOperation createOperation(Object[] args){
        IMOperation op = new IMOperation();
                op.addImage("-");  
                customConversion(op, args);
                op.addImage("png:-");                        // output
                return op;
    }

    void runOperation(IMOperation op) throws IOException {
        /*
         * 1. Invalidate bufferedImage by ImageMagic
         * 2. this.Image = new BI
         */
        Pipe pipeIn = new Pipe(IOUtils.createISfromBI(inputBuf), null);
        BI2BIIMConverter.convert.setInputProvider(pipeIn);
        Stream2BufferedImage s2b = new Stream2BufferedImage();
        BI2BIIMConverter.convert.setOutputConsumer(s2b);
        try {
            BI2BIIMConverter.convert.run(op);
            BufferedImage outputBuf = s2b.getImage();
//            refreshImage(outputBuf);
        } catch (InterruptedException | IM4JavaException ex) {
            Logger.getLogger(BI2BIIMConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
