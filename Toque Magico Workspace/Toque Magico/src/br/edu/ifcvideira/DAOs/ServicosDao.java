package br.edu.ifcvideira.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.ifcvideira.beans.Servicos;
import br.edu.ifcvideira.utils.Conexao;

public class ServicosDao {

	/**
	 * Cadastra um novo serviço
	 * 
	 * @param se
	 * @throws SQLException
	 * @throws Exception
	 */
	public void cadastrarServico(Servicos se) throws SQLException, Exception {
		try {
			String sql = "INSERT INTO servicos (nome, descricao, preco) VALUES (?,?,?)";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, se.getNome());
			sqlPrep.setString(contador++, se.getDescricao());
			sqlPrep.setDouble(contador++, se.getPreco());
			sqlPrep.execute();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Atualiza determinado serviço
	 * 
	 * @param se
	 * @throws Exception
	 */
	public void atualizarServico(Servicos se) throws Exception {
		try {
			String sql = "UPDATE servicos SET nome=?, descricao=?, preco=? WHERE id_servico=?";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, se.getNome());
			sqlPrep.setString(contador++, se.getDescricao());
			sqlPrep.setDouble(contador++, se.getPreco());
			sqlPrep.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Deleta um serviço
	 * 
	 * @param se
	 * @throws Exception
	 */
	public void deletarServico(Servicos se) throws Exception {
		try {
			String sql = "DELETE FROM servicos WHERE id_servico=? ";
			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, se.getCodigo());
			sqlPrep.execute();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Busca todos os serviços cadastrados
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object> buscarTodos() throws SQLException, Exception {
		List<Object> servicos = new ArrayList<Object>();
		try {
			String sql = "SELECT * FROM servicos";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			while (rs.next()) {
				Object[] linha = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4) };
				servicos.add(linha);
			}
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return servicos;
	}

	/**
	 * Apenas retorna qual será o próximo código de serviço
	 * 
	 * @return
	 * @throws Exception
	 */
	public int retornarProximoCodigoServico() throws Exception {
		try {
			String sql = "SELECT MAX(id_servico)+1 AS id_servico FROM servicos";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			ResultSet rs = sqlPrep.executeQuery();
			if (rs.next()) {
				return rs.getInt("id_servico");
			} else {
				return 1;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 1;
		}
	}

	/**
	 * Retorna nome dos servicos, especialmente criado para preencher comboBoxes dos
	 * frames
	 */
	public String[] buscarNomesServicos() throws SQLException, Exception {
		String[] nomeServicos = null;
		int qntNomes = 0;
		int i = 1;
		try {
			String sql = "SELECT COUNT(nome) FROM servicos";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			// Conta quantos nomes tem na tabela
			while (rs.next()) {
				qntNomes = Integer.parseInt(rs.getString(1));
			}
			rs.close();

			// Declara vetor com a quantidade de nomes recebida
			nomeServicos = new String[qntNomes+1];
			
			//Adiciona primeiro indice como vazio para exibir nos comboBoxes
			nomeServicos[0] = "";
			
			sql = "SELECT nome FROM servicos";
			rs = state.executeQuery(sql);
			// Passa nomes da tabela para o vetor
			while (rs.next()) {
				nomeServicos[i] = rs.getString("nome");
				i++;
			}
			rs.close();
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return nomeServicos;
	}
}
