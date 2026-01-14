package biblioteca.validacao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.time.Period;
import biblioteca.excecoes.DataException;


public class ValidaData {
    public static LocalDate dataValida(String data) throws DataException{
        if (dataVazia(data)){
            throw new DataException("Data Vazia");
        }
        String[] formatos = {"dd/MM/yyyy", "d/M/yyyy", "dd-MM-yyyy", "d-M-yyyy", "dd.MM.yyyy",
                            "d.M.yyyy", "yyyy-MM-dd", "yyyy/MM/dd", "ddMMyyyy", "yyyyMMdd",
                            "d MMM yyyy", "d MMMM yyyy", "d M yyyy", "dd MM yyyy"
        };
        
        LocalDate DATA = null;
        data = paraMinuscula(data);
        
        for(String formato : formatos){
            try{
                DateTimeFormatter dtf = formato.contains("MMM") 
                        ? DateTimeFormatter.ofPattern(formato, Locale.forLanguageTag("pt-BR")) 
                        : DateTimeFormatter.ofPattern(formato);
                DATA = LocalDate.parse(data,dtf);
                break;
            }
            catch(Exception e){
                continue;
            }
        }
        validaData(DATA);
        return DATA;
    } 
    
    public static String dataString(LocalDate data){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(dtf);
    }
    
    public static void validaData(LocalDate data) throws DataException{
        dataNula(data);
        dataDepoisDaDataAtual(data);
        idadeMuitoAlta(data);
    }
    
    private static boolean dataVazia(String data){
        return (data==null || data.isBlank());
    }
    
    private static void dataNula(LocalDate data) throws DataException{
        if(data==null){
            throw new DataException("Data Vazia");
        }
    }
    
    private static void dataDepoisDaDataAtual(LocalDate data) throws DataException{
        if (data.isAfter(LocalDate.now())){
            throw new DataException("Data Inválida! Data ainda não aconteceu");
        }
    }
    
    private static void idadeMuitoAlta(LocalDate data) throws DataException{
        int idade = getIdade(data);
        if (idade > 120){
            throw new DataException("Data Inválida! Período muito longo");
        }
    }
    
    public static int getIdade(LocalDate data){
        int idade = Period.between(data, LocalDate.now()).getYears();
        return idade;
    }
    
    private static String paraMinuscula(String texto){
        if(texto==null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < texto.length(); i++){
            char c = texto.charAt(i);
            
            if(Character.isLetter(c)){
                sb.append(Character.toLowerCase(c));
            }
            else{
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
