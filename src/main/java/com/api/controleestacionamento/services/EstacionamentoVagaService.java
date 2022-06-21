package com.api.controleestacionamento.services;


import com.api.controleestacionamento.model.EstacionamentoVagaModel;
import com.api.controleestacionamento.repositories.EstacionamentoVagaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EstacionamentoVagaService {
   final EstacionamentoVagaRepository estacionamentoVagaRepository;

    public EstacionamentoVagaService(EstacionamentoVagaRepository estacionamentoVagaRepository){
        this.estacionamentoVagaRepository = estacionamentoVagaRepository;
    }

    @Transactional
    public EstacionamentoVagaModel salvar(EstacionamentoVagaModel estacionamentoVagaModel) {
        return estacionamentoVagaRepository.save(estacionamentoVagaModel);
    }

    public boolean existsByPlacaCarro(String placaCarro) {
        return estacionamentoVagaRepository.existsByPlacaCarro(placaCarro);
    }

    public boolean existsByNumeroVaga(String numeroVaga) {
        return estacionamentoVagaRepository.existsByNumeroVaga(numeroVaga);
    }

    public boolean existsByApartamentoAndBloco(String apartamento, String bloco) {
        return  estacionamentoVagaRepository.existsByApartamentoAndBloco(apartamento, bloco);
    }

    public List<EstacionamentoVagaModel> findAll() {
        return estacionamentoVagaRepository.findAll();
    }

    public Optional<EstacionamentoVagaModel> findById(UUID id) {
        return estacionamentoVagaRepository.findById(id);
    }

    @Transactional
    public void deletarVaga(EstacionamentoVagaModel estacionamentoVagaModel) {
        estacionamentoVagaRepository.delete(estacionamentoVagaModel);
    }
}
