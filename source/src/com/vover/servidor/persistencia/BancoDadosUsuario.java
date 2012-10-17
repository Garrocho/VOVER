package com.vover.servidor.persistencia;

import java.sql.*;

/**
 * Esta classe e responsavel pela insercao e atualizacao da tabela "usuario".
 *  
 * @author Charles Garrocho
 */
public class BancoDadosUsuario extends BancoDados {
	private String SQL;
	private ResultSet resultado;

	/**
	 * Este e o construtor. Ele instancia um novo <code>BancoDados</code>.
	 * 
	 * @see BancoDados
	 */
	public BancoDadosUsuario() {
		super(); 
	}
	
	/**
	 * Atualiza status de um Usuario.
	 * 
	 * @param nick um <code>String</code> com o nick do usuario.
	 * @param status um <code>String</code> com o status do usuario.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public void atualizaStatus(String nick, String status) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		SQL = "UPDATE usuario SET online = '" + status + "' WHERE nick LIKE '" + nick + "'";
		executaSemRetorno(SQL);
		fechaConexao();
	}
	
	/**
	 * Adiciona um novo Usuario.
	 * 
	 * @param nick um <code>String</code> com o nick do usuario.
	 * @param senha um <code>String</code> com a denha do usuario.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public void adicionarUsuario(String nick, String senha) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		SQL = "INSERT INTO usuario (nick, senha) VALUES ('" + nick + "', '" + senha + "')";
		executaSemRetorno(SQL);
		fechaConexao();
	}
	
	/**
	 * Retorna a quantidade de usuario a partir do nome.
	 * 
	 * @param nick um <code>String</code> com o nick do usuario.
	 * @return um <code>int</code> com a quantidade de usuarios.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public int verificaNickUsuario(String nick) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		SQL = "SELECT COUNT(usuario.nick) AS qtde FROM usuario WHERE usuario.nick LIKE '" + nick + "'";
		resultado = executaComando(SQL);
		int qtde = 0;
		while (resultado.next())
			qtde = resultado.getInt("qtde");
		fechaConexao();
		return qtde;
	}
	
	/**
	 * Retorna a quantidade de usuario online.
	 * 
	 * @return um <code>int</code> com a quantidade de usuarios.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public int qtdeUsuarioOnline() throws SQLException, ClassNotFoundException {
		iniciaConexao();
		SQL = "SELECT COUNT(usuario.online) AS qtde FROM usuario WHERE usuario.online <> '0'";
		resultado = executaComando(SQL);
		int qtde = 0;
		while (resultado.next())
			qtde = resultado.getInt("qtde");
		fechaConexao();
		return qtde;
	}
	
	/**
	 * Retorna a quantidade de usuario online a partir de um nick.
	 * 
	 * @param nick um <code>String</code> com o nick do usuario.
	 * @return um <code>int</code> com a quantidade de usuarios.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public int verificaUsuarioOnline(String nick) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		SQL = "SELECT COUNT(usuario.online) AS qtde FROM usuario WHERE usuario.nick LIKE '" + nick + "' AND usuario.online <> '0'";
		resultado = executaComando(SQL);
		int qtde = 0;
		while (resultado.next())
			qtde = resultado.getInt("qtde");
		fechaConexao();
		return qtde;
	}
	
	/**
	 * Retorna a quantidade de usuario a partir do nome.
	 * 
	 * @param qtde um <code>int</code> com a quantidade de usuario.
	 * @return um <code>int</code> com a quantidade de usuarios.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public String[] obterIpUsuarios(int qtde) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		String[] usuarios = new String[qtde]; 
		SQL = "SELECT online FROM usuario WHERE usuario.online <> '0'";
		resultado = executaComando(SQL);
		qtde = 0;
		while (resultado.next())
			usuarios[qtde++] = resultado.getString("online");
		fechaConexao();
		return usuarios;
	}
	
	/**
	 * Retorna os nomes dos usuarios online.
	 * 
	 * @param qtde um <code>int</code> com a quantidade de usuario.
	 * @return um <code>int</code> com a quantidade de usuarios.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public String[] obterUsuariosOnline(int qtde) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		String[] usuarios = new String[qtde]; 
		SQL = "SELECT nick FROM usuario WHERE usuario.online <> '0'";
		resultado = executaComando(SQL);
		qtde = 0;
		while (resultado.next())
			usuarios[qtde++] = resultado.getString("nick");
		fechaConexao();
		return usuarios;
	}
	
	/**
	 * Retorna a quantidade de usuario a partir do nome.
	 * 
	 * @param nick um <code>String</code> com o nick do usuario.
	 * @param senha um <code>String</code> com a senha do usuario.
	 * @return um <code>int</code> com a quantidade de usuarios.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public int verificaUsuario(String nick, String senha) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		SQL = "SELECT COUNT(usuario.nick) AS qtde FROM usuario WHERE usuario.nick LIKE '" + nick + "' AND usuario.senha LIKE '" + senha + "'";
		resultado = executaComando(SQL);
		int qtde = 0;
		while (resultado.next())
			qtde = resultado.getInt("qtde");
		fechaConexao();
		return qtde;
	}
	
} // classe BancoDadosUsuario.