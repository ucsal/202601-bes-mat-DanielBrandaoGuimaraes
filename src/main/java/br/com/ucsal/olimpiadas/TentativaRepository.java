package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.model.Tentativa;

import java.util.List;

public interface TentativaRepository {
    Tentativa salvar(Tentativa tentativa);
    List<Tentativa> listarTodos();
}
