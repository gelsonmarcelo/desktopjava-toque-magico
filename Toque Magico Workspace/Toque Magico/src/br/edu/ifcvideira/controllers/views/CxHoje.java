package br.edu.ifcvideira.controllers.views;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JDateChooser;

import br.edu.ifcvideira.DAOs.CaixaDao;
import br.edu.ifcvideira.DAOs.ClienteDao;
import br.edu.ifcvideira.beans.Caixa;
import java.awt.Color;

public class CxHoje extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfValor;
	private JTextField tfCodigo;
	private JComboBox<?> cbCliente;
	private JTable table;
	private JTextArea txDescricao;
	private List<Object> caixa = new ArrayList<Object>();
	private JTextField tfPesquisa;
	private JTextField tfTotal;
	private JDateChooser dcData;

	SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
	Caixa cx = new Caixa();
	CaixaDao cxDao = new CaixaDao();
	ClienteDao clDao = new ClienteDao();
	private JButton btAtualizar;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CxHoje() {
		super("Caixa de Hoje", 
				false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable
		getContentPane().setBackground(new Color(204, 255, 255));
		setFrameIcon(new ImageIcon(ClCadastrarCliente.class.getResource("/br/edu/ifcvideira/imgs/ico16.png")));
		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...
		setSize(1320, 714);

		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 408, 1284, 213);
		getContentPane().add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				btAtualizar.setEnabled(true);
				setCamposFromTabela();
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "C?digo", "Valor", "Cliente", "Descri??o", "Hora" }));

		atualizarTabela();

		JLabel lblContas = new JLabel("Entradas Hoje");
		lblContas.setForeground(new Color(51, 153, 204));
		lblContas.setFont(new Font("Trebuchet MS", Font.PLAIN, 30));
		lblContas.setHorizontalAlignment(SwingConstants.CENTER);
		lblContas.setBounds(0, 0, 1304, 70);
		getContentPane().add(lblContas);

		JLabel lblValor = new JLabel("Valor:");
		lblValor.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblValor.setBounds(260, 81, 120, 32);
		getContentPane().add(lblValor);

		JLabel lblFornecedor = new JLabel("Cliente:");
		lblFornecedor.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblFornecedor.setBounds(260, 124, 120, 32);
		getContentPane().add(lblFornecedor);

		JLabel lblDescrio = new JLabel("Descri\u00E7\u00E3o:");
		lblDescrio.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblDescrio.setBounds(260, 167, 120, 32);
		getContentPane().add(lblDescrio);

		JLabel lblVencimento = new JLabel("Data:");
		lblVencimento.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblVencimento.setBounds(726, 81, 74, 32);
		getContentPane().add(lblVencimento);

		tfValor = new JTextField();
		tfValor.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfValor.setBounds(390, 81, 173, 32);
		getContentPane().add(tfValor);
		tfValor.setColumns(10);

		cbCliente = new JComboBox();
		try {
			cbCliente.setModel(new DefaultComboBoxModel(clDao.buscarNomesClientes()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		cbCliente.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		cbCliente.setBounds(390, 124, 465, 32);
		getContentPane().add(cbCliente);

		JScrollPane scrollPaneDescricao = new JScrollPane();
		scrollPaneDescricao.setBounds(390, 167, 581, 159);
		getContentPane().add(scrollPaneDescricao);

		txDescricao = new JTextArea();
		txDescricao.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));
		scrollPaneDescricao.setViewportView(txDescricao);
		txDescricao.setWrapStyleWord(true);
		txDescricao.setLineWrap(true);

		JButton btRegistrar = new JButton("Registrar");
		btRegistrar.setBackground(new Color(0, 204, 204));
		btRegistrar.setForeground(new Color(255, 255, 255));
		btRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (dcData.getDate() != null && !tfValor.getText().equals("") && cbCliente.getSelectedIndex() != 0) {
					try {
						// Atribui??o dos valores dos campos para o objeto
						cx.setValor(Double.parseDouble(tfValor.getText()));
						cx.setDescricao(txDescricao.getText());
						cx.setDataRegistro(formato.format(dcData.getDate()));
						cx.setCliente(String.valueOf(cbCliente.getSelectedItem()));

						// Chamada do m?todo de cadastro na classe Dao
						cxDao.registrarCaixa(cx);
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
		btLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpar();
			}
		});
		btLimpar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btLimpar.setBounds(798, 337, 173, 32);
		getContentPane().add(btLimpar);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 395, 1284, 2);
		getContentPane().add(separator);

		JButton btExcluir = new JButton("Excluir Registro");
		btExcluir.setBackground(new Color(0, 204, 204));
		btExcluir.setForeground(new Color(255, 255, 255));
		btExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cx.setCodigo(Integer.parseInt(tfCodigo.getText()));
				try {
					cxDao.deletarRegistro(cx);
				} catch (Exception e) {
					e.printStackTrace();
				}
				atualizarTabela();
				limpar();
			}
		});
		btExcluir.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btExcluir.setBounds(581, 632, 203, 32);
		btExcluir.setEnabled(false);
		btExcluir.setToolTipText("Indispon?vel nesta vers?o");
		getContentPane().add(btExcluir);

		tfPesquisa = new JTextField();
		tfPesquisa.setForeground(new Color(0, 153, 102));
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
		tfPesquisa.setBounds(1054, 632, 240, 32);
		getContentPane().add(tfPesquisa);

		JLabel lblTotalCaixa = new JLabel("Total Caixa:");
		lblTotalCaixa.setForeground(new Color(0, 0, 0));
		lblTotalCaixa.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		lblTotalCaixa.setEnabled(false);
		lblTotalCaixa.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblTotalCaixa.setBounds(10, 632, 120, 32);
		getContentPane().add(lblTotalCaixa);

		tfTotal = new JTextField();
		tfTotal.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		tfTotal.setEditable(false);
		tfTotal.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfTotal.setColumns(10);
		tfTotal.setBounds(140, 632, 173, 32);
		getContentPane().add(tfTotal);

		dcData = new JDateChooser();
		dcData.setBounds(798, 81, 175, 32);
		getContentPane().add(dcData);

		tfCodigo = new JTextField();
		tfCodigo.setEditable(false);
		tfCodigo.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		tfCodigo.setColumns(10);
		tfCodigo.setBounds(356, 299, 0, 23);
		getContentPane().add(tfCodigo);

		btAtualizar = new JButton("Atualizar");
		btAtualizar.setBackground(new Color(0, 204, 204));
		btAtualizar.setForeground(new Color(255, 255, 255));
		btAtualizar.setEnabled(false);
		btAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (dcData.getDate() != null && !tfValor.getText().equals("") && cbCliente.getSelectedIndex() != 0) {
					cx.setCodigo(Integer.parseInt(tfCodigo.getText()));
					cx.setDescricao(txDescricao.getText());
					cx.setDataRegistro(formato.format(dcData.getDate()));
					cx.setValor(Double.parseDouble(tfValor.getText()));

					try {
						cxDao.atualizarRegistro(cx);
					} catch (Exception e) {
						e.printStackTrace();
					}
					btAtualizar.setEnabled(false);
					atualizarTabela();
					limpar();
				} else {
					JOptionPane.showMessageDialog(null, "Por favor preencha todas as informa??es necess?rias", "Aviso",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btAtualizar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btAtualizar.setBounds(592, 337, 173, 32);
		getContentPane().add(btAtualizar);

		JButton btNovoCliente = new JButton("Novo");
		btNovoCliente.setEnabled(false);
		btNovoCliente.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		btNovoCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btNovoCliente.setForeground(Color.WHITE);
		btNovoCliente.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btNovoCliente.setBackground(new Color(0, 204, 204));
		btNovoCliente.setBounds(865, 124, 106, 32);
		getContentPane().add(btNovoCliente);

		JLabel lbBackground = new JLabel("");
		lbBackground.setIcon(new ImageIcon(CxContas.class.getResource("/br/edu/ifcvideira/imgs/background2.png")));
		lbBackground.setBounds(0, 0, 1320, 721);
		getContentPane().add(lbBackground);
	}

	/**
	 * Manda valores da tabela para os campos
	 */
	public void setCamposFromTabela() {
		cbCliente.setEnabled(false);
		cbCliente.setToolTipText("N?o ? poss?vel atualizar o cliente. Clique em limpar para desbloquear!");
		tfCodigo.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
		tfValor.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
		cbCliente.setSelectedItem(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
		txDescricao.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 3)));
		Date date = null;
		try {
			date = formato.parse(getDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dcData.setDate(date);
	}

	/**
	 * Limpa os campos
	 */
	public void limpar() {
		tfValor.setText(null);
		cbCliente.setSelectedIndex(0);
		txDescricao.setText(null);
		dcData.setDate(null);
		cbCliente.setEnabled(true);
		cbCliente.setToolTipText("");
		try {
			tfCodigo.setText(String.valueOf(cxDao.RetornarProximoCodigoRegistro()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Atualiza a tabela com valores do banco
	 */
	public void atualizarTabela() {
		try {
			caixa = cxDao.buscarTodosHoje();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
			for (int x = 0; x != caixa.size(); x++) {
				model.addRow((Object[]) caixa.get(x));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Pega a data atual
	 * 
	 * @return
	 */
	public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dateFormat.format(date);
	}
}