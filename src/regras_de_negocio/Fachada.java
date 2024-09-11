package regras_de_negocio;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import modelo.Pessoa;
import modelo.Reuniao;
import aplicacao.Util;


public class Fachada {
	private static EntityManager manager;
	
	static {
        manager = Util.conectarBanco();
    }
	
	public static void inicializar() {
    }

    public static void finalizar() {
        Util.fecharBanco();
    }
    
    public static Pessoa buscarPessoa(String nome) {
    	TypedQuery<Pessoa> q = manager.createQuery("SELECT p FROM Pessoa p WHERE p.nome = :nome", Pessoa.class);
        q.setParameter("nome", nome);
        return q.getResultStream().findFirst().orElse(null);
    }    
    
    public static Reuniao buscarReuniao(int id) {
    	TypedQuery<Reuniao> q = manager.createQuery("SELECT r FROM Reuniao r WHERE r.assunto = :id", Reuniao.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }    

    public static Pessoa criarPessoa(String nome)throws Exception {
    	EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        
        try {
        	if (buscarPessoa(nome) != null) {
                throw new Exception("Pessoa já cadastrada!");
        	}
            Pessoa pessoa = new Pessoa(nome);
            manager.persist(pessoa);
            transaction.commit();
            return pessoa;
        } catch (Exception e) {
        	if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static void criarReuniao(String data, String assunto, ArrayList<String> nomesPessoas) throws Exception {
    	EntityTransaction transaction = manager.getTransaction();
        transaction.begin();

        try {
            LocalDate dt = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate hoje = LocalDate.now();
            if (dt.isBefore(hoje)) {
                throw new Exception("A data não pode ser anterior a de hoje.");
            }

            if (nomesPessoas.size() < 2) {
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
                        throw new Exception("A pessoa " + p.getNome() + " já está participando de outra reunião ao mesmo tempo.");
                    }
                }
                reuniao.addPessoa(p);
            }
            manager.persist(reuniao);
            
            for (Pessoa pessoa : reuniao.getPessoas()) {
                pessoa.adicionar(reuniao);
                manager.merge(pessoa);
            }
            transaction.commit();
        } catch (DateTimeParseException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new Exception("Formato de data inválido: " + data);
        } catch (Exception e) {
        	if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
    
    public static void addPessoaReuniao(String nome, Reuniao reuniao) throws Exception {
    	EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        
        try {
        	Pessoa p = buscarPessoa(nome);
        	if(p ==null) {
        		p = criarPessoa(nome);
        	}
        	reuniao.addPessoa(p);
    		p.adicionar(reuniao);
    		manager.merge(p);
    		transaction.commit();
        } catch (Exception e) {
        	if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static void alterarAssuntoReuniao(int id, String novoAssunto) throws Exception {
    	EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
    	
        try {
        	Reuniao reuniao = buscarReuniao(id);
            if (reuniao == null) {
                throw new Exception("Reunião não encontrada.");
            }
            reuniao.setAssunto(novoAssunto);
            manager.merge(reuniao);
            transaction.commit();
        } catch (Exception e) {
        	if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }

    }

    public static void alterarNomePessoa(String nome, String novoNome) throws Exception {
    	EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        
        try {
        	Pessoa pessoa = buscarPessoa(nome);
            if (pessoa == null) {
                throw new Exception("Pessoa não encontrada");
            }
            pessoa.setNome(novoNome);
            manager.merge(pessoa);
            transaction.commit();
        } catch (Exception e) {
        	if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }

    }
    public static void deletarReuniao(int id) throws Exception {
    	EntityTransaction transaction = manager.getTransaction();
        transaction.begin();

        try {
        	Reuniao reuniao = buscarReuniao(id);
            if (reuniao == null) {
                throw new Exception("Reunião não encontrada.");
            }
            for (Pessoa pessoa : reuniao.getPessoas()) {
                pessoa.remover(reuniao);
                manager.merge(pessoa);
            }

            manager.merge(reuniao);
            transaction.commit();

        } catch (Exception e) {
        	if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
    
    public static void deletarPessoa(String nome) throws Exception {
    	EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        
        try {
            Pessoa pessoa = buscarPessoa(nome);
            if (pessoa == null ) {
                throw new Exception("Pessoa não encontrada");
            }
            if (!pessoa.getReuniao().isEmpty()) {
                throw new Exception("Não é possível deletar a pessoa, pois ela está participando de uma ou mais reuniões.");
            }

            manager.merge(pessoa);
            transaction.commit();
        } catch (Exception e) {
        	if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }

    }

        public static List<Pessoa> listarPessoas () {
        	TypedQuery<Pessoa> q = manager.createQuery("SELECT p FROM Pessoa p", Pessoa.class);
            return q.getResultList();
        }
      

        public static List<Reuniao> listarReunioes () {
        	TypedQuery<Reuniao> q = manager.createQuery("SELECT r FROM Reuniao r", Reuniao.class);
            return q.getResultList();
        }

        public static List<Reuniao> consultarReunioes (String data){
        	LocalDate date = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            TypedQuery<Reuniao> q = manager.createQuery("SELECT r FROM Reuniao r WHERE r.data = :data", Reuniao.class);
            q.setParameter("data", date);
            return q.getResultList();
        }
        
        public static List<Reuniao> reuniaosComPessoa (String nome){
        	TypedQuery<Reuniao> q = manager.createQuery("SELECT r FROM Reuniao r JOIN r.pessoas p WHERE p.nome = :nome", Reuniao.class);
            q.setParameter("nome", nome);
            return q.getResultList();
        }
        public static List<Pessoa> pessoasComMaisDeNReunioes (int n){
        	TypedQuery<Pessoa> query = manager.createQuery("SELECT p FROM Pessoa p WHERE SIZE(p.reunioes) > :n", Pessoa.class);
            query.setParameter("n", n);
            return query.getResultList();
        }
    }

