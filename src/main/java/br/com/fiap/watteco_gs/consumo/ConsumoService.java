package br.com.fiap.watteco_gs.consumo;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumoService {

    private final ConsumoRepository consumoRepository;

    public ConsumoService(ConsumoRepository consumoRepository) {
        this.consumoRepository = consumoRepository;
    }

    public Consumo criar(@Valid Consumo consumo) {
        return consumoRepository.save(consumo);
    }

    public List<Consumo> buscarTodos() {
        return consumoRepository.findAll();
    }

}
