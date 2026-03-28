package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.repository.memory.InMemoryParticipanteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParticipanteServiceTest {

    private ParticipanteService service;

    @BeforeEach
    void setUp() {
        service = new ParticipanteService(new InMemoryParticipanteRepository());
    }

    @Test
    void deveCadastrarParticipanteComSucesso() {
        var p = service.cadastrar("João Silva", "joao@email.com");

        assertNotNull(p);
        assertTrue(p.getId() > 0);
        assertEquals("João Silva", p.getNome());
        assertEquals("joao@email.com", p.getEmail());
    }

    @Test
    void deveGerarIdsSequenciais() {
        var p1 = service.cadastrar("Ana", "");
        var p2 = service.cadastrar("Bruno", "");

        assertNotEquals(p1.getId(), p2.getId());
    }

    @Test
    void deveListarTodosOsParticipantes() {
        service.cadastrar("Ana", "");
        service.cadastrar("Bruno", "");

        assertEquals(2, service.listarTodos().size());
    }

    @Test
    void deveLancarExcecaoParaNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar("", ""));
    }

    @Test
    void deveLancarExcecaoParaNomeNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(null, ""));
    }

    @Test
    void deveLancarExcecaoParaNomeSoComEspacos() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar("   ", ""));
    }

    @Test
    void deveBuscarParticipantePorId() {
        var p = service.cadastrar("Carla", "carla@email.com");

        var resultado = service.buscarPorId(p.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Carla", resultado.get().getNome());
    }

    @Test
    void deveBuscarIdInexistenteRetornarVazio() {
        assertTrue(service.buscarPorId(999L).isEmpty());
    }
}
