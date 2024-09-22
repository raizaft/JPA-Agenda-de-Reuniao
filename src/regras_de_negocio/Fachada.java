package regras_de_negocio;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import modelo.Pessoa;
import modelo.Reuniao;
import daojpa.DAO;
import daojpa.DAOPessoa;
import daojpa.DAOReuniao;


public class Fachada {
    private Fachada() {
    }

    private static DAOPessoa daopessoa = new DAOPessoa();
    private static DAOReuniao daoreuniao = new DAOReuniao();

    public static void inicializar() {
        DAO.open();
    }

    public static void finalizar() {
        DAO.close();
    }

    public static Pessoa buscarPessoa(String nome) {
        Pessoa p = daopessoa.read(nome);
        return p;
    }

    public static Reuniao buscarReuniao(int id) {
        Reuniao r = daoreuniao.read(id);
        return r;
    }

    public static Pessoa criarPessoa(String nome) throws Exception {
        DAO.begin();

        try {
            if (buscarPessoa(nome) != null) {
                DAO.rollback();
                throw new Exception("Pessoa já cadastrada!");
            }
            Pessoa pessoa = new Pessoa(nome);
            daopessoa.create(pessoa);
            DAO.commit();
            return pessoa;
        } catch (Exception e) {
            throw e;
        }
    }

    public static Reuniao criarReuniao(String data, String assunto, ArrayList<String> nomesPessoas) throws Exception {
        DAO.begin();

        try {
            LocalDate dt = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate hoje = LocalDate.now();
            if (dt.isBefore(hoje)) {
                DAO.rollback();
                throw new Exception("A data não pode ser anterior a de hoje.");
            }

            if (nomesPessoas.size() < 2) {
                DAO.rollback();
                throw new Exception("Uma reunião deve ter no mínimo 2 pessoas.");
            }

            Reuniao reuniao = new Reuniao(data, assunto);

            for (String nome : nomesPessoas) {
                Pessoa p = buscarPessoa(nome);

                if (p == null) {
                    criarPessoa(nome);
                }

                for (Reuniao r : p.getReuniao()) {
                    if (r.getData().equals(data)) {
                        DAO.rollback();
                        throw new Exception("A pessoa " + p.getNome() + " já está participando de outra reunião ao mesmo tempo.");
                    }
                }
                reuniao.addPessoa(p);
            }
            daoreuniao.create(reuniao);

            for (Pessoa pessoa : reuniao.getPessoas()) {
                pessoa.adicionar(reuniao);
                daopessoa.update(pessoa);
            }
            DAO.commit();
            return reuniao;
        } catch (DateTimeParseException e) {
            throw new Exception("Formato de data inválido: " + data);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void addPessoaReuniao(String nome, Reuniao reuniao) throws Exception {
        DAO.begin();

        try {
            Pessoa p = buscarPessoa(nome);

            if (p == null) {
                p = criarPessoa(nome);
            } else {
                if (reuniao.getPessoas().stream().anyMatch(participante -> participante.getNome().equals(nome))) {
                    throw new Exception("A pessoa " + nome + " já está participando desta reunião.");
                }
                for (Reuniao outraReuniao : p.getReuniao()) {
                    if (outraReuniao.getData().equals(reuniao.getData())) {
                        throw new Exception("A pessoa " + nome + " já está participando de outra reunião na mesma data.");
                    }
                }
            }

            reuniao.addPessoa(p);
            daoreuniao.update(reuniao);
            p.adicionar(reuniao);
            daopessoa.update(p);

            DAO.commit();
        } catch (Exception e) {
            DAO.rollback();
            throw e;
        }
    }

    public static void removerPessoaReuniao(String nome, int id) throws Exception {
        DAO.begin();
        try {
            Reuniao r = buscarReuniao(id);
            Pessoa p = buscarPessoa(nome);
            if (p == null) {
                DAO.rollback();
                throw new Exception("Pessoa não encontrada.");
            }
            r.removerPessoa(p);
            p.remover(r);
            daopessoa.update(p);
            daoreuniao.update(r);
            DAO.commit();
        } catch (Exception e) {
            throw e;
        }
    }


    public static void alterarAssuntoReuniao(int id, String novoAssunto) throws Exception {
        DAO.begin();

        try {
            Reuniao reuniao = buscarReuniao(id);
            if (reuniao == null) {
                throw new Exception("Reunião não encontrada.");
            }
            reuniao.setAssunto(novoAssunto);
            daoreuniao.update(reuniao);
            DAO.commit();
        } catch (Exception e) {
            throw e;
        }

    }

    public static void alterarNomePessoa(String nome, String novoNome) throws Exception {
        DAO.begin();

        try {
            Pessoa pessoa = buscarPessoa(nome);
            if (pessoa == null) {
                DAO.rollback();
                throw new Exception("Pessoa não encontrada");
            }
            pessoa.setNome(novoNome);
            daopessoa.update(pessoa);
            DAO.commit();
        } catch (Exception e) {
            throw e;
        }

    }

    public static void deletarReuniao(int id) throws Exception {
        DAO.begin();

        try {
            Reuniao reuniao = buscarReuniao(id);
            if (reuniao == null) {
                DAO.rollback();
                throw new Exception("Reunião não encontrada.");
            }
            for (Pessoa pessoa : reuniao.getPessoas()) {
                pessoa.remover(reuniao);
                daopessoa.update(pessoa);
            }

            daoreuniao.delete(reuniao);
            DAO.commit();

        } catch (Exception e) {
            throw e;
        }
    }

    public static void deletarPessoa(String nome) throws Exception {
        DAO.begin();

        try {
            Pessoa pessoa = buscarPessoa(nome);
            if (pessoa == null) {
                DAO.rollback();
                throw new Exception("Pessoa não encontrada");
            }
            if (!pessoa.getReuniao().isEmpty()) {
                DAO.rollback();
                throw new Exception("Não é possível deletar a pessoa, pois ela está participando de uma ou mais reuniões.");
            }

            daopessoa.delete(pessoa);
            DAO.commit();
        } catch (Exception e) {
            throw e;
        }

    }

    public static List<Pessoa> listarPessoas() {
        return daopessoa.readAll();
    }


    public static List<Reuniao> listarReunioes() {
        return daoreuniao.readAll();
    }

    public static List<Reuniao> consultarReunioes(String d) {
        List<Reuniao> result = daoreuniao.readByDate(d);
        return result;
    }

    public static List<Reuniao> reunioesComPessoa(String nome) {
        List<Reuniao> result = daoreuniao.readByName(nome);
        return result;
    }

    public static List<Pessoa> pessoasComMaisDeNReunioes(int n) {
        List<Pessoa> result = daopessoa.readByNReunioes(n);
        return result;
    }
}

