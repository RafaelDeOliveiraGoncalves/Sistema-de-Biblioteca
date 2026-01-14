package biblioteca.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDate;
import biblioteca.excecoes.NomeException;
import biblioteca.excecoes.DataException;
import biblioteca.excecoes.CpfException;
import biblioteca.excecoes.PesoException;
import biblioteca.excecoes.AlturaException;
import biblioteca.excecoes.EnderecoException;
import biblioteca.emprestimo.EmprestimoLivro;
import biblioteca.validacao.ValidaEndereco;


public class Usuario extends Pessoa implements Serializable{
    private String endereco;
    private ArrayList<EmprestimoLivro> historico;
    private int qtdLivrosEmprestados;
    
    public Usuario(String nome, String sobrenome, String dataNascimento, String cpf, double peso, double altura, String endereco) 
            throws NomeException, DataException, CpfException, PesoException, AlturaException, EnderecoException{
        super(nome, sobrenome, dataNascimento, cpf, peso, altura);
        this.setEndereco(endereco);
        this.qtdLivrosEmprestados = 0;
        this.historico = new ArrayList<>();
    }
    
    public void setEndereco(String endereco) throws EnderecoException{
        if(ValidaEndereco.validaEndereco(endereco)){
            this.endereco = endereco;
        }
        else{
            throw new EnderecoException();
        }
    }
    
    public String getEndereco(){
        return this.endereco;
    }
    
    public void addLivroHistorico(LocalDate data, int codigoLivro){
        EmprestimoLivro novoEmprestimo = new EmprestimoLivro(data, codigoLivro);
        this.historico.add(novoEmprestimo);
        this.qtdLivrosEmprestados++;
    }
    
    public boolean registrarDevolucaoLivro(int codigoLivro, LocalDate dataDevolucao){
        for (EmprestimoLivro emprestimo : this.historico){
            if (emprestimo.getCodigoLivro() == codigoLivro && emprestimo.isPendente()){
                emprestimo.setDataDevolucao(dataDevolucao);
                this.qtdLivrosEmprestados--;
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<EmprestimoLivro> getHistorico(){
        return this.historico;
    }
    
    public int getQuantidadeLivrosEmprestados(){
        return this.qtdLivrosEmprestados;
    }
    
    public boolean historicoVazio(){
        return getHistorico().isEmpty();
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("Endereço: " + this.getEndereco() + "\n");
        sb.append("Quantidade de Livros Emprestados: " + this.getQuantidadeLivrosEmprestados() + "\n");
        if (historicoVazio()){
            sb.append("Nenhum emprestimo de livro registrado");
        }
        else{
            sb.append("Livros Emprestados:\n");
            for (EmprestimoLivro emprestimo : historico){
                sb.append(emprestimo.toString() + "\n");
            }
        }
        
        return sb.toString();
    }
    
}
