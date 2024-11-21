package br.com.fiap.watteco_gs.consumo;

import br.com.fiap.watteco_gs.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "watteco_consumo")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Consumo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "{campo.obrigatorio}")
    @PastOrPresent
    private LocalDate dataRegistro;

    @NotNull(message = "{campo.obrigatorio}")
    private Double consumo; // em kW

    @ManyToOne
    private Usuario usuario;
}
