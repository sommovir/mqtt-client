/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt.client;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class EventManager {
    
    private static EventManager _instance = null;
    private List<EventListener> listeners = new ArrayList<>();
    
    public static EventManager getInstance() {
        if (_instance == null) {
            _instance = new EventManager();
            return _instance;
        } else {
            return _instance;
        }
    }
    
    private EventManager() {
        super();
    }
    
    public void addEventListener(EventListener listener){
        this.listeners.add(listener);
    }
    
    public void imalive(String message){
        for (EventListener listener : listeners) {
            listener.imalive(message);
        }
    }
    
}
