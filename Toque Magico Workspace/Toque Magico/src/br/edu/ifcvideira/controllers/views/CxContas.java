package br.edu.ifcvideira.controllers.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import br.edu.ifcvideira.DAOs.ContasDao;
import br.edu.ifcvideira.DAOs.FornecedorDao;
import br.edu.ifcvideira.beans.Contas;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.toedter.calendar.JDateChooser;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.ImageIcon;

public class CxContas extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfValor;
	public static JComboBox<Object> cbFornecedor; // Static para atualizar quando chamar FornecedorFrame
	public static boolean atualizar = false;
	private JTable table;
	private JTextArea txDescricao;
	private JCheckBox cbPago;
	private List<Object> contas = new ArrayList<Object>();
	private JTextField tfPesquisa;

	SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
	Contas co = new Contas();
	ContasDao coDao = new ContasDao();
	FornecedorDao foDao = new FornecedorDao();
	private JTextField tfTotal;
	private JDateChooser dcVencimento;
	private JTextField tfCodigo;
	private JButton btRegistrar;
	private JButton btAtualizar;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CxContas() {
		super("Contas", 
				false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable
		setFrameIcon(new ImageIcon(CxContas.class.getResource("/br/edu/ifcvideira/imgs/ico16.png")));
		getContentPane().setBackground(new Color(204, 255, 255));

		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...
		setSize(1320, 800);

		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 408, 1284, 262);
		getContentPane().add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				setCamposFromTabela();
				btRegistrar.setEnabled(false);
				btRegistrar.setToolTipText("Limpe os campos para habilitar o bot?o.");
				btAtualizar.setEnabled(true);
				btAtualizar.setToolTipText("Atualizar informa\u00E7\u00F5es da conta");
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "C?digo", "Valor", "Nome", "Descri??o", "Vencimento", "Pago?" }));

		atualizarTabela();

		JLabel lblContas = new JLabel("Contas");
		lblContas.setForeground(new Color(51, 153, 204));
		lblContas.setFont(new Font("Trebuchet MS", Font.PLAIN, 30));
		lblContas.setHorizontalAlignment(SwingConstants.CENTER);
		lblContas.setBounds(0, 0, 1304, 70);
		getContentPane().add(lblContas);

		JLabel lblValor = new JLabel("Valor:");
		lblValor.setForeground(Color.BLACK);
		lblValor.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblValor.setBounds(260, 81, 120, 32);
		getContentPane().add(lblValor);

		JLabel lblFornecedor = new JLabel("Fornecedor:");
		lblFornecedor.setForeground(Color.BLACK);
		lblFornecedor.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblFornecedor.setBounds(260, 124, 120, 32);
		getContentPane().add(lblFornecedor);

		JLabel lblDescrio = new JLabel("Descri\u00E7\u00E3o:");
		lblDescrio.setForeground(Color.BLACK);
		lblDescrio.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblDescrio.setBounds(260, 167, 120, 32);
		getContentPane().add(lblDescrio);

		JLabel lblVencimento = new JLabel("Vencimento:");
		lblVencimento.setForeground(Color.BLACK);
		lblVencimento.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblVencimento.setBounds(656, 81, 132, 32);
		getContentPane().add(lblVencimento);

		tfValor = new JTextField();
		tfValor.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfValor.setBounds(390, 81, 132, 32);
		getContentPane().add(tfValor);
		tfValor.setColumns(10);

		cbFornecedor = new JComboBox<>();
		try {
			cbFornecedor.setModel(new DefaultComboBoxModel(foDao.buscarNomesFornecedores()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		cbFornecedor.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		cbFornecedor.setBounds(390, 124, 475, 32);
		getContentPane().add(cbFornecedor);

		JScrollPane scrollPaneDescricao = new JScrollPane();
		scrollPaneDescricao.setBounds(390, 167, 581, 159);
		getContentPane().add(scrollPaneDescricao);

		txDescricao = new JTextArea();
		txDescricao.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));
		scrollPaneDescricao.setViewportView(txDescricao);
		txDescricao.setWrapStyleWord(true);
		txDescricao.setLineWrap(true);

		btRegistrar = new JButton("Registrar");
		btRegistrar.setBackground(new Color(0, 204, 204));
		btRegistrar.setForeground(new Color(255, 255, 255));
		btRegistrar.setToolTipText("Registrar nova conta");
		btRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (dcVencimento.getDate() != null && !tfValor.getText().equals("")
						&& cbFornecedor.getSelectedIndex() != 0) {
					try {
						// atribui??o dos valores dos campos para o objeto cliente
						co.setDescricao(txDescricao.getText());
						co.setDataVencimento(formato.format(dcVencimento.getDate()));
						co.setValor(Double.parseDouble(tfValor.getText()));
						co.setStatus(cbPago.isSelected());
						co.setFornecedor(String.valueOf(cbFornecedor.getSelectedItem()));

						// chamada do m?todo de cadastro na classe Dao
						coDao.cadastrarConta(co);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
					atualizarTabela();
					limpar();
				} else {
					JOptionPane.showMessageDialog(null, "Por favor preencha todas as informa??es necess?rias", "Aviso",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btRegistrar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btRegistrar.setBounds(390, 337, 173, 32);
		getContentPane().add(btRegistrar);

		JButton btLimpar = new JButton("Limpar");
		btLimpar.setBackground(new Color(0, 204, 204));
		btLimpar.setForeground(new Color(255, 255, 255));
		btLimpar.setToolTipText("Limpar campos");
		btLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpar();
			}
		});
		btLimpar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btLimpar.setBounds(798, 337, 173, 32);
		getContentPane().add(btLimpar);

		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(51, 153, 204));
		separator.setBounds(10, 395, 1284, 2);
		getContentPane().add(separator);

		cbPago = new JCheckBox("Pago?");
		cbPago.setBackground(null);
		cbPago.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!btRegistrar.isEnabled()) {
					try {
						if (cbPago.isSelected()) {
							co.setStatus(true);
						} else {
							co.setStatus(false);
						}
						co.setCodigo(Integer.parseInt(tfCodigo.getText()));
						coDao.atualizarEstadoConta(co);
						JOptionPane.showMessageDialog(null, "Estado da conta alterado");
						atualizarTabela();
						limpar();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		cbPago.setForeground(new Color(0, 153, 102));
		cbPago.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		cbPago.setBounds(260, 277, 83, 23);
		getContentPane().add(cbPago);

		btAtualizar = new JButton("Atualizar");
		btAtualizar.setBackground(new Color(0, 204, 204));
		btAtualizar.setForeground(new Color(255, 255, 255));
		btAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dcVencimento.getDate() != null && !tfValor.getText().equals("")
						&& cbFornecedor.getSelectedIndex() != 0) {
					try {
						co.setDescricao(txDescricao.getText());
						co.setDataVencimento(formato.format(dcVencimento.getDate()));
						co.setValor(Double.parseDouble(tfValor.getText()));
						co.setStatus(cbPago.isSelected());
						co.setCodigo(Integer.parseInt(tfCodigo.getText()));

						coDao.atualizarConta(co);
						atualizarTabela();
						limpar();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Por favor preencha todas as informa??es necess?rias", "Aviso",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btAtualizar.setEnabled(false);
		btAtualizar.setToolTipText("Atualizar informa\u00E7\u00F5es da conta");
		btAtualizar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btAtualizar.setBounds(595, 337, 173, 32);
		getContentPane().add(btAtualizar);

		JButton btExcluir = new JButton("Excluir Conta");
		btExcluir.setBackground(new Color(0, 204, 204));
		btExcluir.setForeground(new Color(255, 255, 255));
		btExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				co.setCodigo(Integer.parseInt(tfCodigo.getText()));
				try {
					coDao.deletarConta(co);
					atualizarTabela();
					limpar();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btExcluir.setToolTipText("Deletar conta");
		btExcluir.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btExcluir.setBounds(565, 681, 203, 32);
		getContentPane().add(btExcluir);

		tfPesquisa = new JTextField();
		tfPesquisa.setForeground(new Color(0, 153, 102));
		tfPesquisa.setBackground(new Color(255, 255, 255));
		tfPesquisa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfPesquisa.setText("");
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				tfPesquisa.setText("Pesquisar por nome");
			}
		});
		/**
		 * Evento que ativa quando o cursor de escrita do mouse troca de posi??o
		 */
		tfPesquisa.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (!tfPesquisa.getText().equals("Pesquisar por nome")) {
					// Atualizar a tabela apenas com valores correspondentes aos digitados no campo
					// de busca por codigo
					TableRowSorter<TableModel> filtro = null;
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					filtro = new TableRowSorter<TableModel>(model);
					table.setRowSorter(filtro);
					if (tfPesquisa.getText().length() == 0) {
						filtro.setRowFilter(null);
					} else {
						filtro.setRowFilter(RowFilter.regexFilter("(?i)" + tfPesquisa.getText(), 2));
					}
				}
			}
		});
		tfPesquisa.setToolTipText("");
		tfPesquisa.setText("Pesquisar por nome");
		tfPesquisa.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfPesquisa.setColumns(10);
		tfPesquisa.setBounds(1054, 681, 240, 32);
		getContentPane().add(tfPesquisa);

		JLabel label = new JLabel("Total Caixa:");
		label.setEnabled(false);
		label.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		label.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		label.setBounds(10, 681, 120, 32);
		getContentPane().add(label);

		tfTotal = new JTextField();
		tfTotal.setEnabled(false);
		tfTotal.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		tfTotal.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfTotal.setEditable(false);
		tfTotal.setColumns(10);
		tfTotal.setBounds(140, 681, 173, 32);
		getContentPane().add(tfTotal);

		dcVencimento = new JDateChooser();
		dcVencimento.setBounds(798, 81, 175, 32);
		getContentPane().add(dcVencimento);

		tfCodigo = new JTextField();
		tfCodigo.setEditable(false);
		tfCodigo.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		tfCodigo.setColumns(10);
		tfCodigo.setBounds(357, 299, 0, 23);
		getContentPane().add(tfCodigo);

		JButton btNovoFornecedor = new JButton("Novo");
		btNovoFornecedor.setBackground(new Color(0, 204, 204));
		btNovoFornecedor.setForeground(new Color(255, 255, 255));
		btNovoFornecedor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FornecedorFrame janela = null;
				try {
					janela = new FornecedorFrame();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				atualizar = true;
				janela.setVisible(true);
			}
		});
		btNovoFornecedor.setToolTipText("Cadastrar novo fornecedor");
		btNovoFornecedor.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btNovoFornecedor.setBounds(874, 124, 97, 32);
		getContentPane().add(btNovoFornecedor);

		JLabel lbBackground = new JLabel("");
		lbBackground.setIcon(new ImageIcon(CxContas.class.getResource("/br/edu/ifcvideira/imgs/background2.png")));
		lbBackground.setBounds(0, 0, 1320, 721);
		getContentPane().add(lbBackground);
	}

	/**
	 * Passa informa??es da tabela para os campos
	 */
	public void setCamposFromTabela() {
		tfCodigo.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
		tfValor.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
		cbFornecedor.setSelectedItem(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
		txDescricao.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 3)));

		Date date = null;
		try {
			date = formato.parse((String.valueOf(table.getValueAt(table.getSelectedRow(), 4))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dcVencimento.setDate(date);

		if (table.getValueAt(table.getSelectedRow(), 5).equals("Sim")) {
			cbPago.setSelected(true);
		} else {
			cbPago.setSelected(false);
		}
	}

	/**
	 * Limpa os campos
	 */
	public void limpar() {
		tfValor.setText(null);
		cbFornecedor.setSelectedIndex(0);
		txDescricao.setText(null);
		dcVencimento.setDate(null);
		try {
			tfCodigo.setText(String.valueOf(coDao.RetornarProximoCodigoConta()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		cbPago.setSelected(false);
		btRegistrar.setEnabled(true);
		btRegistrar.setToolTipText("");
		btAtualizar.setEnabled(false);
		btAtualizar.setToolTipText("Selecione uma conta da tabela para atualizar");
	}

	/**
	 * Atualiza a tabela
	 */
	public void atualizarTabela() {
		try {
			contas = coDao.buscarTodos();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
			for (int x = 0; x != contas.size(); x++) {
				model.addRow((Object[]) contas.get(x));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}