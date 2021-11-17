package org.example.puntuacionesprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class PuntuacionesProvider extends ContentProvider {
    public static final String AUTORIDAD="org.example.puntuacionesprovider";
    public static final Uri CONTENT_URI=Uri.parse("content://"+AUTORIDAD+"/puntuaciones");
    public static final  int TODOS_LOS_ELEMENTOS=1;
    public static final  int UN_ELEMENTO=2;
    private static UriMatcher URI_MACHER=null;
    static
    {
        URI_MACHER=new UriMatcher(UriMatcher.NO_MATCH);
        URI_MACHER.addURI(AUTORIDAD,"puntuaciones",TODOS_LOS_ELEMENTOS);
        URI_MACHER.addURI(AUTORIDAD,"puntuaciones/#",UN_ELEMENTO);
    }
    public static final String TABLA="puntuaciones";
    private SQLiteDatabase baseDatos;
    @Override
    public boolean onCreate() {
        PuntuacionesSQLiteHelper dbHelper=new PuntuacionesSQLiteHelper(getContext());
        baseDatos=dbHelper.getWritableDatabase();
        return baseDatos!=null && baseDatos.isOpen();
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MACHER.match(uri))
        {
            case TODOS_LOS_ELEMENTOS:
                return "vnd.android.sursor.dir/vnd.org.example.puntuacion";
            case UN_ELEMENTO:
                return "vnd.android.sursor.item/vnd.org.example.puntuacion";
                default:
                    throw new IllegalArgumentException("Uri incorrecta "+uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projeccion, @Nullable String seleccion, @Nullable String[] argSeleccion, @Nullable String orden) {
        SQLiteQueryBuilder queryBuilder=new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLA);
        switch (URI_MACHER.match(uri))
        {
            case TODOS_LOS_ELEMENTOS:
                break;
            case UN_ELEMENTO:
                String id=uri.getPathSegments().get(1);
                queryBuilder.appendWhere("_id="+id);
                break;
            default:
                throw new IllegalArgumentException("Uri incorrecta "+uri);
        }
        return queryBuilder.query(baseDatos,projeccion,seleccion,argSeleccion,null,null,orden);
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues valores) {
        long idFila=baseDatos.insert(TABLA,null,valores);
        if(idFila>0)
        {
            return ContentUris.withAppendedId(CONTENT_URI,idFila);
        }
        else
        {
            throw new SQLException("Error al insertar el registro en "+uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String seleccion, @Nullable String[] argSeleccion) {
        switch (URI_MACHER.match(uri))
        {
            case TODOS_LOS_ELEMENTOS:
                break;
            case UN_ELEMENTO:
                String id=uri.getPathSegments().get(1);
                if(TextUtils.isEmpty(seleccion))
                {
                    seleccion="_id="+id;
                }
                else
                {
                    seleccion="_id="+id+" and ("+seleccion+")";
                }
                break;
            default:
                throw new IllegalArgumentException("Uri incorrecta "+uri);
        }
        return  baseDatos.delete(TABLA,seleccion,argSeleccion);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues valores, @Nullable String seleccion, @Nullable String[] argumentosSeleccion) {
        switch (URI_MACHER.match(uri))
        {
            case TODOS_LOS_ELEMENTOS:
                break;
            case UN_ELEMENTO:
                String id=uri.getPathSegments().get(1);
                if(TextUtils.isEmpty(seleccion))
                {
                    seleccion="_id="+id;
                }
                else
                {
                    seleccion="_id="+id+" and ("+seleccion+")";
                }
                break;
            default:
                throw new IllegalArgumentException("Uri incorrecta "+uri);
        }

        return  baseDatos.update(TABLA,valores,seleccion,argumentosSeleccion);
    }
}
