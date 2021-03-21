package br.edu.ifcvideira.controllers.views;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import br.edu.ifcvideira.DAOs.ProdutosDao;
import br.edu.ifcvideira.DAOs.ServicosDao;
import br.edu.ifcvideira.beans.Produtos;
import br.edu.ifcvideira.beans.Servicos;
import java.awt.Color;

public class PsListar extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private List<Object> array = new ArrayList<Object>();
	private JTextField tfPesquisa;

	Produtos pr = new Produtos();
	ProdutosDao prDao = new ProdutosDao();
	Servicos se = new Servicos();
	ServicosDao seDao = new ServicosDao();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PsListar() {
		super("Listar Produtos/Serviços", 
				false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable
		getContentPane().setBackground(new Color(204, 255, 255));
		setFrameIcon(new ImageIcon(ClCadastrarCliente.class.getResource("/br/edu/ifcvideira/imgs/ico16.png")));
		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...
		setSize(931, 618);

		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setBounds(10, 124, 895, 389);
		getContentPane().add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Código", "Nome", "Descrição", "Preço", "Quantidade" }));
		atualizarTabelaServicos();

		JLabel lbClientesLista = new JLabel("Lista de ");
		lbClientesLista.setForeground(new Color(51, 153, 204));
		lbClientesLista.setFont(new Font("Trebuchet MS", Font.PLAIN, 30));
		lbClientesLista.setHorizontalAlignment(SwingConstants.CENTER);
		lbClientesLista.setBounds(0, 0, 764, 70);
		getContentPane().add(lbClientesLista);

		JButton btExcluir = new JButton("Excluir");
		btExcluir.setBackground(new Color(0, 204, 204));
		btExcluir.setForeground(new Color(255, 255, 255));
		btExcluir.setEnabled(false);
		btExcluir.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		btExcluir.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btExcluir.setBounds(469, 533, 225, 32);
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
		 * Evento que ativa quando o cursor de escrita do mouse troca de posição
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
		tfPesquisa.setBounds(661, 81, 244, 32);
		getContentPane().add(tfPesquisa);

		JButton btDetalhes = new JButton("+Detalhes/Atualizar");
		btDetalhes.setBackground(new Color(0, 204, 204));
		btDetalhes.setForeground(new Color(255, 255, 255));
		btDetalhes.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		btDetalhes.setEnabled(false);
		btDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/**
				 * ####FAZER FRAME PARA ABRIR AQUI: EDIÇÃO/ATUALIZAÇÃO DE PRODUTO OU SERVIÇO
				 */
			}
		});
		btDetalhes.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btDetalhes.setBounds(188, 533, 225, 32);
		getContentPane().add(btDetalhes);

		JComboBox<Object> cbProdutoServico = new JComboBox<Object>();
		cbProdutoServico.setForeground(new Color(51, 153, 204));
		cbProdutoServico.setEnabled(false);
		cbProdutoServico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Selecionado produto
				if (cbProdutoServico.getSelectedIndex() == 0) {
					atualizarTabelaProdutos();
					// Selecionado serviço
				} else {
					atualizarTabelaServicos();
				}
			}
		});
		cbProdutoServico.setFont(new Font("Trebuchet MS", Font.PLAIN, 28));
		cbProdutoServico.setModel(new DefaultComboBoxModel(new String[] {"Servi\u00E7os", "Produtos"}));
		cbProdutoServico.setBounds(445, 20, 155, 32);
		getContentPane().add(cbProdutoServico);
		
		JLabel lbBackground = new JLabel("");
		lbBackground.setIcon(new ImageIcon(CxContas.class.getResource("/br/edu/ifcvideira/imgs/background2.png")));
		lbBackground.setBounds(0, 0, 1320, 721);
		getContentPane().add(lbBackground);
	}

	/**
	 * ####FALTA: fazer apenas um método colocando parâmetros
	 */
	/**
	 * Atualizar tabela de produtos
	 */
	public void atualizarTabelaProdutos() {
		try {
			array = prDao.buscarTodos();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
			for (int x = 0; x != array.size(); x++) {
				model.addRow((Object[]) array.get(x));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Atualizar tabela de serviços
	 */
	public void atualizarTabelaServicos() {
		try {
			array = seDao.buscarTodos();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
			for (int x = 0; x != array.size(); x++) {
				model.addRow((Object[]) array.get(x));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}