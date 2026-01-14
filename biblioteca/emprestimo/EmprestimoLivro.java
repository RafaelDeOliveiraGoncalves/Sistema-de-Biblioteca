package biblioteca.emprestimo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import biblioteca.validacao.ValidaData;


public class EmprestimoLivro implements Serializable{
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private int codigoLivro;
    
    public EmprestimoLivro(LocalDate dataEmprestimo, int codigo){
        this.dataEmprestimo = dataEmprestimo;
        this.codigoLivro = codigo;
        this.dataDevolucao = null;
    }
    
    public String getDataEmprestimo(){
        return ValidaData.dataString(dataEmprestimo);
    }
    
    public int getCodigoLivro(){
        return this.codigoLivro;
    }
    
    public String getDataDevolucao(){
        return (!this.isPendente()) ? ValidaData.dataString(dataDevolucao) : "Pendente";
    }
    
    public void setDataDevolucao(LocalDate dataDevolucao){
        this.dataDevolucao = dataDevolucao;
    }
    
    public boolean isPendente(){
        return this.dataDevolucao == null;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Código do Livro: " + getCodigoLivro() + "\n");
        sb.append("Data de Emprestimo: " + getDataEmprestimo() + "\n");
        sb.append("Data de Devolução: " + getDataDevolucao() + "\n");
        
        return sb.toString();
    }
}
