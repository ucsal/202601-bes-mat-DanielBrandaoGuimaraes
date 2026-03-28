package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.model.Participante;

import java.util.List;
import java.util.Optional;

public interface ParticipanteRepository {
    Participante salvar(Participante participante);
    Optional<Participante> buscarPorId(long id);
    List<Participante> listarTodos();
}
