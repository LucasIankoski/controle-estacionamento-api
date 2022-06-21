package com.api.controleestacionamento.controllers;

import com.api.controleestacionamento.dtos.EstacionamentoVagaDto;
import com.api.controleestacionamento.model.EstacionamentoVagaModel;
import com.api.controleestacionamento.services.EstacionamentoVagaService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/vaga-estacionamento")
public class EstacionamentoVagaController {
    final EstacionamentoVagaService estacionamentoVagaService;

    public EstacionamentoVagaController(EstacionamentoVagaService estacionamentoVagaService) {
        this.estacionamentoVagaService = estacionamentoVagaService;
    }

    @PostMapping
    public ResponseEntity<Object> salvarVagaEstacionamento(@RequestBody @Valid EstacionamentoVagaDto estacionamentoVagaDto){

        if(estacionamentoVagaService.existsByPlacaCarro(estacionamentoVagaDto.getPlacaCarro())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A placa do carro já está em uso.");
        }

        if(estacionamentoVagaService.existsByNumeroVaga(estacionamentoVagaDto.getNumeroVaga())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("O número da vaga de estacionamento já está em uso.");
        }

        if(estacionamentoVagaService.existsByApartamentoAndBloco(estacionamentoVagaDto.getApartamento(), estacionamentoVagaDto.getBloco())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A vaga já está registrada no apartamento do bloco selecionado.");
        }
        var estacionamentoVagaModel = new EstacionamentoVagaModel();
        BeanUtils.copyProperties(estacionamentoVagaDto, estacionamentoVagaModel);
        estacionamentoVagaModel.setDataRegistro(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(estacionamentoVagaService.salvar(estacionamentoVagaModel));
    }

    @GetMapping
    public ResponseEntity<List<EstacionamentoVagaModel>> getAllVagasEstacionamento(){
        return ResponseEntity.status(HttpStatus.OK).body(estacionamentoVagaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getVagaEspecifica(@PathVariable(value = "id") UUID id){
        Optional<EstacionamentoVagaModel> estacionamentoVagaModelOptional = estacionamentoVagaService.findById(id);
        if(!estacionamentoVagaModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O registro da vaga não foi encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(estacionamentoVagaModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> detelarVagaSelecionada(@PathVariable(value = "id") UUID id){
        Optional<EstacionamentoVagaModel> estacionamentoVagaModelOptional = estacionamentoVagaService.findById(id);
        if(!estacionamentoVagaModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O registro da vaga não foi encontrado.");
        }
        estacionamentoVagaService.deletarVaga(estacionamentoVagaModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("O registro da vaga foi apagado com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateVagaSelecionada(@PathVariable(value = "id") UUID id,
                                                         @RequestBody @Valid EstacionamentoVagaDto estacionamentoVagaDto){
        Optional<EstacionamentoVagaModel> estacionamentoVagaModelOptional = estacionamentoVagaService.findById(id);
        if(!estacionamentoVagaModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O registro da vaga não foi encontrado.");
        }
        var estacionamentoVagaModel = new EstacionamentoVagaModel();
        BeanUtils.copyProperties(estacionamentoVagaDto, estacionamentoVagaModel);
        estacionamentoVagaModel.setId(estacionamentoVagaModelOptional.get().getId());
        estacionamentoVagaModel.setDataRegistro(estacionamentoVagaModelOptional.get().getDataRegistro());

        return ResponseEntity.status(HttpStatus.OK).body(estacionamentoVagaService.salvar(estacionamentoVagaModel));
    }



}
