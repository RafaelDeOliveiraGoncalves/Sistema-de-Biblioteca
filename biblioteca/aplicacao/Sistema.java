package biblioteca.aplicacao;

import java.util.Scanner;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import biblioteca.excecoes.*;
import biblioteca.modelo.Biblioteca;
import biblioteca.modelo.Usuario;
import biblioteca.modelo.Livro;
import biblioteca.emprestimo.EmprestimoLivro;
import biblioteca.validacao.ValidaData;


public class Sistema {
    
    private static Scanner scan = new Scanner(System.in);
    private static Biblioteca biblioteca;
    private static String arquivoUsuarioAtual;
    private static String arquivoLivroAtual;
    private static int MAXIMO_LIVROS_POR_USUARIOS;
    public static int DIAS_LIMITE_EMPRESIMO;
    
    public static void carregarConfiguracoes(){
        Properties propriedades = new Properties();
        try(FileInputStream fis = new FileInputStream("config.properties")){
            propriedades.load(fis);
            MAXIMO_LIVROS_POR_USUARIOS = Integer.parseInt(propriedades.getProperty("limite.livros.usuario","3"));
            DIAS_LIMITE_EMPRESIMO = Integer.parseInt(propriedades.getProperty("limite.dias.emprestimo", " 14"));
            arquivoUsuarioAtual = propriedades.getProperty("arquivo.padrao.usuarios", "u.dat");
            arquivoLivroAtual = propriedades.getProperty("arquivo.padrao.livros","l.dat");
            System.out.println("Configurações carregadas: Limite de " + MAXIMO_LIVROS_POR_USUARIOS + " livros por " + DIAS_LIMITE_EMPRESIMO + "dias.");
        }
        catch(IOException e){
            System.err.println("Erro ao carregar 'config.properties'. Usando valores padrão.");
            MAXIMO_LIVROS_POR_USUARIOS = 3;
            DIAS_LIMITE_EMPRESIMO = 14;
            arquivoUsuarioAtual = "u.dat";
            arquivoLivroAtual = "l.dat";
        }
    }
    
    public static void iniciaPrograma(){
        System.out.println("=== Bem-vindo ao Sistema de Biblioteca ===");
        System.out.println("1. Iniciar com cadastro zerado");
        System.out.println("2. Carregar arquivos padrão (" + arquivoUsuarioAtual + "," + arquivoLivroAtual + ")");
        System.out.println("3. Carregar arquivos específicos");
        System.out.println("4. Gerar arquivos de dados (u.dat, l.dat) para entrega"); //// sujeito a remoção
        
        int opcao = lerInt("Escolha uma opcão: ");
        
        switch(opcao){
            case 1:
                biblioteca = new Biblioteca();
                System.out.println("Biblioteca iniciada com cadastros zerados.");
                break;
            case 2:
                biblioteca = new Biblioteca(arquivoUsuarioAtual, arquivoLivroAtual);
                System.out.println("Biblioteca carregada dos arquivos padrão.");
                break;
            case 3:
                String arqUsuarios = lerString("Nome do arquivo de usuários: ");
                String arqLivros = lerString("Nome do arquivo de livros: ");
                biblioteca = new Biblioteca(arqUsuarios, arqLivros);
                arquivoUsuarioAtual = arqUsuarios;
                arquivoLivroAtual = arqLivros;
                System.out.println("Biblioteca carregada dos arquivos específicos");
                break;
            case 4:
                gerarDados();
                if (biblioteca == null){
                    biblioteca = new Biblioteca();
                }
                break;
            default:
                System.out.println("Opcão inválida. Iniciando com cadastro zerado:");
                biblioteca = new Biblioteca();
        }
    }
    
