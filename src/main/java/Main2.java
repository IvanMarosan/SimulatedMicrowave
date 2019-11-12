import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttSubscribe;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Main2 {


    public static void main(String[] args) {

        String topic        = "MQTTExamples";
        String content      = "Message from MqttPublishSample";
        int qos             = 2;
        String broker       = "tcp://localhost:1883";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
         //   sampleClient.disconnect();
            System.out.println("Disconnected");
          //  System.exit(0);
            CountDownLatch receivedSignal = new CountDownLatch(10);
            MqttClient subscriber = new MqttClient(broker, clientId, persistence);
          //  subscriber.subscribe("home/garden/fountain");
            subscriber.connect(connOpts);
            subscriber.subscribe("home/garden/fountain", (topics, msg) -> {
                byte[] payload = msg.getPayload();
             String messagetake = new String(payload);
                receivedSignal.countDown();
                System.out.println(messagetake);
            });




        } catch(MqttException me) {
            System.out.println("reason ");
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }

   /* public static void main(String[] args) throws MqttException {
        Main myapp = new Main();
        myapp.connectToServer();
    }
public void connectToServer() throws MqttException {
    String publisherId = UUID.randomUUID().toString();
    IMqttClient publisher = new MqttClient("localhost:1883",publisherId);

    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(10);
    publisher.connect(options);
}
public void connectWithHive(){

}*/
   public void messageArrived(String topic, MqttMessage message) throws MqttException {
       // Called when a message arrives from the server that matches any
       // subscription made by the client
       String time = new Timestamp(System.currentTimeMillis()).toString();
       System.out.println("Time:\t" +time +
               "  Topic:\t" + topic +
               "  Message:\t" + new String(message.getPayload()) +
               "  QoS:\t" + message.getQos());
   }
}
