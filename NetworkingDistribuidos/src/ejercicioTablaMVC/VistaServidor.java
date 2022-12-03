package ejercicioTablaMVC;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class VistaServidor extends JFrame {

	private JPanel contentPane;
	JTable table;
	JTextField txtSocket;
	JButton btnIniciarServidor;
	JButton btnTerminarServidor;
	JTextField txtEstado;
	DefaultTableModel model;
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
		setTitle("Dashboard - servidor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1400, 441);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitulo = new JLabel("Conexiones");
		lblTitulo.setFont(new Font("Arial", Font.PLAIN, 40));
		lblTitulo.setBounds(267, 53, 217, 46);
		contentPane.add(lblTitulo);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(64, 139, 1280, 182);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setFont(new Font("Arial", Font.PLAIN, 12));
		table.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 15));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Cliente", "Puesto", "IP", "Estado", "Procesador", "Velocidad", "Nucleos", "Capacidad DD", "Version S.O.", "Uso CPU", "Uso Memoria", "Velocidad", "Puntuación"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(73);
		table.getColumnModel().getColumn(1).setPreferredWidth(73);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(85);
		table.getColumnModel().getColumn(4).setPreferredWidth(251);
		table.getColumnModel().getColumn(5).setPreferredWidth(77);
		table.getColumnModel().getColumn(6).setPreferredWidth(74);
		table.getColumnModel().getColumn(7).setPreferredWidth(113);
		table.getColumnModel().getColumn(8).setPreferredWidth(180);
		table.getColumnModel().getColumn(9).setPreferredWidth(86);
		table.getColumnModel().getColumn(10).setPreferredWidth(106);
		table.getColumnModel().getColumn(11).setPreferredWidth(78);
		
		table.setRowHeight(30);
		model = (DefaultTableModel) table.getModel();
		scrollPane.setViewportView(table);
		
		JLabel lblSocket = new JLabel("Socket:");
		lblSocket.setHorizontalAlignment(SwingConstants.CENTER);
		lblSocket.setFont(new Font("Arial", Font.PLAIN, 20));
		lblSocket.setBounds(639, 88, 87, 24);
		contentPane.add(lblSocket);
		
		txtSocket = new JTextField();
		txtSocket.setText("5432");
		txtSocket.setFont(new Font("Arial", Font.PLAIN, 19));
		txtSocket.setColumns(10);
		txtSocket.setBounds(735, 88, 166, 24);
		contentPane.add(txtSocket);
		
		btnIniciarServidor = new JButton("Iniciar servidor");
		btnIniciarServidor.setFont(new Font("Arial", Font.PLAIN, 25));
		btnIniciarServidor.setBounds(292, 346, 192, 36);
		contentPane.add(btnIniciarServidor);
		
		btnTerminarServidor = new JButton("Terminar servidor");
		btnTerminarServidor.setFont(new Font("Arial", Font.PLAIN, 25));
		btnTerminarServidor.setBounds(698, 346, 236, 36);
		contentPane.add(btnTerminarServidor);
		
		JLabel lblEstado = new JLabel("Estado:");
		lblEstado.setHorizontalAlignment(SwingConstants.CENTER);
		lblEstado.setFont(new Font("Arial", Font.PLAIN, 20));
		lblEstado.setBounds(639, 53, 87, 24);
		contentPane.add(lblEstado);
		
		txtEstado = new JTextField();
		txtEstado.setHorizontalAlignment(SwingConstants.CENTER);
		txtEstado.setText("Apagado");
		txtEstado.setFont(new Font("Arial", Font.PLAIN, 19));
		txtEstado.setEditable(false);
		txtEstado.setColumns(10);
		txtEstado.setBounds(735, 54, 166, 24);
		contentPane.add(txtEstado);
	}

}
