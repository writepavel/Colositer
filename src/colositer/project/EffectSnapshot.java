package colositer.project;

import static colositer.ColositerController.colorEffect;
import static colositer.ColositerController.imEffect;
import static colositer.ColositerController.picArray;
import colositer.picture.SmartPicture;
import static colositer.project.Project.round;
import java.util.ArrayList;

class EffectSnapshot {

    private final double contrast;
    private final double hue;
    private final double brightness;
    private final double saturation;
    
    private final String lineIM;
    private final String hintLineIM;
    
//    private final ArrayList<String> originalFileList = new ArrayList<String>();
    
    
   EffectSnapshot(){
        contrast =   round(colorEffect.getContrast());
        hue =        round(colorEffect.getHue());
        brightness = round(colorEffect.getBrightness());
        saturation = round(colorEffect.getSaturation()); 
        
        hintLineIM = IMeffect.getHintManualIM();
        lineIM = imEffect.getIMCommand();
        
//        for (SmartPicture pic : picArray){
//            originalFileList.add(pic.getFileAbsolutePath());
//        }
    }

    /**
     * @return the contrast
     */
    double getContrast() {
        return contrast;
    }

    /**
     * @return the hue
     */
    double getHue() {
        return hue;
    }

    /**
     * @return the brightness
     */
    double getBrightness() {
        return brightness;
    }

    /**
     * @return the saturation
     */
    double getSaturation() {
        return saturation;
    }
}