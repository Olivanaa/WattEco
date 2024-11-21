package br.com.fiap.watteco_gs.missao;

import br.com.fiap.watteco_gs.usuarioMissao.UsuarioMissao;
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
@Table(name = "watteco_missao")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Missao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String titulo;

    @NotBlank
    @Size(max = 255)
    private String descricao;

    @NotNull
    @Min(0)
    private Integer pontos;

    @NotBlank
    @Size(max = 255)
    private String meta;

    @OneToMany(mappedBy = "missao", cascade = CascadeType.ALL)
    private Set<UsuarioMissao> usuarioMissoes = new HashSet<>();
}
