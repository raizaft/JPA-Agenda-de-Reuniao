package aplicacao;

import modelo.Pessoa;
import modelo.Reuniao;
import regras_de_negocio.Fachada;

public class Deletar {
    public Deletar() {
        try {
            Fachada.inicializar();
            System.out.println("Listagem de reuniões antes da exclusão:");
            for(Reuniao r: Fachada.listarReunioes())
                System.out.println(r);

            System.out.println("\nDeletando a reunião com ID 2...");
            Fachada.deletarReuniao(2);

            System.out.println("\nListagem de reuniões após a exclusão:");
            for(Reuniao r: Fachada.listarReunioes())
                System.out.println(r);

            System.out.println("\n-----------------------------------------\n");

            System.out.println("Listagem de pessoas antes da deleção:");
            for(Pessoa p : Fachada.listarPessoas())
                System.out.println(p);

            System.out.println("\nDeletando a pessoa 'Lucas'...");
            Fachada.deletarPessoa("Lucas");

            System.out.println("\nListagem de pessoas após deleção:");
            for(Pessoa p : Fachada.listarPessoas())
                System.out.println(p);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Fachada.finalizar();
        System.out.println("\nfim do programa");
    }

    // =================================================
    public static void main(String[] args) {
        new Deletar();
    }
}
