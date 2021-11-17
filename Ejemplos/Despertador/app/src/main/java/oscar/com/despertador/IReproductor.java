package oscar.com.despertador;

import android.content.Context;

import java.util.List;

public interface IReproductor {
    void setPlayIcon(int recurso);
    int getId();
    List<Cancion> getCacnciones();
    int getTipo();
    Context getContexto();
}
