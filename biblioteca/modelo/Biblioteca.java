package biblioteca.modelo;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import biblioteca.excecoes.CpfDuplicadoException;
import biblioteca.excecoes.CpfException;
import biblioteca.excecoes.CodigoLivroDuplicadoException;
import biblioteca.excecoes.CopiaNaoDisponivelException;
import biblioteca.excecoes.DataException;
import biblioteca.excecoes.LivroNaoCadastradoException;
import biblioteca.excecoes.NenhumaCopiaEmprestadaException;
import biblioteca.excecoes.UsuarioNaoCadastradoException;
import biblioteca.emprestimo.EmprestimoLivro;


public class Biblioteca {
    private HashMap<Long, Usuario> cadastroUsuarios;
    private HashMap<Integer, Livro> cadastroLivros;
    
    public Biblioteca(){
        this.cadastroUsuarios = new HashMap<>();
        this.cadastroLivros = new HashMap<>();
    }
    
    public Biblioteca(String arquivoUsuario, String arquivoLivros){
        this.cadastroUsuarios = carregarDados(arquivoUsuario);
        this.cadastroLivros = carregarDados(arquivoLivros);
    }
    
    public void cadastraUsuario(Usuario usuario) throws CpfDuplicadoException, CpfException{
        long cpfKey = biblioteca.validacao.ValidaCpf.toLong(usuario.getCpf());
        if(usuarioCadastrado(cpfKey)){
            throw new CpfDuplicadoException();
        }
        this.cadastroUsuarios.put(cpfKey, usuario);
    }
    
    public void cadastraLivro(Livro livro) throws CodigoLivroDuplicadoException{
        int codKey = livro.getCodigoLivro();
        if(livroCadastrado(codKey)){
            throw new CodigoLivroDuplicadoException(codKey);
        }
        this.cadastroLivros.put(codKey, livro);
    }
    
    public Livro getLivro(int codigoLivro) throws LivroNaoCadastradoException{
        if(!livroCadastrado(codigoLivro)){
            throw new LivroNaoCadastradoException();
        }
        return this.cadastroLivros.get(codigoLivro);
    }
    
    public Usuario getUsuario(long cpf) throws UsuarioNaoCadastradoException{
        if(!usuarioCadastrado(cpf)){
            throw new UsuarioNaoCadastradoException();
        }
        return this.cadastroUsuarios.get(cpf);
    }
    
    public void emprestaLivro(Usuario usuario, Livro livro) throws CopiaNaoDisponivelException, CpfException, DataException{
        LocalDate hoje = LocalDate.now();
        livro.empresta();
        livro.addUsuarioHistorico(hoje, usuario.getCpf());
        usuario.addLivroHistorico(hoje, livro.getCodigoLivro());
    }
    
    public void devolveLivro(Usuario usuario, Livro livro, long maximoDias) throws NenhumaCopiaEmprestadaException, DataException{
        LocalDate hoje = LocalDate.now();
        boolean usando = usuario.registrarDevolucaoLivro(livro.getCodigoLivro(), hoje);
        if(!usando){
            throw new NenhumaCopiaEmprestadaException();
        }
        boolean usandoLivro = livro.registrarDevolucao(usuario.getCpf(), hoje);
        livro.devolve();
        verificaMulta(usuario, livro.getCodigoLivro(),hoje, maximoDias);

    }
    
    public void verificaMulta(Usuario usuario, int codigoLivro, LocalDate dataDevolucao, long maximoDias) throws DataException{
        for(EmprestimoLivro emprestimo : usuario.getHistorico()){
            if(achouLivro(emprestimo,codigoLivro) && emprestimo.isPendente() && dataDevolucaoEHoje(emprestimo,dataDevolucao)){
                LocalDate dataEmprestimo = biblioteca.validacao.ValidaData.dataValida(emprestimo.getDataEmprestimo());
                long diasEmprestados = calculaDiasUsando(dataEmprestimo, dataDevolucao);
                
                if(diasEmprestados > maximoDias){
                    System.out.println("Usuário " + usuario.getNome() + " está com " + (diasEmprestados - maximoDias) + "dias atrasados");
                }
                break;
            }
        }
    }
    
    public String imprimeLivros(){
        List<Integer> codigos = new ArrayList<>(this.cadastroLivros.keySet());
        Collections.sort(codigos);
        StringBuilder sb = new StringBuilder("===Cadastro de Livros (Total: " + codigos.size() + ")===\n");
        for(Integer codigo : codigos){
            sb.append(this.cadastroLivros.get(codigo).toString() + "\n");
        }
        return sb.toString();
    }
    
    public String imprimeUsuarios(){
        List<Long> cpfs = new ArrayList<>(this.cadastroUsuarios.keySet());
        Collections.sort(cpfs);
        StringBuilder sb = new StringBuilder("===Cadastro de Usuários (Total: " + cpfs.size() + ")===\n");
        for(Long cpf : cpfs){
            sb.append(this.cadastroUsuarios.get(cpf).toString() + "\n");
        }
        return sb.toString();
    }
    
    public void salvarArquivo(HashMap<?,?> hashMap, String nomeArquivo) throws IOException{
        try(FileOutputStream fis = new FileOutputStream(nomeArquivo);
            ObjectOutputStream oos = new ObjectOutputStream(fis)){
            oos.writeObject(hashMap);
        }
    }
    
    private <K,V> HashMap<K,V> carregarDados(String nomeArquivo){
        try(FileInputStream fis = new FileInputStream(nomeArquivo);
            ObjectInputStream ois = new ObjectInputStream(fis)){
            return (HashMap<K,V>) ois.readObject();
        }
        catch(FileNotFoundException e){
            System.err.println("Arquivo " + nomeArquivo + " não encontrado");
            return new HashMap<K,V>();
        }
        catch(IOException | ClassNotFoundException e){
            System.err.println("Erro ao ler " + nomeArquivo + ": " + e.getMessage());
            e.printStackTrace();
            return new HashMap<K,V>();
        }
    }
    
    public void lerArquivoUsuario(String nomeArquivo){
        this.cadastroUsuarios = carregarDados(nomeArquivo);
    }
    
    public void lerArquivoLivros(String nomeArquivo){
        this.cadastroLivros = carregarDados(nomeArquivo);
    }
    
    public HashMap<Long, Usuario> getCadastroUsuarios(){
        return this.cadastroUsuarios;
    }
    
    public HashMap<Integer, Livro> getCadastroLivros(){
        return this.cadastroLivros;
    }
    
    private boolean usuarioCadastrado(long cpf){
        return this.cadastroUsuarios.containsKey(cpf);
    }
    
    private boolean livroCadastrado(int codigoLivro){
        return this.cadastroLivros.containsKey(codigoLivro);
    }
    
    private boolean achouLivro(EmprestimoLivro emprestimo, int codigoLivro){
        return emprestimo.getCodigoLivro() == codigoLivro;
    }
    
    private boolean dataDevolucaoEHoje(EmprestimoLivro emprestimo, LocalDate dataDevolucao){
        return emprestimo.getDataDevolucao().equals(biblioteca.validacao.ValidaData.dataString(dataDevolucao));
    }
    
    public long calculaDiasUsando(LocalDate dataEmprestimo, LocalDate dataDevolucao){
        return ChronoUnit.DAYS.between(dataEmprestimo, dataDevolucao);
    }
    
}
