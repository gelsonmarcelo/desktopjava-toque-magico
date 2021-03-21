package br.edu.ifcvideira.DAOs;

import java.sql.ResultSet;
import javax.swing.JOptionPane;

import br.edu.ifcvideira.beans.Login;
import br.edu.ifcvideira.utils.Conexao;

public class LoginDao {

	/**
	 * Busca o usuário do banco que possui tal login e senha
	 * 
	 * @param lg
	 * @return true or false
	 */
	public boolean realizarLogin(Login lg) {
		boolean sucesso = false;
		try {
			String sql = "SELECT * FROM usuario WHERE login=? AND senha=? limit 1";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, lg.getLogin());
			sqlPrep.setString(contador++, lg.getSenha());

			ResultSet rs = sqlPrep.executeQuery();

			while (rs.next()) {
				sucesso = true;
				Login.idUser = Integer.parseInt(rs.getString(1));
			}
			Login.senhaUser = lg.getSenha();
			rs.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return sucesso;
	}
}
