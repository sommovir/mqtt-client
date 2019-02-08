/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt.client;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class MqttClient implements MqttCallback {

    private static MqttClient _instance = null;
    private MqttAsyncClient sampleClient = null;
    private String broker = "tcp://localhost:1883";
    public static final String IM_ALIVE_TOPIC = "imalive";
    private String clientId = "unknown";

    public static MqttClient getInstance() {
        if (_instance == null) {
            _instance = new MqttClient();
            return _instance;
        } else {
            return _instance;
        }
    }

    private MqttClient() {
        super();
    }

    /**
     * Identifica questo client
     *
     * @param clientId
     */
    public void connect(String clientId) {
        this.clientId = clientId;
        MemoryPersistence dataStore = new MemoryPersistence();

        try {

            clientId = MqttAsyncClient.generateClientId();
            System.out.println("CLIENT ID : " + clientId);

            broker = "tcp://" + SettingsManager.getInstance().getServerIP() + ":1883"; //generazione ip di connessione
            System.out.println("Connecting: " + broker);
            sampleClient = new MqttAsyncClient(this.broker, clientId, dataStore);

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);
            connOpts.setKeepAliveInterval(Integer.MAX_VALUE);
            IMqttToken mqttToken = sampleClient.connect(connOpts);
            mqttToken.waitForCompletion(10000);
            if (mqttToken.isComplete()) {
                if (mqttToken.getException() != null) {
                    // TODO: retry
                    System.out.println("retry.. not done");
                }
            }
            sampleClient.setCallback(_instance);
            sampleClient.subscribe("#", 2); //si sottoscrive solo al topic titolato come il suo ID, ognirerà quindi tutti gli altri messaggi circolanti su altri topic

            System.out.println("CONNECTION DONE");
        } catch (MqttException e) {
            e.printStackTrace();

            // TODO:  Log
            System.out.println("fatal error");
            JOptionPane.showMessageDialog(null, "Il server ha riscontrato un problema ed è stato disconnesso", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage mm) throws Exception {
        String message = new String(mm.getPayload());
        System.out.println("[Client][Message arrived]Topic: " + topic);
        System.out.println("[Client][Message arrived]Message: " + message);
        if (topic.equals(clientId)) {
            EventManager.getInstance().imalive(message);
        }

    }

    public synchronized void publish(String topic, String message) throws MqttException {
//        int t = 1;
        sampleClient.publish(topic, new MqttMessage(message.getBytes()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
    }

}
