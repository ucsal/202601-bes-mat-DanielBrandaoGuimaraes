# Olimpíadas de Questões — Refatoração SOLID

## O que foi feito

O código original estava todo concentrado na classe `App.java`, misturando lógica de negócio, acesso a dados e interface com o usuário no mesmo lugar. A refatoração separou essas responsabilidades em camadas distintas, sem alterar nenhum comportamento funcional do sistema.

---

## Estrutura de pastas após a refatoração

```
src/main/java/br/com/ucsal/olimpiadas/
├── App.java
├── DataSeeder.java
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
│   ├── TentativaRepository.java
│   └── memory/
│       ├── InMemoryParticipanteRepository.java
│       ├── InMemoryProvaRepository.java
│       ├── InMemoryQuestaoRepository.java
│       └── InMemoryTentativaRepository.java
├── service/
│   ├── ParticipanteService.java
│   ├── ProvaService.java
│   ├── QuestaoService.java
│   └── TentativaService.java
└── ui/
    ├── ConsoleMenu.java
    └── ChessBoardRenderer.java

src/test/java/br/com/ucsal/olimpiadas/service/
├── ParticipanteServiceTest.java
├── ProvaServiceTest.java
├── QuestaoServiceTest.java
└── TentativaServiceTest.java
```

---

## Onde cada princípio SOLID foi aplicado

### S — Single Responsibility Principle (SRP)
**Problema original:** a classe `App` tinha múltiplas responsabilidades: ler entradas do usuário, aplicar regras de negócio, armazenar dados e renderizar o tabuleiro de xadrez.

**O que foi feito:**
- `ConsoleMenu` ficou responsável apenas pela interação com o usuário via console
- `ParticipanteService`, `ProvaService`, `QuestaoService` e `TentativaService` ficaram responsáveis cada um pelas regras de negócio da sua entidade
- `InMemory*Repository` ficaram responsáveis apenas pelo armazenamento dos dados
- `ChessBoardRenderer` ficou responsável apenas por renderizar o tabuleiro FEN no console
- `DataSeeder` ficou responsável apenas por popular os dados iniciais do sistema

---

### O — Open/Closed Principle (OCP)
**Problema original:** toda a lógica de persistência estava embutida na `App` com listas estáticas. Qualquer mudança no mecanismo de armazenamento exigiria alterar a classe principal.

**O que foi feito:**
- As interfaces `ParticipanteRepository`, `ProvaRepository`, `QuestaoRepository` e `TentativaRepository` definem contratos fixos
- As implementações `InMemory*` são as versões atuais desses contratos
- Para trocar para banco de dados no futuro, basta criar novas implementações como `JpaParticipanteRepository` sem alterar nenhum serviço

---

### L — Liskov Substitution Principle (LSP)
**Problema original:** não havia hierarquia de tipos, então o princípio não era violado, mas também não era aproveitado.

**O que foi feito:**
- As classes `InMemory*Repository` implementam fielmente os contratos das interfaces
- Os serviços funcionam corretamente com qualquer implementação dessas interfaces, bastando passá-la no construtor
- Por exemplo, `TentativaService` recebe um `TentativaRepository` e funciona da mesma forma independente de qual implementação for passada

---

### I — Interface Segregation Principle (ISP)
**Problema original:** não havia interfaces, então os consumidores dependiam de uma classe monolítica que fazia tudo.

**O que foi feito:**
- Cada interface de repositório expõe apenas os métodos necessários para o seu contexto
- `QuestaoRepository` tem apenas `salvar` e `buscarPorProvaId`, sem expor um `listarTodos` genérico que não seria usado
- `TentativaRepository` tem apenas `salvar` e `listarTodos`, sem métodos de busca que não fazem sentido para esse contexto

---

### D — Dependency Inversion Principle (DIP)
**Problema original:** a `App` criava e usava diretamente as listas estáticas, ou seja, dependia de detalhes concretos de armazenamento.

**O que foi feito:**
- Os serviços recebem os repositórios via construtor, dependendo das interfaces e não das implementações concretas
- O `ConsoleMenu` recebe todos os serviços via construtor
- Apenas o `App.java` conhece as implementações concretas, funcionando como o ponto de composição do sistema (Composition Root)

**Exemplo:**
```java
// App.java — único lugar que conhece as implementações concretas
var participanteRepo = new InMemoryParticipanteRepository();
var participanteService = new ParticipanteService(participanteRepo);
```
```java
// ParticipanteService — depende apenas da interface
public class ParticipanteService {
    private final ParticipanteRepository repository;

    public ParticipanteService(ParticipanteRepository repository) {
        this.repository = repository;
    }
}
```

---

## Como executar

Sem frameworks externos. Basta compilar e rodar a classe `App.java` com Java 17 ou superior.

```bash
javac -r src/main/java src/main/java/br/com/ucsal/olimpiadas/App.java
java -cp src/main/java br.com.ucsal.olimpiadas.App
```

## Como rodar os testes

```bash
mvn test
```
