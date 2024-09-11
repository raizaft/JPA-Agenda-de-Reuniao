package aplicacao;

import java.util.List;

import modelo.Pessoa;
import modelo.Reuniao;
import regras_de_negocio.Fachada;

public class Consultar {

    public Consultar() {
        try {
            Fachada.inicializar();
            System.out.println("Consultando Reunioes na Data...\n");

            System.out.println("Reunioes na data 01/10/2024:");

            List<Reuniao> reunioes = Fachada.consultarReunioes("01/10/2024");
            if (!reunioes.isEmpty()){
                for(Reuniao r : reunioes)
                    System.out.println(r);
            } else {
                System.out.println("Não existem reuniões cadastradas\n");
            }
            System.out.println("-----------------------------------------\n");

            System.out.println("Iniciando consulta de pessoas com mais de 2 reuniões...\n");

            System.out.println("Pessoas com mais de 2 reuniões:");
            List<Pessoa> pessoas = Fachada.pessoasComMaisDeNReunioes(2);
            if (!pessoas.isEmpty()){
                for (Pessoa p : pessoas) {
                    System.out.println(p);
                }
            }else{
                System.out.println("Não existem Pessoas cadastradas\n");
            }

            System.out.println("-----------------------------------------\n");

            System.out.println("Consultando Reunioes da Pessoa...\n");

            System.out.println("Reuniões com a pessoa de nome Lavinia:");
            List<Reuniao> reunioes1 = Fachada.reuniaosComPessoa("Lavinia");
            if (!reunioes1.isEmpty()){
                for (Reuniao r : reunioes1) {
                    System.out.println(r);
                }
            } else{
                System.out.println("Não existem Reunioes cadastradas");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Fachada.finalizar();
        System.out.println("\nfim do programa");
    }

    // =================================================
    public static void main(String[] args) {
        new Consultar();
    }
}
