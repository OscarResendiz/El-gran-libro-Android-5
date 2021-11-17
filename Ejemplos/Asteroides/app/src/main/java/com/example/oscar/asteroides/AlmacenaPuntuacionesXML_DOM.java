package com.example.oscar.asteroides;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class AlmacenaPuntuacionesXML_DOM implements AlmacenPuntuaciones  {
    private static String FICHERO="puntuaciones.xml";
    private Context contexto;
    private Document documento;
    private boolean cargadoDocumento;
    public AlmacenaPuntuacionesXML_DOM(Context context)
    {
        contexto=context;
        cargadoDocumento=false;
    }
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        try
        {
            if(!cargadoDocumento)
            {
                leerXML(contexto.openFileInput(FICHERO));
            }
        }
        catch (FileNotFoundException e) {
            crearXML();
        }
        catch (Exception e)
        {
            Log.e("asteroides",e.getMessage(),e);
        }
        nuevo(puntos,nombre,fecha);
        try
        {
            escribirXML(contexto.openFileOutput(FICHERO,Context.MODE_PRIVATE));
        }
        catch (Exception e)
        {
            Log.e("asteroides",e.getMessage(),e);
        }

    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        try
        {
            if(!cargadoDocumento)
            {
                leerXML(contexto.openFileInput(FICHERO));
            }

        }
        catch (FileNotFoundException e) {
            crearXML();
        }
        catch (Exception e)
        {
            Log.e("asteroides",e.getMessage(),e);
        }
        return aVectorString();
    }
    public void crearXML()
    {
        try
        {
            DocumentBuilderFactory fabrica= DocumentBuilderFactory.newInstance();
            DocumentBuilder constructor=fabrica.newDocumentBuilder();
            documento=constructor.newDocument();
            org.w3c.dom.Element raiz=documento.createElement("lista_puntuaciones");
            documento.appendChild(raiz);
            cargadoDocumento=true;
        }
        catch (Exception e)
        {
            Log.e("asteroides",e.getMessage(),e);
        }
    }
    public void leerXML(InputStream entrada) throws Exception
    {
        DocumentBuilderFactory fabrica=DocumentBuilderFactory.newInstance();
        DocumentBuilder constructor=fabrica.newDocumentBuilder();
        documento=constructor.parse(entrada);
        cargadoDocumento=true;
    }
    public void nuevo(int puntos, String nombre, long fecha)
    {
        ///Puntuacion
        org.w3c.dom.Element puntuacion=documento.createElement("puntuacion");
        puntuacion.setAttribute("fecha", String.valueOf(fecha));
        ///Nombre
        org.w3c.dom.Element e_nombre=documento.createElement("nombre");
        Text texto=documento.createTextNode(nombre);
        e_nombre.appendChild(texto);
        puntuacion.appendChild(e_nombre);
        //Puntos
        org.w3c.dom.Element e_puntos=documento.createElement("puntos");
        texto=documento.createTextNode(String.valueOf(puntos));
        e_puntos.appendChild(texto);
        puntuacion.appendChild(e_puntos);
        //agregado al raiz
        org.w3c.dom.Element raiz=documento.getDocumentElement();
        raiz.appendChild(puntuacion);
    }
    public Vector<String> aVectorString()
    {
        Vector<String> result=new Vector<String>();
        String nombre="",puntos="";
        org.w3c.dom.Element raiz=documento.getDocumentElement();
        NodeList puntuaciones=raiz.getElementsByTagName("puntuacion");
        //recorre todas las puntuaciones
        for(int i=0;i<puntuaciones.getLength();i++)
        {
            Node puntuacion=puntuaciones.item(i);
            NodeList propiedades=puntuacion.getChildNodes();
            //recorre los nodos hijos de las puntuaciones que son nombre y puntos
            for(int j=0;j< propiedades.getLength();j++)
            {
                Node propiedad=propiedades.item(j);
                String etiqueta=propiedad.getNodeName();
                if(etiqueta.equals("nombre"))
                {
                    nombre=propiedad.getFirstChild().getNodeValue();
                }
                else if(etiqueta.equals("puntos"))
                {
                    puntos=propiedad.getFirstChild().getNodeValue();
                }
            }
            result.add(nombre+" "+puntos);
        }
        return result;
    }
    public void escribirXML(OutputStream salida) throws Exception
    {
        TransformerFactory fabrica=TransformerFactory.newInstance();
        Transformer transformador=fabrica.newTransformer();
        transformador.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
        transformador.setOutputProperty(OutputKeys.INDENT,"yes");
        DOMSource fuente=new DOMSource(documento);
        StreamResult resultado=new StreamResult(salida);
        transformador.transform(fuente,resultado);
    }

}
