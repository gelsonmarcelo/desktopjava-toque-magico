package br.edu.ifcvideira.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.ifcvideira.beans.Usuario;
import br.edu.ifcvideira.utils.Conexao;

public class UsuarioDao {

	/**
	 * Cadastra um novo usuário no banco
	 * 
	 * @param lg
	 * @throws SQLException
	 * @throws Exception
	 */
	public void cadastrarUsuario(Usuario us) throws SQLException, Exception {
		try {
			String sql = "INSERT INTO usuario (login, senha, saldo) VALUES (?,?,?)";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, us.getLogin());
			sqlPrep.setString(contador++, us.getSenha());
			sqlPrep.setDouble(contador++, us.getSaldo());
			sqlPrep.execute();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Atualiza informações de determinado usuário
	 * 
	 * @param lg
	 * @throws Exception
	 */
	public void atualizarUsuario(Usuario us) throws Exception {
		try {
			String sql = "UPDATE usuario SET login=?, senha=?, saldo=? WHERE id_usuario=?";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, us.getLogin());
			sqlPrep.setString(contador++, us.getSenha());
			sqlPrep.setDouble(contador++, us.getSaldo());
			sqlPrep.setInt(contador++, us.getCodigo());
			sqlPrep.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/*
	 * Por enquanto não serão excluidos usuários public void deletarUsuario(Usuario
	 * us) throws Exception{ try{ String sql =
	 * "DELETE FROM usuario WHERE id_usuario=? "; PreparedStatement sqlPrep =
	 * (PreparedStatement) Conexao.conectar().prepareStatement(sql);
	 * sqlPrep.setInt(1, us.getCodigo()); sqlPrep.execute(); } catch (SQLException
	 * e){ JOptionPane.showMessageDialog(null, e.getMessage()); } }
	 */

	/**
	 * Busca todos os usuários cadastrados
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object> buscarTodos() throws SQLException, Exception {
		List<Object> usuarios = new ArrayList<Object>();
		try {
			String sql = "SELECT * FROM usuarios";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			while (rs.next()) {
				Object[] linha = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4) };
				usuarios.add(linha);
			}
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return usuarios;
	}

	/**
	 * Apenas retorna qual será o próximo código de usuário
	 * 
	 * @return
	 * @throws Exception
	 */
	public int RetornarProximoCodigoUsuario() throws Exception {
		try {
			String sql = "SELECT MAX(id_usuario)+1 AS id_usuario FROM usuario";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			ResultSet rs = sqlPrep.executeQuery();
			if (rs.next()) {
				return rs.getInt("id_usuario");
			} else {
				return 1;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 1;
		}
	}
}
