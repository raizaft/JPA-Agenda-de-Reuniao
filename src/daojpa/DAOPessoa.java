package daojpa;

import java.util.List;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Pessoa;

public class DAOPessoa extends DAO<Pessoa>{


	public Pessoa read (Object chave){
		try{
			String nome = (String) chave;
			TypedQuery<Pessoa> q = manager.createQuery("select p from Pessoa p where p.nome=:n",Pessoa.class);
			q.setParameter("n", nome);
			return q.getSingleResult();
			
		}catch(NoResultException e){
			return null;
		}
	}

	public List<Pessoa> readAll(){
		TypedQuery<Pessoa> q = manager.createQuery("select p from Pessoa p", Pessoa.class);
		return  q.getResultList();
	}
	
	public List<Pessoa> readByNReunioes(int n){
		TypedQuery<Pessoa> query = manager.createQuery("SELECT p FROM Pessoa p WHERE SIZE(p.reunioes) > :n", Pessoa.class);
        query.setParameter("n", n);
        return query.getResultList();
	}
}

