package biblioteca.validacao;

import java.util.InputMismatchException;
import biblioteca.excecoes.CpfException;

public class ValidaCpf {
    
    public static boolean isCPF(String CPF){
        return (ValidaCPF(CPF));
    }

    public static String imprimeCPF(String CPF){
        String cpf = removerCaracteresEspecificosDoCpf(CPF);
        if(!verificaSeCpfTemOnzeCaracteres(cpf)){
            cpf = preencheInicioCpfComZeros(cpf);
        }
        return(cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." +
        cpf.substring(6, 9) + "-" + cpf.substring(9, 11));
    }
        
    public static long toLong(String CPF) throws CpfException{
        if(isCPF(CPF)){
            String cpf = removerCaracteresEspecificosDoCpf(CPF);
            long numCPF = Long.parseLong(cpf);
            return numCPF;
        }
        else{
            throw new CpfException();  
        }
    }
    
    private static boolean ValidaCPF(String CPF){
        if(CPF == null) return false;
        
        boolean formatoValido = CPF.matches("\\d{11}") || CPF.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}") || CPF.matches("\\d{3}\\.\\d{3}\\.\\d{3}/\\d{2}");;
        if(!formatoValido) return false;
        
        CPF = removerCaracteresEspecificosDoCpf(CPF);
        
        // considera-se erro CPF"s formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
            CPF.equals("11111111111") ||
            CPF.equals("22222222222") || CPF.equals("33333333333") ||
            CPF.equals("44444444444") || CPF.equals("55555555555") ||
            CPF.equals("66666666666") || CPF.equals("77777777777") ||
            CPF.equals("88888888888") || CPF.equals("99999999999"))
            return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48);

            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                 dig11 = '0';
            else dig11 = (char)(r + 48);

            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                 return(true);
            else return(false);
        } 
        catch (InputMismatchException erro) {
                return false;
        }
    }
    
    private static String removerCaracteresEspecificosDoCpf(String cpf){
        return cpf.replaceAll("[\\.\\-/]", "");
    }
    
    private static boolean verificaSeCpfTemOnzeCaracteres(String cpf){
        if (cpf.length()<11){
            return false;
        }
        return true;
    }
    
    private static String preencheInicioCpfComZeros(String cpf){
        int tamanhoPreencher = 11 - cpf.length();
        StringBuilder CPF = new StringBuilder();
        for (int i = 0; i<tamanhoPreencher; i++){
            CPF.append('0');
        }
        CPF.append(cpf);
        
        return CPF.toString();
    }
}
