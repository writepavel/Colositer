/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package colositer.project;

import colositer.ColositerController;
import colositer.picture.SmartPicture;
import static colositer.ColositerController.colorEffect;
import static colositer.ColositerController.picArray;
import colositer.utils.File2FileIMConverter;
import colositer.utils.Filename;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import static java.util.Calendar.*;
import static java.nio.file.StandardCopyOption.*;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.effect.ColorAdjust;
import eu.hansolo.enzo.notification.Notifier;

/**
 *
 * @author pavel
 */
public final class Project {

    public static Project fileman = new Project();
    public File projectDir;
    private File originalImagesDir;
    private File coloredImagesDir;
    private File imConvertedImagesDir;
    private File snapshotFile;
    private String projectName = "";
    private static Project instance;

    public static double round(double value){
        return Math.round( value * 100.0 ) / 100.0;
    }
    
    public static Project createProject(String name) {
//        if (instance == null) {
            instance = new Project(name);
//        }
        return instance;
    }

    private static String current(int field) {
        String val;
        if (field == MONTH) {
            val = Calendar.getInstance().getDisplayName(field, SHORT, Locale.US);
        } else {
            int intval = Calendar.getInstance().get(field);
            val = String.valueOf(intval);
        }
        return val;

    }

    static String now() {
        return current(DATE) + "-" + current(MONTH) + "-" + current(YEAR) + "-at-" + current(HOUR) + "-" + current(MINUTE);

    }

