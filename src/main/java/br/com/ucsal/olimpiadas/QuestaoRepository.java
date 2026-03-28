package br.com.ucsal.olimpiadas.repository;

import br.com.ucsal.olimpiadas.model.Questao;

import java.util.List;

public interface QuestaoRepository {
    Questao salvar(Questao questao);
    List<Questao> buscarPorProvaId(long provaId);
}
