package br.edu.ifcvideira.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.ifcvideira.beans.Cliente;
import br.edu.ifcvideira.beans.Login;
import br.edu.ifcvideira.beans.Telefone;
import br.edu.ifcvideira.utils.Conexao;

public class ClienteDao {
	Telefone te = new Telefone();
	TelefoneDao teDao = new TelefoneDao();
	AgendaDao agDao = new AgendaDao();

	/**
	 * Cadastra um novo cliente na tabela
	 * 
	 * @param cl
	 * @throws SQLException
	 * @throws Exception
	 */
	public void cadastrarCliente(Cliente cl) throws SQLException, Exception {
		try {
			String sql = "INSERT INTO cliente (nome, cpf, data_nascimento, logradouro, anamnese, observacao, data_cadastro, saldo, id_usuario_cliente) VALUES (?,?,?,?,?,?,?,?,?)";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, cl.getNome()); // ou no lugar de "contador++" 1, 2, 3...
			sqlPrep.setString(contador++, cl.getCpf());
			sqlPrep.setString(contador++, cl.getDataNascimento());
			sqlPrep.setString(contador++, cl.getLogradouro());
			sqlPrep.setString(contador++, cl.getAnamnese());
			sqlPrep.setString(contador++, cl.getObservacao());
			sqlPrep.setString(contador++, cl.getDataCadastro());
			sqlPrep.setDouble(contador++, cl.getSaldo());
			sqlPrep.setDouble(contador++, Login.idUser);
			sqlPrep.execute();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Atualizar informações de um cliente
	 * 
	 * @param cl
	 * @throws Exception
	 */
	public void atualizarCliente(Cliente cl) throws Exception {
		try {
			String sql = "UPDATE cliente SET nome=?, cpf=?, data_nascimento=?, logradouro=?, anamnese=?, observacao=?, saldo=?, id_usuario_cliente=? WHERE id_cliente=?";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, cl.getNome());
			sqlPrep.setString(contador++, cl.getCpf());
			sqlPrep.setString(contador++, cl.getDataNascimento());
			sqlPrep.setString(contador++, cl.getLogradouro());
			sqlPrep.setString(contador++, cl.getAnamnese());
			sqlPrep.setString(contador++, cl.getObservacao());
			sqlPrep.setDouble(contador++, cl.getSaldo());
			sqlPrep.setInt(contador++, Login.idUser);
			sqlPrep.setInt(contador++, cl.getCodigo());
			sqlPrep.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Infelizmente ocorreu um erro inesperado, se persistir contate o desenvolvedor:\n" + e.getMessage(),
					"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Exclui um cliente do banco ####Fazer backup de todos os telefones do cliente
	 * 
	 * @param cl
	 * @throws Exception
	 */
	public void deletarCliente(Cliente cl) throws Exception {
		String backupTelefone = "";
		try {
			// Backup do número caso não consiga excluir o cliente
			backupTelefone = teDao.backupTelefone(cl.getCodigo());

			// Exclui o número de telefone, pois se o usuário ainda está vinculado apenas a
			// esta tabela, pode-se exclí-lo
			String sql = "DELETE FROM telefone WHERE id_cliente_telefone=?";
			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, cl.getCodigo());
			sqlPrep.execute();

			// Exclui o cliente
			String sql2 = "DELETE FROM cliente WHERE id_cliente=?";
			PreparedStatement sqlPrep2 = (PreparedStatement) Conexao.conectar().prepareStatement(sql2);
			sqlPrep2.setInt(1, cl.getCodigo());
			sqlPrep2.execute();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"O cliente não pode ser excluído, provavelmente ele está vinculado a algum histórico de registro.",
					"Aviso", JOptionPane.WARNING_MESSAGE);
			// Se não foi possível deletar, adiciona um número qualquer novamente
			te.setId_cliente(cl.getCodigo());
			te.setNumero(backupTelefone);
			teDao.cadastrarTelefone(te);
		}
	}

