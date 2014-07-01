package colositer.project;

import static colositer.ColositerController.colorEffect;
import static colositer.project.Project.round;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IMeffect {

    private final DoubleProperty contrast   = new SimpleDoubleProperty();
    private final DoubleProperty hue        = new SimpleDoubleProperty();
    private final DoubleProperty brightness = new SimpleDoubleProperty();
    private final DoubleProperty saturation = new SimpleDoubleProperty();
    private final StringProperty imCommand  = new SimpleStringProperty();
    
    private static final String hintManualIM = "Use string lineIM between thiese strings: \"convert <IMAGE1> \" and \" <IMAGE2> \"";
    
   public IMeffect(){
       initBindings();
         
    }
   
   public static String getHintManualIM(){
       return hintManualIM;
   } 
   
   public String getIMCommand(){
       return imCommand.get();
   }
   
   public StringProperty imCommandProperty(){
       return imCommand;
   }

    public double getContrast() {
        return Math.round(contrast.get());
    }

    public double getHue() {
        return Math.round(hue.get());
    }

    public double getBrightness() {
        return Math.round(brightness.get());
    }

    public double getSaturation() {
        return Math.round(saturation.get());
    }

    private void initBindings() {
        
        bindCommandLine();
        contrast.bind(new DoubleBinding() {
            {
                bind(colorEffect.contrastProperty());
            }
            @Override
            protected double computeValue() {
                return Math.round(colorEffect.getContrast()*100);
            }
        });
        
        hue.bind(new DoubleBinding() {
            {
                bind(colorEffect.hueProperty());
            }
            @Override
            protected double computeValue() {
                return Math.round(colorEffect.getHue()*100+100);
            }
        });
        
        brightness.bind(new DoubleBinding() {
            {
                bind(colorEffect.brightnessProperty());
            }
            @Override
            protected double computeValue() {
                return Math.round(colorEffect.getBrightness()*100+100);
            }
        });
        
        saturation.bind(new DoubleBinding() {
            {
                bind(colorEffect.saturationProperty());
            }
            @Override
            protected double computeValue() {
                return Math.round(colorEffect.getSaturation()*100+100);
            }
        });
    }
    
    private final StringBinding clBinding = new StringBinding() {
            {
                bind(contrast);
                bind(hue);
                bind(saturation);
                bind(brightness);
            }
            @Override
            protected String computeValue() {
//                return "convert <original_image> -modulate "+getBrightness()+","+getSaturation()+","+getHue()+" -brightness-contrast 0x"+getContrast()+" <destImage>";
                return "-set option:modulate:colorspace hsb -modulate "+getBrightness()+","+getSaturation()+","+getHue()+" -brightness-contrast 0x"+getContrast();
            }
        };
    
    public void bindCommandLine(){
        imCommand.bind(clBinding);
    }
    
    public void unbindCommandLine(){
        imCommand.unbind();
    }
}