    public static void menuPrincipal(){
        boolean rodando = true;
        while(rodando){
            System.out.println("\n===MENU PRINCIPAL===");
            System.out.println("1. Manutenção de Arquivos");
            System.out.println("2. Cadastro (Usuários/Livros)");
            System.out.println("3. Emprestimo/Devolução)");
            System.out.println("4. Relatórios");
            System.out.println("5. Sair e Salvar");
            
            int opcao = lerInt("Escolha um módulo: ");
            
            switch(opcao){
                case 1: menuManutencao(); break;
                case 2: menuCadastro(); break;
                case 3: menuEmprestimo(); break;
                case 4: menuRelatorio(); break;
                case 0:
                    salvarArquivos(arquivoUsuarioAtual, arquivoLivroAtual);
                    rodando = false;
                    break;
                default:
                    System.out.println("Opção Inválida");
            }
        }
    }
    
    private static void menuManutencao(){
        System.out.println("\n(MANUTENÇÃO)");
        System.out.println("1. Salvar (nos arquivos atuais: " + arquivoUsuarioAtual + ", " + arquivoLivroAtual + ")");
        System.out.println("2. Salvar como ");
        System.out.println("3. Carregar Arquivos (Substitui dados atuais)");
        System.out.println("0. Voltar");
        
        int opcao = lerInt("Opção: ");
        
        switch(opcao){
            case 1:
                salvarArquivos(arquivoUsuarioAtual, arquivoLivroAtual);
                break;
            case 2:
                System.out.println("\n===Salvar como===");
                String arqUsuarios = lerString("Novo nome do arquivo de usuários (atual: " + arquivoUsuarioAtual + "):");
                String arqLivros = lerString("Novo nome do arquivo de livros (atual: " + arquivoLivroAtual + "): ");
                salvarArquivos(arqUsuarios, arqLivros);
                arquivoUsuarioAtual = arqUsuarios;
                arquivoLivroAtual = arqLivros;
                System.out.println("Arquivos salvos com novos nomes.");
                break;
            case 3:
                System.out.println("Atenção: Os dados atuais em memória serão perdidos.");
                iniciaPrograma();
                break;
            case 0:
                break;
            default:
                System.out.println("Opção Inválida.");
        }
    }
    
    private static void salvarArquivos(String nomeArquivoUsuarios, String nomeArquivoLivros){
        try{
            biblioteca.salvarArquivo(biblioteca.getCadastroUsuarios(), nomeArquivoUsuarios);
            biblioteca.salvarArquivo(biblioteca.getCadastroLivros(), nomeArquivoLivros);
            System.out.println("Arquivos salvos com sucesso!");
        }
        catch(IOException e){
            System.out.println("Erro ao salvar arquivos: " + e.getMessage());
        }
    }
    
    private static void menuCadastro(){
        System.out.println("\n(CADASTRO)");
        System.out.println("1. Cadastrar Novo Usuário");
        System.out.println("2. Cadastrar Novo Livro");
        System.out.println("3. Salvar Cadastro");
        System.out.println("0. Voltar");
        
        int opcao = lerInt("Escolha uma opção: ");
        
        switch(opcao){
            case 1: cadastrarNovoUsuario(); break;
            case 2: cadastrarNovoLivro(); break;
            case 3: subMenuSalvarCadastro(); break;
            case 0: break;
            default:
                System.out.println("Opção Inválida.");
        }
    }
    
    private static void cadastrarNovoUsuario(){
        try{
            System.out.println("\n===Cadastro de Usuário===");
            String nome = lerString("Nome: ");
            String sobrenome = lerString("Sobrenome: ");
            String dataNascimento = lerString("Data de Nascimento: ");
            String cpf = lerString("CPF: ");
            double peso = lerDouble("Peso: ");
            double altura = lerDouble("Altura: ");
            String endereco = lerString("Endereço: ");
            
            Usuario usuario = new Usuario(nome, sobrenome, dataNascimento, cpf, peso, altura, endereco);
            biblioteca.cadastraUsuario(usuario);
            
            System.out.println("Usuário cadastrado com sucesso!");
            System.out.println(usuario.toString());
        }
        catch(NomeException | DataException | CpfException | PesoException | AlturaException | EnderecoException e){
            System.err.println("Erro no cadastro: " + e.getMessage());
        }
        catch(Exception e){
            System.err.println("ERRO INESPERADO: " + e.getMessage());
        }
    }
    
