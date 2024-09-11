package modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity 
public class Pessoa {
	@Id		
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String nome;
	
	@ManyToMany(mappedBy = "pessoas")
	private List<Reuniao> reunioes = new ArrayList<>();
 
	public Pessoa () {}
	
	public Pessoa(String nome) {
		this.nome = nome;
	}
 
	public String getNome() {
		return nome;
	}
 
	public void setNome(String nome) {
		this.nome = nome;
	}
	 
	public int getId() {
		return id;
	}
	 
	public void setId(int id) {
		this.id=id;
	}
	 
	//relacionamento
	 
	public void adicionar(Reuniao r) {
		reunioes.add(r);
	}
	 
	public void remover(Reuniao r) {
		reunioes.remove(r);
	}
	
	public Reuniao localizar(String assunto) {
		for(Reuniao r : reunioes)
			if (r.getAssunto().equals(assunto))
				return r;
		return null;
	}
	
	public List<Reuniao> getReuniao(){
		return reunioes;
	}

	public ArrayList<String> getAssuntosReunioes() {
		ArrayList<String> assuntosreunioes = new ArrayList<>();
		for (Reuniao r : reunioes)
			assuntosreunioes.add(r.getAssunto());
		return assuntosreunioes;
	}
	
	@Override
	public String toString() {
		return "Pessoa [nome=" + nome + ", reunioes=" + getAssuntosReunioes() + "]";
	}

}
