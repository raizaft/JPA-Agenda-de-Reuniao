package daojpa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import modelo.Reuniao;

public class DAOReuniao extends DAO<Reuniao> {

	public Reuniao read(Object chave) {
		try {
			int id = (int) chave;
			TypedQuery<Reuniao> q = manager.createQuery("select r from Reuniao r where r.id = :id ",
					Reuniao.class);
			q.setParameter("id", id);

			return q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Reuniao> readAll() {
		TypedQuery<Reuniao> q = manager.createQuery(
				"select r from Reuniao r",
				Reuniao.class);
		return q.getResultList();
	}
	
	public List<Reuniao> readByName(String nome){
		TypedQuery<Reuniao> q = manager.createQuery("SELECT r FROM Reuniao r JOIN r.pessoas p WHERE p.nome = :nome", Reuniao.class);
        q.setParameter("nome", nome);
        return q.getResultList();
	}
	
	public List<Reuniao> readByDate(String  d){
	    TypedQuery<Reuniao> q = manager.createQuery("SELECT r FROM Reuniao r WHERE r.data = :data", Reuniao.class);
	    q.setParameter("data", d);
        return q.getResultList();
	}
}
