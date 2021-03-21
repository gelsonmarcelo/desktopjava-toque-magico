package br.edu.ifcvideira.controllers.views;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import br.edu.ifcvideira.DAOs.FornecedorDao;
import br.edu.ifcvideira.beans.Fornecedor;
import java.awt.Color;

/**
 * Frame para fazer a atualização e cadastro das informações do fornecedor e
 * lista-los
 * 
 * @author Família
 *
 */
public class FornecedorFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private List<Object> fornecedores = new ArrayList<Object>();
	private JTextField tfNome;
	private JTable table;
	Fornecedor fo = new Fornecedor();
	FornecedorDao foDao = new FornecedorDao();

	SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");
	private JTextField tfContato;
	private JTextField tfCodFornecedor;
	private JButton btAtualizar;
	private JButton btCadastrar;
	private JButton btExcluir;

	/**
	 * Executa a aplicação
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FornecedorFrame frame = new FornecedorFrame();
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
	public FornecedorFrame() throws SQLException, Exception {
		setTitle("Fornecedores Toque M\u00E1gico");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(ClFrameCliente.class.getResource("/br/edu/ifcvideira/imgs/ico32.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 575, 452);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lb1 = new JLabel("Fornecedores");
		lb1.setForeground(new Color(51, 153, 204));
		lb1.setBounds(0, 5, 569, 24);
		lb1.setHorizontalAlignment(SwingConstants.CENTER);
		lb1.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		contentPane.add(lb1);

		JLabel lblCliente = new JLabel("Nome:");
		lblCliente.setHorizontalAlignment(SwingConstants.LEFT);
		lblCliente.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblCliente.setBounds(15, 40, 76, 24);
		contentPane.add(lblCliente);

		tfNome = new JTextField();
		tfNome.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		tfNome.setEditable(true);
		tfNome.setBounds(112, 40, 447, 24);
		contentPane.add(tfNome);

		btCadastrar = new JButton("Cadastrar");
		btCadastrar.setForeground(new Color(255, 255, 255));
		btCadastrar.setBackground(new Color(0, 204, 204));
		btCadastrar.addActionListener(new ActionListener() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void actionPerformed(ActionEvent arg0) {
				if (!tfNome.getText().equals("") && !tfContato.getText().equals("")) {
					try {
						// Atribuição dos valores dos campos para o objeto fornecedor
						fo.setNome(tfNome.getText());
						fo.setContato(tfContato.getText());

						foDao.cadastrarFornecedor(fo);
						limpar();
						atualizarTabela();
						// Se foi aberto a partir do CxContas precisa atualizar o comboBox quando fecha
						// o frame
						if (CxContas.atualizar) {
							try {
								CxContas.cbFornecedor
										.setModel(new DefaultComboBoxModel(foDao.buscarNomesFornecedores()));
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							dispose();
						}

					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(null, "Por favor preencha todas as informações necessárias", "Aviso",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btCadastrar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btCadastrar.setBounds(35, 330, 149, 33);
		contentPane.add(btCadastrar);

		JButton btLimpar = new JButton("Limpar");
		btLimpar.setForeground(new Color(255, 255, 255));
		btLimpar.setBackground(new Color(0, 204, 204));
		btLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpar();
			}
		});
		btLimpar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btLimpar.setBounds(213, 374, 149, 33);
		contentPane.add(btLimpar);

		JLabel lblContato = new JLabel("Contato:");
		lblContato.setHorizontalAlignment(SwingConstants.LEFT);
		lblContato.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblContato.setBounds(15, 75, 98, 24);
		contentPane.add(lblContato);

		JScrollPane spContato = new JScrollPane();
		spContato.setBounds(15, 110, 544, 198);
		getContentPane().add(spContato);

		table = new JTable();
		table.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		table.setShowGrid(false);
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				setCamposFromTabela();
				btAtualizar.setEnabled(true);
				btAtualizar.setToolTipText("");
				btExcluir.setEnabled(true);
				btExcluir.setToolTipText("");
				btCadastrar.setEnabled(false);
				btCadastrar.setToolTipText("Limpe os campos para habilitar");
			}
		});
		spContato.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Código", "Nome", "Contato" }));

		tfContato = new JTextField();
		tfContato.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		tfContato.setColumns(10);
		tfContato.setBounds(112, 75, 200, 24);
		contentPane.add(tfContato);

		tfCodFornecedor = new JTextField((String) null);
		tfCodFornecedor.setEnabled(false);
		tfCodFornecedor.setEditable(false);
		tfCodFornecedor.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		tfCodFornecedor.setColumns(10);
		tfCodFornecedor.setBounds(545, 242, 0, 24);
		contentPane.add(tfCodFornecedor);

		btAtualizar = new JButton("Atualizar");
		btAtualizar.setForeground(new Color(255, 255, 255));
		btAtualizar.setBackground(new Color(0, 204, 204));
		btAtualizar.addActionListener(new ActionListener() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void actionPerformed(ActionEvent arg0) {
				if (!tfNome.getText().equals("") && !tfContato.getText().equals("")) {
					fo.setCodigo(Integer.parseInt(tfCodFornecedor.getText()));
					fo.setNome(tfNome.getText());
					fo.setContato(tfContato.getText());

					try {
						foDao.atualizarFornecedor(fo);
					} catch (Exception e) {
						e.printStackTrace();
					}
					limpar();
					atualizarTabela();

					// Se foi aberto a partir do CxContas precisa atualizar o comboBox quando fecha
					// o frame
					if (CxContas.atualizar) {
						try {
							CxContas.cbFornecedor.setModel(new DefaultComboBoxModel(foDao.buscarNomesFornecedores()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						dispose();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Por favor preencha todas as informações necessárias", "Aviso",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btAtualizar.setToolTipText("Selecione um fornecedor para atualizar");
		btAtualizar.setEnabled(false);
		btAtualizar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btAtualizar.setBounds(213, 330, 149, 33);
		contentPane.add(btAtualizar);

		btExcluir = new JButton("Excluir");
		btExcluir.setForeground(new Color(255, 255, 255));
		btExcluir.setBackground(new Color(0, 204, 204));
		btExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fo.setCodigo(Integer.parseInt(tfCodFornecedor.getText()));
				try {
					foDao.deletarFornecedor(fo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				limpar();
				atualizarTabela();
			}
		});
		btExcluir.setToolTipText("Selecione um fornecedor para excluir");
		btExcluir.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btExcluir.setEnabled(false);
		btExcluir.setBounds(390, 330, 149, 33);
		contentPane.add(btExcluir);

		atualizarTabela();
	}

	/**
	 * Limpa campos
	 */
	public void limpar() {
		tfNome.setText(null);
		tfContato.setText(null);
		btCadastrar.setEnabled(true);
		btCadastrar.setToolTipText("");
		btAtualizar.setEnabled(false);
		btAtualizar.setToolTipText("Selecione um fornecedor para atualizar");
		btExcluir.setEnabled(false);
		btExcluir.setToolTipText("Selecione um fornecedor para excluir");
	}

	/**
	 * Atualiza a tabela de telefones
	 */
	public void atualizarTabela() {
		try {
			fornecedores = foDao.buscarTodos();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
			for (int x = 0; x != fornecedores.size(); x++) {
				model.addRow((Object[]) fornecedores.get(x));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Definir campos a partir da tabela
	 */
	public void setCamposFromTabela() {
		tfCodFornecedor.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
		tfNome.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
		tfContato.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
	}
}
