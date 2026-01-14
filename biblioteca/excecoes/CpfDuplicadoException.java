package biblioteca.excecoes;

public class CpfDuplicadoException extends Exception{
    public CpfDuplicadoException(){
        super("Usuário já cadastrado");
    }
}
