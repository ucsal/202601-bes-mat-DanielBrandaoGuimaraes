package br.com.ucsal.olimpiadas.repository.memory;

import br.com.ucsal.olimpiadas.model.Participante;
import br.com.ucsal.olimpiadas.repository.ParticipanteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryParticipanteRepository implements ParticipanteRepository {

    private final List<Participante> lista = new ArrayList<>();
    private long proximoId = 1;

    @Override
    public Participante salvar(Participante participante) {
        if (participante.getId() == 0)
            participante.setId(proximoId++);
        lista.add(participante);
        return participante;
    }

    @Override
    public Optional<Participante> buscarPorId(long id) {
        return lista.stream().filter(p -> p.getId() == id).findFirst();
    }

    @Override
    public List<Participante> listarTodos() {
        return List.copyOf(lista);
    }
}
