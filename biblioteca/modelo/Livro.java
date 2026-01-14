package biblioteca.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDate;
import biblioteca.emprestimo.EmprestimoUsuario;
import biblioteca.excecoes.CodigoException;
import biblioteca.excecoes.CopiaNaoDisponivelException;
import biblioteca.excecoes.NenhumaCopiaEmprestadaException;
import biblioteca.excecoes.DataException;
import biblioteca.excecoes.CpfException;


public class Livro implements Serializable{
    private int codigoLivro;
    private String titulo;
    private CategoriaLivro categoria;
    private int qtdDisponivel;
    private int qtdEmprestado;
    private ArrayList<EmprestimoUsuario> historico;
    
    
    public Livro(int codigo, String titulo, String categoria, int qtdDisponivel) throws CodigoException{
        setCodigoLivro(codigo);
        this.titulo = titulo;
        setCategoriaLivro(categoria);
        setQuantidadeDisponivel(qtdDisponivel);
        this.qtdEmprestado = 0;
        this.historico = new ArrayList<>();
    } 
    
    public void setCodigoLivro(int codigo) throws CodigoException{
        if (codigoEntre1E999(codigo)){
            this.codigoLivro = codigo;
        }
        else{
            throw new CodigoException();  
        }
    }
    
    private void setCategoriaLivro(String categoria){
        for (CategoriaLivro categoriaLivro : CategoriaLivro.values()){
            if (categoriaLivro.name().equalsIgnoreCase(categoria)){
                this.categoria = categoriaLivro;
            }
        }
        
        if (this.categoria == null){
            this.categoria = CategoriaLivro.OUTROS;
        }
    }
    
    public void setQuantidadeDisponivel(int quantidade){
        this.qtdDisponivel = quantidade;
    }
    
    public int getCodigoLivro(){
        return this.codigoLivro;
    }
    
    public String getTitulo(){
        return this.titulo;
    }
    
    public String getCategoria(){
        return String.valueOf(this.categoria).substring(0, 1).toUpperCase() + String.valueOf(this.categoria).substring(1).toLowerCase();
    }
    
    public int getQuantidadeDisponivel(){
        return this.qtdDisponivel;
    }
    
    public int getQuantidadeEmprestados(){
        return this.qtdEmprestado;
    }
    
    public ArrayList<EmprestimoUsuario> getHistorico(){
        return this.historico;
    }
    
    public void empresta() throws CopiaNaoDisponivelException{
        if (copiaDisponivel()){
            this.qtdEmprestado++;
            this.qtdDisponivel--;
        }
        else{
            throw new CopiaNaoDisponivelException();
        }
    }
    
    public void devolve() throws NenhumaCopiaEmprestadaException{
        if(nenhumaCopiaEmprestada()){
            throw new NenhumaCopiaEmprestadaException();
        }
        this.qtdEmprestado--;
        this.qtdDisponivel++;
    }
    
    public boolean registrarDevolucao(String cpfUsuario, LocalDate dataDevolucao) throws DataException{
        for (EmprestimoUsuario registro : historico){
            if (registro.getCpfUsuario().equals(cpfUsuario) && registro.isPendente()){
                registro.setDataDevolucao(dataDevolucao);
                return true;
            }
        }
        return false;
    }
    
    public void addUsuarioHistorico(LocalDate dataEmprestimo, String cpfUsuario) throws CpfException, DataException{
        EmprestimoUsuario novoRegistro = new EmprestimoUsuario(dataEmprestimo, cpfUsuario);
        this.historico.add(novoRegistro);
    }
    
    private boolean codigoEntre1E999(int codigo){
        if (codigo >= 1 && codigo <= 999){
            return true;
        }
        return false;
    }
    
    private boolean copiaDisponivel(){
        return this.qtdDisponivel>0;
    }
    
    private boolean nenhumaCopiaEmprestada(){
        return this.qtdEmprestado==0;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Código do livro: " + getCodigoLivro() + "\n");
        sb.append("Titulo: " + getTitulo() + "\n" );
        sb.append("Categoria: " + getCategoria() + "\n");
        sb.append("Disponivel: " + getQuantidadeDisponivel() + "\n");
        sb.append("Emprestimo: " + getQuantidadeEmprestados() + "\n");
        
        return sb.toString();
    }
    
}
