package br.edu.ifcvideira.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.ifcvideira.beans.Fornecedor;
import br.edu.ifcvideira.utils.Conexao;

public class FornecedorDao {

	/**
	 * Cadastra novo fornecedor
	 * 
	 * @param fo
	 * @throws SQLException
	 * @throws Exception
	 */
	public void cadastrarFornecedor(Fornecedor fo) throws SQLException, Exception {
		try {
			String sql = "INSERT INTO fornecedor (nome, contato) VALUES (?,?)";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, fo.getNome());
			sqlPrep.setString(contador++, fo.getContato());
			sqlPrep.execute();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Atualiza informa??es de um fornecedor
	 * 
	 * @param fo
	 * @throws Exception
	 */
	public void atualizarFornecedor(Fornecedor fo) throws Exception {
		try {
			String sql = "UPDATE fornecedor SET nome=?, contato=? WHERE id_fornecedor=?";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, fo.getNome());
			sqlPrep.setString(contador++, fo.getContato());
			sqlPrep.setInt(contador++, fo.getCodigo());
			sqlPrep.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Exclui um fornecedor selecionado a partir da tabela
	 * 
	 * @param fo
	 * @throws Exception
	 */
	public void deletarFornecedor(Fornecedor fo) throws Exception {
		try {
			String sql = "DELETE FROM fornecedor WHERE id_fornecedor=? ";
			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, fo.getCodigo());
			sqlPrep.execute();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Erro ao excluir fornecedor:\nTalvez o fornecedor j? esteja vinculado a algum registro, ent?o n?o ser? poss?vel exclui-lo!",
					"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Busca todas as informa??es de todos os fornecedores do banco
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object> buscarTodos() throws SQLException, Exception {
		List<Object> fornecedor = new ArrayList<Object>();
		try {
			String sql = "SELECT * FROM fornecedor";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			while (rs.next()) {
				Object[] linha = { rs.getString(1), rs.getString(2), rs.getString(3) };
				fornecedor.add(linha);
			}
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return fornecedor;
	}

	/**
	 * Apenas retorna qual ser? o pr?ximo c?digo de fornecedor
	 * 
	 * @return
	 * @throws Exception
	 */
	public int retornarProximoCodigoFornecedor() throws Exception {
		try {
			String sql = "SELECT MAX(id_fornecedor)+1 AS id_fornecedor FROM fornecedor";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			ResultSet rs = sqlPrep.executeQuery();
			if (rs.next()) {
				return rs.getInt("id_fornecedor");
			} else {
				return 1;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 1;
		}
	}

	/**
	 * Retorna nome dos fornecedores, especialmente criado para preencher comboBoxes
	 * dos frames
	 */
	public String[] buscarNomesFornecedores() throws SQLException, Exception {
		String[] nomeFornecedores = null;
		int qntNomes = 0;
		int i = 1;
		try {
			String sql = "SELECT COUNT(nome) FROM fornecedor";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			// Conta quantos nomes tem na tabela
			while (rs.next()) {
				qntNomes = Integer.parseInt(rs.getString(1));
			}
			rs.close();

			// Declara vetor com a quantidade de nomes recebida
			nomeFornecedores = new String[qntNomes + 1];

			// Adiciona primeiro indice como vazio para exibir nos comboBoxes
			nomeFornecedores[0] = "";

			sql = "SELECT nome FROM fornecedor";
			rs = state.executeQuery(sql);
			// Passa nomes da tabela para o vetor
			while (rs.next()) {
				nomeFornecedores[i] = rs.getString("nome");
				i++;
			}
			rs.close();
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return nomeFornecedores;
	}
}