    private static void cadastrarNovoLivro(){
        try{
            System.out.println("\n===Cadatro de Livros===");
            
            int codigo = lerInt("Código do Livro (1-999): ");
            String titulo = lerString("Título: ");
            String categoria = lerString("Categoria: ");
            int quantidade = lerInt("Quantidade inicial de cópias: ");
            
            Livro livro = new Livro(codigo, titulo, categoria, quantidade);
            biblioteca.cadastraLivro(livro);
            
            System.out.println("Livro cadastrado com sucesso!");
            System.out.println(livro.toString());
        }
        catch(CodigoLivroDuplicadoException | CodigoException e){
            System.err.println("Erro no cadastro: " + e.getMessage());
        }
        catch(Exception e){
            System.err.println("ERRO INESPERADO: " + e.getMessage());
        }
    }
    
    private static void subMenuSalvarCadastro(){
        System.out.println("===Salvar Cadastro(em nov arquivo)===");
        System.out.println("1. Salvar Somente Usuários");
        System.out.println("2. Salvar Somente Livros");
        System.out.println("0. Cancelar");
        
        
        int opcao = lerInt("Escolha uma opção: ");
        try{
            switch(opcao){
                case 1:
                    String arqUsuario = lerString("Nome do arquivo de backup de usuários: ");
                    biblioteca.salvarArquivo(biblioteca.getCadastroUsuarios(), arqUsuario);
                    System.out.println("Usuários salvos em " + arqUsuario);
                    break;
                case 2:
                    String arqLivros = lerString("Nome do aruivo de backup de livros: ");
                    biblioteca.salvarArquivo(biblioteca.getCadastroLivros(), arqLivros);
                    System.out.println("Livros salvos em " + arqLivros);
                case 0:
                    break;
                default:
                    System.out.println("Opção Inválida.");
            }
        }
        catch(IOException e){
            System.out.println("Erro ao salvar arquivos " + e.getMessage());
        }
    } 
    
    private static void menuEmprestimo(){
        System.out.println("\n===EMPRESTIMO===");
        System.out.println("1. Fazer um emprestimo");
        System.out.println("2. Fazer uma devolução");
        System.out.println("3. Listar livros");
        System.out.println("0. Voltar");
        
        int opcao = lerInt("Escolha uma opção: ");
        
        switch(opcao){
            case 1: fazerEmprestimo(); break;
            case 2: fazerDevolucao(); break;
            case 3: System.out.println(biblioteca.imprimeLivros()); break;
            case 0: break;
            default:
                System.out.println("Opção Inválida");
        }
    }
    
    private static void fazerEmprestimo(){
        System.out.println("\n===Fazer Emprestimo===");
        try{
            long cpf = lerLong("Digite o CPF do usuário: ");
            Usuario usuario = biblioteca.getUsuario(cpf);
            
            if(usuario.getQuantidadeLivrosEmprestados() >= MAXIMO_LIVROS_POR_USUARIOS){
                System.out.println("Usuário já atingiu o limite de " + MAXIMO_LIVROS_POR_USUARIOS + " livros.");
                return;
            }
            if(temAtraso(usuario)){
                System.out.println("Usuario possui devoluções em atraso!");
                return;
            }
            
            int codigo = lerInt("Digite o código do Livro: ");
            Livro livro = biblioteca.getLivro(codigo);
            
            biblioteca.emprestaLivro(usuario, livro);
            
            System.out.println("\n===Recibo de Empréstimo===");
            System.out.println("Empréstimo bem-sucedido!");
            System.out.println("Usuário: " + usuario.getNome() + " " + usuario.getSobrenome());
            System.out.println("Livro: " + livro.getTitulo() + "(Disponíveis agora: " + livro.getQuantidadeDisponivel() + ")");
            System.out.println("Data Limite para Devolução: " + LocalDate.now().plusDays(DIAS_LIMITE_EMPRESIMO));
        }
        catch(UsuarioNaoCadastradoException | LivroNaoCadastradoException | CopiaNaoDisponivelException e){
            System.err.println("Erro no empréstimo: " + e.getMessage());
        }
        catch(Exception e){
            System.err.println("ERRO INESPERADO: " + e.getMessage());
        }
    }
    
