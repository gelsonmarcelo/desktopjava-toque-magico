package br.edu.ifcvideira.controllers.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import br.edu.ifcvideira.DAOs.ProdutosDao;
import br.edu.ifcvideira.DAOs.ServicosDao;
import br.edu.ifcvideira.beans.Produtos;
import br.edu.ifcvideira.beans.Servicos;

public class PsCadastrarPs extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfPreco;
	private JTextField tfNome;
	private JSpinner spQnt;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextArea txDescricao;
	private JRadioButton rbServico;
	private JRadioButton rbProduto;

	Servicos se = new Servicos();
	Produtos pr = new Produtos();
	ServicosDao seDao = new ServicosDao();
	ProdutosDao prDao = new ProdutosDao();

	public PsCadastrarPs() {
		super("Cadastrar Produto/Servi?o", 
				false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable
		getContentPane().setBackground(new Color(204, 255, 255));
		setFrameIcon(new ImageIcon(ClCadastrarCliente.class.getResource("/br/edu/ifcvideira/imgs/ico16.png")));
		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...
		setSize(795, 471);

		getContentPane().setLayout(null);

		JLabel lblCadastro = new JLabel("Cadastrar Novo");
		lblCadastro.setForeground(new Color(51, 153, 204));
		lblCadastro.setFont(new Font("Trebuchet MS", Font.PLAIN, 30));
		lblCadastro.setHorizontalAlignment(SwingConstants.CENTER);
		lblCadastro.setBounds(0, 0, 779, 70);
		getContentPane().add(lblCadastro);

		JLabel lbPreco = new JLabel("Pre\u00E7o:");
		lbPreco.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lbPreco.setBounds(75, 167, 120, 32);
		getContentPane().add(lbPreco);

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblNome.setBounds(75, 124, 120, 32);
		getContentPane().add(lblNome);

		tfPreco = new JTextField();
		tfPreco.setBackground(Color.WHITE);
		tfPreco.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfPreco.setBounds(205, 167, 173, 32);
		getContentPane().add(tfPreco);
		tfPreco.setColumns(10);

		tfNome = new JTextField();
		tfNome.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfNome.setColumns(10);
		tfNome.setBounds(205, 124, 478, 32);
		getContentPane().add(tfNome);

		JButton btCadastrar = new JButton("Cadastrar");
		btCadastrar.setForeground(new Color(255, 255, 255));
		btCadastrar.setBackground(new Color(0, 204, 204));
		btCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!tfNome.getText().equals("") && !txDescricao.getText().equals("") && !tfPreco.getText().equals("")) {
					try {
						if (rbServico.isSelected()) {
							se.setNome(tfNome.getText());
							se.setDescricao(txDescricao.getText());
							se.setPreco(Double.parseDouble(tfPreco.getText()));

							seDao.cadastrarServico(se);
						} else {
							pr.setNome(tfNome.getText());
							pr.setDescricao(txDescricao.getText());
							pr.setPreco(Double.parseDouble(tfPreco.getText()));
							pr.setEstoque(Integer.parseInt(spQnt.getValue() + ""));

							prDao.cadastrarProduto(pr);
						}

					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
					limpar();
				} else {
					JOptionPane.showMessageDialog(null, "Por favor preencha todas as informa??es necess?rias", "Aviso",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btCadastrar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btCadastrar.setBounds(203, 361, 173, 32);
		getContentPane().add(btCadastrar);

		JButton btLimpar = new JButton("Limpar");
		btLimpar.setForeground(new Color(255, 255, 255));
		btLimpar.setBackground(new Color(0, 204, 204));
		btLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpar();
			}
		});
		btLimpar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btLimpar.setBounds(435, 361, 173, 32);
		getContentPane().add(btLimpar);

		JLabel lbQuant = new JLabel("Quantidade:");
		lbQuant.setEnabled(false);
		lbQuant.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lbQuant.setBounds(476, 167, 132, 32);
		getContentPane().add(lbQuant);

		spQnt = new JSpinner();
		spQnt.setForeground(new Color(0, 0, 0));
		spQnt.setBackground(new Color(255, 255, 255));
		spQnt.setEnabled(false);
		spQnt.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		spQnt.setBounds(618, 167, 65, 32);
		getContentPane().add(spQnt);

		JLabel lbDescricao = new JLabel("Descri\u00E7\u00E3o:");
		lbDescricao.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lbDescricao.setBounds(75, 210, 120, 32);
		getContentPane().add(lbDescricao);

		JLabel lblSelecione = new JLabel("Selecione:");
		lblSelecione.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblSelecione.setBounds(75, 81, 120, 32);
		getContentPane().add(lblSelecione);

		rbServico = new JRadioButton("Servi\u00E7o");
		rbServico.setBackground(new Color(255, 255, 255));
		rbServico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lbQuant.setEnabled(false);
				spQnt.setEnabled(false);
			}
		});
		rbServico.setSelected(true);
		buttonGroup.add(rbServico);
		rbServico.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		rbServico.setBounds(267, 86, 109, 23);
		getContentPane().add(rbServico);

		rbProduto = new JRadioButton("Produto");
		rbProduto.setBackground(new Color(255, 255, 255));
		rbProduto.setEnabled(false);
		rbProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lbQuant.setEnabled(true);
				spQnt.setEnabled(true);
			}
		});
		buttonGroup.add(rbProduto);
		rbProduto.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		rbProduto.setBounds(493, 86, 109, 23);
		getContentPane().add(rbProduto);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(205, 210, 478, 119);
		getContentPane().add(scrollPane);

		txDescricao = new JTextArea();
		scrollPane.setViewportView(txDescricao);
		txDescricao.setWrapStyleWord(true);
		txDescricao.setLineWrap(true);
		txDescricao.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));

		JLabel lbBackground = new JLabel("");
		lbBackground.setIcon(new ImageIcon(CxContas.class.getResource("/br/edu/ifcvideira/imgs/background2.png")));
		lbBackground.setBounds(0, 0, 1320, 721);
		getContentPane().add(lbBackground);
	}

	/**
	 * Limpa campos
	 */
	public void limpar() {
		tfPreco.setText(null);
		tfNome.setText(null);
		txDescricao.setText(null);
		spQnt.setValue(0);
		/*
		 * try {
		 * textCodigo.setText(String.valueOf(clDao.RetornarProximoCodigoCliente())); }
		 * catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
		 */
	}
}