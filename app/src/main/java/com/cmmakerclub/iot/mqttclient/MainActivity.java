package com.cmmakerclub.iot.mqttclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;


public class MainActivity extends Activity implements MqttCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String user = "free:test";
        String pass = "test";

        try {
            MqttConnectOptions conOpt = new MqttConnectOptions();
            boolean clean = true;
            conOpt.setCleanSession(clean);
            conOpt.setPassword(pass.toCharArray());
            conOpt.setUserName(user);

//                    url = protocol + broker + ":" + port;
            String tmpDir = System.getProperty("java.io.tmpdir");
            MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);
            MqttClient client = new MqttClient("tcp://rabbit.cmmc.ninja:1883", "hello", dataStore);
            client.connect(conOpt);
//            client.connect();
            Log.d("CLIENT-NAT", "Connected to " + " with client ID " + client.getClientId());
            client.setCallback(this);
            client.subscribe("#");
            MqttMessage message =  new MqttMessage();
            message.setPayload("hello".getBytes());

            client.publish("KIKI", message);

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void connectionLost(Throwable throwable) {
        
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        Log.d("messageArrvied", String.format("%s : %s", s, new String(mqttMessage.getPayload())));

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
