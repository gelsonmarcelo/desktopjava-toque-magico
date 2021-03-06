package br.edu.ifcvideira.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.ifcvideira.beans.Contas;
import br.edu.ifcvideira.beans.Login;
import br.edu.ifcvideira.utils.Conexao;

public class ContasDao {

	/**
	 * Registra uma nova conta
	 * 
	 * @param co
	 * @throws SQLException
	 * @throws Exception
	 */
	public void cadastrarConta(Contas co) throws SQLException, Exception {
		int id_fornecedor = 0;
		//Busca o id do fornecedor a partir do nome
		try {
			String sql = "SELECT id_fornecedor FROM fornecedor WHERE nome LIKE ?";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, co.getFornecedor());
			ResultSet rs = sqlPrep.executeQuery();
			
			while (rs.next()) {
				id_fornecedor = Integer.parseInt(rs.getString(1));
			}
			sqlPrep.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		//Insere as informa??es
		try {
			String sql = "INSERT INTO contas (descricao, data_vencimento, valor, status, id_usuario_contas, id_fornecedor) VALUES (?,?,?,?,?,?)";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, co.getDescricao());
			sqlPrep.setString(contador++, co.getDataVencimento());
			sqlPrep.setDouble(contador++, co.getValor());
			sqlPrep.setBoolean(contador++, co.isStatus());
			sqlPrep.setInt(contador++, Login.idUser);
			sqlPrep.setInt(contador++, id_fornecedor);
			sqlPrep.execute();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Altera o estado da conta (pago ou n?o pago)
	 * 
	 * @param co
	 * @throws Exception
	 */
	public void atualizarEstadoConta(Contas co) throws Exception {
		try {
			String sql = "UPDATE contas SET status=? WHERE id_conta=?";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setBoolean(contador++, co.isStatus());
			sqlPrep.setInt(contador++, co.getCodigo());
			sqlPrep.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Atualizar informa??es de um registro de conta
	 * 
	 * @param co
	 * @throws Exception
	 */
	public void atualizarConta(Contas co) throws Exception {
		try {
			String sql = "UPDATE contas SET descricao=?, data_vencimento=?, valor=?, status=?, id_usuario_contas=? WHERE id_conta=?";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, co.getDescricao());
			sqlPrep.setString(contador++, co.getDataVencimento());
			sqlPrep.setDouble(contador++, co.getValor());
			sqlPrep.setBoolean(contador++, co.isStatus());
			sqlPrep.setInt(contador++, Login.idUser);
			sqlPrep.setInt(contador++, co.getCodigo());
			sqlPrep.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Exclui um registro de conta
	 * 
	 * @param co
	 * @throws Exception
	 */
	public void deletarConta(Contas co) throws Exception {
		try {
			String sql = "DELETE FROM contas WHERE id_conta=? ";
			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, co.getCodigo());
			sqlPrep.execute();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Busca todas as informa??es da tabela de contas
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object> buscarTodos() throws SQLException, Exception {
		List<Object> contas = new ArrayList<Object>();
		try {
			String status;
			String sql = "SELECT id_conta, valor, fornecedor.nome, descricao, data_vencimento, status FROM contas " + 
					"inner join tqmagico.fornecedor where contas.id_fornecedor=fornecedor.id_fornecedor";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			while (rs.next()) {
				// Altera o status para sim e n?o ao inv?s de 0 e 1 registrados no banco

				if (rs.getString(6).equals("1")) {
					status = "Sim";
				} else {
					status = "N?o";
				}
				Object[] linha = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),	status};
				contas.add(linha);
			}
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return contas;
	}

	/**
	 * Apenas retorna qual ser? o pr?ximo c?digo de conta
	 * 
	 * @return
	 * @throws Exception
	 */
	public int RetornarProximoCodigoConta() throws Exception {
		try {
			String sql = "SELECT MAX(id_conta)+1 AS id_conta FROM contas";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			ResultSet rs = sqlPrep.executeQuery();
			if (rs.next()) {
				return rs.getInt("id_conta");
			} else {
				return 1;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 1;
		}
	}
}
