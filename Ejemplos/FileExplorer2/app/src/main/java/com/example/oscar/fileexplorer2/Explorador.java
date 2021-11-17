package com.example.oscar.fileexplorer2;

import android.app.ListActivity;
import java.io.File;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class Explorador extends ListActivity {
    private Adapter adapter;
    private File FullDir;
    private String SDCARD_PATH = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras(); // Llamamos los extras
        SDCARD_PATH = extras.getString("SD"); // Llamamos la ruta de
        // almacenamiento que mandamos
        // en el main
        File Directorio = new File(SDCARD_PATH); // Creamos un objeto File con
        // la ruta de almacenamiento
        if (Directorio.exists()) { // Primero vemos si existe,por si algo
            // saliera mal
            try { // Iniciamos un try,por si algo saliera mal de nuevo
                startList(Directorio); // Mandamos la ruta inicial a la funcion
                // que creara la vista
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(
                        getApplicationContext(),
                        "No puedes acceder en este momento a " + " "
                                + SDCARD_PATH, Toast.LENGTH_LONG).show();
                // Sino podemos acceder lo mostramos
            }
        }
    }

    private void startList(File Directorio) {
        String titulo = Directorio.getName(); // Obtenemos el nombre de la ruta actual
        this.setTitle(titulo); // Colocamos el titulo a la vista
        FullDir = Directorio; // Asignamos esta variable extra que usaremos despues

        File Allfiles[] = Directorio.listFiles(); // Listamos todos los archivos del directorio actual
        if (Directorio.listFiles() == null) { // Si es null,esta vacio y lo mostramos
            Toast.makeText(getApplicationContext(), "Carpeta vacia",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // Creamos un Array para los directorios
        ArrayList<File> Dirs = new ArrayList<File>();
        // Creamos un Array para los archivos
        ArrayList<File> Files = new ArrayList<File>();
        //Creamos un array para guardar Archivos y Directorios
        //Esto servira para ordenarlos
        ArrayList<File> AllFilesGood = new ArrayList<File>();
        // Creamos un Array de Archivos que guardara la informacion de archivos y directorios
        ArrayList<Archivo> Archivos = new ArrayList<Archivo>();
        for (int i = 0; i < Allfiles.length; i++) { // Recorremos la ruta actual
            // Si es .android_secure no la usamos ya que no nos deja Android
            if (Allfiles[i].getName().equals(".android_secure"))
                continue;
            // Si es un directorio lo ponemos en Dirs
            if (Allfiles[i].isDirectory()) {
                Dirs.add(Allfiles[i]);
                // Si es un Archivo lo ponemos en Files
            } else if (Allfiles[i].isFile()) {
                Files.add(Allfiles[i]);
            } else {
                // Si no es directorio ni archivo
                // Continuamos(podemos omitir esta linea de codigo)
                continue;
            }
        }
        // Podemos poner directorios y luego archivos,o viceversa
        AllFilesGood.addAll(Dirs); // Ponemos directorios primero
        AllFilesGood.addAll(Files); // Pondemos Archivos despues
        // Vemos si es la raiz(la ruta del almacenamiento)
        if (!Directorio.getAbsolutePath().equals(SDCARD_PATH))
            Archivos.add(new Archivo("...", "", true)); // Si no es la raiz agregamos una forma de regresar
        for (int i = 0; i < AllFilesGood.size(); i++) { // Recorremos Todos los archivos
            File tmp = AllFilesGood.get(i); // Creamos un objeto que se destruira muy rapidamente
            boolean flag = false; // Usamos esta variable para ver si es un directorio
            if (tmp.isDirectory()) // Vemos is es un directorio
                flag = true;
            Archivos.add(new Archivo(tmp.getName(), tmp.getPath(), flag)); // Agregamos un nuevo objeto
        }
        // Asignamos al adaptador el context,el layout y un array de archivos
        adapter = new Adapter(Explorador.this, R.layout.explorer, Archivos);
        this.setListAdapter(adapter); // Asignamos el adaptador
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // Manejamos los clic
        super.onListItemClick(l, v, position, id);
        Archivo archivo = adapter.getArchivo(position); // Creamos un archivo temporal
        if (archivo.isDirectory()) { // Vemos si es directorio
            if (archivo.getName().equals("...")) { // Si es el regreso regresamos al path anterior
                // Con estas lineas de codigo obtenemos el path anterior
                String[] segments = FullDir.getAbsolutePath().split("/");
                String lastPath = "";
                for (int i = 1; i < segments.length; i++) {
                    if (i != segments.length - 1) {
                        lastPath = lastPath + "/" + segments[i];
                    }
                }
                startList(new File(lastPath)); // Recargamos la vista
                return;
            }
            File Directorio = new File(archivo.getPath()); // Obtenemos el path
            startList(Directorio); // Recargamos la vista
        }
        // Aqui manejariamos los archivos
    }
}
