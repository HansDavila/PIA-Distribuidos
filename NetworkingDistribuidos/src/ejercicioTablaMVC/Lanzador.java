package ejercicioTablaMVC;

public class Lanzador 
{
	public static void main(String[] args) 
	{
		VistaCliente VC = new VistaCliente();
		VistaServidor VS = new VistaServidor();
		Modelos M = new Modelos();
		
		Controlador C = new Controlador(VC, VS, M);
	}
	
}
