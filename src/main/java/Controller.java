import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label remainingTimeLabel;

    @FXML
    private Label currentWattageLabel;

    @FXML
    private Label doorStatusLabel;

    @FXML
    private Label errorLabel;

    private Thread th;

    @FXML
    public void initialize() {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                int i = SettingsSingleton.getInstance().getTime();
                while (i >= 0) {
                    final int finalI = i;
                    Platform.runLater(() -> remainingTimeLabel.setText(Integer.toString(finalI)));
                    i--;
                    SettingsSingleton.getInstance().setTime(i);
                    Thread.sleep(1000);
                }
                return null;
            }
        };
        th = new Thread(task);
        th.setDaemon(true);
    }

    @FXML
    void turnOn() {
        if(!SettingsSingleton.getInstance().isDoorOpen()){
            th.start();
        }else{
            errorLabel.setText("Please close the door!");
        }
    }

    @FXML
    void turnOff() {
        th.interrupt();
        initialize();
    }

    @FXML
    void decreaseTime() {
        if (SettingsSingleton.getInstance().getTime() > 0 && !th.isAlive()) {
            SettingsSingleton.getInstance().setTime(SettingsSingleton.getInstance().getTime() - 10);
            remainingTimeLabel.setText(Integer.toString(SettingsSingleton.getInstance().getTime()));
        }
    }

    @FXML
    void decreaseWattage() {
        if(SettingsSingleton.getInstance().getWattage() > 0 && !th.isAlive()){
            SettingsSingleton.getInstance().setWattage(SettingsSingleton.getInstance().getWattage() - 50);
            currentWattageLabel.setText(Integer.toString(SettingsSingleton.getInstance().getWattage()));
        }
    }

    @FXML
    void increaseTime() {

        if (SettingsSingleton.getInstance().getTime() < 3600 && !th.isAlive()) {
            SettingsSingleton.getInstance().setTime(SettingsSingleton.getInstance().getTime() + 10);
            remainingTimeLabel.setText(Integer.toString(SettingsSingleton.getInstance().getTime()));
        }else if(!th.isAlive()){
            errorLabel.setText("Time limit reached!");
        }
    }

    @FXML
    void increaseWattage() {

        if(SettingsSingleton.getInstance().getWattage() < 1000 && !th.isAlive()){
            SettingsSingleton.getInstance().setWattage(SettingsSingleton.getInstance().getWattage() + 50);
            currentWattageLabel.setText(Integer.toString(SettingsSingleton.getInstance().getWattage()));
        }else if(!th.isAlive()){
            errorLabel.setText("Power limit reached!");
        }
    }

    @FXML
    void defrostButtonPressed() {
        if (!th.isAlive()) {
            SettingsSingleton.getInstance().setWattage(500);
            SettingsSingleton.getInstance().setTime(120);
            currentWattageLabel.setText(Integer.toString(SettingsSingleton.getInstance().getWattage()));
            remainingTimeLabel.setText(Integer.toString(SettingsSingleton.getInstance().getTime()));
        }
    }
    @FXML
    void fishButtonPressed() {
        if (!th.isAlive()) {
            SettingsSingleton.getInstance().setWattage(800);
            SettingsSingleton.getInstance().setTime(180);
            currentWattageLabel.setText(Integer.toString(SettingsSingleton.getInstance().getWattage()));
            remainingTimeLabel.setText(Integer.toString(SettingsSingleton.getInstance().getTime()));

        }
    }
    @FXML
    void beefButtonPressed() {
        if (!th.isAlive()) {
            SettingsSingleton.getInstance().setWattage(600);
            SettingsSingleton.getInstance().setTime(360);
            currentWattageLabel.setText(Integer.toString(SettingsSingleton.getInstance().getWattage()));
            remainingTimeLabel.setText(Integer.toString(SettingsSingleton.getInstance().getTime()));
        }

    }
    @FXML
    void poultryButtonPressed() {
        if (!th.isAlive()) {
            SettingsSingleton.getInstance().setWattage(750);
            SettingsSingleton.getInstance().setTime(240);
            currentWattageLabel.setText(Integer.toString(SettingsSingleton.getInstance().getWattage()));
            remainingTimeLabel.setText(Integer.toString(SettingsSingleton.getInstance().getTime()));

        }

    }

    @FXML
    void openCloseDoor() {


        if(SettingsSingleton.getInstance().isDoorOpen()){
            SettingsSingleton.getInstance().setDoorOpen(false);
            doorStatusLabel.setText("Closed");
        }else{
            SettingsSingleton.getInstance().setDoorOpen(true);
            doorStatusLabel.setText("Open");
            if(th.isAlive()){
                th.interrupt();
                initialize();
            }
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DraggableFood draggableFood = new DraggableFood(new Image(String.valueOf(getClass().getResource("images/chicken.png"))));
        anchorPane.getChildren().addAll(draggableFood);
        draggableFood.relocate(-50, 450);
    }
}