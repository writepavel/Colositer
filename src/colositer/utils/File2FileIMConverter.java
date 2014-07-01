/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package colositer.utils;

import colositer.project.IMeffect;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;
import org.im4java.process.Pipe;
import org.im4java.process.ProcessStarter;
import static colositer.utils.LinuxInteractor.*;

/**
 *
 * @author pavel
 */
public abstract class File2FileIMConverter {

    
    public static void applyManualIM(String commandLine, File inputFile, File outputFile) {
//        LinuxInteractor bash = new LinuxInteractor();
        String cmd = "convert "+inputFile.getAbsolutePath()+" "+commandLine+" "+outputFile.getAbsolutePath();
        String r = executeCommand(cmd, true);
        System.out.println(r);
    }
    public static void applyManualIM_old(String commandLine, File inputFile, File outputFile) {
        
        File2FileIMConverter colorfile = new File2FileIMConverter(inputFile, outputFile) {
            @Override
            void customConversion(IMOperation op, Object[] args) {
                String commandLine = (String) args[0];
                op.addRawArgs(commandLine);
//                ImageCommand s = new ConvertCmd();
//                LinkedList<String> cmdlist = new LinkedList<>();
//                cmdlist.add(commandLine);
//                convert.setCommand(commandLine);
//                s.
//                op.set("option:modulate:colorspace", "hsb");
//                op.modulate(effect.getBrightness(), effect.getSaturation(), effect.getHue());
//                op.brightnessContrast(Double.valueOf(0), effect.getContrast());
            }
        };
        Object[] args = {commandLine};
        colorfile.run(args);
    }
    
    File inFile;
    File outFile;
String fileFormat;// = "png"; // DEFAULT. If fileFormat did not setup explicitly.
    
    
    
    public File2FileIMConverter(File input, File output) {
        inFile = input;
        outFile = output;
        fileFormat = new Filename(output.getName()).extension();
    }
    
    public File2FileIMConverter(File input, File output, String fileFormat){
        this(input, output);
        //this.fileFormat = fileFormat;
        fileFormat = new Filename(output.getName()).extension();
    }
    
    abstract void customConversion(IMOperation op, Object[] args);
    
    private IMOperation createOperation(Object[] args){
        IMOperation op = new IMOperation();
                op.addImage("-");  
                customConversion(op, args);
                op.addImage(fileFormat+":-");                        // output
                return op;
    }

    ConvertCmd convert = new ConvertCmd();
    
    void run(Object[] args) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inFile.getAbsolutePath());
            try (final FileOutputStream fos = new FileOutputStream(outFile.getAbsolutePath())) {
                Pipe pipeIn = new Pipe(fis, null);
                Pipe pipeOut = new Pipe(null, fos);
                
                convert.setInputProvider(pipeIn);
                convert.setOutputConsumer(pipeOut);
                System.out.println(convert.getCommand());
                convert.run(createOperation(args));
                
                fis.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(File2FileIMConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(File2FileIMConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException | IM4JavaException ex) {
            Logger.getLogger(File2FileIMConverter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(File2FileIMConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
        public static void applyIMEffect(IMeffect effect, File inputfile, File outputfile) {
        File2FileIMConverter colorfile = new File2FileIMConverter(inputfile, outputfile) {
            @Override
            void customConversion(IMOperation op, Object[] args) {
                IMeffect effect = (IMeffect) args[0];
                op.set("option:modulate:colorspace", "hsb");
                op.modulate(effect.getBrightness(), effect.getSaturation(), effect.getHue());
                op.brightnessContrast(Double.valueOf(0), effect.getContrast());
            }
        };
        Object[] args = {effect};
        colorfile.run(args);
    }

}
