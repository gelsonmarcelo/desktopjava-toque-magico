package br.edu.ifcvideira.beans;

public class Agenda {
	private int codigo;
	private String data;
	private String hora;
	private double valorCombinado;
	private boolean realizado;
	private String cliente;
	private String servico;
	private double precoCobrado;
	/**
	 * Para controlar a abertura de v?rios frames no mesmo tempo
	 */
	public static boolean frameAberto;

	// construtor vazio, sem necessidade de par?metros
	public Agenda() {
	}

	// encapsulamento dos atributos
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public double getValorCombinado() {
		return valorCombinado;
	}

	public void setValorCombinado(double valorCombinado) {
		this.valorCombinado = valorCombinado;
	}

	public boolean isRealizado() {
		return realizado;
	}

	public void setRealizado(boolean realizado) {
		this.realizado = realizado;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getServico() {
		return servico;
	}

	public void setServico(String servico) {
		this.servico = servico;
	}

	public double getPrecoCobrado() {
		return precoCobrado;
	}

	public void setPrecoCobrado(double precoCobrado) {
		this.precoCobrado = precoCobrado;
	}
	
}
