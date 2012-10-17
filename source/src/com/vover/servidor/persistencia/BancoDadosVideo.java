package com.vover.servidor.persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.vover.recursos.Video;

/**
 * Esta classe e responsavel pela insercao e atualizacao da tabela "video".
 *  
 * @author Charles Garrocho
 */
public class BancoDadosVideo extends BancoDados {
	private String SQL;
	private ResultSet resultado;

	/**
	 * Este e o construtor. Ele instancia um novo <code>BancoDados</code>.
	 * 
	 * @see BancoDados
	 */
	public BancoDadosVideo() {
		super(); 
	}
	
	/**
	 * Adiciona um novo Video.
	 * 
	 * @param nome um <code>String</code> com o nome do Video.
	 * @param descricao um <code>String</code> com a descricao do Video.
	 * @param endereco um <code>String</code> com o endereco do Video.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public void adicionarVideo(String usuario, String nome, String descricao, String endereco) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		SQL = "INSERT INTO video (usuario, nome, descricao, endereco) VALUES ('" + usuario + "', '" + nome + "', '" + descricao + "', '" + endereco + "')";
		executaSemRetorno(SQL);
		fechaConexao();
	}
	
	/**
	 * Atualiza um Video.
	 * 
	 * @param nome um <code>String</code> com o nome do Video.
	 * @param descricao um <code>String</code> com a descricao do Video.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public void atualizarVideo(String nome, String descricao, String endereco) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		SQL = "UPDATE video SET descricao = '" + descricao + "', endereco = '" + endereco + "' WHERE video.nome = '" + nome + "'";
		executaSemRetorno(SQL);
		fechaConexao();
	}
	
	/**
	 * Atualiza um Video.
	 * 
	 * @param nomeAnterior um <code>String</code> com o nome anterior do Video.
	 * @param nome um <code>String</code> com o nome do Video.
	 * @param descricao um <code>String</code> com a descricao do Video.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public void atualizarVideo(String nomeAnterior, String nome, String descricao, String endereco) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		SQL = "UPDATE video SET nome = '" + nome + "', descricao = '" + descricao+ "', endereco = '" + endereco + "' WHERE nome = '" + nomeAnterior + "'";
		executaSemRetorno(SQL);
		fechaConexao();
	}
	
	public void excluirVideo(String nome) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		SQL = "DELETE FROM video WHERE nome = '" + nome + "'";
		executaSemRetorno(SQL);
		fechaConexao();
	}
	
	/**
	 * Retorna a quantidade de Video a partir do nome.
	 * 
	 * @param nome um <code>String</code> com o nome do Video.
	 * @return um <code>int</code> com a quantidade de Videos.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public int verificaNomeVideo(String nome) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		SQL = "SELECT COUNT(video.nome) AS qtde FROM video WHERE nome LIKE '" + nome + "'";
		resultado = executaComando(SQL);
		int qtde = 0;
		while (resultado.next())
			qtde = resultado.getInt("qtde");
		fechaConexao();
		return qtde;
	}
	
	/**
	 * Retorna a quantidade de Videos de acordo com o usuario e a partir do nome do video.
	 * 
	 * @param usuario um <code>String</code> com o nome do usuario.
	 * @param nome um <code>String</code> com o nome do Video.
	 * @return um <code>int</code> com a quantidade de Videos.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public int verificaNomeVideoUsuario(String usuario, String nome) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		SQL = "SELECT COUNT(video.nome) AS qtde FROM video WHERE usuario LIKE '" + usuario + "' AND nome LIKE '" + nome + "'";
		resultado = executaComando(SQL);
		int qtde = 0;
		while (resultado.next())
			qtde = resultado.getInt("qtde");
		fechaConexao();
		return qtde;
	}
	
	/**
	 * Retorna o endereco de um video a partir do nome.
	 * 
	 * @param nome um <code>String</code> com o nome do Video.
	 * @return um <code>String</code> com o endereco do Video.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public String enderecoVideo(String nome) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		SQL = "SELECT endereco FROM video WHERE video.nome LIKE '" + nome + "'";
		resultado = executaComando(SQL);
		String endereco = "";
		while (resultado.next())
			endereco = resultado.getString("endereco");
		fechaConexao();
		return endereco;
	}
	
	/**
	 * Retorna a descricao de um video a partir do nome.
	 * 
	 * @param nome um <code>String</code> com o nome do Video.
	 * @return um <code>String</code> com a descricao do Video.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public String descricaoVideo(String nome) throws SQLException, ClassNotFoundException {
		iniciaConexao();
		SQL = "SELECT descricao FROM video WHERE video.nome LIKE '" + nome + "'";
		resultado = executaComando(SQL);
		String descricao = "";
		while (resultado.next())
			descricao = resultado.getString("descricao");
		fechaConexao();
		return descricao;
	}
	
	/**
	 * Retorna um vetor de videos a partir do nome.
	 * 
	 * @param nome um <code>String</code> com o nome do Video.
	 * @return um <code>Video[]</code> com um vetor de objetos do tipo Video.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public Video[] obterDadosVideo(String nome,int quantidade) throws SQLException, ClassNotFoundException {
		
		Video[] videos = new Video[quantidade];
		iniciaConexao();
		SQL = "SELECT nome, descricao, endereco FROM video WHERE video.nome LIKE '" + nome + "'";
		resultado = executaComando(SQL);
		for (int x = 0; resultado.next(); x++) {
			videos[x] = new Video();
			videos[x].setNome(resultado.getString("nome"));
			videos[x].setDescricao(resultado.getString("descricao"));
			videos[x].setEndereco(resultado.getString("endereco"));
		}
		fechaConexao();
		return videos;
	}
	
	/**
	 * Retorna um vetor de videos a partir do nome.
	 * 
	 * @param usuario um <code>String</code> com o nome do usuario.
	 * @param nome um <code>String</code> com o nome do Video.
	 * @return um <code>Video[]</code> com um vetor de objetos do tipo Video.
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public Video[] obterDadosVideoUsuario(String usuario, String nome,int quantidade) throws SQLException, ClassNotFoundException {
		
		Video[] videos = new Video[quantidade];
		iniciaConexao();
		SQL = "SELECT nome, descricao, endereco FROM video WHERE video.usuario LIKE '" + usuario + "' AND video.nome LIKE '" + nome + "'";
		resultado = executaComando(SQL);
		for (int x = 0; resultado.next(); x++) {
			videos[x] = new Video();
			videos[x].setNome(resultado.getString("nome"));
			videos[x].setDescricao(resultado.getString("descricao"));
			videos[x].setEndereco(resultado.getString("endereco"));
		}
		fechaConexao();
		return videos;
	}

} // classe BancoDadosVideo.