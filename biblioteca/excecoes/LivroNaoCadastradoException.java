package biblioteca.excecoes;

public class LivroNaoCadastradoException extends Exception{
    
    public LivroNaoCadastradoException(){
        super("Livro não cadastrado no sistema");
    }
    
    public LivroNaoCadastradoException(int codigo){
        super("Livro com código: " + codigo + " não cadastrado");
    }
}
