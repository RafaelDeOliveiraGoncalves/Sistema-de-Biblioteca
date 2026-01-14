package biblioteca.excecoes;

public class NenhumaCopiaEmprestadaException extends Exception{
    public NenhumaCopiaEmprestadaException(){
        super("Nenhuma copia desse livro foi emprestada");
    } 
}
