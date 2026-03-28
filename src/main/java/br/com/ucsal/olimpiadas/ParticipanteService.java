package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.model.Participante;
import br.com.ucsal.olimpiadas.repository.ParticipanteRepository;

import java.util.List;
import java.util.Optional;

public class ParticipanteService {

    private final ParticipanteRepository repository;

    public ParticipanteService(ParticipanteRepository repository) {
        this.repository = repository;
    }

    public Participante cadastrar(String nome, String email) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome não pode ser vazio.");

        var p = new Participante();
        p.setNome(nome.trim());
        p.setEmail(email == null ? "" : email.trim());
        return repository.salvar(p);
    }

    public List<Participante> listarTodos() {
        return repository.listarTodos();
    }

    public Optional<Participante> buscarPorId(long id) {
        return repository.buscarPorId(id);
    }
}
