package ejercicioTablaMVC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import ejercicioTablaMVC.Modelos.modTabla;
import oshi.SystemInfo;


public class Controlador implements ActionListener, ChangeListener
{
	static VistaCliente VC;
	static VistaServidor VS;
	static Modelos M;
	static Server DS;
	static Cliente C;
	static EscuchaClientes E;
	static UdpListener U;
	
	static boolean bandera = false;
	boolean activarEscuchador = true;
	
	//ESTA DE MOMENTO SERA FALSE
	static boolean cambioServer = false;
	
	//Esto es un metodo de modelos que se utiliza si se usa el servidor
	//sirve para estar actualizando la tabla en la vista en un hilo
	static modTabla MT;
	
	static ArrayList<Computadora> computadoras = new ArrayList<Computadora>();
	static String nombre;
	static String Direccion;
	static String ActualServer;
	
	public Controlador(VistaCliente VC, VistaServidor VS, Modelos M)
	{
		this.VC = VC;
		this.VS = VS;
		this.M = M;
		
		if(M.desicion())
		{
			//se lanza servidor
			this.VS.lanzarGUI();
			this.VC.lanzarGUI();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			VS.setVisible(true);
			ActualServer = Direccion;
			VC.txtNombre.setText(nombre);
			VC.txtIpServer.setText(Direccion);
			escuchadores(true);
		}
		else
		{	//se lanza cliente
			this.VC.lanzarGUI();
			this.VS.lanzarGUI();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cambioServer = true;
			VC.setVisible(true);
			VC.txtNombre.setText(nombre);
			VC.txtIpServer.setText(Direccion);
			
			escuchadores(true);
		}	
		
	}
	
	private void escuchadores(boolean desicion)
	{
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(desicion)
		{
			VS.btnIniciarServidor.addActionListener(this);
			VS.btnTerminarServidor.addActionListener(this);
			VC.btnConectarse.addActionListener(this);
			VC.btnDesconectarse.addActionListener(this);
			VC.btnSalir.addActionListener(this);
			VC.slider.addChangeListener(this);
			//VC.btnUDP.addActionListener(this);
		}
		else
		{
			VC.btnConectarse.addActionListener(this);
			VC.btnDesconectarse.addActionListener(this);
			VC.btnSalir.addActionListener(this);
			VS.btnIniciarServidor.addActionListener(this);
			VS.btnTerminarServidor.addActionListener(this);
			VC.slider.addChangeListener(this);
			//VC.btnUDP.addActionListener(this);

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == VS.btnIniciarServidor) 
		{
			bandera = true;
			//Inicia el servidor en un hilo
			DS = new Server(Integer.parseInt(VS.txtSocket.getText()), true);
			DS.start();
			
			//inicia los hilos que cargan el cpu
			M.IniciarCarga(0.05);
			
			//Se inicia el hilo que va a estar actualizando la tabla
			MT = new modTabla(VS.model, true);
			MT.start();
			
			
			
			U = new UdpListener(Integer.parseInt(VS.txtSocket.getText()), true, Direccion);
			U.start();
			
			//se limpia la tabla y se modifican los txts
			VS.model.setRowCount(0);
			VS.txtEstado.setText("Activo");
			VS.txtSocket.setEditable(false);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//SE CREA CLIENTE DEL SERVER
			//Rellenar objeto computadora
			Computadora MiCompu = M.inicializarComputadora();
			MiCompu.setCliente(nombre);
			
			MiCompu.setEstado("Conectado");
			
			//cambia los txts de la vista
			VC.txtEstado.setText("Conectado");
			VC.txtSocket.setEditable(false);
			VC.txtIpServer.setEditable(false);
			
			
			//CREAR CLIENTE de parte del servidor
			C = new Cliente(MiCompu, Direccion, Integer.parseInt(VC.txtSocket.getText()), "Servidor" );
			
			//iniciar hilo cliente
			C.start();
			
			if(activarEscuchador) {
				//Se añade el escuchador del cliente servidor
				E = new EscuchaClientes(C, VC, DS, MT, VS, M);
				E.start();
				activarEscuchador = false;
			}else {
				E.update(C, VC, DS, MT, VS, M);
			}
			
			
			//instancia en una linea... interesante...
			//(new modTabla(VS.model, true)).start();
			
			C.MiCompu.setLoad(0.01);
		}
		else if( e.getSource() == VS.btnTerminarServidor)
		{

			C.MiCompu.setEstado("Desconectado");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			
			
			//Se cambian los txts de la vista
			VS.txtEstado.setText("Apagado");
			VS.txtSocket.setEditable(true);
			
			M.nuevoServidor();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for(Computadora Cliente: Controlador.computadoras) 
			{
				int indice = Controlador.computadoras.indexOf(Cliente);
				Cliente.setEstado("Desconectado");
				Controlador.computadoras.set(indice, Cliente);
				System.out.println("Modificando tabla");
			}	
			
			//deja de esperar conexiones y el hilo muere
			DS.setContinuar(false);
			
			//deja de actualizar la tabla de la vista y el hilo muere
			MT.setContinuar(false);
			
			
		}
		else if( e.getSource() == VC.btnConectarse)
		{
			//Rellenar objeto computadora
			Computadora MiCompu = M.inicializarComputadora();
			MiCompu.setCliente(VC.txtNombre.getText());
			MiCompu.setEstado("Conectado");
			
			//cambia los txts de la vista
			VC.txtEstado.setText("Conectado");
			VC.txtSocket.setEditable(false);
			VC.txtIpServer.setEditable(false);
			
			
			//CREAR CLIENTE
			C = new Cliente(MiCompu, VC.txtIpServer.getText(), Integer.parseInt(VC.txtSocket.getText()), "Cliente");
			C.MiCompu.setLoad(0.01);
			
			//iniciar hilo cliente
			C.start();
			
			if(activarEscuchador) {
				EscuchaClientes E = new EscuchaClientes(C, VC, DS, MT, VS, M);
				E.start();
				activarEscuchador = false;
			}
			
			
		}
		else if( e.getSource() == VC.btnDesconectarse)
		{
			//aqui ya debe es estar creado el cliente y se le pone el estado Desconectado para que el 
			//while deje de estar mandando el objeto MiCompu y el hilo muere
			C.MiCompu.setEstado("Desconectado");
			
			//cambia los txts de la vista
			VC.txtEstado.setText("Desconectado");
			VC.txtSocket.setEditable(true);
			VC.txtIpServer.setEditable(true);
			
		}
		else if( e.getSource() == VC.btnSalir)
		{
			System.exit(0);
			
		}
		
	}
	
