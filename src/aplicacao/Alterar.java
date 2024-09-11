package aplicacao;

import modelo.Pessoa;
import modelo.Reuniao;
import regras_de_negocio.Fachada;

public class Alterar {
    public Alterar() {

        try {
            Fachada.inicializar();
            System.out.println("Alterando o assunto da reunião com ID 1...\n");
            Fachada.alterarAssuntoReuniao(1, "Novo Projeto de POB");

            System.out.println("\n Listagem de reuniões após alteração:");
            for(Reuniao r: Fachada.listarReunioes())
                System.out.println(r);

            System.out.println("-----------------------------------------\n");

            System.out.println("Alterando o nome da pessoa 'Lavinia' para 'Lavinia Morais'...\n");
            Fachada.alterarNomePessoa("Lavinia", "Lavinia Morais");

            System.out.println("Listagem de pessoas após alteração:");
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
        new Alterar();
    }
}
