package br.edu.ifcvideira.controllers.views;

import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.KeyStroke;

import java.awt.event.*;
import java.sql.SQLException;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import br.edu.ifcvideira.beans.Login;

public class MainInternalFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	public JDesktopPane desktop;
	/**
	 * Est?tica e p?blica para ter a visibilidade alterada referente ao estado de
	 * login
	 */
	public static JMenuBar menuBar;
	private JMenuItem miLogin;

	public MainInternalFrame() {
		super("InternalFrame");
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainInternalFrame.class.getResource("/br/edu/ifcvideira/imgs/ico32.png")));
		/*addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {

				/*lookandfeel muda cores: biblioteca looksdemo-2.3.1.jar
				SkyBlue()
		        BrownSugar()
		        DarkStar()  
		        DesertGreen()
		        Silver()
		        ExperienceRoyale()
				try {
					PlasticLookAndFeel.setPlasticTheme(new SkyBlue());
					try {
						UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
					} catch (InstantiationException ex) {
						/**
						 * VERIFICAR USO DESTAS LOGGERS
						 *//*
						Logger.getLogger(MainInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
					} catch (IllegalAccessException ex) {
						Logger.getLogger(MainInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
					} catch (UnsupportedLookAndFeelException ex) {
						Logger.getLogger(MainInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
					}

				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(desktop);

				desktop.setBackground(SystemColor.WHITE);

			}
		});*/
		setTitle("Toque M\u00E1gico - Massoterapia");
		/*
		 * Fazer internal frames pegarem decora??o do sistema try {
		 * UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch
		 * (ClassNotFoundException e) { e.printStackTrace(); } catch
		 * (InstantiationException e) { e.printStackTrace(); } catch
		 * (IllegalAccessException e) { e.printStackTrace(); } catch
		 * (UnsupportedLookAndFeelException e) { e.printStackTrace(); }
		 */

		// Make the big window be indented
		int inset = 0;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

		// Set up the GUI.
		desktop = new JDesktopPane();
		login(); // Cria a primeira janela
		setContentPane(desktop);

		JLabel imgFundo = new JLabel("");
		imgFundo.setBackground(new Color(255, 255, 255));
		imgFundo.setForeground(new Color(255, 255, 255));
		imgFundo.setHorizontalAlignment(SwingConstants.CENTER);
		imgFundo.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifcvideira/imgs/Frontal.png")));
		imgFundo.setBounds(0, 0, 1344, 709);
		desktop.add(imgFundo);
		setJMenuBar(createMenuBar());
		menuBar.setVisible(false);
	}

	protected JMenuBar createMenuBar() {
		menuBar = new JMenuBar();
		menuBar.setMargin(new Insets(0, 0, 0, 0));
		menuBar.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));

		// Set up the lone menu.
		JMenu mnArquivo = new JMenu("Arquivo");
		mnArquivo.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		mnArquivo.setMnemonic(KeyEvent.VK_D);
		menuBar.add(mnArquivo);

		miLogin = new JMenuItem("Login");
		miLogin.setMnemonic(KeyEvent.VK_N);
		miLogin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		miLogin.setActionCommand("login");
		miLogin.addActionListener(this);
		miLogin.setEnabled(false);
		mnArquivo.add(miLogin);

		JMenuItem miSair = new JMenuItem("Sair");
		miSair.setMnemonic(KeyEvent.VK_Q);
		miSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
		miSair.setActionCommand("sair");
		miSair.addActionListener(this);
		mnArquivo.add(miSair);

		JMenu mnCaixa = new JMenu("Caixa");
		mnCaixa.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		menuBar.add(mnCaixa);

		JMenuItem miContas = new JMenuItem("Contas");
		miContas.setActionCommand("contas");
		miContas.addActionListener(this);
		mnCaixa.add(miContas);

		JMenuItem miEntradas = new JMenuItem("Entradas Hoje");
		miEntradas.setActionCommand("hoje");
		miEntradas.addActionListener(this);
		mnCaixa.add(miEntradas);

		JMenuItem miCaixa = new JMenuItem("Caixa");
		miCaixa.setActionCommand("caixa");
		miCaixa.addActionListener(this);
		mnCaixa.add(miCaixa);

		JMenu mnClientes = new JMenu("Clientes");
		mnClientes.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		menuBar.add(mnClientes);

		JMenuItem miCadastrarCliente = new JMenuItem("Cadastrar Novo");
		miCadastrarCliente.setActionCommand("cadastrarCliente");
		miCadastrarCliente.addActionListener(this);
		mnClientes.add(miCadastrarCliente);

		JMenuItem miListarCliente = new JMenuItem("Listar");
		miListarCliente.setActionCommand("listarCliente");
		miListarCliente.addActionListener(this);
		mnClientes.add(miListarCliente);

		JMenu mnFornecedores = new JMenu("Fornecedores");
		mnFornecedores.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		menuBar.add(mnFornecedores);

		JMenuItem miAcessar = new JMenuItem("Acessar");
		miAcessar.setActionCommand("acessarFornecedores");
		miAcessar.addActionListener(this);
		mnFornecedores.add(miAcessar);

		JMenu mnAgenda = new JMenu("Agenda");
		mnAgenda.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		menuBar.add(mnAgenda);

		JMenuItem miCalendario = new JMenuItem("Calend\u00E1rio");
		miCalendario.setActionCommand("calendario");
		miCalendario.addActionListener(this);
		mnAgenda.add(miCalendario);

		JMenu mnProdutosservios = new JMenu("Produtos/Servi\u00E7os");
		mnProdutosservios.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		menuBar.add(mnProdutosservios);

		JMenuItem miCadastrarPs = new JMenuItem("Cadastrar Novo");
		miCadastrarPs.setActionCommand("cadastrarPs");
		miCadastrarPs.addActionListener(this);
		mnProdutosservios.add(miCadastrarPs);

		JMenuItem miListarPs = new JMenuItem("Listar");
		miListarPs.setActionCommand("listarPs");
		miListarPs.addActionListener(this);
		mnProdutosservios.add(miListarPs);

		JMenu mnImpressao = new JMenu("Impress\u00E3o");
		mnImpressao.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		mnImpressao.setEnabled(false);
		menuBar.add(mnImpressao);

		JMenuItem miAbrirPagina = new JMenuItem("Abrir P\u00E1gina");
		miAbrirPagina.setActionCommand("abrirPagina");
		miAbrirPagina.addActionListener(this);
		mnImpressao.add(miAbrirPagina);

		JMenu mnLogs = new JMenu("Logs");
		mnLogs.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		mnLogs.setEnabled(false);
		menuBar.add(mnLogs);

		JMenuItem miExibir = new JMenuItem("Exibir");
		miExibir.setActionCommand("exibirLogs");
		miExibir.addActionListener(this);
		mnLogs.add(miExibir);

		return menuBar;
	}

	// React to menu selections.
	public void actionPerformed(ActionEvent e) {
		if ("login".equals(e.getActionCommand())) {
			login();
		} else if ("contas".equals(e.getActionCommand())) {
			contas();
		} else if ("hoje".equals(e.getActionCommand())) {
			hoje();
		} else if ("caixa".equals(e.getActionCommand())) {
			caixa();
		} else if ("cadastrarCliente".equals(e.getActionCommand())) {
			cadastroCliente();
		} else if ("listarCliente".equals(e.getActionCommand())) {
			listarClientes();
		} else if ("acessarFornecedores".equals(e.getActionCommand())) {
			fornecedores();
		} else if ("calendario".equals(e.getActionCommand())) {
			calendario();
		} else if ("cadastrarPs".equals(e.getActionCommand())) {
			CadastroPs();
		} else if ("listarPs".equals(e.getActionCommand())) {
			listarPs();
		} else if ("abrirPagina".equals(e.getActionCommand())) {
			abrirPag();
		} else if ("exibirLogs".equals(e.getActionCommand())) {
			exibir();
		} else { // logout
			Login.idUser = 0;
			FechaInternalFrames();
			menuBar.setVisible(false);
			login();
		}
	}

	// Create a new internal frame.
	protected void login() {
		LgLogin frame = new LgLogin();
		frame.setVisible(true);
		desktop.add(frame);
		frame.centralizarJanela();
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void contas() {
		CxContas frame = new CxContas();
		frame.setLocation(5, 5);
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void hoje() {
		CxHoje frame = new CxHoje();
		frame.setLocation(5, 5);
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void caixa() {
		CxCaixa frame = new CxCaixa();
		frame.setLocation(5, 5);
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void cadastroCliente() {
		ClCadastrarCliente frame = new ClCadastrarCliente();
		frame.setLocation(5, 5);
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void listarClientes() {
		ClListar frame = new ClListar();
		frame.setLocation(5, 5);
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void fornecedores() {
		FornecedorFrame frame = null;
		try {
			frame = new FornecedorFrame();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame.setLocation(200, 200);
		frame.setVisible(true);
	}

	protected void calendario() {
		AgCalendario frame = new AgCalendario();
		frame.setLocation(5, 5);
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void CadastroPs() {
		PsCadastrarPs frame = new PsCadastrarPs();
		frame.setLocation(5, 5);
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void listarPs() {
		PsListar frame = new PsListar();
		frame.setLocation(5, 5);
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void abrirPag() {
		AgCalendario frame = new AgCalendario();
		frame.setLocation(5, 5);
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void exibir() {
		AgCalendario frame = new AgCalendario();
		frame.setLocation(5, 5);
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}
	}

	// Quit the application.
	public static void quit() {
		System.exit(0);
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		MainInternalFrame frame = new MainInternalFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Display the window.
		frame.setVisible(true);
	}

	/**
	 * Fecha todos o JInternalFrames abertos
	 */
	public void FechaInternalFrames() {
		JInternalFrame[] frames = desktop.getAllFrames();
		for (int i = 0; i < frames.length; i++) {
			frames[i].dispose();
		}
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}