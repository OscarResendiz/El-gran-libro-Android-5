package com.example.oscar.hilos;

public class hiloprueba  extends Thread
{
    private int n, res;
    public  hiloprueba(int n)
    {
        this.n=n;
    }
    @Override
    public void run()
    {
        //res=factorial(n);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                salida.append(res+"\n");
            }
        });
    }
}