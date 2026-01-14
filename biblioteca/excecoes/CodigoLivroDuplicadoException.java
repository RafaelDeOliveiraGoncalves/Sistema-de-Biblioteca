package biblioteca.excecoes;

public class CodigoLivroDuplicadoException extends Exception{
    public CodigoLivroDuplicadoException(int codigo){
        super("Ja existe um livro cadastrado com o código: " + codigo);
    }
}
