package br.com.fiap.watteco_gs.consumo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/consumo")
@Slf4j
public class ConsumoController {

    private final ConsumoService consumoService;

    public ConsumoController(ConsumoService consumoService) {
        this.consumoService = consumoService;
    }

    @GetMapping("/todos")
    public String mostrarConsumo(Model model) {
        List<Consumo> consumos = consumoService.buscarTodos();
        model.addAttribute("consumos", consumos);
        return "consumo/index";
    }

    @GetMapping("/adicionar")
    public String mostrarFormulario(Model model, Consumo consumo) {
        model.addAttribute("consumos", consumo);
        return "registro/create";
    }

    @PostMapping("/adicionar")
    public String adicionarConsumo(Model model, Consumo consumo, BindingResult result) {

        if (result.hasErrors()) {
            model.addAttribute("consumo", consumo);
            log.error(result.getAllErrors().toString());
            return "consumo/create";
        }

        consumoService.criar(consumo);
        return "redirect:/consumo/todos";
    }
}
