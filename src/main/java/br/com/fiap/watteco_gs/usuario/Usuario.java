package br.com.fiap.watteco_gs.usuario;

import br.com.fiap.watteco_gs.usuarioMissao.UsuarioMissao;
import br.com.fiap.watteco_gs.usuarioRecompensa.UsuarioRecompensa;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "watteco_usuario")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(min=3, max= 200)
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min=6)
    private String senha;

    @NotBlank
    @Size(min=10, max=11)
    private String telefone;

    @NotNull
    @Past
    private LocalDate dtaNascimento;

    @NotNull
    @PastOrPresent
    private LocalDate dtaCadastro;

    private Integer pontosAcumulados;

    private String avatar;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.PERSIST)
    private Set<UsuarioMissao> usuarioMissoes = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.PERSIST)
    private Set<UsuarioRecompensa> usuarioRecompensas = new HashSet<>();
}
