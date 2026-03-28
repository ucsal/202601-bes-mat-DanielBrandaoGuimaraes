package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.model.Questao;
import br.com.ucsal.olimpiadas.repository.ProvaRepository;
import br.com.ucsal.olimpiadas.repository.QuestaoRepository;

import java.util.List;

public class QuestaoService {

    private final QuestaoRepository questaoRepository;
    private final ProvaRepository provaRepository;

    public QuestaoService(QuestaoRepository questaoRepository, ProvaRepository provaRepository) {
        this.questaoRepository = questaoRepository;
        this.provaRepository = provaRepository;
    }

    public Questao cadastrar(long provaId, String enunciado, String[] alternativas, char correta, String fenInicial) {
        provaRepository.buscarPorId(provaId)
                .orElseThrow(() -> new IllegalArgumentException("Prova não encontrada: " + provaId));

        if (enunciado == null || enunciado.isBlank())
            throw new IllegalArgumentException("Enunciado não pode ser vazio.");

        var q = new Questao();
        q.setProvaId(provaId);
        q.setEnunciado(enunciado.trim());
        q.setAlternativas(alternativas);
        q.setAlternativaCorreta(correta);
        q.setFenInicial(fenInicial);
        return questaoRepository.salvar(q);
    }

    public List<Questao> buscarPorProvaId(long provaId) {
        return questaoRepository.buscarPorProvaId(provaId);
    }
}