    void createFolderForProject() {
        String currentDir = System.getProperty("user.home") + File.separator + "ColositerProjects";
        projectDir = new File(currentDir, projectName + "_" + now());
        projectDir.mkdirs();
        originalImagesDir = new File(projectDir, "originalImages");
        originalImagesDir.mkdir();
        coloredImagesDir = new File(projectDir, "coloredImages");
        coloredImagesDir.mkdir();
        imConvertedImagesDir = new File(projectDir, "imConvertedImages");
        imConvertedImagesDir.mkdir();
        snapshotFile = new File(projectDir, projectName + "_" + now()+".xml");
        try {
            snapshotFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("CREATED PROJECT DIRS IN: " + projectDir.getAbsolutePath());
    }

    // DO NOT USE
    private Project() {}
    
    static XStream xstream = new XStream(new StaxDriver());

    private Project(String projectName) {
        this.projectName = projectName;
        createFolderForProject();
        saveEffectToFile();
        xstream.alias("effectSnapshot", EffectSnapshot.class);
    }
    
    public File getImConvertedImagesDir(){
        return imConvertedImagesDir;
    }

    private void saveEffectToFile() {
        String text = convertEffectToXML();
        writeToFile(text, snapshotFile);
    }

    private static String convertEffectToXML() {

        String xml = xstream.toXML(new EffectSnapshot());
        return xml;
    }

    private static EffectSnapshot loadEffectSnapshot(File xmlfile) {
        String xml = Project.readFromTxtfile(xmlfile);
        EffectSnapshot eff = (EffectSnapshot) xstream.fromXML(xml);
        return eff;
    }

    public static void setColorEffectFromXML(File xmlfile) {
        EffectSnapshot eff = Project.loadEffectSnapshot(xmlfile);
        colorEffect.setContrast(eff.getContrast());
        colorEffect.setBrightness(eff.getBrightness());
        colorEffect.setHue(eff.getHue());
        colorEffect.setSaturation(eff.getSaturation());
    }


    /*
     * Text took from http://docs.oracle.com/javase/tutorial/essential/io/file.html#textfiles
     */
    private static void writeToFile(String s, File file) {
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), charset)) {
            writer.write(s, 0, s.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    private static String readFromTxtfile(File txtfile) {
        Charset charset = Charset.forName("US-ASCII");
        String line = "";
        try {
            List<String> lineList = Files.readAllLines(txtfile.toPath(), charset);
            for (String str : lineList) {
                line = line + str;
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        return line;
    }

    public void saveAllOriginalImages() {
        for (SmartPicture pic : picArray) {
            saveOriginalImage(pic);
        }
    }

    public void saveAllModifiedImages() {
        for (SmartPicture pic : picArray) {
            saveModifiedImage(pic);
        }
    }

    private void saveOriginalImage(SmartPicture picture) {
        // COPY FILE
        //Image img = view.getImage();
        String filename = picture.getFileName();
        String filepath = picture.getFileAbsolutePath();
//        System.out.println("SAVING ORIGINAL IMAGE: " + filename);
        File srfile = new File(filepath);
        Path sr = srfile.toPath();
        Path tg = new File(originalImagesDir.getAbsolutePath() + "/" + filename).toPath();
        try {
            Files.copy(sr, tg, REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void saveModifiedImage(SmartPicture picture) {
        String filename = picture.getFileName();
        String name = new Filename(filename).filename();
//        System.out.println("SAVING MODIFIED IMAGE TO " + extension(filename) + ": " + filename);
        
        //saveImageToFile(picture, coloredImagesDir.getAbsolutePath() + "/" + filename, "png");
        saveImageToFile(picture, coloredImagesDir.getAbsolutePath() + "/" + filename, extension(filename));
        // saver.saveImageToFile(coloredImagesDir.getAbsolutePath() + "/" + filename, extension(filename));
    }

    static void saveImageToFile(SmartPicture view, String name, String format) {
        File file = new File(name);// + "." + format);
        view.saveToFile(file, format);
    }

    private String extension(String name) {
        Filename filename = new Filename(name, '/', '.');
        return filename.extension();
    }

    private void saveImConvertedImage(SmartPicture pic) {
                String filename = pic.getFileName();
//        String name = new Project.Filename(filename).filename();
//        System.out.println("SAVING MODIFIED IMAGE TO " + extension(filename) + ": " + filename);
        
        //saveImageToFile(picture, coloredImagesDir.getAbsolutePath() + "/" + filename, "png");
        saveImageToFile(pic, imConvertedImagesDir.getAbsolutePath() + "/" + filename, extension(filename));
    }

    public void applyFileIMConversion() {
        for (int ind = 0; ind < ColositerController.picArray.size(); ind = ind + 2) {
            SmartPicture pic = ColositerController.picArray.get(ind);
            String curName = pic.getFileName();
            File inputFile = new File(pic.getFileAbsolutePath());
            File outputFile = new File(createProject(projectName).getImConvertedImagesDir(), pic.getFileName());
            File2FileIMConverter.applyIMEffect(ColositerController.imEffect, inputFile, outputFile);
            SmartPicture picim = SmartPicture.createFromFile(outputFile);
            picim.applyEffect(new ColorAdjust(0.0, 0.0, 0.0, 0.0));
            ColositerController.picArray.add(ind + 1, picim);
            Notifier.INSTANCE.notifyInfo("Converted", "Image " + curName + " is converted by ImageMagic \n and saved to " + outputFile.getParent());
        }
    }

    public void convertAllImagesManually(String commandLine) {
for (int ind = 0; ind < ColositerController.picArray.size(); ind = ind + 2) {
            SmartPicture pic = ColositerController.picArray.get(ind);
            String curName = pic.getFileName();
            File inputFile = new File(pic.getFileAbsolutePath());
            File outputFile = new File(createProject(projectName).getImConvertedImagesDir(), pic.getFileName());
            File2FileIMConverter.applyManualIM(commandLine, inputFile, outputFile);
            SmartPicture picim = SmartPicture.createFromFile(outputFile);
            picim.applyEffect(new ColorAdjust(0.0, 0.0, 0.0, 0.0));
            ColositerController.picArray.add(ind + 1, picim);
            Notifier.INSTANCE.notifyInfo("Converted", "Image " + curName + " is converted by ImageMagic \n and saved to " + outputFile.getParent());
        }        
    }



}
