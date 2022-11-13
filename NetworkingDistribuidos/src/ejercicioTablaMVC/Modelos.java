package ejercicioTablaMVC;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;

public class Modelos 
{
	
	boolean desicion()
	{
		String[] botones = {"Servidor", "Cliente"};
		int ventana = JOptionPane.showOptionDialog(null, 
						"Que desea utilizar?:", 
						"Desicion", 
						JOptionPane.DEFAULT_OPTION, 
						JOptionPane.QUESTION_MESSAGE, null, 
						botones, botones[0]);
		
		if(ventana == 0) 
		{
			//Se escogio servidor
			return true;
		} 
		else if(ventana == 1) 
		{
			//Se escogio cliente
			return false;
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Esto no debio de suceder");
			return false;
		}
		
	}
	
	Computadora inicializarComputadora()
	{
		Computadora MiCompu = new Computadora();
		
		SystemInfo sys = new SystemInfo();
		
		MiCompu.setProcesador(sys.getHardware().getProcessor().getProcessorIdentifier().getName());
		
		float velocidad = sys.getHardware().getProcessor().getProcessorIdentifier().getVendorFreq();
		MiCompu.setVelocidad(velocidad/1000000000+"Ghz");
		
		MiCompu.setNucleos(sys.getHardware().getProcessor().getPhysicalProcessors().size());
		
		ArrayList<HWDiskStore> Discos = (ArrayList<HWDiskStore>) sys.getHardware().getDiskStores();
		float tamanio = 0;
		for(HWDiskStore A:Discos)
		{
			tamanio += A.getSize()/1000000000;
		}
		
		MiCompu.setCapacidad(tamanio+" Giga bytes");
		
		MiCompu.setSO(sys.getOperatingSystem().getFamily() +" "+sys.getOperatingSystem().getVersionInfo());
		
		
		
		return MiCompu;
	}
	/*
	int CalcularPuntos(Computadora PC) 
	{
		
	}
	*/
	
	//---------------------------hilo para actualizar la tabla---------------------------
	static class modTabla extends Thread
	{
		//el de continuar es para checar si queremos seguir actualizando la tabla
		boolean continuar;
		DefaultTableModel model;
		
		public modTabla(DefaultTableModel model, boolean continuar)
		{
			this.model = model;
			this.continuar = continuar;
		}

		public void run()
		{	
			while(continuar == true)
			{
				
				try 
				{
					Thread.sleep(1000);
				} 
				catch (InterruptedException e) 
				{
				e.printStackTrace();
				}
				System.out.println("Se updatea tabla");
				//Se destruye la tabla y se vuelve a contruir
				model.setRowCount(0);
				for(Computadora A:Controlador.computadoras)
				{
					model.addRow(
								new Object[]{
				                     A.getCliente(),
				                     A.getIP(),
				                     A.getEstado(),
				                     A.getProcesador(),
				                     A.getVelocidad(),
				                     A.getNucleos(),
				                     A.getCapacidad(),
				                     A.getSO(),
				                     Math.round(A.getUsoCpu()*100) +" %",
				                     A.getUsoMemoria()+" %",
				                     A.getLatencia(),
				              });
					
				}
				
			}			
		}		
		public boolean getContinuar() {
			return continuar;
		}

		public void setContinuar(boolean continuar) {
			this.continuar = continuar;
		}
	}
}
