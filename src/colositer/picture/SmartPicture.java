/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package colositer.picture;

import colositer.ColositerController;
import colositer.res.Resource;
import eu.hansolo.enzo.notification.Notifier;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * 1) Replaces initiation of ObservableHashMap by standard ObservableList 2)
 * Contains methods for saving pictures - original and colored.
 *
 * @author pavel
 */
public class SmartPicture extends Group {

    private NamedImageView picture;
    private SmartPicture instance;
    private Text msgGenerating;
    private Tooltip tooltip;
    private ImageView imgClose;

    /**
     * Allocates a new PictureView object with image loaded from the specified
     * URL. The new ImageView(url) has the same effect as new ImageView(new
     * Image(url)). Parameters: url - the string representing the URL from which
     * to load the image Throws: java.lang.NullPointerException - if URL is null
     * java.lang.IllegalArgumentException - if URL is invalid or unsupported
     * Since: JavaFX 2.1
     */
    public SmartPicture(String url, String filename) {
        super();
//        picture = new IMimageView(url, filename);
        picture = new NamedImageView(url, filename);
        init();
    }
    
    

    public SmartPicture(Image image, String filename) {
        super();
//        picture = new IMimageView(image, filename);
        picture = new NamedImageView(image, filename);
        init();
    }
    
    public static SmartPicture createFromFile(File file){
        
        try {
            String stringpath = file.getAbsolutePath();
        Image img;
            img = new Image(file.toURI().toURL().toString());
            return new SmartPicture (img, stringpath);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SmartPicture.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void applyEffect(ColorAdjust eff){
        this.picture.effectProperty().setValue(eff);
    }
    
    private SmartPicture(){}

    private void init() {
        instance = this;
        initPicCoords();
        tooltip = createTooltip();
        Tooltip.install(picture, tooltip);
        msgGenerating = createMsgGenerating();
        imgClose = createCloseButton();
        this.getChildren().add(picture);
        this.getChildren().add(msgGenerating);
        this.getChildren().add(imgClose);
        this.hoverProperty().addListener(hoverListener);
    }
    
    void replacePictureByIM(){
        System.out.println("replacing pic: "+ picture.getFileName());
        msgGenerating.setVisible(true);
        msgGenerating.toFront();
        Notifier.INSTANCE.notifyWarning("Please wait...", "Generating new "+picture.getFileName()+" by ImageMagic...");
//        IMimageView improvedPic = new IMimageView(picture.getImage(), getFileName());
        this.getChildren().remove(picture);
//        this.getChildren().add(improvedPic);
        msgGenerating.setVisible(false);
        Notifier.INSTANCE.notifyInfo("Image converted", "Image "+picture.getFileName()+" has been converted by ImageMagic");
    }
    
    public String getFileAbsolutePath() {
        return picture.getFileAbsolutePath();
    }
    
    public String getFileName(){
        return picture.getFileName();
    }
    
    final InvalidationListener hoverListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable ov) {
            if (instance.isHover()) {
                imgClose.setVisible(true);
            } else {
                tooltip.hide();
                imgClose.setVisible(false);
            }
        }
    };
    
    double x, y, width, height, layoutX, layoutY;
    
    private void initPicCoords(){
    x = picture.getX();
    y = picture.getY();
    width = picture.getImage().getWidth();
    height = picture.getImage().getHeight();
    layoutX = picture.getLayoutX();
    layoutY = picture.getLayoutY();
    }
    
    private Tooltip createTooltip() {
        Tooltip tip = new Tooltip(picture.getFileName());
        tip.setFont(Font.font("SansSerif", FontWeight.BOLD, FontPosture.REGULAR, 14));//Style("-fx-font-size: 16;");
        tip.setStyle("-fx-fill: FIREBRICK; -fx-font-weight: bold; -fx-effect: dropshadow( gaussian , rgba(255,255,255,0.5) , 0,0,0,1 );");
        return tip;
    }
    
    private Text createMsgGenerating(){
        Text txt = new Text("Generating picture...");
        txt.setFont(Font.font("SansSerif", FontWeight.BOLD, FontPosture.REGULAR, 14));
        txt.setFill(Paint.valueOf("FIREBRICK"));
        txt.setVisible(false);
        txt.toFront();
        return txt;
    }
    
    private ImageView createCloseButton() {
        Image pic = new Image(Resource.class.getResourceAsStream("close.png"));
        ImageView closePic = new ImageView(pic);
        closePic.setFitWidth(40);
        closePic.setFitHeight(40);
        closePic.setCursor(Cursor.HAND);
        closePic.setLayoutX(width - 43);
        closePic.setLayoutY(5);
        closePic.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                removeThisElement();
            }
        });

        closePic.setVisible(false);
        closePic.toFront();
        return closePic;
    }

    private void removeThisElement() {
        ColositerController.picArray.remove(this);
    }

    public void saveToFile(File file, String format) {
        picture.saveToFile(file, format);
    }
}
