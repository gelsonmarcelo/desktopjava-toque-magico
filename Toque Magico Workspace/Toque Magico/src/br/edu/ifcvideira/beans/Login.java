package br.edu.ifcvideira.beans;

public class Login {

	public static int idUser = 0;
	public static String senhaUser = "";
	private int codigo;
	private String login;
	private String senha;
	private double saldo;

	// construtor vazio, sem necessidade de parâmetros
	public Login() {
	}

	// encapsulamento dos atributos
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String usuario) {
		this.login = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
}
