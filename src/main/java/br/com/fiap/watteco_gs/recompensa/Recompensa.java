package br.com.fiap.watteco_gs.recompensa;

import br.com.fiap.watteco_gs.usuarioRecompensa.UsuarioRecompensa;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "watteco_recompensa")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recompensa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String descricao;

    @NotNull
    @Min(0)
    private Integer pontosNecessarios;

    @OneToMany(mappedBy = "recompensa", cascade = CascadeType.ALL)
    private Set<UsuarioRecompensa> usuarioRecompensas = new HashSet<>();

}
