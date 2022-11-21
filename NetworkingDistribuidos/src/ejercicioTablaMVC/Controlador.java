package ejercicioTablaMVC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import ejercicioTablaMVC.Modelos.modTabla;


public class Controlador implements ActionListener
{
	VistaCliente VC;
	VistaServidor VS;
	Modelos M;
	Server DS;
	Cliente C;
	
	//Esto es un metodo de modelos que se utiliza si se usa el servidor
	//sirve para estar actualizando la tabla en la vista en un hilo
	modTabla MT;
	
	static ArrayList<Computadora> computadoras = new ArrayList<Computadora>();
	static String nombre;
	static String Direccion;
	
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
			
			VC.setVisible(true);
			VC.txtNombre.setText(nombre);
			VC.txtIpServer.setText(Direccion);
			escuchadores(false);
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
		}
		else
		{
			VC.btnConectarse.addActionListener(this);
			VC.btnDesconectarse.addActionListener(this);
			VC.btnSalir.addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == VS.btnIniciarServidor) 
		{
			//Inicia el servidor en un hilo
			DS = new Server(Integer.parseInt(VS.txtSocket.getText()), true);
			DS.start();
			
			//Se inicia el hilo que va a estar actualizando la tabla
			MT = new modTabla(VS.model, true);
			MT.start();
			
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
			
			
			//CREAR CLIENTE
			C = new Cliente(MiCompu, Direccion, Integer.parseInt(VC.txtSocket.getText()), "Servidor");
			
			//iniciar hilo cliente
			C.start();
			
			//Se añade el escuchador del cliente servidor
			EscuchaClientes E = new EscuchaClientes(C, VC, DS, MT, VS);
			E.start();
			
			//instancia en una linea... interesante...
			//(new modTabla(VS.model, true)).start();
		}
		else if( e.getSource() == VS.btnTerminarServidor)
		{
			//deja de esperar conexiones y el hilo muere
			DS.setContinuar(false);
			
			//deja de actualizar la tabla de la vista y el hilo muere
			MT.setContinuar(false);
			
			//Se cambian los txts de la vista
			VS.txtEstado.setText("Apagado");
			VS.txtSocket.setEditable(true);
			
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
			
			//iniciar hilo cliente
			C.start();
			
			
			EscuchaClientes E = new EscuchaClientes(C, VC, DS, MT, VS);
			E.start();
			
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
		Cliente C;
		VistaCliente VC;
		VistaServidor VS;
		Server DS;
		modTabla MT;
		boolean band = false;
		public EscuchaClientes(Cliente C, VistaCliente VC, Server DS, modTabla MT, VistaServidor VS ) 
		{
			this.C = C;
			this.VC = VC;
			this.DS = DS;
			this.MT = MT;
			this.VS = VS;
		}
		public void run() 
		{
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			while(true) 
			{
				try {
					Thread.sleep(2000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(C.MiCompu.getPuesto().equals("Servidor")) 
				{
					
					if((C.MiCompu.getUsoCpu()*100) > 90) 
					{
						JOptionPane.showMessageDialog(VS, "APAGANDO EL SERVIDOR EN 10 SEGUNDOS");
						
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//deja de esperar conexiones y el hilo muere
						DS.setContinuar(false);
						
						//deja de actualizar la tabla de la vista y el hilo muere
						MT.setContinuar(false);
						
						//Se cambian los txts de la vista
						VS.txtEstado.setText("Apagado");
						VS.txtSocket.setEditable(true);
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
	
	
}
