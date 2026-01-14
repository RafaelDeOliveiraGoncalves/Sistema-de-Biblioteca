package biblioteca.modelo;

import biblioteca.excecoes.AlturaException;
import biblioteca.excecoes.CpfException;
import biblioteca.excecoes.DataException;
import biblioteca.excecoes.NomeException;
import biblioteca.excecoes.PesoException;

public class Homem extends Pessoa{ // Acho que não vai precisar
    private final String genero;
    
    public Homem(String nome, String sobrenome, String dataNascimento, String cpf, double peso, double altura) 
            throws NomeException, DataException, CpfException, PesoException, AlturaException{
        super(nome, sobrenome, dataNascimento, cpf, peso, altura);
        this.genero = "Masculino";
    }
    
    public String getGenero(){
        return this.genero;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(this.getGenero());
        
        return sb.toString();
    }
}
