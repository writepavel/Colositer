/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package colositer;

import eu.hansolo.enzo.notification.Notification;
import eu.hansolo.enzo.notification.Notifier;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javax.imageio.ImageIO;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.stage.WindowEvent;
import static colositer.project.Project.round;
import colositer.picture.SmartPicture;
import colositer.project.IMeffect;
import colositer.project.Project;
import colositer.res.Resource;
import colositer.utils.File2FileIMConverter;
import static colositer.project.Project.createProject;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.CheckBox;
/**
 *
 * @author pavel
 */
public class ColositerController implements Initializable {

    @FXML
    private Label label;
    @FXML
    Button saveButton;
    @FXML
    Slider contrastSlider;
    @FXML
    Slider hueSlider;
    @FXML
    Slider brightnessSlider;
    @FXML
    Slider saturationSlider;
    @FXML
    TextField contrastTextField;
    @FXML
    TextField hueTextField;
    @FXML
    TextField brightnessTextField;
    @FXML
    TextField saturationTextField;
    @FXML
    ScrollPane scrollPane;
    @FXML
    VBox vBox;
    @FXML
    TextField projectName;
    @FXML
    Button loadEffect;
    @FXML
    TextField lineIM;
    @FXML
    Button applyIM;
    @FXML
    Button clearList;
    @FXML
    CheckBox chkManualIM;
    
        private static final Notification[] NOTIFICATIONS = {
        new Notification("Info", "New information", Notification.INFO_ICON),
        new Notification("Warning", "Attention, somethings wrong", Notification.WARNING_ICON),
        new Notification("Success", "Great it works", Notification.SUCCESS_ICON),
        new Notification("Error", "ZOMG", Notification.ERROR_ICON)
    };
    private Notifier notifier;
    private Stage stage;

        private void startNotifiers(){
        notifier = Notifier.INSTANCE;

//        StackPane pane = new StackPane();
//        pane.setPadding(new Insets(10, 10, 10, 10));
//        pane.getChildren().addAll(button);
//
//        Scene scene = new Scene(pane);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {
                notifier.stop();
            }
        });
//        stage.setScene(scene);
//        stage.show();
    }
    
    //======================================

    
    final FileChooser fileChooser = new FileChooser();

    void setStage(Stage primaryStage) {
        stage = primaryStage;
        startNotifiers();
    }
    
    @FXML
    private void onClearList(){
        picArray.clear();
    }
    
    @FXML
    private void onApplyIM(){
//        project.runManualIM();
        project = Project.createProject("m_"+projectName.getText());
        project.saveAllOriginalImages();
        project.convertAllImagesManually(imEffect.getIMCommand());
    }

    @FXML
    private void loadImagesAction(ActionEvent event) {
        List<File> list =
                fileChooser.showOpenMultipleDialog(stage);
        if (list != null) {
            for (File file : list) {
                addFileToViewArray(file);
            }
        }
    }
    Project project;

    @FXML
    private void saveImagesAction(ActionEvent event) {

        project = Project.createProject(projectName.getText());
        project.saveAllOriginalImages();
        project.saveAllModifiedImages();
        project.applyFileIMConversion();
        notifier.notifySuccess("Great!", "All images are conveted and copied into \n"+project.projectDir.getAbsolutePath());
    }
    
    @FXML
    private void loadEffectAction(ActionEvent event){
                File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Project.setColorEffectFromXML(file);
        }
    }
    
    @FXML
    private void onCheckManualIM(){
        if (chkManualIM.isSelected()){
            imEffect.unbindCommandLine();
        }
        else{
            imEffect.bindCommandLine();
        }
    }
    
      

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bindAll();
//        picArray.add(new SmartPicture(BOAT, "boat.jpg"));
    }

    private void bindAll() {

        StringConverter sc = new StringConverter<Double>() {
            @Override
            public String toString(Double t) {
                return String.valueOf(round(t));
            }
            @Override
            public Double fromString(String string) {
                return Double.parseDouble(string);
            }
        };
        
        Bindings.bindBidirectional(contrastTextField.textProperty(), contrastSlider.valueProperty(), sc);
        Bindings.bindBidirectional(hueTextField.textProperty(), hueSlider.valueProperty(), sc);
        Bindings.bindBidirectional(brightnessTextField.textProperty(), brightnessSlider.valueProperty(), sc);
        Bindings.bindBidirectional(saturationTextField.textProperty(), saturationSlider.valueProperty(), sc);

        Bindings.bindBidirectional(colorEffect.contrastProperty(), contrastSlider.valueProperty());
        Bindings.bindBidirectional(colorEffect.hueProperty(), hueSlider.valueProperty());
        Bindings.bindBidirectional(colorEffect.brightnessProperty(), brightnessSlider.valueProperty());
        Bindings.bindBidirectional(colorEffect.saturationProperty(), saturationSlider.valueProperty());
        
        Bindings.bindContent(vBox.getChildren(), picArray);
        Bindings.bindBidirectional(lineIM.textProperty(), imEffect.imCommandProperty());
        
//        Bindings.bindBidirectional(chkManualIM.selectedProperty(), Bindings.not(contrastTextField.editableProperty()));
        contrastTextField.editableProperty().bind(isNotManualIM());
        hueTextField.editableProperty().bind(isNotManualIM());
        brightnessTextField.editableProperty().bind(isNotManualIM());
        saturationTextField.editableProperty().bind(isNotManualIM());
        contrastSlider.disableProperty().bind(isManualIM());
        hueSlider.disableProperty().bind(isManualIM());
        brightnessSlider.disableProperty().bind(isManualIM());
        saturationSlider.disableProperty().bind(isManualIM());
        lineIM.editableProperty().bind(isManualIM());
        applyIM.visibleProperty().bind(isManualIM());
        loadEffect.visibleProperty().bind(isNotManualIM());
        saveButton.visibleProperty().bind(isNotManualIM());
        
    }
    
//    private static final Image BOAT = new Image(Resource.class.getResourceAsStream("boat.jpg"));
    public static final ObservableList<SmartPicture> picArray = FXCollections.observableArrayList();
    public static final ColorAdjust colorEffect = new ColorAdjust(0.0, 0.0, 0.0, 0.0);
    public static final colositer.project.IMeffect imEffect = new IMeffect();

    private ObservableBooleanValue isManualIM(){
        return chkManualIM.selectedProperty();
    }
    
    private ObservableBooleanValue isNotManualIM(){
        return Bindings.not(chkManualIM.selectedProperty());
    }
    
        private void addFileToViewArray(File file) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                picArray.add(new SmartPicture(image, file.getAbsolutePath()));
            } catch (IOException ex) {
                Logger.getLogger(ColositerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void addPictureView(String filename) {
            Image img = new Image(Resource.class.getResourceAsStream(filename));
            picArray.add(new SmartPicture(img, filename));
        }
    }
