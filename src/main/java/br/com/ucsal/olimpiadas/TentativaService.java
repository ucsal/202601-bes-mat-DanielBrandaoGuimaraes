package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.model.Resposta;
import br.com.ucsal.olimpiadas.model.Tentativa;
import br.com.ucsal.olimpiadas.repository.QuestaoRepository;
import br.com.ucsal.olimpiadas.repository.TentativaRepository;

import java.util.List;
import java.util.Map;

public class TentativaService {

    private final TentativaRepository tentativaRepository;
    private final QuestaoRepository questaoRepository;

    public TentativaService(TentativaRepository tentativaRepository, QuestaoRepository questaoRepository) {
        this.tentativaRepository = tentativaRepository;
        this.questaoRepository = questaoRepository;
    }

    public Tentativa realizar(long participanteId, long provaId, Map<Long, Character> respostas) {
        var questoes = questaoRepository.buscarPorProvaId(provaId);
        if (questoes.isEmpty())
            throw new IllegalStateException("A prova não possui questões cadastradas.");

        var tentativa = new Tentativa();
        tentativa.setParticipanteId(participanteId);
        tentativa.setProvaId(provaId);

        for (var q : questoes) {
            char marcada = respostas.getOrDefault(q.getId(), 'X');

            var r = new Resposta();
            r.setQuestaoId(q.getId());
            r.setAlternativaMarcada(marcada);
            r.setCorreta(q.isRespostaCorreta(marcada));

            tentativa.getRespostas().add(r);
        }

        return tentativaRepository.salvar(tentativa);
    }

    public int calcularNota(Tentativa tentativa) {
        return (int) tentativa.getRespostas().stream()
                .filter(Resposta::isCorreta)
                .count();
    }

    public List<Tentativa> listarTodos() {
        return tentativaRepository.listarTodos();
    }
}
