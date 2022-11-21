package ejercicioTablaMVC;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import oshi.SystemInfo;

public class Cliente extends Thread
{
	Computadora MiCompu;
	String Ip; //IP donde esta el servidor
	int Puerto;
	String Puesto;
	
	public Cliente(Computadora MiCompu, String Ip, int Puerto, String puesto)
	{
		this.MiCompu = MiCompu;
		this.Ip = Ip;
		this.Puerto = Puerto;
		this.Puesto = puesto;
	}
	
	public void run()
	{
		int contador = 0;
		SystemInfo sys = new SystemInfo();
		Socket s = null;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		boolean primeraConexion = true;
		
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
				
				if(Puesto.equals("Cliente")) 
				{
					//Mensaje que confirma conexion
					if(primeraConexion) 
					{
						JOptionPane.showMessageDialog(null, "Cliente conectado");
						primeraConexion = false;
					}
				}
				
				
				//obtiene los datos del sistema (memoria, porcentaje de uso y latencia)
				long disponible = sys.getHardware().getMemory().getAvailable(); 
				long total = sys.getHardware().getMemory().getTotal(); 
				MiCompu.setUsoMemoria(100 - (disponible*100/total));
				
				//esto retarda todo el ciclo .8 segundos
				MiCompu.setUsoCpu(sys.getHardware().getProcessor().getSystemCpuLoad(800));
				
				MiCompu.setPuesto(Puesto);
				
			
				
				
				
				oos.writeObject(MiCompu);
				
				Controlador.computadoras = (ArrayList<Computadora>) ois.readObject();
				Controlador.ActualServer = (String) ois.readObject();
				
				if(ois != null) ois.close();
				if(oos != null) oos.close();
				if(s != null) s.close();
				
				
			}
		}
		catch(ConnectException a)
		{
			//esta exception significa que no se establecio correctamente la conexion, asi que 
			//se asigna que el estado es desconectado para que el controlador se de cuenta de esto
			if(contador < 3) 
			{
				System.out.println("No se pudo establecer conexion con el servidor");
				
				Ip = Controlador.ActualServer;
				System.out.println("ACTUAL SERVER IP -> " + Ip);
				contador++;
			}else 
			{
				MiCompu.setEstado("Desconectado");
				
				if(MiCompu.getPuesto().equals("Cliente"))
				JOptionPane.showMessageDialog(null, "No se pudo establecer conexion con el servidor");
			}		
		}
		catch(EOFException terminar)
		{
			System.out.println("Se presiono voluntariamente el boton de terminar el servidor");
			JOptionPane.showMessageDialog(null, "Se presiono voluntariamente el boton de terminar el servidor");
			MiCompu.setEstado("Desconectado");
		}
		catch(SocketException sock)
		{
			System.out.println("Se cerro la ventana del servidor mientras estaba activo");
			JOptionPane.showMessageDialog(null, "Se cerro la ventana del servidor mientras estaba activo\"");
			MiCompu.setEstado("Desconectado");
		}
		catch (Exception ex) 
		{
			JOptionPane.showMessageDialog(null, "No se conecto");
			ex.printStackTrace();
		}
		
	}
	
	public void SetPuesto(String puesto) 
	{
		Puesto = puesto;
	}
	
	
	
}
