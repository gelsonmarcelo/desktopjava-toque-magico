package br.edu.ifcvideira.controllers.views;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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

import br.edu.ifcvideira.DAOs.AgendaDao;
import br.edu.ifcvideira.DAOs.ClienteDao;
import br.edu.ifcvideira.DAOs.ServicosDao;
import br.edu.ifcvideira.beans.Agenda;
import java.awt.Color;
import java.awt.Toolkit;

/**
 * Frame para ver e modificar hor?rios marcados, ? chamado a partir do
 * calend?rio ####ADICIONAR FUN??O DE ATUALIZAR VALORES DA AGENDA
 * 
 * @author Fam?lia
 *
 */
public class AgFrameDia extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JLabel lblNovoEvento;
	private JTextField tfValor;
	private JTextField tfHorario;
	private List<Object> agenda = new ArrayList<Object>();
	private JComboBox<?> cbClientes;
	private JComboBox<Object> cbServicos;
	private JTextArea txObservacoes;
	private JTextField tfCodigo;
	private JCheckBox cbRealizado;

	Agenda ag = new Agenda();
	AgendaDao agDao = new AgendaDao();
	ServicosDao seDao = new ServicosDao();
	ClienteDao clDao = new ClienteDao();
	private JButton btRegistrar;

	/**
	 * Executa a aplica??o
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AgFrameDia frame = new AgFrameDia();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Cria o frame
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AgFrameDia() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(AgFrameDia.class.getResource("/br/edu/ifcvideira/imgs/ico32.png")));
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent arg0) {
				atualizarTabela();
				limpar();
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				Agenda.frameAberto = false;
			}
		});

		setTitle("Agenda Toque M\u00E1gico");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 575, 551);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		/**
		 * ####EDITAR DATA PARA NOSSO FORMATO PADR?O
		 */
		JLabel lblAgendaDeDia = new JLabel("Agenda de " + AgCalendario.dataEnviada);
		lblAgendaDeDia.setForeground(new Color(51, 153, 204));
		lblAgendaDeDia.setBounds(0, 5, 569, 24);
		lblAgendaDeDia.setHorizontalAlignment(SwingConstants.CENTER);
		lblAgendaDeDia.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		contentPane.add(lblAgendaDeDia);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 40, 544, 190);
		contentPane.add(scrollPane);

		table = new JTable();
		table.setBackground(new Color(255, 255, 255));
		table.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				setCamposFromTabela();
				btRegistrar.setEnabled(false);
				btRegistrar.setToolTipText("Limpe os campos para habilitar o bot?o.");
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Codigo", "Cliente", "Servi?o", "Valor", "Hora", "Realizado?" }));

		lblNovoEvento = new JLabel("Nova Agenda");
		lblNovoEvento.setHorizontalAlignment(SwingConstants.LEFT);
		lblNovoEvento.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblNovoEvento.setBounds(15, 241, 197, 24);
		contentPane.add(lblNovoEvento);

		JLabel lblCliente = new JLabel("Cliente:");
		lblCliente.setHorizontalAlignment(SwingConstants.LEFT);
		lblCliente.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblCliente.setBounds(15, 276, 76, 24);
		contentPane.add(lblCliente);

		JLabel lblServio = new JLabel("Servi\u00E7o:");
		lblServio.setHorizontalAlignment(SwingConstants.LEFT);
		lblServio.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblServio.setBounds(15, 311, 76, 24);
		contentPane.add(lblServio);

		JLabel lblValor = new JLabel("Valor combinado:");
		lblValor.setHorizontalAlignment(SwingConstants.LEFT);
		lblValor.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblValor.setBounds(303, 346, 125, 24);
		contentPane.add(lblValor);

		JLabel lblHorrio = new JLabel("Hor\u00E1rio:");
		lblHorrio.setHorizontalAlignment(SwingConstants.LEFT);
		lblHorrio.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblHorrio.setBounds(15, 346, 76, 24);
		contentPane.add(lblHorrio);

		cbClientes = new JComboBox<Object>();
		try {
			cbClientes.setModel(new DefaultComboBoxModel(clDao.buscarNomesClientes()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		cbClientes.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		cbClientes.setBounds(112, 276, 347, 24);
		contentPane.add(cbClientes);

		cbServicos = new JComboBox<Object>();
		try {
			cbServicos.setModel(new DefaultComboBoxModel(seDao.buscarNomesServicos()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		cbServicos.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		cbServicos.setBounds(112, 311, 347, 24);
		contentPane.add(cbServicos);

		tfValor = new JTextField();
		tfValor.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		tfValor.setBounds(427, 346, 132, 24);
		contentPane.add(tfValor);
		tfValor.setColumns(10);

		tfHorario = new JTextField();
		tfHorario.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		tfHorario.setColumns(10);
		tfHorario.setBounds(112, 346, 132, 24);
		contentPane.add(tfHorario);

		JLabel lblObservaes = new JLabel("Observa\u00E7\u00F5es:");
		lblObservaes.setEnabled(false);
		lblObservaes.setHorizontalAlignment(SwingConstants.LEFT);
		lblObservaes.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblObservaes.setBounds(15, 381, 98, 24);
		contentPane.add(lblObservaes);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(114, 381, 445, 86);
		contentPane.add(scrollPane_1);

		txObservacoes = new JTextArea();
		txObservacoes.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		txObservacoes.setEnabled(false);
		scrollPane_1.setViewportView(txObservacoes);
		txObservacoes.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		txObservacoes.setWrapStyleWord(true);
		txObservacoes.setLineWrap(true);

		btRegistrar = new JButton("Registrar");
		btRegistrar.setForeground(new Color(255, 255, 255));
		btRegistrar.setBackground(new Color(0, 204, 204));
		btRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!tfHorario.getText().equals("") && cbClientes.getSelectedIndex() != 0 && cbServicos.getSelectedIndex() != 0) {
					try {
						// Atribui??o dos valores dos campos para o objeto agenda
						ag.setCliente(String.valueOf(cbClientes.getSelectedItem()));
						ag.setServico(String.valueOf(cbServicos.getSelectedItem()));
						ag.setValorCombinado(Double.parseDouble(tfValor.getText()));
						ag.setData(AgCalendario.dataEnviada);
						ag.setHora(tfHorario.getText());
						ag.setRealizado(cbRealizado.isSelected());

						// chamada do m?todo de cadastro na classe Dao
						agDao.registrarAgenda(ag);

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
		btRegistrar.setBounds(86, 479, 132, 33);
		contentPane.add(btRegistrar);

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		btnExcluir.setEnabled(false);
		btnExcluir.setForeground(new Color(255, 255, 255));
		btnExcluir.setBackground(new Color(0, 204, 204));
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ag.setCodigo(Integer.parseInt(tfCodigo.getText()));
					agDao.deletarAgenda(ag);
					atualizarTabela();
					limpar();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnExcluir.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btnExcluir.setBounds(228, 479, 132, 33);
		contentPane.add(btnExcluir);

		JButton btnLimpar = new JButton("Limpar");
		btnLimpar.setBackground(new Color(0, 204, 204));
		btnLimpar.setForeground(new Color(255, 255, 255));
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpar();
			}
		});
		btnLimpar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btnLimpar.setBounds(370, 479, 132, 33);
		contentPane.add(btnLimpar);

		tfCodigo = new JTextField();
		tfCodigo.setEditable(false);
		tfCodigo.setEnabled(false);
		tfCodigo.setHorizontalAlignment(SwingConstants.CENTER);
		tfCodigo.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		tfCodigo.setColumns(10);
		tfCodigo.setBounds(417, 241, 0, 24);
		contentPane.add(tfCodigo);

		cbRealizado = new JCheckBox("Realizado?");
		cbRealizado.setBackground(new Color(204, 255, 255));
		cbRealizado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!btRegistrar.isEnabled()) {
					try {
						if (cbRealizado.isSelected()) {
							ag.setRealizado(true);
							ag.setPrecoCobrado(Double.parseDouble(JOptionPane.showInputDialog(null,
									"Qual foi valor cobrado?", "Pre?o", JOptionPane.QUESTION_MESSAGE)));
							ag.setCliente(String.valueOf(cbClientes.getSelectedItem()));
							ag.setServico(String.valueOf(cbServicos.getSelectedItem()));
						} else {
							ag.setRealizado(false);
						}
						ag.setCodigo(Integer.parseInt(tfCodigo.getText()));
						agDao.toogleRealizado(ag);

						JOptionPane.showMessageDialog(null, "Estado da agenda alterado");
						atualizarTabela();
						limpar();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		cbRealizado.setToolTipText("A agenda j\u00E1 foi comprida?");
		cbRealizado.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		cbRealizado.setBounds(427, 237, 132, 28);
		contentPane.add(cbRealizado);

		JButton btNovoCliente = new JButton("Novo");
		btNovoCliente.setEnabled(false);
		btNovoCliente.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		btNovoCliente.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btNovoCliente.setBounds(464, 272, 95, 33);
		contentPane.add(btNovoCliente);

		JButton btNovoServico = new JButton("Novo");
		btNovoServico.setToolTipText("Indispon\u00EDvel nesta vers\u00E3o");
		btNovoServico.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btNovoServico.setEnabled(false);
		btNovoServico.setBounds(464, 306, 95, 33);
		contentPane.add(btNovoServico);
	}

	public void setCamposFromTabela() {
		tfCodigo.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
		cbClientes.setSelectedItem(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
		cbServicos.setSelectedItem(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
		tfValor.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 3)));
		tfHorario.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 4)));
		if (table.getValueAt(table.getSelectedRow(), 5).equals("Sim")) {
			cbRealizado.setSelected(true);
		} else {
			cbRealizado.setSelected(false);
		}
	}

	/**
	 * Limpa campos
	 */
	public void limpar() {
		cbClientes.setSelectedIndex(0);
		cbServicos.setSelectedIndex(0);
		tfValor.setText(null);
		tfHorario.setText(null);
		cbRealizado.setSelected(false);
		txObservacoes.setText(null);
		try {
			tfCodigo.setText(String.valueOf(agDao.RetornarProximoCodigoAgenda()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		btRegistrar.setEnabled(true);
	}

	/**
	 * Pega informa??es do banco e atualiza a tabela
	 */
	public void atualizarTabela() {
		try {
			agenda = agDao.buscarTodos(AgCalendario.dataEnviada);
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
			for (int x = 0; x != agenda.size(); x++) {
				model.addRow((Object[]) agenda.get(x));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
