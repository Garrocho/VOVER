package com.vover.servidor.persistencia;
import java.sql.*;

/**
 * Esta classe e responsavel pela insercao e atualizacao do banco de dados.
 *  
 * @author Charles Garrocho
 */
public class BancoDados {
	private Connection conexao;
	private Statement afirmacao;
	private final java.io.File DATABASE;

	/**
	 * Este e o construtor. O construtor cria uma refencia do
	 */
	public BancoDados() {
		super();
		DATABASE = new java.io.File (
				"Recursos"
				+ System.getProperty("file.separator")
				+ "BancoDados"
				+ System.getProperty("file.separator")
				+ "sevi.db");
	}

	/**
	 * Apaga todas as tabelas do banco de dados.
	 * 
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public void apagarBancoDados() throws SQLException {
		String SQL = "DROP TABLE usuario";
		afirmacao.executeUpdate(SQL);
	}

	/**
	 * Executa uma atualizacao. Nada e retornado para o usuario.
	 * 
	 * @param SQL um <code>String</code> com a query.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public void executaSemRetorno(String SQL) throws SQLException {
		afirmacao.executeUpdate(SQL);
	}

	/**
	 * Executa uma query. E retornado um <code>ResultSet</code>.
	 * 
	 * @param SQL um <code>String</code> com a query.
	 * @return um <code>ResultSet</code> 	/**
	 * 
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public ResultSet executaComando(String SQL) throws SQLException {
		return afirmacao.executeQuery(SQL);
	}

	/**
	 * Inicia uma conexao com o banco de dados.
	 * 
	 * @throws ClassNotFoundException Dispara uma excecao Classe Nao Encontrada.
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public void iniciaConexao() throws ClassNotFoundException, SQLException {

		Class.forName("org.sqlite.JDBC");
		conexao = DriverManager.getConnection("jdbc:sqlite:" + DATABASE.getPath());

		// Utilizamos o metodo createStatement de conexao para criar o Statement.
		afirmacao = conexao.createStatement();
	}

	/**
	 * Fecha uma conexao com o banco de dados.
	 * 
	 * @throws SQLException Dispara uma excecao SQL.
	 */
	public void fechaConexao() throws SQLException {
		conexao.close();
	}

	/**
	 * Verifica se o banco de dados existe. Caso nao exista, o programa roda o metodo <code>criaNovoBancoDados</code>
	 * para criar um arquivo de banco de dados.
	 * 
	 * @throws Exception Dispara uma excecao.
	 * 
	 * @see BancoDados#criaNovoBancoDados()
	 */
	public void verificaBancoDados() throws Exception {
		if (!DATABASE.exists()) {
			criaNovoBancoDados();
		}
	}

	/**
	 * Cria um novo banco de dados com todas as tabelas necessarias para a execucao do programa.
	 * 
	 * @throws Exception Dispara uma excecao.
	 */
	public void criaNovoBancoDados() throws Exception {

		try {
			DATABASE.getParentFile().mkdirs(); //Cria os diretorios pai do arquivo (caso nao existam)
			DATABASE.createNewFile();// Cria o arquivo do banco
			if (!DATABASE.exists()) { //Caso o arquivo ainda nao exista, apos os comandos acima, dispara excecao.
				throw new Exception("Erro ao gravar o arquivo de banco de dados.");
			}

			iniciaConexao();

			// Criacao da Tabela usuario
			afirmacao.execute(
					"CREATE TABLE IF NOT EXISTS usuario ("
					+ "nick character varying(15), "
					+ "senha character varying(15), "
					+ "online character varying(50), "
					+ "CONSTRAINT usuario_pkey PRIMARY KEY (nick)"
					+ ")");

			// Criacao da Tabela video
			afirmacao.execute(
					"CREATE TABLE IF NOT EXISTS video ("
					+ "usuario character varying(15), "
					+ "nome character varying(25), "
					+ "descricao character varying(50), "
					+ "endereco character varying(100), "
					+ "CONSTRAINT video_pkey PRIMARY KEY (nome)"
					+ ")");

		} catch (Exception ex) {
			throw new Exception("Erro na criacao do banco de dados\n" + ex.getMessage());
		}
		conexao.close();
	}	 
}