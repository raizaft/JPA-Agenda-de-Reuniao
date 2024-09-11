package aplicacao;

import modelo.Pessoa;
import modelo.Reuniao;
import regras_de_negocio.Fachada;

public class Listar {
	public Listar(){
		try {
			Fachada.inicializar();

			System.out.println("Listagem de pessoas:");
			for(Pessoa p : Fachada.listarPessoas())
				System.out.println(p);

			System.out.println("\nListagem de reuniões:");
			for(Reuniao r: Fachada.listarReunioes())
				System.out.println(r);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		Fachada.finalizar();
	}


	//=================================================
	public static void main(String[] args) {
		new Listar();
	}
}

