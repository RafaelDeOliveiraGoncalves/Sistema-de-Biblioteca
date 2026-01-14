package biblioteca.modelo;
 
import java.io.Serializable;
import java.time.LocalDate;
import biblioteca.excecoes.NomeException;
import biblioteca.excecoes.DataException;
import biblioteca.excecoes.CpfException;
import biblioteca.excecoes.PesoException;
import biblioteca.excecoes.AlturaException;
import biblioteca.validacao.ValidaData;
import biblioteca.validacao.ValidaCpf;
import biblioteca.validacao.ValidaNome;
import biblioteca.validacao.ValidaPesoAltura;


public class Pessoa implements Serializable{
    private String nome;
    private String sobrenome;
    private LocalDate dataNasc;
    private long cpf;
    private double peso;
    private double altura;
    
    private static int contagem;
    
    public Pessoa(){
        Pessoa.incrementaContagem();
    }
    
    public Pessoa(String nome, String sobrenome, String dataNascimento, String cpf, double peso, double altura) 
            throws NomeException, DataException, CpfException, PesoException, AlturaException{
        this.setNome(nome);
        this.setSobrenome(sobrenome);
        this.setDataNascimento(dataNascimento);
        this.setCpf(cpf);
        this.setPeso(peso);
        this.setAltura(altura);
    }
    
    private  void setNome(String nome) throws NomeException{
        ValidaNome.validaNome(nome);
        this.nome = nome.substring(0,1).toUpperCase() + nome.substring(1).toLowerCase();
    }
    
    public String getNome(){
        return this.nome;
    }
    
    private void setSobrenome(String sobrenome) throws NomeException{
        ValidaNome.validaNome(sobrenome);
        this.sobrenome = sobrenome.substring(0, 1).toUpperCase() + sobrenome.substring(1).toLowerCase(); // Melhorar para quando ter espaço colocar a letra maiuscula
    }
    
    public String getSobrenome(){
        return this.sobrenome;
    }
    
    private void setDataNascimento(String data) throws DataException{
        this.dataNasc = ValidaData.dataValida(data);
    }
    
    public String getDataNascimento(){
        return ValidaData.dataString(this.dataNasc);
    }
    
    public int getIdade(){
        return ValidaData.getIdade(dataNasc);
    }
    
    private void setCpf(String cpf) throws CpfException{
        if (ValidaCpf.isCPF(cpf)){
            this.cpf = ValidaCpf.toLong(cpf);
        }
        else{
            throw new CpfException();
        }
    }
    
    public String getCpf(){
        String cpf = String.valueOf(this.cpf);
        return ValidaCpf.imprimeCPF(cpf);
    }
    
    public void setPeso(double peso) throws PesoException{
        ValidaPesoAltura.validaPeso(peso);
        this.peso = peso;
    }
    
    public double getPeso(){
        return this.peso;
    }
    
    public void setAltura(double altura) throws AlturaException{
        ValidaPesoAltura.validaAltura(altura);
        this.altura = altura;
    }
    
    public double getAltura(){
        return this.altura;
    }
    
    private static void incrementaContagem(){
        contagem++;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: " + getNome() + " " + getSobrenome() + "\n");
        sb.append("Data Nascimento: " + getDataNascimento() + "\n");
        sb.append("Idade: " + getIdade() + "\n");
        sb.append("CPF: " + getCpf() + "\n");
        sb.append("Peso: " + getPeso() + "\n");
        sb.append("Altura: " + getAltura() + "\n");
        
        return sb.toString();
    }
       
}