	/**
	 * Retorna todas as informações de clientes da tabela
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object> buscarTodos() throws SQLException, Exception {
		List<Object> cliente = new ArrayList<Object>();
		try {
			String sql = "SELECT id_cliente, nome, telefone.numero, cpf, observacao, saldo, data_nascimento "
					+ "FROM cliente INNER JOIN telefone " 
					+ "ON cliente.id_cliente=telefone.id_cliente_telefone "
					+ "GROUP BY id_cliente " 
					+ "ORDER BY id_cliente DESC";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			while (rs.next()) {
				Object[] linha = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7) };
				cliente.add(linha);
			}
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return cliente;
	}

	/**
	 * Retorna todas as informações de clientes da tabela possibilitando recuperar
	 * objetos isolados do Array
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public String[] buscarInformacoes(int codigoCliente) throws SQLException, Exception {
		String[] cliente = new String[10];
		try {
			String sql = "SELECT * FROM cliente WHERE id_cliente=?";
			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, codigoCliente);
			ResultSet rs = sqlPrep.executeQuery();

			while (rs.next()) {
				cliente[0] = rs.getString(10); // Id usuario (irrelevante)
				cliente[1] = rs.getString(1); // Codigo
				cliente[2] = rs.getString(2); // Nome
				cliente[3] = rs.getString(3); // Cpf
				cliente[4] = rs.getString(4); // Nascimento
				cliente[5] = rs.getString(5); // Logradouro
				cliente[6] = rs.getString(6); // Anamnese
				cliente[7] = rs.getString(7); // Observação
				cliente[8] = rs.getString(8); // Data Cadastro
				cliente[9] = rs.getString(9); // Saldo
			}
			sqlPrep.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return cliente;
	}

	/**
	 * Apenas retorna qual será o próximo código de cliente
	 * ####TENTATIVA FALHA DE CORREÇÃO DE CADASTRO DO PRIMEIRO CLIENTE DO BANCO
	 * @return
	 * @throws Exception
	 */
	public int RetornarProximoCodigoCliente() throws Exception {
		int retorno = 1;
		try {
			String sql = "SELECT MAX(id_cliente)+1 AS id_cliente FROM cliente";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			ResultSet rs = sqlPrep.executeQuery();
			if (rs.next()) {
				retorno = rs.getInt("id_cliente");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		if (retorno > 0) {
			return retorno;
		}else {
			return 1;
		}
	}

	/**
	 * Retorna nome dos clientes, especialmente criado para preencher comboBoxes dos
	 * frames
	 */
	public String[] buscarNomesClientes() throws SQLException, Exception {
		String[] nomeClientes = null;
		int qntNomes = 0;
		int i = 1;
		try {
			String sql = "SELECT COUNT(nome) FROM cliente";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			// Conta quantos nomes tem na tabela
			while (rs.next()) {
				qntNomes = Integer.parseInt(rs.getString(1));
			}
			rs.close();

			// Declara vetor com a quantidade de nomes recebida
			nomeClientes = new String[qntNomes + 1];

			// Adiciona primeiro indice como vazio para exibir nos comboBoxes
			nomeClientes[0] = "";

			sql = "SELECT nome FROM cliente";
			rs = state.executeQuery(sql);
			// Passa nomes da tabela para o vetor
			while (rs.next()) {
				nomeClientes[i] = rs.getString("nome");
				i++;
			}
			rs.close();
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return nomeClientes;
	}

	/**
	 * Verificar se há aniversariantes no dia atual
	 */
	public void checkAniversariantes() {
		List<String> aniversariantes = new ArrayList<String>();
		//Variável para adicionar vírgula se caso ocorrerem mais de uma aniversariantes no dia
		try {
			String sql = "SELECT nome, telefone.numero FROM cliente " + 
					"INNER JOIN telefone ON cliente.id_cliente=telefone.id_cliente_telefone " + 
					"WHERE DAY(data_nascimento) = DAY(CURDATE()) AND MONTH(data_nascimento) = MONTH(CURDATE()) " + 
					"GROUP BY id_cliente";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);
			
			while (rs.next()) {
				aniversariantes.add(rs.getString(1) + " (" + rs.getString(2) + ")" );
			}
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		if (aniversariantes.size() > 0) {
			//Removendo chaves para mostrar no joptionpane
			String mensagem = String.valueOf(aniversariantes).replace("[", "");
			mensagem = mensagem.replace("]", "");
			JOptionPane.showMessageDialog(null,
					"Você tem clientes fazendo aniversário hoje! Mostre que lembrou deles, parabenize-os!\n" + mensagem,
					"ANIVERSARIANTES", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
