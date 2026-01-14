package biblioteca.excecoes;

public class AlturaException extends Exception{
    public AlturaException(){
        super("Altura fora do intervalo permitido pelo sistema");
    }
}
