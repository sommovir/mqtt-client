/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class SettingsManager {

    public static final String config_file_name = "settings.properties";
    public static final String SERVER_IP = "server-ip";

    private static SettingsManager _instance = null;
    private String serverIP = "";

    public static SettingsManager getInstance() {
        if (_instance == null) {
            _instance = new SettingsManager();
            return _instance;
        } else {
            return _instance;
        }
    }

    private SettingsManager() {
        super();
        loadSettings();
    }
    
    

    public String getServerIP() {
        return serverIP;
    }

    
    

    public void loadSettings() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(config_file_name);
            // load a properties file
            prop.load(input);

            serverIP = prop.getProperty(SERVER_IP);

            System.out.println("Loading: [Server IP]  = "+serverIP);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
