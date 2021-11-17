package oscar.com.despertador;

import java.util.EventListener;

public interface IAlarmaEventListener extends EventListener {
    void OnAlarma(AlarmaEventObject arg);
}
