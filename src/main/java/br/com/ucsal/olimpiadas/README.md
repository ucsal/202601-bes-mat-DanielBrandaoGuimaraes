# Olimpíadas de Questões — Refatoração SOLID

## O que foi feito

O código original estava todo concentrado na classe `App.java`, que acumulava várias responsabilidades ao mesmo tempo: ler a entrada do usuário, guardar os dados em listas estáticas, aplicar as regras de negócio e renderizar o tabuleiro de xadrez. Qualquer alteração pequena exigia mexer em muita coisa ao mesmo tempo.

O objetivo da refatoração foi separar essas responsabilidades em camadas distintas, sem alterar o comportamento do sistema. O menu continua igual, as mensagens continuam iguais, a lógica continua a mesma — só a organização do código mudou.

---

## Estrutura de pastas

```
src/main/java/br/com/ucsal/olimpiadas/
├── App.java
├── model/
│   ├── Participante.java
│   ├── Prova.java
│   ├── Questao.java
│   ├── Resposta.java
│   └── Tentativa.java
├── repository/
│   ├── ParticipanteRepository.java
│   ├── ProvaRepository.java
│   ├── QuestaoRepository.java
│   └── TentativaRepository.java
├── service/
│   ├── ParticipanteService.java
│   ├── ProvaService.java
│   ├── QuestaoService.java
│   └── TentativaService.java
└── ui/
    └── ConsoleMenu.java

src/test/java/br/com/ucsal/olimpiadas/service/
└── ServiceTest.java
```

---

## Onde cada princípio foi aplicado

### S — Single Responsibility Principle (SRP)

Antes, a `App` fazia tudo. Após a refatoração, cada classe passou a ter apenas uma responsabilidade:

- `ConsoleMenu` cuida somente da interação com o usuário
- Os quatro serviços (`ParticipanteService`, `ProvaService`, etc.) cuidam cada um das regras de negócio da sua entidade
- Os quatro repositórios cuidam somente de armazenar e buscar dados
- As classes de `model` apenas representam as entidades do sistema

---

### O — Open/Closed Principle (OCP)

Antes, os dados ficavam em listas estáticas dentro da `App`. Para trocar para um banco de dados, seria necessário reescrever grande parte da classe principal.

Agora os repositórios ficam separados. Se for necessário trocar a forma de armazenar os dados, basta alterar os repositórios — os serviços e o menu não precisam ser modificados.

---

### L — Liskov Substitution Principle (LSP)

As classes de modelo são independentes e bem definidas. A classe `Questao`, por exemplo, encapsula a lógica de verificar se uma resposta está correta, e esse comportamento funciona de forma consistente independentemente de como o objeto foi criado.

---

### I — Interface Segregation Principle (ISP)

Cada repositório expõe apenas os métodos que fazem sentido para o seu contexto. O `QuestaoRepository`, por exemplo, possui apenas `salvar` e `buscarPorProvaId`, sem métodos genéricos que não seriam utilizados.

---

### D — Dependency Inversion Principle (DIP)

No código original, a `App` criava e controlava tudo diretamente, ficando acoplada aos detalhes de implementação.

Após a refatoração, cada classe recebe o que precisa pelo construtor. O `ProvaService` não cria o repositório, ele recebe um já pronto. O `ConsoleMenu` não cria os serviços, recebe todos pelo construtor. Apenas o `App.java` é responsável por instanciar as dependências e injetá-las em quem precisa.

```java
// App.java instancia e injeta as dependências
ProvaRepository provaRepository = new ProvaRepository();
ProvaService provaService = new ProvaService(provaRepository);
```

```java
// ProvaService recebe o repositório pelo construtor, não cria ele mesmo
public class ProvaService {
    private ProvaRepository repository;

    public ProvaService(ProvaRepository repository) {
        this.repository = repository;
    }
}
```

---

## Como executar

Sem frameworks externos. Basta compilar e executar a classe `App.java` com Java 17 ou superior.

## Como rodar os testes

```bash
mvn test
```
