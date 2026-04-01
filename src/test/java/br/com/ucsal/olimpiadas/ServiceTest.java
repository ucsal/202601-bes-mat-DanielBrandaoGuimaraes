package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.model.Resposta;
import br.com.ucsal.olimpiadas.model.Tentativa;
import br.com.ucsal.olimpiadas.repository.ParticipanteRepository;
import br.com.ucsal.olimpiadas.repository.ProvaRepository;
import br.com.ucsal.olimpiadas.repository.QuestaoRepository;
import br.com.ucsal.olimpiadas.repository.TentativaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    private ParticipanteService participanteService;
    private ProvaService provaService;
    private QuestaoService questaoService;
    private TentativaService tentativaService;

    private static final String[] ALTS = { "A) op1", "B) op2", "C) op3", "D) op4", "E) op5" };

    @BeforeEach
    void setUp() {
        participanteService = new ParticipanteService(new ParticipanteRepository());
        provaService = new ProvaService(new ProvaRepository());
        ProvaRepository provaRepository = new ProvaRepository();
        QuestaoRepository questaoRepository = new QuestaoRepository();
        provaService = new ProvaService(provaRepository);
        questaoService = new QuestaoService(questaoRepository, provaRepository);
        tentativaService = new TentativaService(new TentativaRepository());
    }

    // ── ParticipanteService ───────────────────────────────────────────────

    @Test
    void deveCadastrarParticipante() {
        participanteService.cadastrar("João", "joao@email.com");

        assertEquals(1, participanteService.listarTodos().size());
        assertEquals("João", participanteService.listarTodos().get(0).getNome());
    }

    @Test
    void deveLancarExcecaoParaNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> participanteService.cadastrar("", ""));
    }

    @Test
    void deveBuscarParticipantePorId() {
        participanteService.cadastrar("Ana", "");
        long id = participanteService.listarTodos().get(0).getId();

        assertNotNull(participanteService.buscarPorId(id));
    }

    @Test
    void deveBuscarParticipanteInexistenteRetornarNull() {
        assertNull(participanteService.buscarPorId(999L));
    }

    // ── ProvaService ──────────────────────────────────────────────────────

    @Test
    void deveCadastrarProva() {
        provaService.cadastrar("Olimpíada 2026");

        assertEquals(1, provaService.listarTodos().size());
        assertEquals("Olimpíada 2026", provaService.listarTodos().get(0).getTitulo());
    }

    @Test
    void deveLancarExcecaoParaTituloVazio() {
        assertThrows(IllegalArgumentException.class, () -> provaService.cadastrar(""));
    }

    @Test
    void deveBuscarProvaInexistenteRetornarNull() {
        assertNull(provaService.buscarPorId(42L));
    }

    // ── QuestaoService ────────────────────────────────────────────────────

    @Test
    void deveCadastrarQuestao() {
        provaService.cadastrar("Prova");
        long provaId = provaService.listarTodos().get(0).getId();

        questaoService.cadastrar(provaId, "Enunciado?", ALTS, 'B', null);

        assertEquals(1, questaoService.buscarPorProvaId(provaId).size());
    }

    @Test
    void deveLancarExcecaoParaProvaInexistente() {
        assertThrows(IllegalArgumentException.class,
                () -> questaoService.cadastrar(999L, "Enunciado?", ALTS, 'A', null));
    }

    @Test
    void deveBuscarQuestoesPorProva() {
        provaService.cadastrar("Prova 1");
        provaService.cadastrar("Prova 2");
        long idP1 = provaService.listarTodos().get(0).getId();
        long idP2 = provaService.listarTodos().get(1).getId();

        questaoService.cadastrar(idP1, "Q1", ALTS, 'A', null);
        questaoService.cadastrar(idP1, "Q2", ALTS, 'B', null);
        questaoService.cadastrar(idP2, "Q3", ALTS, 'C', null);

        assertEquals(2, questaoService.buscarPorProvaId(idP1).size());
        assertEquals(1, questaoService.buscarPorProvaId(idP2).size());
    }

    // ── TentativaService ──────────────────────────────────────────────────

    @Test
    void deveCalcularNotaCorretamente() {
        Tentativa tentativa = new Tentativa();

        Resposta r1 = new Resposta();
        r1.setCorreta(true);
        tentativa.getRespostas().add(r1);

        Resposta r2 = new Resposta();
        r2.setCorreta(false);
        tentativa.getRespostas().add(r2);

        assertEquals(1, tentativaService.calcularNota(tentativa));
    }

    @Test
    void deveSalvarTentativa() {
        Tentativa tentativa = new Tentativa();
        tentativa.setParticipanteId(1L);
        tentativa.setProvaId(1L);

        tentativaService.salvar(tentativa);

        assertEquals(1, tentativaService.listarTodos().size());
    }
}
