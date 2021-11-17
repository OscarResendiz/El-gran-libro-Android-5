package com.example.oscar.ejemplograficos;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

import java.util.jar.Attributes;

public class EjemploView extends View
{
//    private Drawable miImagen;
    private ShapeDrawable miImagen;
    public EjemploView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        miImagen =new ShapeDrawable(new OvalShape());
        miImagen.getPaint().setColor(0xff0000ff);

//        Resources res=context.getResources();
//            miImagen=res.getDrawable(R.drawable.mi_imagen);
//        miImagen= new ShapeDrawable(new OvalShape());
  //      miImagen.getPaint().setColor(0xFF0000FF);;
    //    miImagen.setBounds(10,10,310,60);
    }
    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anteriro, int alto_anterior)
    {
        miImagen.setBounds(0,0,ancho,alto);
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        //dibujay aqui todo
/*            Paint pincel= new Paint();
            pincel.setColor(Color.BLUE);
            pincel.setStrokeWidth(8);
            pincel.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(150,150,100,pincel);
//            pincel.setColor(Color.argb(127,255,0,0));
//            pincel.setColor(0x7fff0000);
            pincel.setColor(getResources().getColor(R.color.color_circulo));
            canvas.drawCircle(150,250,100,pincel);
  */
/*
            Path trazo=new Path();
            trazo.moveTo(50,100);
            trazo.cubicTo(60,70,150,90,200,110);
            trazo.lineTo(300,200);
            //trazo.addCircle(300,300,250, Path.Direction.CW);
            canvas.drawColor(Color.WHITE);
            Paint pincel=new Paint();
            pincel.setColor(Color.BLUE);
            pincel.setStrokeWidth(20); //grosor de la linea del circulo
            pincel.setStyle(Paint.Style.STROKE);
            canvas.drawPath(trazo,pincel);
            //para el texto
            pincel.setStrokeWidth(10);
            pincel.setStyle(Paint.Style.FILL);
            pincel.setTextSize(50);
            pincel.setTypeface(Typeface.SANS_SERIF);
            canvas.drawTextOnPath("Desarrollo de aplicaciones para moviles con Android",trazo,100,100,pincel);
            */
        miImagen.draw(canvas);

    }
}
