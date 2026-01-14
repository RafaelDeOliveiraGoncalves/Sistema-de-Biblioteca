package biblioteca.validacao;

import java.util.regex.Pattern;

public class ValidaEndereco {
    
    public static boolean validaEndereco(String endereco){
        if (enderecoVazio(endereco)){
            return false;
        }
        return ENDERECO_PATTERN.matcher(endereco).matches();
    }
    
    private static final Pattern ENDERECO_PATTERN = Pattern.compile(
            "^[\\p{L}0-9\\.,\\-\\s]+\\d{1,5}.*$"
    );
    
    private static boolean enderecoVazio(String endereco){
        if (endereco == null || endereco.isBlank()){
            return true;
        }
        return false;
    }
}
