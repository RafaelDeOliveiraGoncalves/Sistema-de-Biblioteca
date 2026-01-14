package biblioteca.validacao;

import biblioteca.excecoes.PesoException;
import biblioteca.excecoes.AlturaException;

public class ValidaPesoAltura {
    public static void validaPeso(double peso) throws PesoException{
        if (!pesoEntre40E300(peso)){
            throw new PesoException();
        }
    }
    
    public static void validaAltura(double altura) throws AlturaException{
        if (!alturaEntreMeioETres(altura)){
            throw new AlturaException();
        }
    }
    
    private static boolean pesoEntre40E300(double peso){
        if (peso<40 || peso>300){
            return false;
        }
        return true;
    }
    
    private static boolean alturaEntreMeioETres(double altura){
        if (altura < 0.50 || altura > 3.0){
            return false;
        }
        return true;
    }
    
    
}
