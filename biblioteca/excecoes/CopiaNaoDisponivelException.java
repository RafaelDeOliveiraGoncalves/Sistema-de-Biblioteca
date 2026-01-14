package biblioteca.excecoes;

public class CopiaNaoDisponivelException extends Exception{
    public CopiaNaoDisponivelException(){
        super("Todas as unidades desse livro foram emprestados");
    }
}
