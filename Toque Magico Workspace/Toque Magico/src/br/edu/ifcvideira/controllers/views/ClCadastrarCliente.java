package br.edu.ifcvideira.controllers.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

import com.toedter.calendar.JDateChooser;

import br.edu.ifcvideira.DAOs.ClienteDao;
import br.edu.ifcvideira.DAOs.TelefoneDao;
import br.edu.ifcvideira.beans.Cliente;
import br.edu.ifcvideira.beans.Telefone;
import javax.swing.ImageIcon;

public class ClCadastrarCliente extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JFormattedTextField tfCpf;
	private JTextField tfNome;
	private JTextArea txObservacao;
	private JTextField tfLogradouro;
	private JFormattedTextField tfContato;
	private JFormattedTextField tfContato2;
	private JFormattedTextField tfContato3;
	private JTextArea txAnamnese;
	private JDateChooser dcNascimento;
	SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");
	Cliente cl = new Cliente();
	Telefone tf = new Telefone();
	ClienteDao clDao = new ClienteDao();
	TelefoneDao tfDao = new TelefoneDao();
	int contClicksAdd = 0;

	public ClCadastrarCliente() {
		super("Cadastro de Clientes", false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable
		setFrameIcon(new ImageIcon(ClCadastrarCliente.class.getResource("/br/edu/ifcvideira/imgs/ico16.png")));
		getContentPane().setBackground(new Color(204, 255, 255));

		// ...Create the GUI and put it in the window...

		// ...Then set the window size...
		setSize(1130, 710);

		getContentPane().setLayout(null);
		
		tfContato3 = new JFormattedTextField(mascaraField("(##) #####-####"));
		tfContato3.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfContato3.setColumns(10);
		tfContato3.setBackground(Color.WHITE);
		tfContato3.setBounds(695, 252, 228, 32);
		getContentPane().add(tfContato3);
		tfContato3.setVisible(false);
		
		tfContato2 = new JFormattedTextField(mascaraField("(##) #####-####"));
		tfContato2.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfContato2.setColumns(10);
		tfContato2.setBackground(Color.WHITE);
		tfContato2.setBounds(447, 252, 228, 32);
		getContentPane().add(tfContato2);
		tfContato2.setVisible(false);;

		JLabel lblCliente = new JLabel("Cadastro de Clientes");
		lblCliente.setForeground(new Color(51, 153, 204));
		lblCliente.setFont(new Font("Trebuchet MS", Font.PLAIN, 30));
		lblCliente.setHorizontalAlignment(SwingConstants.CENTER);
		lblCliente.setBounds(0, 0, 1114, 70);
		getContentPane().add(lblCliente);

		JLabel lblCpf = new JLabel("CPF:");
		lblCpf.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblCpf.setBounds(79, 166, 120, 32);
		getContentPane().add(lblCpf);

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblNome.setBounds(79, 123, 120, 32);
		getContentPane().add(lblNome);

		JLabel lblDescrio = new JLabel("Observa\u00E7\u00E3o:");
		lblDescrio.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblDescrio.setBounds(79, 461, 120, 32);
		getContentPane().add(lblDescrio);

		tfCpf = new JFormattedTextField(mascaraField("###.###.###-##"));
		tfCpf.setBackground(Color.WHITE);
		tfCpf.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfCpf.setBounds(209, 166, 247, 32);
		getContentPane().add(tfCpf);
		tfCpf.setColumns(10);

		tfNome = new JTextField();
		tfNome.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfNome.setColumns(10);
		tfNome.setBounds(209, 123, 803, 32);
		getContentPane().add(tfNome);

		JScrollPane scrollPaneDescricao = new JScrollPane();
		scrollPaneDescricao.setBounds(209, 465, 803, 120);
		getContentPane().add(scrollPaneDescricao);

		txObservacao = new JTextArea();
		scrollPaneDescricao.setViewportView(txObservacao);
		txObservacao.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));
		txObservacao.setWrapStyleWord(true);
		txObservacao.setLineWrap(true);

		JButton btCadastrar = new JButton("Cadastrar");
		btCadastrar.setForeground(new Color(255, 255, 255));
		btCadastrar.setBackground(new Color(0, 204, 204));
		btCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (validado()) {
					try {
						Date dataHj = new Date();

						// atribui??o dos valores dos campos para o objeto cliente
						cl.setNome(tfNome.getText());
						cl.setCpf(tfCpf.getText());
						cl.setDataNascimento(formatador.format(dcNascimento.getDate()));
						cl.setObservacao(txObservacao.getText());
						cl.setLogradouro(tfLogradouro.getText());
						cl.setAnamnese(txAnamnese.getText());
						cl.setDataCadastro(formatador.format(dataHj));
						tf.setId_cliente(clDao.RetornarProximoCodigoCliente());
						cl.setSaldo(0);

						// chamada do m?todo de cadastro na classe Dao
						clDao.cadastrarCliente(cl);
						//Cadastrar o(s) telefone(s)
						tf.setNumero(tfContato.getText());
						tfDao.cadastrarTelefone(tf);
						
						//Verifica se tem algo escrito no telefone
						if (!tfContato2.getText().equals("(  )      -    ")) {
							tf.setNumero(tfContato2.getText());
							tfDao.cadastrarTelefone(tf);
						}
						if (!tfContato3.getText().equals("(  )      -    ")) {
							tf.setNumero(tfContato3.getText());
							tfDao.cadastrarTelefone(tf);
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
					limpar();
				}
			}
		});
		btCadastrar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btCadastrar.setBounds(345, 600, 173, 32);
		getContentPane().add(btCadastrar);

		JButton btLimpar = new JButton("Limpar");
		btLimpar.setBackground(new Color(0, 204, 204));
		btLimpar.setForeground(new Color(255, 255, 255));
		btLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpar();
			}
		});
		btLimpar.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btLimpar.setBounds(577, 600, 173, 32);
		getContentPane().add(btLimpar);

		JLabel lblNascimento = new JLabel("Nascimento:");
		lblNascimento.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblNascimento.setBounds(695, 166, 132, 32);
		getContentPane().add(lblNascimento);

		tfLogradouro = new JTextField();
		tfLogradouro.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfLogradouro.setColumns(10);
		tfLogradouro.setBounds(209, 209, 803, 32);
		getContentPane().add(tfLogradouro);

		JLabel lblLogradouro = new JLabel("Logradouro:");
		lblLogradouro.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblLogradouro.setBounds(79, 209, 120, 32);
		getContentPane().add(lblLogradouro);

		JLabel lblContato = new JLabel("Contato:");
		lblContato.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblContato.setBounds(79, 252, 120, 32);
		getContentPane().add(lblContato);

		tfContato = new JFormattedTextField(mascaraField("(##) #####-####"));
		tfContato.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		tfContato.setColumns(10);
		tfContato.setBackground(Color.WHITE);
		tfContato.setBounds(209, 252, 228, 32);
		getContentPane().add(tfContato);

		JButton btAdd = new JButton("Add+");
		btAdd.setForeground(new Color(255, 255, 255));
		btAdd.setBackground(new Color(0, 204, 204));
		btAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Altera componentes para mostrar outros campos de contato
				contClicksAdd++;
				if(contClicksAdd==1) {
					btAdd.setBounds(695, 252, 91, 32);
					tfContato2.setVisible(true);
				}else if(contClicksAdd==2){
					btAdd.setBounds(935, 252, 80, 32);
					tfContato3.setVisible(true);
				}
			}
		});
		btAdd.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		btAdd.setBounds(447, 252, 91, 32);

		getContentPane().add(btAdd);

		JLabel lblAnamnese = new JLabel("Anamnese:");
		lblAnamnese.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblAnamnese.setBounds(80, 295, 120, 32);
		getContentPane().add(lblAnamnese);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(211, 296, 801, 158);
		getContentPane().add(scrollPane);

		txAnamnese = new JTextArea();
		scrollPane.setViewportView(txAnamnese);
		txAnamnese.setWrapStyleWord(true);
		txAnamnese.setLineWrap(true);
		txAnamnese.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));

		dcNascimento = new JDateChooser();
		dcNascimento.getCalendarButton().setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		dcNascimento.setBounds(837, 166, 175, 32);
		getContentPane().add(dcNascimento);
		
		JLabel lbBackground = new JLabel("");
		lbBackground.setIcon(new ImageIcon(CxContas.class.getResource("/br/edu/ifcvideira/imgs/background2.png")));
		lbBackground.setBounds(0, 0, 1320, 721);
		getContentPane().add(lbBackground);
	}

	/**
	 * Limpa os campos
	 */
	public void limpar() {
		tfCpf.setText(null);
		tfNome.setText(null);
		dcNascimento.setDate(null);
		txObservacao.setText(null);
		tfLogradouro.setText(null);
		tfContato.setText(null);
		tfContato2.setText(null);
		tfContato3.setText(null);
		txAnamnese.setText(null);
		txObservacao.setText(null);
	}

	/**
	 * M?scara dos fields
	 */
	public static MaskFormatter mascaraField(String Mascara) {
		MaskFormatter F_Mascara = new MaskFormatter();
		try {
			F_Mascara.setMask(Mascara); // Atribui a m?scara
			F_Mascara.setPlaceholderCharacter(' '); // Caracter para preencimento
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return F_Mascara;
	}
	
	/**
	 * #### (Fazer outras valida??es) Realiza a valida??o dos campos
	 * De forma alguma deve ser cadastrado cliente sem um telefone sen?o n?o mostrar? na tabela
	 */
	public boolean validado() {
		if (!tfContato.getText().equals("(  )      -    ") && !tfNome.getText().equals("") && !tfCpf.getText().equals("   .   .   -  ") && !tfLogradouro.getText().equals("") && dcNascimento.getDate() != null){
			return true;
		}else {
			JOptionPane.showMessageDialog(null, "Por favor preencha todas as informa??es necess?rias", "Aviso", JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}
}