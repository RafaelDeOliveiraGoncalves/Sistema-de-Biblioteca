package biblioteca.excecoes;

import biblioteca.validacao.ValidaCpf;

public class UsuarioNaoCadastradoException extends Exception{
    
    public UsuarioNaoCadastradoException(){
        super("Usuário não cadastrado no sistema");
    }

    public UsuarioNaoCadastradoException(long cpf){
        super("Usuário com CPF: " + ValidaCpf.imprimeCPF(String.valueOf(cpf)) + "não cadastrado.");
    }
    
}