    private static boolean temAtraso(Usuario usuario) throws DataException{
        LocalDate hoje = LocalDate.now();
        for (EmprestimoLivro emprestimo : usuario.getHistorico()){
            if (emprestimo.isPendente()){
                LocalDate dataEmprestimo = ValidaData.dataValida(emprestimo.getDataEmprestimo());
                long diasUsando = biblioteca.calculaDiasUsando(dataEmprestimo,hoje);
                if(diasUsando > DIAS_LIMITE_EMPRESIMO){
                    return true;
                }
            }
        }
        return false;
    }
    
    private static void fazerDevolucao(){
        System.out.println("\n===Fazer Devolução===");
        try{
            long cpf = lerLong("Digite o CPF do Usuário: ");
            Usuario usuario = biblioteca.getUsuario(cpf);
            
            int codigo = lerInt("Digiteo Código do Livro: ");
            Livro livro = biblioteca.getLivro(codigo);
            
            biblioteca.devolveLivro(usuario, livro, (long) DIAS_LIMITE_EMPRESIMO);
            
            System.out.println("\n===Recibo de Devolução===");
            System.out.println("Devolução bem-sucedida");
            System.out.println("Usuario " + usuario.getNome() + " " + usuario.getSobrenome());
            System.out.println("Livro: " + livro.getTitulo() + "(Disponíveis agora: " + livro.getQuantidadeDisponivel() + ")");
        }
        catch(UsuarioNaoCadastradoException | LivroNaoCadastradoException | NenhumaCopiaEmprestadaException e){
            System.out.println("Erro na Devolução: " + e.getMessage());
        }
        catch(Exception e){
            System.out.println("ERRO INESPERADO: " + e.getMessage());
        }
    }
    
    private static void menuRelatorio(){
        System.out.println("\n===RELATÓRIOS===");
        System.out.println("1. Listar Todos os Usuários (Ordenados por CPF)");
        System.out.println("2. Listar Todos os Livros (Ordenados por Códigos)");
        System.out.println("3. Detalhes de um usuário específico (com histórico)");
        System.out.println("4. Detalhes de um livro específico (com histórico)");
        System.out.println("0. Voltar");
        
        int opcao = lerInt("Escolha uma opção: ");
        switch(opcao){
            case 1:
                System.out.println(biblioteca.imprimeUsuarios());
                break;
            case 2:
                System.out.println(biblioteca.imprimeLivros());
                break;
            case 3:
                try{
                    long cpf = lerLong("Digite o CPF do usuário: ");
                    System.out.println(biblioteca.getUsuario(cpf).toString());
                }
                catch(UsuarioNaoCadastradoException e){
                    System.out.println(e.getMessage());
                }
                break;
            case 4:
                try{
                    int codigo = lerInt("Digite o código do livro: ");
                    System.out.println(biblioteca.getLivro(codigo).toString());
                }
                catch(LivroNaoCadastradoException e){
                    System.out.println(e.getMessage());
                }
                break;
            case 0:
                break;
            default:
                System.out.println("Opção Inválida.");
        }
    }
    
    private static int lerInt(String texto){
        while(true){
            try{
                System.out.print(texto);
                int valor = scan.nextInt();
                scan.nextLine();
                return valor;
            }
            catch(Exception e){
                System.out.println("Entrada Inválida. Por favor, digite um número inteiro.");
                scan.nextLine();
            }
        }
    }
    
