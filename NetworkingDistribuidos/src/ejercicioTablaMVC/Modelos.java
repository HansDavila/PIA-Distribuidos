package ejercicioTablaMVC;

import java.util.ArrayList;
import java.util.Collections;

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
		
		MiCompu.setPuntos(CalcularPuntosEstaticos(MiCompu));
		MiCompu.setPuntosEstaticos(CalcularPuntosEstaticos(MiCompu));
		
		
		
		return MiCompu;
	}
	
	int CalcularPuntosEstaticos(Computadora PC) 
	{
		int puntos = 0;
		double velocidad = 0;
		int numNucleos = 0;
		String elemento;
		String digits;
		
		//Calificar Procesador
		if(PC.getProcesador().contains("Ryzen 5 5500U")) 
		{
			System.out.println("Este cliente contiene un ryzen 5");
			puntos += 500;
			
		}else if(PC.getProcesador().contains("i5-8300H")) 
		{
			System.out.println("Este cliente contiene un Intel Core i5-8300H");
			puntos += 300;
			
		}else if(PC.getProcesador().contains("Ryzen 5 3700U")) 
		{
			System.out.println("Este cliente contiene un Ryzen 5");
			puntos += 250;
			
		}else if(PC.getProcesador().contains("i5-4310U")) 
		{
			System.out.println("Este cliente contiene un Ryzen 5");
			puntos += 100;
		}
		
		
		//Calificar Velocidad
		elemento = PC.getVelocidad();
		digits = elemento.replaceAll("[^0-9.]", "");
		velocidad = Double.parseDouble(digits);
		puntos += (velocidad * 100);
		
		//Calificar N° de Nucleos
		numNucleos = PC.getNucleos();
		puntos += (numNucleos * 50);
		
		//Calificar Capacidad de DD
		elemento = PC.getCapacidad();
		digits = elemento.replaceAll("[^0-9.]", "");
		Double capacidad = Double.parseDouble(digits);
		puntos += (capacidad * 0.2);
		
		
		//Calificar sistema operativo
		if(PC.getSO().contains("Windows 11")) 
		{
			System.out.println("Este cliente tiene Windows 11");
			puntos += 150;
			
		}else if(PC.getSO().contains("Windows 10")) 
		{
			System.out.println("Este cliente tiene Windows 10");
			puntos += 100;
			
		}
		
		return puntos;
	}
	
	
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
				
				Collections.sort(Controlador.computadoras);
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
				                     A.getPuntos()
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