	static class EscuchaClientes extends Thread 
	{
		
		boolean band = false;
		boolean estado;
		public EscuchaClientes(Cliente C, VistaCliente VC, Server DS, modTabla MT, VistaServidor VS, Modelos M ) 
		{
			
			estado = true;
		}
		
		public void update(Cliente C, VistaCliente VC, Server DS, modTabla MT, VistaServidor VS, Modelos M ) {
			
		}
		
		
		public void setEstado(boolean estado) {
			this.estado = estado;
		}
		
		public void run() 
		{
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			while(true) 
			{
				System.out.println("ACTUAL SERVER -> " + ActualServer);
				System.out.println("ACTUAL SERVEG -> " + Direccion);
				if(Direccion.equals(ActualServer)) 
				{
					if(bandera == true) {
						System.out.println("Eres server");
						C.MiCompu.setPuesto("Servidor");
						
						VS.setVisible(true);
						VC.setVisible(false);	
						//JOptionPane.showMessageDialog(VS, C.MiCompu.getLoad());
						
						//JOptionPane.showMessageDialog(VC, MT.getAcumulador());
						
						
						
						if(cambioServer) {
							System.out.println("EN CAMBIO SERVER");
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							//Inicia el servidor en un hilo
							DS = new Server(Integer.parseInt(VS.txtSocket.getText()), true);
							DS.start();
							
							//Se inicia el hilo que va a estar actualizando la tabla
							MT = new modTabla(VS.model, true);
							MT.start();
							
							cambioServer = false;
							
							System.out.println("TERMINO DE CREAR DS Y MT");
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							VS.model.setRowCount(0);
							VS.txtEstado.setText("Activo");
							VS.txtSocket.setEditable(false);
							
						}
						
						C.setIp(ActualServer);
					}else {
						bandera = true;
						C.MiCompu.setPuesto("Servidor");
						C.SetPuesto("Servidor");
						//Inicia el servidor en un hilo
						//DS = new Server(Integer.parseInt(VS.txtSocket.getText()), true);
						//DS.start();
						
						//Se inicia el hilo que va a estar actualizando la tabla
						//MT = new modTabla(VS.model, true);
						//MT.start();
						
						//se limpia la tabla y se modifican los txts
						VS.model.setRowCount(0);
						VS.txtEstado.setText("Activo");
						VS.txtSocket.setEditable(false);
						
					}
					
				}else 
				{
					System.out.println("NO ERES server");
					C.MiCompu.setPuesto("Cliente");		
					C.SetPuesto("Cliente");
					//JOptionPane.showMessageDialog(VS, C.MiCompu.getLoad());
					
					VC.setVisible(true);
					VS.setVisible(false);
					
					
				}
	
				//ESPERA
				try {
					Thread.sleep(2000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(C.MiCompu.getPuesto().equals("Servidor")) 
				{
					System.out.println("CARGA ACTUAL CPU -> " + (C.MiCompu.getUsoCpu()*100));
					//cuando el uso es mayor al 90%
					if((C.MiCompu.getUsoCpu()*100) > 90) 
					{
						//aqui saca la media del uso del cpu durante 3 segundos y si la media es mayor a 90, entonces
						//hace lo del swicheo de servidor
						if((new SystemInfo()).getHardware().getProcessor().getSystemCpuLoad(5000) >0.9)
						{
							JOptionPane.showMessageDialog(VS, "ASIGNANDO SERVER");
							M.nuevoServidor();
							
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						
						
					
					}
				}else if(C.MiCompu.getPuesto().equals("Cliente")) 
				{
					if(C.MiCompu.getEstado() == "Desconectado") 
					{
						if(band == false) 
						{
							VC.txtEstado.setText("Desconectado");
							band = true;
						}
					}else if(C.MiCompu.getEstado() == "Conectado") 
					{
						band = false;
					}
				}
				
			}
		}
	}

	//metodo que se ejecuta al mover el slider de la vista cliente
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		double valorSlider = VC.slider.getValue();
		valorSlider = valorSlider/100;
		
		if(C!=null)
		{
			C.MiCompu.setLoad(valorSlider);
		}
		
	}
	
	
}
