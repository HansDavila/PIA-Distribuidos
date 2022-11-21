package ejercicioTablaMVC;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//EL TRABAJO DEL SERVIDOR ES RECIBIR CONEXIONES
public class Server extends Thread
{
	int Socket;
	boolean Continuar;
	
	Socket s = null;		
	ServerSocket ss = null;
	
	public Server(int Socket, boolean Continuar)
	{
		this.Socket = Socket;
		this.Continuar = Continuar;
	}
	
	public void run()
	{
		
		
		try {
			ss = new ServerSocket(Socket);
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		while(Continuar == true)
		{//OJO, EL SERVIDOR VA A TENER QUE RECIBIR UN ULTIMO OBJETO DESPUES DE SER NOTIFICADO QUE
		 //TERMINE PORQUE EL CODIGO VA A ESTAR ATASCADO EN EL ss.accept();
			try
			{
				System.out.println("Server: Se espera mensaje");
				//el ServerSocket me da el Socket AQUI SE DETIENE EL PROGRAMA
				s = ss.accept();
				
				//EL TRABAJO DE ESTE HILO ES UNICAMENTE AGREGAR LA COMPUTADORA (SI NO SE HA AGREGADO ANTES)
				//AL ARRAYLIST DEL CONTROLADOR
				Tarea T = new Tarea(s,System.currentTimeMillis());
				T.start();
				System.out.println("Server: Se obtuvo mensaje");
				
				
			}
			catch(Exception ex)
			{
				//ex.printStackTrace();
			}
		}
	}	
	
	public boolean getContinuar() {
		return Continuar;
	}

	public void setContinuar(boolean continuar) {
		if(continuar == false) 
		{
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Continuar = continuar;
	}

//TERMINA Server.java Y EMPIEZA LA INNER CLASS Tarea--------------------------------------------------------

	static class Tarea extends Thread
	{
	private Socket s = null;
	private long horaLlegada;
	
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	
	public Tarea(Socket socket, long horaLlegada)
	{
		this.s = socket;
		this.horaLlegada = horaLlegada;
	}
	
	//metodo para saber si ya se encuentra una computadora en el arraylist del controlador
	//regresa -1 si no se encuentra, de lo contrario, regresa el index donde se encuentra
	private int yaSeEncuentra(String ip)
	{
		int index = -1;
		int i=0;
		
		for(Computadora A:Controlador.computadoras)
		{
			if(A.getIP().equals(ip))
			{
				index = i;
				break;
			}
			i++;
		}
		
		if (index>-1)
		{
			return index;
		}
		else
		{
			return -1;
		}
		
	}
	
	public void run()
	{
		try
		{
			
			//enmascaro la entrada y salida de bytes
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
			
			//Se lee el objeto recibido
			Computadora C = (Computadora) ois.readObject();
			
			//se obtiene la ip
			C.setIP(""+s.getInetAddress());
			
			//Se calcula y asigna el tiempo que tardo en llegar el paquete
			C.setLatencia(horaLlegada - C.getLatencia());
			
			//La computadora recibida ya se encontraba en el arraylist?
			int seEncontro = yaSeEncuentra(C.getIP());
			
			if(seEncontro==-1)//No se encontro en la lista
			{
				Controlador.computadoras.add(C);
			}
			else //Se encontro en la lista
			{
				Controlador.computadoras.set(seEncontro, C);
			}
			
			//Le envio el array que tenemos
			oos.writeObject(Controlador.computadoras);
			oos.writeObject(Controlador.ActualServer);
			
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try {
				if(oos != null) oos.close();
				if(ois != null) ois.close();
				if(s != null) s.close();
				
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	} 
}
}
//taskkill -pid 5431 /f

