package ejercicioTablaMVC;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;

public class PrototipoVistaServidor extends JFrame {

	private JPanel contentPane;
	JTable table;
	private JLabel lblSocket;
	private JTextField txtSocket;
	private JButton btnIniciarServidor;
	private JButton btnTerminarServidor;
	private JLabel lblEstado;
	private JTextField txtEstado;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PrototipoVistaServidor frame = new PrototipoVistaServidor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PrototipoVistaServidor() {
		setTitle("Dashboard - servidor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1314, 441);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitulo = new JLabel("Conexiones");
		lblTitulo.setFont(new Font("Arial", Font.PLAIN, 40));
		lblTitulo.setBounds(267, 53, 217, 46);
		contentPane.add(lblTitulo);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(64, 139, 1206, 182);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setFont(new Font("Arial", Font.PLAIN, 12));
		table.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 15));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Cliente", "IP", "Estado", "Procesador", "Velocidad", "N\u00B0 Nucleos", "Capacidad DD", "Version S.O.", "Uso CPU", "Uso Memoria", "Latencia"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(6).setPreferredWidth(90);
		table.setRowHeight(30);
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		scrollPane.setViewportView(table);
		
		lblSocket = new JLabel("Socket:");
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
		
		lblEstado = new JLabel("Estado:");
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
