package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.model.Prova;

import java.util.List;
import java.util.Optional;

public interface ProvaRepository {
    Prova salvar(Prova prova);
    Optional<Prova> buscarPorId(long id);
    List<Prova> listarTodos();
}
