package biblioteca.emprestimo;

import java.io.Serializable;
import java.time.LocalDate;
import biblioteca.excecoes.DataException;
import biblioteca.excecoes.CpfException;
import biblioteca.validacao.ValidaCpf;
import biblioteca.validacao.ValidaData;

public class EmprestimoUsuario implements Serializable{
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private String cpfUsuario;
    
   public EmprestimoUsuario(LocalDate dataEmprestimo, String cpf) throws DataException, CpfException{
       setDataEmprestimo(dataEmprestimo);
       setCpfUsuario(cpf);
   }
   
   private void setDataEmprestimo(LocalDate dataEmprestimo) throws DataException{
       ValidaData.validaData(dataEmprestimo);
       this.dataEmprestimo = dataEmprestimo;
   }
   
   private void setCpfUsuario(String cpf) throws CpfException{
       if(ValidaCpf.isCPF(cpf)){
           this.cpfUsuario = cpf;
       }
       else {
           throw new CpfException();
       }
   }
   
   public void setDataDevolucao(LocalDate dataDevolucao) throws DataException{
       ValidaData.validaData(dataDevolucao);
       this.dataDevolucao = dataDevolucao;
   }
   
   public String getDataEmprestimo(){
       return ValidaData.dataString(dataEmprestimo);
   }
   
   public String getDataDevolucao(){
       return (!isPendente()) ? ValidaData.dataString(dataDevolucao) : "Pendente";
   }
   
   public String getCpfUsuario(){
       return ValidaCpf.imprimeCPF(cpfUsuario);
   }
   
   public boolean isPendente(){
       return this.dataDevolucao == null;
   }
   
   public String toString(){
       StringBuilder sb = new StringBuilder();
       sb.append("CPF do usuário: " + getCpfUsuario());
       sb.append("Data do Empréstimo: " + getDataEmprestimo());
       sb.append("Data da Devolução: " + getDataDevolucao());
       
       return sb.toString();
       
   }
   
}
