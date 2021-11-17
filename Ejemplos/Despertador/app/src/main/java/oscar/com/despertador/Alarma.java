package oscar.com.despertador;

import android.text.format.Time;

public class Alarma
{
    private String nombre; //nombre de la alarma
    private int hora; //hora en que se va a ejecutar
    private int minuto; //minuto en que se va a ejecutar
    private boolean activa; //true la alarma esta activa y debe de ejecutarse en el momento de disparo
    private boolean encendida; //true significa que esta sonando y false que esta en espera
    // dias de la semana en que se puede activar la alarma
    private  boolean lunes;
    private boolean martes;
    private  boolean miercoles;
    private  boolean jueves;

    public int getIdAlarma() {
        return idAlarma;
    }

    public void setIdAlarma(int idAlarma) {
        this.idAlarma = idAlarma;
    }

    private boolean viernes;
    private  boolean sabado;
    private int idAlarma;
    public  Alarma(int idAlarma,String nombre,int hora,int minuto,boolean lunes,boolean martes,boolean miercoles,boolean jueves,boolean viernes,boolean sabado,boolean domingo,boolean activa) {
        this.idAlarma=idAlarma;
        this.nombre = nombre;
        this.hora = hora;
        this.minuto = minuto;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
        this.sabado = sabado;
        this.activa = activa;
    }
    public Alarma()
    {

    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean isEncendida() {
        return encendida;
    }

    public void setEncendida(boolean encendida) {
        this.encendida = encendida;
    }

    public boolean isLunes() {
        return lunes;
    }

    public void setLunes(boolean lunes) {
        this.lunes = lunes;
    }

    public boolean isMartes() {
        return martes;
    }

    public void setMartes(boolean martes) {
        this.martes = martes;
    }

    public boolean isMiercoles() {
        return miercoles;
    }

    public void setMiercoles(boolean miercoles) {
        this.miercoles = miercoles;
    }

    public boolean isJueves() {
        return jueves;
    }

    public void setJueves(boolean jueves) {
        this.jueves = jueves;
    }

    public boolean isViernes() {
        return viernes;
    }

    public void setViernes(boolean viernes) {
        this.viernes = viernes;
    }

    public boolean isSabado() {
        return sabado;
    }

    public void setSabado(boolean sabado) {
        this.sabado = sabado;
    }

    public boolean isDomingo() {
        return domingo;
    }

    public void setDomingo(boolean domingo) {
        this.domingo = domingo;
    }

    private  boolean domingo;
    public String toString()
    {
        String sam=" AM";
        String dias="";
        int htmp=hora;
        if(hora>12) {
            htmp = hora - 12;
            sam = " PM";
        }
        if(lunes)
            dias+=" Lunes";
        if(martes)
            dias+=" Martes";
        if(miercoles)
            dias+=" Miercoles";
        if(jueves)
            dias+=" Jueves";
        if(viernes)
            dias+=" Viernes";
        if(sabado)
            dias+=" Sabado";
        if(domingo)
            dias+=" Domingo";
        return "Hora="+htmp+":"+minuto+sam+dias;
    }
    private int idLista; //lista de reproduccion asignada

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }
    public boolean hayQueActivar(Time time)
    {
        if(activa==false)
            return  false;
        boolean ok=true;
        switch (time.weekDay)
        {
            case Time.MONDAY:
                if(lunes==false)
                    ok=false;
                break;
            case Time.TUESDAY:
                if(martes==false)
                    ok=false;
                break;
            case Time.WEDNESDAY:
                if(miercoles==false)
                    ok=false;
                break;
            case Time.THURSDAY:
                if(jueves==false)
                    ok=false;
                break;
            case Time.FRIDAY:
                if(viernes==false)
                    ok=false;
                break;
            case Time.SATURDAY:
                if(sabado==false)
                    ok=false;
                break;
            case Time.SUNDAY:
                if(domingo==false)
                    ok=false;
                break;
        }
        int horaTmp=time.hour;
        boolean tmpAm=true;
        if(hora!=horaTmp)
            ok=false;
        if(minuto!=time.minute)
            ok=false;
        return ok;
    }
}
