package oscar.com.despertador;

import java.util.EventObject;

public class AlarmaEventObject extends EventObject {
    Alarma alarma;
    public AlarmaEventObject(Object sender,Alarma alarma)
    {
        super(sender);
        this.alarma=alarma;
    }

    public Alarma getAlarma() {
        return alarma;
    }
}
