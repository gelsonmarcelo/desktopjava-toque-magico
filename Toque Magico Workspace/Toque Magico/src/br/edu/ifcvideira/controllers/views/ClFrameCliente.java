package br.edu.ifcvideira.controllers.views;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import br.edu.ifcvideira.DAOs.ClienteDao;
import br.edu.ifcvideira.DAOs.TelefoneDao;
import br.edu.ifcvideira.beans.Cliente;
import br.edu.ifcvideira.beans.Telefone;
import java.awt.Color;
import java.awt.Toolkit;

/**
 * Frame para fazer a atualização das informações do cliente ou ver mais
 * detalhes
 * 
 * @author Família
 *
 */
public class ClFrameCliente extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private List<Object> telefones = new ArrayList<Object>();
	private JTextField tfNome;
	private JTextArea txObservacao;
	private JTable table;

	Cliente cl = new Cliente();
	ClienteDao clDao = new ClienteDao();
	TelefoneDao teDao = new TelefoneDao();
	Telefone te = new Telefone();
	private JFormattedTextField tfCpf;
	private JTextField tfLogradouro;
	SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");
	private JTextArea txAnamnese;
	private JDateChooser dcNascimento;
	private JTextField tfSaldo;
	String[] informacoesCliente = new String[9];
	private JFormattedTextField tfTelefone;
	private JTextField tfCodTelefone;
	private JButton btExcluirTelefone;
	private JButton btAtualizarTelefone;
	private JButton btAdicionarTelefone;

	/**
	 * Executa a aplicação
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClFrameCliente frame = new ClFrameCliente();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Cria o frame
	 * 
	 * @throws Exception
	 * @throws SQLException
	 */
	public ClFrameCliente() throws SQLException, Exception {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(ClFrameCliente.class.getResource("/br/edu/ifcvideira/imgs/ico32.png")));
		informacoesCliente = clDao.buscarInformacoes(ClListar.codigoAtualizarCliente);
		setTitle("Clientes Toque M\u00E1gico");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 575, 587);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lb1 = new JLabel("Detalhes/Atualiza\u00E7\u00E3o");
		lb1.setForeground(new Color(51, 153, 204));
		lb1.setBounds(0, 5, 569, 24);
		lb1.setHorizontalAlignment(SwingConstants.CENTER);
		lb1.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		contentPane.add(lb1);

		JLabel lblCliente = new JLabel("Cliente:");
		lblCliente.setHorizontalAlignment(SwingConstants.LEFT);
		lblCliente.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblCliente.setBounds(15, 40, 76, 24);
		contentPane.add(lblCliente);

		JLabel lblValor = new JLabel("Anamnese:");
		lblValor.setHorizontalAlignment(SwingConstants.LEFT);
		lblValor.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblValor.setBounds(15, 275, 76, 24);
		contentPane.add(lblValor);

		tfNome = new JTextField(informacoesCliente[2]);
		tfNome.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		tfNome.setEditable(true);
		tfNome.setBounds(112, 40, 447, 24);
		contentPane.add(tfNome);

		JLabel lblObservaes = new JLabel("Observa\u00E7\u00F5es:");
		lblObservaes.setHorizontalAlignment(SwingConstants.LEFT);
		lblObservaes.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblObservaes.setBounds(15, 381, 98, 24);
		contentPane.add(lblObservaes);

		JScrollPane spObservacoes = new JScrollPane();
		spObservacoes.setBounds(114, 381, 445, 86);
		contentPane.add(spObservacoes);

		txObservacao = new JTextArea(informacoesCliente[7]);
		txObservacao.setToolTipText("");
		spObservacoes.setViewportView(txObservacao);
		txObservacao.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		txObservacao.setWrapStyleWord(true);
		txObservacao.setLineWrap(true);

		JButton btAtualizar = new JButton("Atualizar");
		btAtualizar.setForeground(new Color(255, 255, 255));
		btAtualizar.setBackground(new Color(0, 204, 204));
		btAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!tfNome.getText().equals("") && !tfCpf.getText().equals("") && !tfLogradouro.getText().equals("")) {
					try {
						// Atribuição dos valores dos campos para o objeto cliente
						cl.setNome(tfNome.getText());
						cl.setCpf(tfCpf.getText());
						cl.setDataNascimento(formatador.format(dcNascimento.getDate()));
						cl.setObservacao(txObservacao.getText());
						cl.setLogradouro(tfLogradouro.getText());
						cl.setAnamnese(txAnamnese.getText());
						cl.setSaldo(Double.parseDouble(tfSaldo.getText()));
						cl.setCodigo(ClListar.codigoAtualizarCliente);

						clDao.atualizarCliente(cl);
						dispose();

					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(null, "Por favor preencha todas as informações necessárias", "Aviso",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btAtualizar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btAtualizar.setBounds(112, 515, 149, 33);
		contentPane.add(btAtualizar);

		JButton btLimpar = new JButton("Limpar");
		btLimpar.setForeground(new Color(255, 255, 255));
		btLimpar.setBackground(new Color(0, 204, 204));
		btLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpar();
			}
		});
		btLimpar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btLimpar.setBounds(327, 515, 149, 33);
		contentPane.add(btLimpar);

		JLabel lblCpf = new JLabel("CPF:");
		lblCpf.setHorizontalAlignment(SwingConstants.LEFT);
		lblCpf.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblCpf.setBounds(15, 75, 76, 24);
		contentPane.add(lblCpf);

		tfCpf = new JFormattedTextField(ClCadastrarCliente.mascaraField("###.###.###-##"));
		tfCpf.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		tfCpf.setColumns(10);
		tfCpf.setBounds(112, 75, 200, 24);
		tfCpf.setText(informacoesCliente[3]);
		contentPane.add(tfCpf);

		JLabel lblLogradouto = new JLabel("Logradouro:");
		lblLogradouto.setHorizontalAlignment(SwingConstants.LEFT);
		lblLogradouto.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblLogradouto.setBounds(15, 145, 98, 24);
		contentPane.add(lblLogradouto);

		tfLogradouro = new JTextField(informacoesCliente[5]);
		tfLogradouro.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		tfLogradouro.setColumns(10);
		tfLogradouro.setBounds(112, 145, 447, 24);
		contentPane.add(tfLogradouro);

		JLabel lblNascimento = new JLabel("Nascimento:");
		lblNascimento.setHorizontalAlignment(SwingConstants.LEFT);
		lblNascimento.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblNascimento.setBounds(15, 110, 98, 24);
		contentPane.add(lblNascimento);

		dcNascimento = new JDateChooser();
		dcNascimento.setBounds(112, 110, 166, 24);
		dcNascimento.setDate(formatador.parse(informacoesCliente[4]));
		contentPane.add(dcNascimento);

		JLabel lblContato = new JLabel("Contato:");
		lblContato.setHorizontalAlignment(SwingConstants.LEFT);
		lblContato.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblContato.setBounds(15, 180, 98, 24);
		contentPane.add(lblContato);

		JScrollPane spContato = new JScrollPane();
		spContato.setBounds(112, 180, 200, 86);
		getContentPane().add(spContato);

		table = new JTable();
		table.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		table.setShowGrid(false);
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				setCamposFromTabela();
				btAtualizarTelefone.setEnabled(true);
				btExcluirTelefone.setEnabled(true);
			}
		});
		spContato.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Código", "Número" }));

		JScrollPane spAnamnese = new JScrollPane();
		spAnamnese.setBounds(112, 277, 447, 84);
		contentPane.add(spAnamnese);

		txAnamnese = new JTextArea(informacoesCliente[6]);
		spAnamnese.setViewportView(txAnamnese);
		txAnamnese.setWrapStyleWord(true);
		txAnamnese.setToolTipText("");
		txAnamnese.setLineWrap(true);
		txAnamnese.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

		JLabel lblSaldo = new JLabel("Saldo:");
		lblSaldo.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		lblSaldo.setEnabled(false);
		lblSaldo.setHorizontalAlignment(SwingConstants.LEFT);
		lblSaldo.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblSaldo.setBounds(15, 478, 76, 24);
		contentPane.add(lblSaldo);

		tfSaldo = new JTextField(informacoesCliente[9]);
		tfSaldo.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		tfSaldo.setEnabled(false);
		tfSaldo.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		tfSaldo.setColumns(10);
		tfSaldo.setBounds(112, 478, 200, 24);
		contentPane.add(tfSaldo);

		btAtualizarTelefone = new JButton("Atualizar");
		btAtualizarTelefone.setForeground(new Color(255, 255, 255));
		btAtualizarTelefone.setBackground(new Color(0, 204, 204));
		btAtualizarTelefone.setEnabled(false);
		btAtualizarTelefone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				te.setCodigo(Integer.parseInt(tfCodTelefone.getText()));
				te.setNumero(tfTelefone.getText());
				try {
					teDao.atualizarTelefone(te);
				} catch (Exception e) {
					e.printStackTrace();
				}
				btAtualizarTelefone.setEnabled(false);
				btExcluirTelefone.setEnabled(false);
				tfCodTelefone.setText(null);
				tfTelefone.setText(null);
				atualizarTelefones();
			}
		});
		btAtualizarTelefone.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		btAtualizarTelefone.setBounds(338, 242, 98, 24);
		contentPane.add(btAtualizarTelefone);

		btExcluirTelefone = new JButton("Excluir");
		btExcluirTelefone.setForeground(new Color(255, 255, 255));
		btExcluirTelefone.setBackground(new Color(0, 204, 204));
		btExcluirTelefone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				te.setCodigo(Integer.parseInt(tfCodTelefone.getText()));
				try {
					teDao.deletarTelefone(te);
				} catch (Exception e) {
					e.printStackTrace();
				}
				btAtualizarTelefone.setEnabled(false);
				btExcluirTelefone.setEnabled(false);
				tfCodTelefone.setText(null);
				tfTelefone.setText(null);
				atualizarTelefones();
			}
		});
		btExcluirTelefone.setEnabled(false);
		btExcluirTelefone.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		btExcluirTelefone.setBounds(440, 242, 98, 24);
		contentPane.add(btExcluirTelefone);

		tfTelefone = new JFormattedTextField(ClCadastrarCliente.mascaraField("(##) #####-####"));
		tfTelefone.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		tfTelefone.setColumns(10);
		tfTelefone.setBounds(338, 180, 200, 24);
		contentPane.add(tfTelefone);

		tfCodTelefone = new JTextField((String) null);
		tfCodTelefone.setEnabled(false);
		tfCodTelefone.setEditable(false);
		tfCodTelefone.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		tfCodTelefone.setColumns(10);
		tfCodTelefone.setBounds(545, 242, 0, 24);
		contentPane.add(tfCodTelefone);

		btAdicionarTelefone = new JButton("Adicionar");
		btAdicionarTelefone.setForeground(new Color(255, 255, 255));
		btAdicionarTelefone.setBackground(new Color(0, 204, 204));
		btAdicionarTelefone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				te.setId_cliente(ClListar.codigoAtualizarCliente);
				te.setNumero(tfTelefone.getText());
				try {
					teDao.cadastrarTelefone(te);
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				tfTelefone.setText(null);
				atualizarTelefones();
			}
		});
		btAdicionarTelefone.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		btAdicionarTelefone.setBounds(390, 215, 98, 24);
		contentPane.add(btAdicionarTelefone);

		atualizarTelefones();
	}

	/**
	 * Limpa campos
	 */
	public void limpar() {
		tfCpf.setText(null);
		dcNascimento.setDate(null);
		tfLogradouro.setText(null);
		tfTelefone.setText(null);
		txAnamnese.setText(null);
		txObservacao.setText(null);
		tfSaldo.setText("0.0");
	}

	/**
	 * Atualiza a tabela de telefones
	 */
	public void atualizarTelefones() {
		try {
			telefones = teDao.buscarTelefones(ClListar.codigoAtualizarCliente);
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
			for (int x = 0; x != telefones.size(); x++) {
				model.addRow((Object[]) telefones.get(x));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Definir campos a partir da tabela
	 */
	public void setCamposFromTabela() {
		tfCodTelefone.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
		tfTelefone.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
	}
}
