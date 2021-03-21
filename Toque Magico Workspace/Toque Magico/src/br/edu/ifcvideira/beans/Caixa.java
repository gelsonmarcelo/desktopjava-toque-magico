package br.edu.ifcvideira.beans;

/**
 * 
 * Ordem de cria��o sugerida: 1� Bean 2� DAO 3� View
 *
 */

public class Caixa {

	private int codigo;
	private double valor;
	private String descricao;
	private String dataRegistro;
	private String cliente;

	// construtor vazio, sem necessidade de par�metros
	public Caixa() {
	}

	// encapsulamento dos atributos
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(String dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
}
