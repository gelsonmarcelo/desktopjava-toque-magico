package br.edu.ifcvideira.beans;

public class Telefone {
	private int codigo;
	private int id_cliente;
	private String numero;

	// Construtor
	public Telefone() {
	}

	// Encapsulamento
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(int id_cliente) {
		this.id_cliente = id_cliente;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
}
