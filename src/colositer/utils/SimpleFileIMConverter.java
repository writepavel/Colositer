/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package colositer.utils;

import java.io.File;
import org.im4java.core.IMOperation;

/**
 *
 * @author pavel
 */
public final class SimpleFileIMConverter extends File2FileIMConverter{

    public SimpleFileIMConverter(File input, File output, String fileFormat) {
        super(input, output, fileFormat);
    }
    
    void run(){
        Object[] o = {};
        super.run(o);
    }
    

    void customConversion(IMOperation op, Object[] args) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
        
        public static void convertFile(File inputfile, File outputfile, String fileFormat) {
           new SimpleFileIMConverter(inputfile, outputfile, fileFormat).run();
    }

}
