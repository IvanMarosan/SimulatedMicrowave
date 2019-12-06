import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private int qos = 2;
    private MqttClient mqttPeer;

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
    private void threadStart() {

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                Platform.runLater(() -> currentWattageLabel.setText(Integer.toString(SettingsSingleton.getInstance().getWattage())));
                int i = SettingsSingleton.getInstance().getTime();
                while (i > 0) {
                    i--;
                    final int finalI = i;
                    Platform.runLater(() -> remainingTimeLabel.setText(Integer.toString(finalI)));
                    SettingsSingleton.getInstance().setTime(i);
                    Thread.sleep(1000);
                }

                MqttMessage message = new MqttMessage("done".getBytes());
                message.setQos(qos);
                mqttPeer.publish("smarthouse/microwave/error", message);
                System.out.println("Message published: done");
                return null;
            }

        };
        th = new Thread(task);
        th.setDaemon(true);
    }

    @FXML
    void turnOn() {

        if (!SettingsSingleton.getInstance().isDoorOpen() && !th.isAlive()) {
            threadStart();
            th.start();
        } else {
            try {
                MqttMessage message = new MqttMessage("door open".getBytes());
                message.setQos(qos);
                mqttPeer.publish("smarthouse/microwave/error", message);
                System.out.println("Message published: door open");
            } catch (Exception me) {
                me.printStackTrace();
            }
        }
    }

    @FXML
    void turnOff() {
        th.interrupt();
        threadStart();
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
        if (SettingsSingleton.getInstance().getWattage() > 0 && !th.isAlive()) {
            SettingsSingleton.getInstance().setWattage(SettingsSingleton.getInstance().getWattage() - 50);
            currentWattageLabel.setText(Integer.toString(SettingsSingleton.getInstance().getWattage()));
        }
    }

    @FXML
    void increaseTime() {

        if (SettingsSingleton.getInstance().getTime() < 3600 && !th.isAlive()) {
            SettingsSingleton.getInstance().setTime(SettingsSingleton.getInstance().getTime() + 2);
            remainingTimeLabel.setText(Integer.toString(SettingsSingleton.getInstance().getTime()));
        } else if (!th.isAlive()) {
            errorLabel.setText("Time limit reached!");
        }
    }

    @FXML
    void increaseWattage() {

        if (SettingsSingleton.getInstance().getWattage() < 1000 && !th.isAlive()) {
            SettingsSingleton.getInstance().setWattage(SettingsSingleton.getInstance().getWattage() + 50);
            currentWattageLabel.setText(Integer.toString(SettingsSingleton.getInstance().getWattage()));
        } else if (!th.isAlive()) {
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


        if (SettingsSingleton.getInstance().isDoorOpen()) {
            SettingsSingleton.getInstance().setDoorOpen(false);
            doorStatusLabel.setText("Closed");
        } else {
            SettingsSingleton.getInstance().setDoorOpen(true);
            doorStatusLabel.setText("Open");
            if (th.isAlive()) {
                th.interrupt();
                threadStart();
            }
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //----------------------
        //Setting up presets
        ArrayList<Preset> presets = new ArrayList<>();
        presets.add(new Preset("defrost", 500, 120));
        presets.add(new Preset("fish", 800, 180));
        presets.add(new Preset("beef", 600, 360));
        presets.add(new Preset("poultry", 750, 240));
        //----------------------

        threadStart();
        DraggableFood draggableFood = new DraggableFood(new Image(String.valueOf(getClass().getResource("images/chicken.png"))));
        anchorPane.getChildren().addAll(draggableFood);
        draggableFood.relocate(-50, 450);

        try {
            MqttConnectOptions connOpts2 = new MqttConnectOptions();
            connOpts2.setCleanSession(true);

            mqttPeer = new MqttClient("tcp://localhost:1883", "Microwave", new MemoryPersistence());
            mqttPeer.connect(connOpts2);

            mqttPeer.subscribe("smarthouse/microwave/manual_start", (topics, msg) -> {
                byte[] payload = msg.getPayload();
                String messagetake = new String(payload);

                int wattage = Integer.valueOf(messagetake.substring(1, 5));
                int time = Integer.valueOf(messagetake.substring(6, 10));

                SettingsSingleton.getInstance().setWattage(wattage);
                SettingsSingleton.getInstance().setTime(time);

                turnOn();
            });

            mqttPeer.subscribe("smarthouse/microwave/preset_start", (topics, msg) -> {
                byte[] payload = msg.getPayload();
                String messagetake = new String(payload);

                for (Preset p : presets) {
                    if (p.getName().equals(messagetake)) {
                        SettingsSingleton.getInstance().setWattage(p.getWattage());
                        SettingsSingleton.getInstance().setTime(p.getTime());
                        break;
                    }
                }

                turnOn();
            });

            mqttPeer.subscribe("smarthouse/microwave/create_preset", (topics, msg) -> {
                byte[] payload = msg.getPayload();
                String messagetake = new String(payload);
                String[] values = messagetake.split("-");
                //1st one is name
                //2nd one is wattage
                //3rd one is time

                boolean duplicate = false;
                for (Preset p : presets) {
                    if (p.getName().equals(values[0])) {
                        duplicate = true;
                    }
                }

                if (!duplicate) {
                    presets.add(new Preset(values[0], Integer.valueOf(values[1]), Integer.valueOf(values[2])));
                    for (Preset p : presets) {
                        System.out.println(p.getName());
                    }
                } else {
                    MqttMessage message = new MqttMessage("preset already exists".getBytes());
                    message.setQos(qos);
                    mqttPeer.publish("smarthouse/microwave/error", message);
                    System.out.println("Message published: preset already exists");
                }
            });
        } catch (Exception me) {
            me.printStackTrace();
        }


    }
}