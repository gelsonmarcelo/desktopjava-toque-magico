package br.edu.ifcvideira.controllers.views;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import br.edu.ifcvideira.DAOs.ClienteDao;
import br.edu.ifcvideira.beans.Cliente;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.ImageIcon;

public class ClListar extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private List<Object> clientes = new ArrayList<Object>();
	private JTextField tfPesquisa;
	public static int codigoAtualizarCliente;

	Cliente cl = new Cliente();
	ClienteDao clDao = new ClienteDao();

	public ClListar() {
		super("Lista de Clientes", 
				true, // resizable
				true, // closable
				true, // maximizable
				true);// iconifiable
		getContentPane().setBackground(new Color(204, 255, 255));
		setFrameIcon(new ImageIcon(ClCadastrarCliente.class.getResource("/br/edu/ifcvideira/imgs/ico16.png")));
		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...
		setSize(1320, 707);

		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 124, 1284, 477);
		getContentPane().add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "C?digo", "Nome", "Contato", "CPF", "Observa??o", "Saldo", "Nascimento" }));
		atualizarTabela();
		JLabel lbClientesLista = new JLabel("Lista de Clientes");
		lbClientesLista.setForeground(new Color(51, 153, 204));
		lbClientesLista.setFont(new Font("Trebuchet MS", Font.PLAIN, 30));
		lbClientesLista.setHorizontalAlignment(SwingConstants.CENTER);
		lbClientesLista.setBounds(0, 0, 1304, 70);
		getContentPane().add(lbClientesLista);

		JButton btExcluir = new JButton("Excluir Cliente");
		btExcluir.setBackground(new Color(0, 204, 204));
		btExcluir.setForeground(new Color(255, 255, 255));
		btExcluir.setToolTipText("N\u00E3o ser\u00E1 poss\u00EDvel excluir um cliente vinculado a registros!");
		btExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Evita erros por n?o ter linha selecionada
				if (table.getSelectedRow() != -1) {
					cl.setCodigo(Integer.parseInt(String.valueOf(table.getValueAt(table.getSelectedRow(), 0))));
					try {
						clDao.deletarCliente(cl);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					atualizarTabela();
				}
			}
		});
		btExcluir.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btExcluir.setBounds(691, 629, 225, 32);
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
					//Atualizar a tabela apenas com valores correspondentes aos digitados no campo
					//de busca por codigo
					TableRowSorter<TableModel> filtro = null;
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					filtro = new TableRowSorter<TableModel>(model);
					table.setRowSorter(filtro);
					if (tfPesquisa.getText().length() == 0) {
						filtro.setRowFilter(null);
					} else {
						filtro.setRowFilter(RowFilter.regexFilter("(?i)" + tfPesquisa.getText(), 1));
					} 
				}
			}
		});
		tfPesquisa.setToolTipText("");
		tfPesquisa.setText("Pesquisar por nome");
		tfPesquisa.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfPesquisa.setColumns(10);
		tfPesquisa.setBounds(1054, 81, 240, 32);
		getContentPane().add(tfPesquisa);

		JButton btAtualizar = new JButton("");
		btAtualizar.setBackground(new Color(255, 255, 255));
		btAtualizar.setToolTipText("Atualizar tabela <E.g.: Icon made by Freepik from www.flaticon.com> ");
		btAtualizar.setBorder(null);
		btAtualizar.setIcon(new ImageIcon(ClListar.class.getResource("/br/edu/ifcvideira/imgs/updateIcon.png")));
		btAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				atualizarTabela();
			}
		});
		btAtualizar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btAtualizar.setBounds(10, 72, 55, 41);
		getContentPane().add(btAtualizar);

		JButton btDetalhes = new JButton("+Detalhes/Atualizar");
		btDetalhes.setBackground(new Color(0, 204, 204));
		btDetalhes.setForeground(new Color(255, 255, 255));
		btDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				codigoAtualizarCliente = Integer.parseInt(String.valueOf((table.getValueAt(table.getSelectedRow(), 0))));
				ClFrameCliente frame = null;
				try {
					frame = new ClFrameCliente();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				frame.setVisible(true);
			}
		});
		btDetalhes.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btDetalhes.setBounds(410, 629, 225, 32);
		getContentPane().add(btDetalhes);
		
		JLabel lbBackground = new JLabel("");
		lbBackground.setIcon(new ImageIcon(CxContas.class.getResource("/br/edu/ifcvideira/imgs/background2.png")));
		lbBackground.setBounds(0, 0, 1320, 721);
		getContentPane().add(lbBackground);
	}

	/**
	 * Pega informa??es do banco e atualiza a tabela
	 */
	public void atualizarTabela() {
		try {
			clientes = clDao.buscarTodos();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
			for (int x = 0; x != clientes.size(); x++) {
				model.addRow((Object[]) clientes.get(x));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}