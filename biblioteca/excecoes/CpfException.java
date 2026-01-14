package biblioteca.excecoes;

public class CpfException extends Exception{
    public CpfException(){
        super("CPF inválido");
    }
}
