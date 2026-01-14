# Mini Sistema de Biblioteca

Projeto desenvolvido em Java com foco em orientação a objetos, validações, regras de negócio e persistência em arquivos. O sistema simula o funcionamento básico de uma biblioteca, permitindo o cadastro de usuários e livros, além do controle de empréstimos e devoluções.
Este projeto foi desenvolvido com fins acadêmicos e de portifólio, demonstrando boas práticas de programação e modelagem de domínio.

## Objetivo do Projeto

O objetivo deste projeto é aplicar conceitos fundamentais de programação orientada a objetos, como:

- Encapsulamento
- Herança
- Tratamento de exceções
- Separação de responsabilidades
- Validação de dados
  Além disso, o sistema implementa regras reais de negócio, como limite de empréstimos, controle de cópias disponíveis e verificação de devoluções por usuário.

## Funcionalidades

- Cadastro de usuários com validações (nome, CPF, data, peso, altura)
- Cadastro de livros por categoria
- Empréstimo de livros com limite configurável
- Devolução de livros com verificação de posse
- Cálculo de atraso e multa
- Persistência de dados em arquivos
- Relatórios de usuários e livros

## Estrutura do Projeto

biblioteca:

- ├── aplicacao --> Classe principal e menu do sistema
- ├── modelo --> Entidades do domínio (Pessoa, Usuario, Livro, etc.)
- ├── servico --> Regras de negócio (empréstimos)
- ├── validacao --> Validações de dados
- └── excecoes --> Exceções customizadas

## Como Compilar e Executar

### Pré-requisitos

- Java JDK 8 ou superior
- Terminal (CMD, PowerShell ou similar)

### Compilação

Na raiz do projeto:
javac biblioteca\aplicacao\Principal.java
###Execução:
java biblioteca.aplicacao.Principal

## Regras de Negócio Importantes

- Um usuário só pode devolver livros que ele realmente possui emprestado
- O número de cópias disponíveis nunca pode ser negativo
- O limite de livros e dias de empréstimo é configurável
- Todas as entradas passam por validações antes do cadastro

## Tecnologias Utilizadas

- Java
- Java Time API (LocalDate)
- Serialização de objetos
- Estruturas de dados List, HashMap

## Observação

Este projeto não utiliza frameworks externos (como Spring), sendo desenvolvido em Java puro, com foco no entendimento dos conceitos fundamentais da linguagem e da orientação a objetos.

## Autor

Rafael de Oliveira Gonçalves<br>
Estudante de Ciência da Computação
