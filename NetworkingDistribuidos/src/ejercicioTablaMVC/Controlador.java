package ejercicioTablaMVC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
	
	public Controlador(VistaCliente VC, VistaServidor VS, Modelos M)
	{
		this.VC = VC;
		this.VS = VS;
		this.M = M;
		
		if(M.desicion())
		{
			//se lanza servidor
			this.VS.lanzarGUI();
			escuchadores(true);
		}
		else
		{	//se lanza cliente
			this.VC.lanzarGUI();
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
			
		}
		else if( e.getSource() == VC.btnConectarse)
		{
			//Rellenar objeto computadora
			Computadora MiCompu = M.inicializarComputadora();
			MiCompu.setCliente(VC.txtNombre.getText());
			MiCompu.setEstado("Conectado");
			MiCompu.setPuntos(0);
			
			//cambia los txts de la vista
			VC.txtEstado.setText("Conectado");
			VC.txtSocket.setEditable(false);
			VC.txtIpServer.setEditable(false);
			
			//CREAR CLIENTE
			C = new Cliente(MiCompu, VC.txtIpServer.getText(), Integer.parseInt(VC.txtSocket.getText()));
			
			//iniciar hilo cliente
			C.start();
			
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
	
	
}
