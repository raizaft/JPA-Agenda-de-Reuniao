package aplicacao;

import java.util.ArrayList;
import java.util.Arrays;

import regras_de_negocio.Fachada;

public class Cadastrar {
	public Cadastrar(){
		try {
			Fachada.inicializar();
			System.out.println("cadastrando pessoas...");
			Fachada.criarPessoa("Raiza");
			Fachada.criarPessoa("Lavinia");
			Fachada.criarPessoa("Lucas");
			Fachada.criarPessoa("Heitor");
			
		} catch (Exception e) 	{
			System.out.println(e.getMessage());
		}

		try {
			System.out.println("cadastrando reuniões...\n");
			String[] nomesArray = {"Raiza", "Lavinia"};
	        ArrayList<String> nomesPessoas = new ArrayList<>(Arrays.asList(nomesArray));
			Fachada.criarReuniao("01/10/2024", "Projeto de POB", nomesPessoas);
			
			String[] nomesArray2 = {"Lucas", "Heitor", "Raiza"};
	        ArrayList<String> nomesPessoas2 = new ArrayList<>(Arrays.asList(nomesArray2));
			Fachada.criarReuniao("17/10/2024", "Aniversário de Lucas", nomesPessoas2);

			String[] nomesArray3 = {"Raiza", "Lavinia"};
			ArrayList<String> nomesPessoas3 = new ArrayList<>(Arrays.asList(nomesArray3));
			Fachada.criarReuniao("02/11/2024", "Projeto de PWB1", nomesPessoas3);

		} catch (Exception e) 	{
			System.out.println(e.getMessage());
		}

		Fachada.finalizar();
		System.out.println("Reuniões e pessoas cadastradas com sucesso!");
	}

	
	//=================================================
	public static void main(String[] args) {
		new Cadastrar();
	}
}


