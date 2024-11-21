package br.com.fiap.watteco_gs.usuario.dto;

import br.com.fiap.watteco_gs.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateFormRequest(
        @NotBlank(message = "{campo.obrigatorio}")
        @Size(min=3, max=200, message = "{tamanho.campo}")
        String nome,
        @NotBlank(message = "{campo.obrigatorio}")
        @Size(min=10, max=11, message = "{tamanho.telefone}")
        String telefone
) {
    public Usuario toModel() {
        return Usuario.builder()
                .nome(nome)
                .telefone(telefone)
                .build();
    }
}
