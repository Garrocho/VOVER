package com.vover.recursos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.IllegalFormatException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Fornece v�rios m�todos para manipular um arquivo texto em disco.
 */
public class ArquivoTexto 
{
	private Scanner input; // O conte�do do arquivo texto ser� lido usando um objeto Scanner.
	private FileInputStream fileInput; // Representa o arquivo texto como um arquivo de bytes. 
	private Formatter fileOutput; // O conte�do do arquivo texto ser� escrito usando um objeto Formatter.
	  
	/** 
	 * Abre um arquivo texto armazenado em disco.
	 * 
	 * @param nomeArquivo <code>String</code> com o nome do arquivo a ser aberto.
	 */
	  public void abrir(String nomeArquivo) throws FileNotFoundException {
		  // Abre um arquivo de bytes para realizar a entrada de dados. 
		  fileInput = new FileInputStream(nomeArquivo);
		        
		  // O arquivo ser� lido como um arquivo de texto puro usando a classe java.util.Scanner. 
		  input = new Scanner(fileInput);
	  } // abrirArquivo()

	  
	  /** 
	   * Cria um arquivo texto em disco.
	   * 
	   * @param nomeArquivo <code>String</code> com o nome do arquivo a ser criado.
	   */
	  public void criar(String nomeArquivo) throws FileNotFoundException {
		  // Cria um arquivo texto em disco.
		  fileOutput = new Formatter(new File(nomeArquivo));
	  } // criarArquivo()

	  
	  /** 
	   * Escreve no arquivo texto o conte�do do objeto <code>String</code>.
	   * 
	   * @param nomeArquivo <code>String</code> com o conte�do a ser escrito no arquivo texto.
	   */
	  public void escrever(String conteudo) throws IllegalFormatException, FormatterClosedException 
	  {
		  // Escreve o conte�do no arquivo texto.
		  fileOutput.format("%s\n", conteudo);
	  } // escreverArquivo()

	  
	  /** 
	   * L� o conte�do do arquivo texto.
	   * 
	   * @return um <code>String</code> com o conte�do lido do arquivo texto.
	   */
	  public String ler() throws IllegalStateException, NoSuchElementException {
		  String conteudo = "";
		  
		  // L� o conte�do completo do arquivo.
		  while (input.hasNextLine()) 
			  conteudo += input.nextLine() + "\n";
		  
		  return conteudo; 
	  } // lerArquivo()

	  
	  /**
	   * Fecha os objetos Scanner, FileInputStream e Formatter usados para criar os arquivos de texto.
	   */
	  public void fechar() throws IOException, IllegalStateException, FormatterClosedException {
		  if (fileInput != null) fileInput.close();
		  if (input != null) input.close();
		  if (fileOutput != null) fileOutput.close();
	  } // fecharArquivo()
} // class ArquivoTexto
