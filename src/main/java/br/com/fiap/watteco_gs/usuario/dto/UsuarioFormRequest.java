package br.com.fiap.watteco_gs.usuario.dto;

import br.com.fiap.watteco_gs.usuario.Usuario;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UsuarioFormRequest(
        @NotBlank(message = "{campo.obrigatorio}")
        @Size(min=3, max=200, message = "{tamanho.campo}")
        String nome,

        @NotBlank(message = "{campo.obrigatorio}")
        @Email(message = "{email.invalido}")
        String email,

        @NotBlank(message = "{campo.obrigatorio}")
        @Size(min=6, message = "{tamanho.senha}")
        String senha,

        @NotBlank(message = "{campo.obrigatorio}")
        @Size(min=10, max=11, message = "{tamanho.telefone}")
        String telefone,

        @NotNull(message = "{campo.obrigatorio}")
        @Past(message = "{data.passado}")
        LocalDate dtaNascimento

) {
    public Usuario toModel() {
        return Usuario.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .telefone(telefone)
                .dtaNascimento(dtaNascimento)
                .dtaCadastro(LocalDate.now())
                .pontosAcumulados(0)
                .build();
    }
}
