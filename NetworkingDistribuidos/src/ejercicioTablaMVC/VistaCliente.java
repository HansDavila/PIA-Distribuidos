package ejercicioTablaMVC;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VistaCliente extends JFrame {

	private JPanel contentPane;
	JTextField txtEstado;
	JTextField txtIpServer;
	JTextField txtSocket;
	JTextField txtNombre;
	JButton btnDesconectarse;
	JButton btnConectarse;
	JButton btnSalir;
	JSlider slider;

	/**
	 * Launch the application.
	 */
	void lanzarGUI()//Lanzar GUI en un hilo
	{//delimitador de package
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					crearGUI();
					setResizable(false);
					setVisible(false);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private void crearGUI() {
		setTitle("Cliente");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 525);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCliente = new JLabel("Cliente");
		lblCliente.setHorizontalAlignment(SwingConstants.CENTER);
		lblCliente.setFont(new Font("Arial", Font.PLAIN, 40));
		lblCliente.setBounds(137, 39, 217, 46);
		contentPane.add(lblCliente);
		
		btnDesconectarse = new JButton("Desconectarse");
		btnDesconectarse.setFont(new Font("Arial", Font.PLAIN, 20));
		btnDesconectarse.setBounds(40, 384, 184, 33);
		contentPane.add(btnDesconectarse);
		
		btnConectarse = new JButton("Conectarse");
		btnConectarse.setFont(new Font("Arial", Font.PLAIN, 20));
		btnConectarse.setBounds(262, 384, 184, 33);
		contentPane.add(btnConectarse);
		
		btnSalir = new JButton("Salir");
		btnSalir.setFont(new Font("Arial", Font.PLAIN, 20));
		btnSalir.setBounds(194, 425, 114, 33);
		contentPane.add(btnSalir);
		
		JLabel lblEstado = new JLabel("Estado:");
		lblEstado.setFont(new Font("Arial", Font.PLAIN, 20));
		lblEstado.setBounds(107, 250, 76, 24);
		contentPane.add(lblEstado);
		
		txtEstado = new JTextField();
		txtEstado.setText("Desconectado");
		txtEstado.setEditable(false);
		txtEstado.setFont(new Font("Arial", Font.PLAIN, 19));
		txtEstado.setBounds(195, 250, 192, 24);
		contentPane.add(txtEstado);
		txtEstado.setColumns(10);
		
		JLabel lblIpServer = new JLabel("IP Server:");
		lblIpServer.setFont(new Font("Arial", Font.PLAIN, 20));
		lblIpServer.setBounds(99, 165, 87, 24);
		contentPane.add(lblIpServer);
		
		txtIpServer = new JTextField();
		txtIpServer.setFont(new Font("Arial", Font.PLAIN, 19));
		txtIpServer.setColumns(10);
		txtIpServer.setBounds(195, 165, 192, 24);
		contentPane.add(txtIpServer);
		
		JLabel lblSocket = new JLabel("Socket:");
		lblSocket.setHorizontalAlignment(SwingConstants.CENTER);
		lblSocket.setFont(new Font("Arial", Font.PLAIN, 20));
		lblSocket.setBounds(99, 207, 87, 24);
		contentPane.add(lblSocket);
		
		txtSocket = new JTextField();
		txtSocket.setText("5432");
		txtSocket.setFont(new Font("Arial", Font.PLAIN, 19));
		txtSocket.setColumns(10);
		txtSocket.setBounds(195, 207, 192, 24);
		contentPane.add(txtSocket);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
		lblNombre.setFont(new Font("Arial", Font.PLAIN, 20));
		lblNombre.setBounds(99, 124, 87, 24);
		contentPane.add(lblNombre);
		
		txtNombre = new JTextField();
		txtNombre.setFont(new Font("Arial", Font.PLAIN, 19));
		txtNombre.setColumns(10);
		txtNombre.setBounds(195, 124, 192, 24);
		contentPane.add(txtNombre);
		
		JLabel lblEstres = new JLabel("Estrés deseado en servidor");
		lblEstres.setHorizontalAlignment(SwingConstants.CENTER);
		lblEstres.setFont(new Font("Arial", Font.PLAIN, 20));
		lblEstres.setBounds(83, 290, 333, 24);
		contentPane.add(lblEstres);
		
		slider = new JSlider(0, 100, 10);
		slider.setBounds(150, 324, 200, 51);
		slider.setMajorTickSpacing(25);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		contentPane.add(slider);

	}
}
	

