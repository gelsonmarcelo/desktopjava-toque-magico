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

import br.edu.ifcvideira.beans.Agenda;
import br.edu.ifcvideira.beans.Caixa;
import br.edu.ifcvideira.beans.Login;
import br.edu.ifcvideira.utils.Conexao;

public class AgendaDao {
	CaixaDao cxDao = new CaixaDao();
	Caixa cx = new Caixa();
	/**
	 * Registra um novo evento/Marca??o de hor?rio no calend?rio (agenda)
	 * 
	 * @param ag
	 * @throws SQLException
	 * @throws Exception
	 */
	public void registrarAgenda(Agenda ag) throws SQLException, Exception {
		int id_cliente = 0;
		int id_servico = 0;
		// Busca o id's a partir dos nomes
		try {
			String sql = "SELECT id_cliente FROM cliente WHERE nome LIKE ?";
			String sql2 = "SELECT id_servico FROM servicos WHERE nome LIKE ?";
			// Pegando o id do cliente
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, ag.getCliente());
			ResultSet rs = sqlPrep.executeQuery();

			while (rs.next()) {
				id_cliente = Integer.parseInt(rs.getString(1));
			}
			// Pegando o id do servico
			sqlPrep = Conexao.conectar().prepareStatement(sql2);
			sqlPrep.setString(1, ag.getServico());
			rs = sqlPrep.executeQuery();

			while (rs.next()) {
				id_servico = Integer.parseInt(rs.getString(1));
			}
			sqlPrep.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

		// Insere valores da tabela agenda
		try {
			String sql = "INSERT INTO agenda (data, hora, valor_combinado, id_cliente_agenda, id_usuario_agenda, realizado) VALUES (?,?,?,?,?,?)";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, ag.getData());
			sqlPrep.setString(contador++, ag.getHora());
			sqlPrep.setDouble(contador++, ag.getValorCombinado());
			sqlPrep.setInt(contador++, id_cliente);
			sqlPrep.setInt(contador++, Login.idUser);
			sqlPrep.setBoolean(contador++, ag.isRealizado());
			sqlPrep.execute();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

		// Insere valores da tabela de relacionamento agenda_servicos
		try {
			String sql = "INSERT INTO agenda_servicos VALUES (null,?,?)";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, RetornarProximoCodigoAgenda()-1); //-1 por que esse deve ser o atual, j? que ja foi criado
			sqlPrep.setInt(2, id_servico);
			sqlPrep.execute();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Atualiza um evento registrado
	 * ####AJUSTAR PARA ATUALIZAR SERVI?O TAMB?M
	 * ####DEVE-SE PODER ESCOLHER MAIS DE UM SERVI?O POR AGENDA, DE ACORDO COM O BANCO 
	 * @param ag
	 * @throws Exception
	 */
	public void atualizarAgenda(Agenda ag) throws Exception {
		int id_cliente = 0;
		// Busca o id do cliente a partir do nome
		try {
			String sql = "SELECT id_cliente FROM cliente WHERE nome LIKE ?";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, ag.getCliente());
			ResultSet rs = sqlPrep.executeQuery();

			while (rs.next()) {
				id_cliente = Integer.parseInt(rs.getString(1));
			}
			sqlPrep.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		try {
			String sql = "UPDATE agenda SET data=?, hora=?, valor_combinado=?, id_cliente_agenda=?, realizado=?, id_usuario_agenda=? WHERE id_evento=?";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, ag.getData());
			sqlPrep.setString(contador++, ag.getHora());
			sqlPrep.setDouble(contador++, ag.getValorCombinado());
			sqlPrep.setInt(contador++, id_cliente);
			sqlPrep.setBoolean(contador++, ag.isRealizado());
			sqlPrep.setInt(contador++, Login.idUser);
			sqlPrep.setInt(contador++, ag.getCodigo());
			sqlPrep.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Exclui um evento, selecionado a partir da tabela
	 * 
	 * @param ag
	 * @throws Exception
	 */
	public void deletarAgenda(Agenda ag) throws Exception {
		try {
			String sql = "DELETE FROM agenda WHERE id_evento=? ";
			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, ag.getCodigo());
			sqlPrep.execute();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Busca os eventos do dia que foi selecionado
	 * 
	 * @param data
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object> buscarTodos(String data) throws SQLException, Exception {
		List<Object> agenda = new ArrayList<Object>();
		try {
			String sql = "SELECT id_evento, cliente.nome as 'cliente', servicos.nome as 'servico', valor_combinado, hora, realizado " + 
					"FROM agenda " + 
					"INNER JOIN cliente ON agenda.id_cliente_agenda=cliente.id_cliente " + 
					"INNER JOIN agenda_servicos ON agenda.id_evento=agenda_servicos.id_evento_as " + 
					"INNER JOIN servicos ON agenda_servicos.id_servico_as=servicos.id_servico " + 
					"WHERE data LIKE ?";

			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, data);

			ResultSet rs = sqlPrep.executeQuery();
			
			//Altera resultado para sim ou n?o ao inv?s de mostrar 0 e 1 como no banco
			String status="";			
			while (rs.next()) {
				if (rs.getString(6).equals("1")) {
					status = "Sim";
				} else {
					status = "N?o";
				}
				Object[] linha = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), status};
				agenda.add(linha);
			}
			sqlPrep.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return agenda;
	}

	/**
	 * Apenas retorna qual ser? o pr?ximo c?digo de agenda
	 * 
	 * @return
	 * @throws Exception
	 */
	public int RetornarProximoCodigoAgenda() throws Exception {
		try {
			String sql = "SELECT MAX(id_evento)+1 AS id_evento FROM agenda";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			ResultSet rs = sqlPrep.executeQuery();
			if (rs.next()) {
				return rs.getInt("id_evento");
			} else {
				return 1;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 1;
		}
	}

	/**
	 * Altera o estado da agenda (realizado ou n?o) e registra no caixa a entrada
	 * ####CONSIDERANDO PAGAMENTOS SOMENTE A VISTA POR ENQUANTO (SEMPRE QUE REALIZADO REGISTRA ENTRADA)
	 * @param ag
	 */
	public void toogleRealizado(Agenda ag) {

		//Altera estado de realizado
		try {
			String sql = "UPDATE agenda SET realizado=? WHERE id_evento=?";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setBoolean(contador++, ag.isRealizado());
			sqlPrep.setInt(contador++, ag.getCodigo());
			sqlPrep.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		//Registra entrada caixa
		if (ag.isRealizado()) {
			try {
				//Atribui??o dos valores dos campos para o objeto
				cx.setValor(ag.getPrecoCobrado());
				cx.setDescricao(ag.getServico());
				cx.setDataRegistro(getDate());
				cx.setCliente(ag.getCliente());

				//Chamada do m?todo de cadastro na classe Dao
				cxDao.registrarCaixa(cx);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
			
			//Insere valores da tabela de relacionamento agenda_caixa
			try {
				String sql = "INSERT INTO agenda_caixa VALUES (null,?,?,?)";
				java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
				sqlPrep.setInt(1, ag.getCodigo());
				sqlPrep.setInt(2, cxDao.RetornarProximoCodigoRegistro()-1); //-1 pra pegar c?digo atual j? registrado
				sqlPrep.setDouble(3, ag.getPrecoCobrado());
				sqlPrep.execute();

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	/**
	 * Pega a data atual
	 * @return
	 */
	public String getDate() { 
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Date date = new Date(); 
		return dateFormat.format(date); 
	}
}
