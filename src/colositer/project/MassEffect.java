/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package colositer.project;

import javafx.scene.image.ImageView;
import org.im4java.core.IMOperation;

/**
 * All picture transformations has many the same parts.
 * View - Showing SmartPictures
 *      - menu for choosing effect, loading images, and others buttons
 * Model - Project name, Project dir
 *       - input filelist
 *       - enum of enabled transformations
 * Controller - loops of transformations
 *            - loading and saving data to xml
 *            - file oerations
 * 
 * 
 * Parts that are different are separate into three kinds:
 * * View - controls of UI that user uses to setup parameters for transformation
 * * Model - parameters' structure - is bind with controls  of View
 *         - dirName for output image files
 * * Controller
 *            - converter of params from JAVA-mode to IM-mode
 *            - set of operations (setters) for ImageView - to make effect by JAVA
 *            - set of operations for IMOperation - to make effect by IM
 * 
 * it is useful to use lambda from http://jdk8.java.net/lambda/
 * @author pavel
 */
public abstract class MassEffect {

    public enum EffectType {COLOR_ADJUST, MANUAL_IM};
    
    public abstract void drawUIparams();
    
    /**
     * Defines structure of effect parameters
     */
    public abstract <T extends EffectType> EffectParam getEffectParamList();
    
    static class EffectParam <T extends EffectType>{
        
    }
    
    public abstract String getOutputDirName();
    
    /**
     * binds parameters for java-effect to parameters for im-effect
     */
    public abstract void initParamConverterBinding();
    
    public abstract void makeJavaEffect(EffectParam param, ImageView image);
    
    public abstract void makeIMEffect(EffectParam param, IMOperation op);
    
}
