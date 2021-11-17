import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorEcho {
	public static void main(String arg[])
	{
		try
		{
			System.out.println("Servidor en marcha...");
			ServerSocket sk=new ServerSocket(50000);
			while(true)
			{
	            System.out.println("esperando cliente");
				Socket cliente=sk.accept();
	            System.out.println("Cliente conectado");
				InputStreamReader isr=new InputStreamReader(cliente.getInputStream());
				BufferedReader entrada=new BufferedReader(isr); 
	            PrintWriter salida=new PrintWriter(new OutputStreamWriter(cliente.getOutputStream()),true);
	            String datos=entrada.readLine();
	            salida.println(datos);
	            cliente.close();
	            System.out.println(datos);
			}
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}

}
