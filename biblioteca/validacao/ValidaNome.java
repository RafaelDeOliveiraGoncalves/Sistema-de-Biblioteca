package biblioteca.validacao;

import biblioteca.excecoes.NomeException;

public class ValidaNome {
    public static void validaNome(String nome) throws NomeException{
        verificaNomeVazio(nome);
        verificaNomeApenasComLetra(nome);        
    }
    
    private static void verificaNomeApenasComLetra(String nome) throws NomeException{
        if (!nome.matches("[\\p{L}]+([\\p{L}\\s\\-']*[\\p{L}]+)?")) {
            throw new NomeException("Nome Inválido! Nome deve conter apenas letras");
        }
    }
    
    private static void verificaNomeVazio(String nome) throws NomeException{
        if (nome==null || nome.isBlank()){
            throw new NomeException("Nome não pode ser nulo!");
        }
    }
}
