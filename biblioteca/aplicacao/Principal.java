package biblioteca.aplicacao;

public class Principal {
    public static void main(String[] args) {
        Sistema.carregarConfiguracoes();
        Sistema.iniciaPrograma();
        Sistema.menuPrincipal();
        System.out.println("Saindo e Salvando...");
    }
}