    private static long lerLong(String texto){
        while(true){
            try{
                System.out.print(texto);
                long valor = scan.nextLong();
                scan.nextLine();
                return valor;
            }
            catch(Exception e){
                System.out.println("Entrada inválida. Por favor, digete um número tipo long");
                scan.nextLine();
            }
        }
    }
    
    private static double lerDouble(String texto){
        while(true){
            try{
                System.out.print(texto);
                double valor = scan.nextDouble();
                scan.nextLine();
                return valor;
            }
            catch(Exception e){
                System.out.println("Entrada inválida. Por favor, digite um número decimal");
                scan.nextLine();
            }
        }
    }
    
    private static String lerString(String texto){
        System.out.print(texto);
        return scan.nextLine();
    }
    
    public static void gerarDados(){
        System.out.println("Gerando arquivos de dados (" + arquivoUsuarioAtual + ", " + arquivoLivroAtual +")");
        biblioteca = new Biblioteca();
        
        try{
            Usuario u1 = new Usuario("Ana", "Silva", "14/09/2001", "529.982.247-25", 70, 1.6, "Rua A, 1");
            Usuario u2 = new Usuario("Bruno", "Costa", "14/03/1998", "123.456.789-09", 90, 1.7, "Rua B, 2");
            Usuario u3 = new Usuario("Carla", "Dias", "27/11/2004", "111.444.777-35", 60, 1.5, "Rua C, 3");
            Usuario u4 = new Usuario("Davi", "Luz","18/09/2002", "390.533.447-05", 80, 1.9, "Rua D, 4");
            Usuario u5 = new Usuario("Elisa", "Rios", "03/05/1995", "987.654.321-00", 55, 1.6, "Rua E, 5");
            
            biblioteca.cadastraUsuario(u1);
            biblioteca.cadastraUsuario(u2);
            biblioteca.cadastraUsuario(u3);
            biblioteca.cadastraUsuario(u4);
            biblioteca.cadastraUsuario(u5);
            
            Livro l1 = new Livro(101, "Java Como Programar", "FICCAO", 3);
            Livro l2 = new Livro(102, "O Senhor dos Anéis", "FANTASIA", 2);
            Livro l3 = new Livro(103, "O Código Da Vinci", "SUSPENSE", 5);
            Livro l4 = new Livro(104, "Sapiens", "BIOGRAFIAS", 1);
            Livro l5 = new Livro(105, "Moby Dick", "AVENTURA", 3);
            
            biblioteca.cadastraLivro(l1);
            biblioteca.cadastraLivro(l2);
            biblioteca.cadastraLivro(l3);
            biblioteca.cadastraLivro(l4);
            biblioteca.cadastraLivro(l5);
            
            biblioteca.emprestaLivro(u1, l1);
            biblioteca.emprestaLivro(u2, l2);
            biblioteca.devolveLivro(u2, l2, DIAS_LIMITE_EMPRESIMO);
            biblioteca.emprestaLivro(u3, l4);
            biblioteca.emprestaLivro(u4, l5);
            biblioteca.emprestaLivro(u5, l3);
            biblioteca.devolveLivro(u4, l5, DIAS_LIMITE_EMPRESIMO);
            
            biblioteca.salvarArquivo(biblioteca.getCadastroUsuarios(), arquivoUsuarioAtual);
            biblioteca.salvarArquivo(biblioteca.getCadastroLivros(), arquivoLivroAtual);
            
            System.out.println("Arquivos '" + arquivoUsuarioAtual + "' e '" + arquivoLivroAtual + "' gerados com sucesso!");
            System.out.println("\n===Dados Gerados===");
            System.out.println(biblioteca.imprimeUsuarios());
            System.out.println(biblioteca.imprimeLivros());
        }
        catch(Exception e){
            System.err.println("Erro ao Gerar ao Dados:");
            e.printStackTrace();
        }
    }
}
