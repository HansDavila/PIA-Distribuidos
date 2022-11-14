package ejercicioTablaMVC;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import oshi.SystemInfo;

public class Cliente extends Thread
{
	Computadora MiCompu;
	String Ip; //IP donde esta el servidor
	int Puerto;
	
	public Cliente(Computadora MiCompu, String Ip, int Puerto)
	{
		this.MiCompu = MiCompu;
		this.Ip = Ip;
		this.Puerto = Puerto;
	}
	
	public void run()
	{
		SystemInfo sys = new SystemInfo();
		Socket s = null;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		
		try 
		{
			//envio un objeto
			while(MiCompu.getEstado().equals("Conectado"))
			{
				//la latencia es la primer cosa que se hace porque al hacer el socket, es cuando el server
				//recibe la senal de un paquete
				MiCompu.setLatencia(System.currentTimeMillis());
				
				s = new Socket(Ip, Puerto);
				oos = new ObjectOutputStream(s.getOutputStream());
				ois = new ObjectInputStream(s.getInputStream());
				
				//obtiene los datos del sistema (memoria, porcentaje de uso y latencia)
				long disponible = sys.getHardware().getMemory().getAvailable(); 
				long total = sys.getHardware().getMemory().getTotal(); 
				MiCompu.setUsoMemoria(100 - (disponible*100/total));
				
				//esto retarda todo el ciclo .8 segundos
				MiCompu.setUsoCpu(sys.getHardware().getProcessor().getSystemCpuLoad(800));
				
			
				
				
				
				oos.writeObject(MiCompu);
				
				if(ois != null) ois.close();
				if(oos != null) oos.close();
				if(s != null) s.close();
				
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		
	}
	
	
	
}
