package br.edu.ifcvideira.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.ifcvideira.beans.Caixa;
import br.edu.ifcvideira.beans.Login;
import br.edu.ifcvideira.utils.Conexao;

public class CaixaDao {

	/**
	 * Insere um registro na tabela do caixa (entradas de valores)
	 * 
	 * @param cx
	 * @throws SQLException
	 * @throws Exception
	 */
	public void registrarCaixa(Caixa cx) throws SQLException, Exception {
		int id_cliente = 0;
		//Busca o id do fornecedor a partir do nome
		try {
			String sql = "select id_cliente from cliente where nome like ?";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, cx.getCliente());
			ResultSet rs = sqlPrep.executeQuery();
			
			while (rs.next()) {
				id_cliente = Integer.parseInt(rs.getString(1));
			}
			sqlPrep.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		try {
			String sql = "INSERT INTO caixa (valor, descricao, data_registro, id_usuario_caixa, id_cliente_caixa, hora) VALUES (?,?,?,?,?,?)";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setDouble(contador++, cx.getValor());
			sqlPrep.setString(contador++, cx.getDescricao());
			sqlPrep.setString(contador++, cx.getDataRegistro());
			sqlPrep.setInt(contador++, Login.idUser);
			sqlPrep.setInt(contador++, id_cliente);
			sqlPrep.setString(contador++, getTime());
			sqlPrep.execute();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Atualiza um registro do caixa
	 * 
	 * @param cx
	 * @throws Exception
	 */
	public void atualizarRegistro(Caixa cx) throws Exception {
		try {
			String sql = "UPDATE caixa SET valor=?, descricao=?, data_registro=?, id_usuario_caixa=?, hora=? WHERE id_registro=?";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setDouble(contador++, cx.getValor());
			sqlPrep.setString(contador++, cx.getDescricao());
			sqlPrep.setString(contador++, cx.getDataRegistro());
			sqlPrep.setInt(contador++, Login.idUser);
			sqlPrep.setString(contador++, getTime());
			sqlPrep.setInt(contador++, cx.getCodigo());
			sqlPrep.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Exclui um registro do caixa
	 * 
	 * @param cx
	 * @throws Exception
	 */
	public void deletarRegistro(Caixa cx) throws Exception {
		try {
			String sql = "DELETE FROM caixa WHERE id_registro=? ";
			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, cx.getCodigo());
			sqlPrep.execute();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Busca todos os registros do caixa
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object> buscarTodos() throws SQLException, Exception {
		List<Object> servicos = new ArrayList<Object>();
		try {
			String sql = "SELECT id_registro AS 'id', valor, cliente.nome AS 'id_nome', descricao, data_registro AS 'data' FROM caixa " + 
					"INNER JOIN tqmagico.cliente WHERE caixa.id_cliente_caixa=cliente.id_cliente " + 
					"UNION " + 
					"SELECT id_conta, valor, fornecedor.nome, descricao, data_vencimento FROM contas " + 
					"INNER JOIN tqmagico.fornecedor WHERE contas.id_fornecedor=fornecedor.id_fornecedor AND status=1 "
					+ "order by data desc;";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			while (rs.next()) {
				Object[] linha = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5)};
				servicos.add(linha);
			}
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return servicos;
	}

	/**
	 * Retorna todos os registros do dia atual
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object> buscarTodosHoje() throws SQLException, Exception {
		List<Object> servicos = new ArrayList<Object>();
		try {
			String sql = "SELECT id_registro, valor, cliente.nome, descricao, hora FROM caixa "
					+ "inner join tqmagico.cliente where caixa.id_cliente_caixa=cliente.id_cliente and data_registro = CURDATE()";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			while (rs.next()) {
				Object[] linha = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5) };
				servicos.add(linha);
			}
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return servicos;
	}

	/**
	 * Apenas retorna qual será o próximo código de registro
	 * 
	 * @return
	 * @throws Exception
	 */
	public int RetornarProximoCodigoRegistro() throws Exception {
		try {
			String sql = "SELECT MAX(id_registro)+1 AS id_registro FROM caixa";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			ResultSet rs = sqlPrep.executeQuery();
			if (rs.next()) {
				return rs.getInt("id_registro");
			} else {
				return 1;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 1;
		}
	}
	
	/**
	 * Pegar a hora atual
	 */
	private String getTime() { 
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss"); 
		Date date = new Date();
		return dateFormat.format(date); 
	}
}
