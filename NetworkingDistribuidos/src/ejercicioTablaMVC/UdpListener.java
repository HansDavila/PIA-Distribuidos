package ejercicioTablaMVC;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpListener extends Thread{
	int Socket;
	boolean Continuar;
	String ip;
	byte[] buffer = new byte[1024];
	
	
	public UdpListener(int Socket, boolean Continuar, String Ip)
	{
		this.Socket = Socket;
		this.Continuar = Continuar;
		this.ip = Ip;
	}
	
	public boolean getContinuar() {
		return Continuar;
	}

	
	public void run() 
	{
		while(Continuar == true) 
		{
			try {
				DatagramSocket socketUDP = new DatagramSocket(Socket);
				DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
				
				System.out.println("Esperando mensaje UDP");
				socketUDP.receive(peticion);
				
				String mensaje = new String(peticion.getData());
				System.out.println(mensaje);
				
				int puertoCliente = peticion.getPort();
				InetAddress direccion = peticion.getAddress();
				
				mensaje = ip;
				System.out.println("MANDANDO MENSAJE------------------------------------------------------------------");
				
				DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length, direccion, Socket);
				socketUDP.send(respuesta);
				
				socketUDP.close();
				
				
				
			}catch(IOException ex) {
				
			}
		}
	}

}
