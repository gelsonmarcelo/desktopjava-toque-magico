package br.edu.ifcvideira.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.ifcvideira.beans.Telefone;
import br.edu.ifcvideira.utils.Conexao;

public class TelefoneDao {
	// Armazena a quantidade de telefones que o ?ltimo cliente acessado pelo m?todo
	// buscarTelefones() possui,
	public static int qntTelefones;

	/**
	 * Registra telefones (clientes)
	 * 
	 * @param te
	 * @throws SQLException
	 * @throws Exception
	 */
	public void cadastrarTelefone(Telefone te) throws SQLException, Exception {
		try {
			String sql = "INSERT INTO telefone (id_cliente_telefone, numero) VALUES (?,?)";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setInt(contador++, te.getId_cliente());
			sqlPrep.setString(contador++, te.getNumero());
			sqlPrep.execute();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Atualiza determinado telefone
	 * 
	 * @param te
	 * @throws Exception
	 */
	public void atualizarTelefone(Telefone te) throws Exception {
		try {
			String sql = "UPDATE telefone SET numero=? WHERE id_telefone=?";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, te.getNumero());
			sqlPrep.setInt(contador++, te.getCodigo());
			sqlPrep.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Exclui um telefone
	 * 
	 * @param te
	 * @throws Exception
	 */
	public void deletarTelefone(Telefone te) throws Exception {
		// Verifica se tem mais de um telefone para excluir
		if (qntTelefones > 1) {
			// Neste caso h? mais de um n?mero de telefone: Exclui
			try {
				String sql = "DELETE FROM telefone WHERE id_telefone=?";
				PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
				sqlPrep.setInt(1, te.getCodigo());
				sqlPrep.execute();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		} else {// Sen?o ? o ?ltimo telefone
			JOptionPane.showMessageDialog(null,
					"Imposs?vel excluir o ?ltimo n?mero de telefone do cliente, voc? pode atualiza-lo", "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Retorna todas os telefones do cliente passado como par?metro
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object> buscarTelefones(int codigoCliente) throws SQLException, Exception {
		List<Object> telefones = new ArrayList<Object>();
		try {
			String sql = "SELECT id_telefone, numero FROM telefone WHERE id_cliente_telefone=?";
			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, codigoCliente);
			ResultSet rs = sqlPrep.executeQuery();

			while (rs.next()) {
				Object[] linha = { rs.getString(1), rs.getString(2) };
				telefones.add(linha);
			}
			sqlPrep.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		qntTelefones = telefones.size();
		return telefones;
	}

	/**
	 * Retorna um n?mero do cliente para backup quando for feita tentativa de
	 * exclus?o do cliente
	 * 
	 * @param codigoCliente
	 * @return backupTelefone
	 * @throws SQLException
	 * @throws Exception
	 */
	public String backupTelefone(int codigoCliente) throws SQLException, Exception {
		String backupTelefone = "";
		try {
			String sql = "SELECT numero FROM telefone WHERE id_cliente_telefone=? ORDER BY id_telefone LIMIT 1";
			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, codigoCliente);
			ResultSet rs = sqlPrep.executeQuery();

			while (rs.next()) {
				backupTelefone = rs.getString(1);
			}
			sqlPrep.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return backupTelefone;
	}

	/**
	 * ####VERIFICAR public int RetornarProximoCodigoTelefone() throws Exception {
	 * try{ String sql ="SELECT MAX(id_conta)+1 AS id_conta FROM contas";
	 * PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
	 * ResultSet rs = sqlPrep.executeQuery(); if (rs.next()){ return
	 * rs.getInt("id_conta"); }else{ return 1; } } catch(Exception e) {
	 * JOptionPane.showMessageDialog(null, e.getMessage()); return 1; } }
	 */
}
