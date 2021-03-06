package br.edu.ifcvideira.controllers.views;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
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

import br.edu.ifcvideira.DAOs.CaixaDao;
import br.edu.ifcvideira.beans.Caixa;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class CxCaixa extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private List<Object> caixa = new ArrayList<Object>();
	private JTextField tfPesquisa;
	private JTextField tfTotal;

	Caixa co = new Caixa();
	CaixaDao coDao = new CaixaDao();

	public CxCaixa() {
		super("Caixa", 
				false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable
		getContentPane().setBackground(new Color(204, 255, 255));
		setFrameIcon(new ImageIcon(ClCadastrarCliente.class.getResource("/br/edu/ifcvideira/imgs/ico16.png")));
		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...
		setSize(1320, 702);

		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 124, 1284, 476);
		getContentPane().add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				// setCamposFromTabela();
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "C?digo", "Valor", "Nome", "Descri??o", "Data" }));
		atualizarTabela();
		JLabel lblCaixa = new JLabel("Caixa");
		lblCaixa.setFont(new Font("Trebuchet MS", Font.PLAIN, 30));
		lblCaixa.setHorizontalAlignment(SwingConstants.CENTER);
		lblCaixa.setBounds(0, 0, 1304, 70);
		getContentPane().add(lblCaixa);

		JButton btExcluir = new JButton("Excluir Registro");
		btExcluir.setBackground(new Color(0, 204, 204));
		btExcluir.setForeground(new Color(255, 255, 255));
		btExcluir.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		btExcluir.setEnabled(false);
		btExcluir.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btExcluir.setBounds(568, 624, 203, 32);
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
						filtro.setRowFilter(RowFilter.regexFilter("(?i)" + tfPesquisa.getText(), 2));
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

		JLabel lblTotalCaixa = new JLabel("Total Caixa:");
		lblTotalCaixa.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		lblTotalCaixa.setEnabled(false);
		lblTotalCaixa.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblTotalCaixa.setBounds(10, 624, 120, 32);
		getContentPane().add(lblTotalCaixa);

		tfTotal = new JTextField();
		tfTotal.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		tfTotal.setEditable(false);
		tfTotal.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfTotal.setColumns(10);
		tfTotal.setBounds(140, 624, 173, 32);
		getContentPane().add(tfTotal);

		JButton btAtualizar = new JButton();
		btAtualizar.setBackground(new Color(255, 255, 255));
		btAtualizar.setToolTipText("Atualizar tabela <E.g.: Icon made by Freepik from www.flaticon.com >");
		btAtualizar.setBorder(null);
		btAtualizar.setIcon(new ImageIcon(ClListar.class.getResource("/br/edu/ifcvideira/imgs/updateIcon.png")));
		btAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				atualizarTabela();
			}
		});
		btAtualizar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btAtualizar.setBounds(10, 81, 52, 32);
		getContentPane().add(btAtualizar);
		
		JLabel lbBackground = new JLabel("");
		lbBackground.setIcon(new ImageIcon(CxContas.class.getResource("/br/edu/ifcvideira/imgs/background2.png")));
		lbBackground.setBounds(0, 0, 1320, 721);
		getContentPane().add(lbBackground);
	}

	/**
	 * Atualiza a tabela
	 */
	public void atualizarTabela() {
		try {
			caixa = coDao.buscarTodos();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
			for (int x = 0; x != caixa.size(); x++) {
				model.addRow((Object[]) caixa.get(x));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}