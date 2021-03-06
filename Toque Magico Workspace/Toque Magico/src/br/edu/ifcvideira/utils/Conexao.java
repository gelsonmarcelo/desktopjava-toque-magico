package br.edu.ifcvideira.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*Utils: coisas que servem para o programa em geral*/
public class Conexao {	
		/**
		 * conex?o MySQL
		 * @return
		 * @throws Exception
		 */
		public static Connection conectar() throws Exception {
			try{
					Class.forName("com.mysql.cj.jdbc.Driver");
					/*Casa*/
					Connection conexao = DriverManager.getConnection("jdbc:mysql://localhost/tmagico?user=root&password=password&useTimezone=true&serverTimezone=UTC");
					/*IFC*/
//					Connection conexao = DriverManager.getConnection("jdbc:mysql://localhost/tqmagico?user=root&password=ironman&useTimezone=true&serverTimezone=UTC");
					//localhost: ip [:3306(porta)]/ clienteteste: nome minha DataBase /usuario: root
					return conexao;
			}catch(ClassNotFoundException | SQLException e){
	            throw new Exception(e.getMessage());
	        }
	}
		
}
/*conex?o Postgres
private final static String driver = "org.postgresql.Driver";
private final static String usuario = "postgres";
private final static String senha = "1234";
private final static String host = "localhost";
private final static String porta = "5432";
private final static String banco = "db_Cliente";
private final static String url = "jdbc:postgresql://" + host + ":" + porta + "/" + banco;
private static Connection conexao = null;
    
public static Connection conectar(){
	 try {
		 Class.forName(driver);
		 conexao = DriverManager.getConnection(url, usuario, senha);
		 System.out.println("Conex?o efetuada com sucesso");
       
	 } catch (Exception ex) {
		 ex.printStackTrace();
	 }
	return conexao; 
}

public void fechar() {
	try {
		conexao.close();
		System.out.println("Conex?o encerrada");
	} 
        
	catch (SQLException e) {
		e.printStackTrace();
	}
}	
*/
	