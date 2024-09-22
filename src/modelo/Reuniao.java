package modelo;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Reuniao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Convert(converter = LocalDateConverter.class)
    private String data;

    private String assunto;

    @ManyToMany()
    private List<Pessoa> pessoas = new ArrayList<>();

    public Reuniao() {
    }

    public Reuniao(String data, String assunto) {
        this.data = data;
        this.assunto = assunto;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    //relacionamento

    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    public void addPessoa(Pessoa p) {
        pessoas.add(p);
    }

    public void removerPessoa(Pessoa p) {
        pessoas.remove(p);
    }

    public ArrayList<String> getNomesPessoas() {
        ArrayList<String> nomespessoas = new ArrayList<>();
        for (Pessoa p : pessoas)
            nomespessoas.add(p.getNome());
        return nomespessoas;
    }

    @Override
    public String toString() {
        return "Reuniao [id=" + id + ", data=" + data + ", assunto=" + assunto + ", pessoas=" + getNomesPessoas() + "]";
    }

}
