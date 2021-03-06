package br.edu.ifcvideira.controllers.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import br.edu.ifcvideira.DAOs.ClienteDao;
import br.edu.ifcvideira.DAOs.LoginDao;
import br.edu.ifcvideira.beans.Login;

public class LgLogin extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfUser;
	private JPasswordField psSenha;
	Login lg = new Login();
	LoginDao lgDao = new LoginDao();
	ClienteDao clDao = new ClienteDao();

	public LgLogin() {
		super("Login", false, // resizable
				false, // closable
				false, // maximizable
				false);
		getContentPane().setBackground(new Color(204, 255, 255));
		setFrameIcon(new ImageIcon(ClCadastrarCliente.class.getResource("/br/edu/ifcvideira/imgs/ico16.png")));
		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...
		setSize(570, 327);

		getContentPane().setLayout(null);

		JLabel lblLoginToqueMgico = new JLabel("Login Toque M\u00E1gico 1.0");
		lblLoginToqueMgico.setForeground(new Color(51, 153, 204));
		lblLoginToqueMgico.setBounds(0, 0, 554, 83);
		lblLoginToqueMgico.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoginToqueMgico.setFont(new Font("Trebuchet MS", Font.PLAIN, 22));
		getContentPane().add(lblLoginToqueMgico);

		JLabel lblUsurio = new JLabel("Usu\u00E1rio:");
		lblUsurio.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblUsurio.setBounds(91, 94, 85, 26);
		getContentPane().add(lblUsurio);

		tfUser = new JTextField();
		tfUser.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		tfUser.setBounds(172, 94, 308, 26);
		getContentPane().add(tfUser);
		tfUser.setColumns(10);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblSenha.setBounds(91, 147, 85, 26);
		getContentPane().add(lblSenha);

		psSenha = new JPasswordField();
		psSenha.setBounds(172, 147, 308, 26);
		getContentPane().add(psSenha);

		JButton btLogin = new JButton("Entrar");
		btLogin.setForeground(new Color(255, 255, 255));
		btLogin.setBackground(new Color(0, 204, 204));
		btLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lg.setLogin(tfUser.getText());
				lg.setSenha(String.valueOf(psSenha.getPassword()));

				/**
				 * Para facilitar execu??o r?pida em testes: Comentar if e setar idUser = 1
				 */
				if (lgDao.realizarLogin(lg)) {
					MainInternalFrame.menuBar.setVisible(true);
					clDao.checkAniversariantes();
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Usu?rio ou senha incorretos", "Acesso Negado",
							JOptionPane.WARNING_MESSAGE);
					limpar();
				}
			}
		});
		btLogin.setFont(new Font("Trebuchet MS", Font.PLAIN, 23));
		btLogin.setBounds(91, 203, 151, 37);
		getContentPane().add(btLogin);

		JButton btCancelar = new JButton("Sair");
		btCancelar.setForeground(new Color(255, 255, 255));
		btCancelar.setBackground(new Color(0, 204, 204));
		btCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainInternalFrame.menuBar.setVisible(false);
				MainInternalFrame.quit();
			}
		});
		btCancelar.setFont(new Font("Trebuchet MS", Font.PLAIN, 23));
		btCancelar.setBounds(329, 203, 151, 37);
		getContentPane().add(btCancelar);

		JButton btAtualizar = new JButton("Atualizar");
		btAtualizar.setForeground(new Color(255, 255, 255));
		btAtualizar.setBackground(new Color(0, 204, 204));
		btAtualizar.setEnabled(false);
		btAtualizar.setToolTipText("Alterar: nome de usu\u00E1rio, senha ou saldo");
		btAtualizar.setFont(new Font("Trebuchet MS", Font.PLAIN, 23));
		btAtualizar.setBounds(213, 277, 151, 37);
		btAtualizar.setVisible(false);
		getContentPane().add(btAtualizar);

		JLabel lbImg = new JLabel("");
		lbImg.setIcon(new ImageIcon(LgLogin.class.getResource("/br/edu/ifcvideira/imgs/loginImg.png")));
		lbImg.setHorizontalAlignment(SwingConstants.CENTER);
		lbImg.setBounds(170, 131, 228, 173);
		getContentPane().add(lbImg);
	}

	/**
	 * Limpa campos
	 */
	private void limpar() {
		tfUser.setText(null);
		psSenha.setText(null);
	}

	/**
	 * Centraliza janela
	 */
	public void centralizarJanela() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		setLocation(x, y);
	}
}