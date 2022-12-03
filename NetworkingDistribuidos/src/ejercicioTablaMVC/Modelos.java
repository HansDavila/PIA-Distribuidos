package ejercicioTablaMVC;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class Modelos 
{
	static Double valor = 0.0;
	static ArrayList<BusyThread> arrayHilos = new ArrayList<BusyThread>();
	
	
	
	public static void setValor(Double valor) {
		Modelos.valor = valor;
	}
	
	boolean desicion()
	{
		Controlador.nombre = JOptionPane.showInputDialog("Ingrese Nombre");
		Controlador.Direccion = JOptionPane.showInputDialog("Ingrese la direccion ip del Servidor");
		
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
			puntos += 400;
			
		}else if(PC.getProcesador().contains("Ryzen 7 3700U")) 
		{
			System.out.println("Este cliente contiene un Ryzen 7");
			puntos += 300;
			
		}else if(PC.getProcesador().contains("i5-4310U")) 
		{
			System.out.println("Este cliente contiene un intel Core i5-4310U");
			puntos += 300;
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
		puntos += (capacidad * 0.1);
		
		
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
	
	public void nuevoServidor() 
	{
		int max = 0;
		int indiceNuevo = 0;
		int indiceViejo = 0;
		for(Computadora cliente: Controlador.computadoras) 
		{
			if(cliente.getPuesto().equals("Servidor")) 
			{
				indiceViejo = Controlador.computadoras.indexOf(cliente);
			}
			if(cliente.getPuntos() > max) {
				max = cliente.getPuntos();
				indiceNuevo = Controlador.computadoras.indexOf(cliente);
			}
		}
		
		Computadora nuevoServer = Controlador.computadoras.get(indiceNuevo);
		Computadora AntiguoServer = Controlador.computadoras.get(indiceViejo);
		nuevoServer.setPuesto("Servidor");
		AntiguoServer.setPuesto("Cliente");
		
		Controlador.computadoras.set(indiceNuevo, nuevoServer);
		Controlador.computadoras.set(indiceViejo, AntiguoServer);
		Controlador.ActualServer = nuevoServer.getIP();
		Controlador.ActualServer = Controlador.ActualServer.replace("/", "");
		
	}
	
	public String getActualServer() 
	{
		return Controlador.ActualServer;
	}
	
	 public void IniciarCarga(double carga)
	    {
	    	//1. Se obtiene el numero de nucleos del sistema
	        int numCore = (new SystemInfo()).getHardware().getProcessor().getPhysicalProcessorCount();
	        
	        int numThreadsPerCore = 2;
	        double load = carga;
	        	for (int thread = 0; thread < numCore * numThreadsPerCore; thread++) {
	            arrayHilos.add(new BusyThread(load));
	            arrayHilos.get(thread).start();
	            
	        }
	    }
	
	public static class BusyThread extends Thread {
        private double load;
        private boolean continuar = true;

        public BusyThread(double load) 
        {
        	this.load = load; 
        }

        @Override
        public void run() {
            try {
            	// Loop hasta que continuar ya no sea true
                while (continuar) {
                    // Every 100ms, sleep for the percentage of unladen time
                    if (System.currentTimeMillis() % 100 == 0) {
                        Thread.sleep((long) Math.floor((1 - load) * 100));
                    }
                    
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        public void setLoad(double load) {
        	this.load = load;
        }
        public void setContinuar(boolean continuar) {
			this.continuar = continuar;
		}
    }
	
	
	//---------------------------hilo para actualizar la tabla---------------------------
	static class modTabla extends Thread
	{
		//el de continuar es para checar si queremos seguir actualizando la tabla
		boolean continuar;
		DefaultTableModel model;
		static Double speed = 0.0; 
		//este es para sumar todas las peticiones de load de los clientes
		private double acumulador = 0.0;
		
		public modTabla(DefaultTableModel model, boolean continuar)
		{
			this.model = model;
			this.continuar = continuar;
		}

		public void run()
		{	
			while(continuar == true)
			{

				acumulador = calcularCarga();

				for(BusyThread hilo: arrayHilos) {
					hilo.setLoad(acumulador);
				}
				
				//System.out.println("EL VALOR ACUMULADO ES "+acumulador);
				
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
				
				
				//Calculo de puntos dinamicos
				for(Computadora PC:Controlador.computadoras) 
				{
					PC.setPuntos(CalcularPuntosDinamicos(PC));

				}
				
				
				//Se ordenan elementos de la tabla
				Collections.sort(Controlador.computadoras);
				
				for(Computadora A:Controlador.computadoras)
				{
					model.addRow(
								new Object[]{
				                     A.getCliente(),
				                     A.getPuesto(),
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
			
			//si se llega aqui es que se le ordeno al programa terminar el servidor
			//asi que tambien se terminan todos los hilos abiertos
			for(BusyThread hilo: arrayHilos) {
				hilo.setContinuar(false);
			}
			//se limpia el arraylist
			arrayHilos.clear();
			
		}		
		public boolean getContinuar() {
			return continuar;
		}

		public void setContinuar(boolean continuar) {
			this.continuar = continuar;
		}
		
		int CalcularPuntosDinamicos(Computadora PC) 
		{
			int puntos = PC.getPuntosEstaticos();
			if(PC.getEstado().equals("Desconectado")) 
			{
				puntos = 0;
			}else 
			{
				//System.out.println("ESTATICOS -> " + puntos);
				double limCpu = 1000;
				double limRam = 1000;
				double cargaCpu = 0;
				double cargaRam = 0;
				
				
				//Calificar craga de Cpu Dinamica
				cargaCpu = (PC.getUsoCpu()* 100);
				puntos += (limCpu - (cargaCpu * 10));
				
				//Calificar craga de Cpu Dinamica
				cargaRam = PC.getUsoMemoria();
				puntos += (limRam - (cargaRam * 10));
			}
			
			
			return puntos;
		}
		
		//Suma todos los loads del arraylist para saber cuanto decirle al programa que se estrese
		private double calcularCarga() {
			double acumulador = 0.0;
			//Calculo de puntos dinamicos
			for(Computadora PC:Controlador.computadoras) 
			{
				//Obtenemos las cargas totales
				acumulador += PC.getLoad();					
			
			}

			if(acumulador > 1) {
				acumulador = 1;
			}
			
			return acumulador;
		}
		
	}
}
