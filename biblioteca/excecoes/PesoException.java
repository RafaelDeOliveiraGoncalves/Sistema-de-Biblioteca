package biblioteca.excecoes;

public class PesoException extends Exception{
    public PesoException(){
        super("Peso fora do intervalo permitido pelo sistema");
    }
}
