package prueba;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

import ejercicioTablaMVC.Computadora;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class bandwith {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//long variable = testVelocidadDescarga(null)

	}
	
	static public long testVelocidadDescarga(Computadora PC)
	{
		long anchoBanda = 0;
		SpeedTestSocket speedTestSocket = new SpeedTestSocket();
		speedTestSocket.addSpeedTestListener(new ISpeedTestListener()
		
		
				{
				@Override
			    public void onCompletion(SpeedTestReport report) {
			        // called when download/upload is complete
			        System.out.println("[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
			    }

			    @Override
			    public void onError(SpeedTestError speedTestError, String errorMessage) {
			    	JOptionPane.showMessageDialog(null, "Hubo un error en la prueba de la velocidad de descarga");
			    }

			    @Override
			    public void onProgress(float percent, SpeedTestReport report) {
			    	BigDecimal speed = report.getTransferRateBit();
			    	BigDecimal speedMbps = speed.divide(new BigDecimal(1000000));
			    	
			    	
			    	long latencia = Long.parseLong(speedMbps.toString()); 
			    	final long anchoBanda = latencia;
			    	
			        // called to notify download/upload progress
			        System.out.println("[PROGRESS] progress : " + percent + "%");
			        System.out.println("[PROGRESS] rate in Mbps   : " + speedMbps);
			        
			        
			    }
				});
		
		
		//Inicia descarga, tambien se puede con archivos de 100 MB
		//speedTestSocket.startDownload("https://speed.hetzner.de/100MB.bin",1500);
		speedTestSocket.startDownload("https://speed.hetzner.de/1GB.bin",1500);
		return anchoBanda;
		
		
		
	}

}



