package br.edu.ifcvideira.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.ifcvideira.beans.Login;
import br.edu.ifcvideira.beans.Produtos;
import br.edu.ifcvideira.utils.Conexao;

public class ProdutosDao {

	/**
	 * Cadastra um novo produto
	 * 
	 * @param pr
	 * @throws SQLException
	 * @throws Exception
	 */
	public void cadastrarProduto(Produtos pr) throws SQLException, Exception {
		try {
			String sql = "INSERT INTO produtos (nome, descricao, preco, estoque, id_usuario_produtos) VALUES (?,?,?,?,?)";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, pr.getNome());
			sqlPrep.setString(contador++, pr.getDescricao());
			sqlPrep.setDouble(contador++, pr.getPreco());
			sqlPrep.setInt(contador++, pr.getEstoque());
			sqlPrep.setInt(contador++, Login.idUser);
			sqlPrep.execute();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Atualiza informações de um produto
	 * 
	 * @param pr
	 * @throws Exception
	 */
	public void atualizarProduto(Produtos pr) throws Exception {
		try {
			String sql = "UPDATE produtos SET nome=?, descricao=?, preco=?, estoque=? WHERE id_produto=?";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, pr.getNome());
			sqlPrep.setString(contador++, pr.getDescricao());
			sqlPrep.setDouble(contador++, pr.getPreco());
			sqlPrep.setInt(contador++, pr.getEstoque());
			sqlPrep.setInt(contador++, Login.idUser);
			sqlPrep.setInt(contador++, pr.getCodigo());
			sqlPrep.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Deleta um produto
	 * 
	 * @param pr
	 * @throws Exception
	 */
	public void deletarProduto(Produtos pr) throws Exception {
		try {
			String sql = "DELETE FROM produtos WHERE id_produto=? ";
			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, pr.getCodigo());
			sqlPrep.execute();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Busca todos os registros de produtos
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object> buscarTodos() throws SQLException, Exception {
		List<Object> produto = new ArrayList<Object>();
		try {
			String sql = "SELECT * FROM produtos";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			while (rs.next()) {
				Object[] linha = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6) };
				produto.add(linha);
			}
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return produto;
	}

	/**
	 * Apenas retorna qual será o próximo código de produto
	 * 
	 * @return
	 * @throws Exception
	 */
	public int retornarProximoCodigoProduto() throws Exception {
		try {
			String sql = "SELECT MAX(id_produto)+1 AS id_produto FROM produtos";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			ResultSet rs = sqlPrep.executeQuery();
			if (rs.next()) {
				return rs.getInt("id_produto");
			} else {
				return 1;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 1;
		}
	}
}
