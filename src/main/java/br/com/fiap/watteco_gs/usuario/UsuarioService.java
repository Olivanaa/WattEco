package br.com.fiap.watteco_gs.usuario;

import br.com.fiap.watteco_gs.missao.Missao;
import br.com.fiap.watteco_gs.missao.MissaoRepository;
import br.com.fiap.watteco_gs.recompensa.Recompensa;
import br.com.fiap.watteco_gs.recompensa.RecompensaRepository;
import br.com.fiap.watteco_gs.usuario.dto.UsuarioFormRequest;
import br.com.fiap.watteco_gs.usuario.dto.UsuarioUpdateFormRequest;
import br.com.fiap.watteco_gs.usuarioMissao.UsuarioMissaoService;
import br.com.fiap.watteco_gs.usuarioRecompensa.UsuarioRecompensaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMissaoService usuarioMissaoService;
    private final MissaoRepository missaoRepository;
    private final UsuarioRecompensaService usuarioRecompensaService;
    private final RecompensaRepository recompensaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, UsuarioMissaoService usuarioMissaoService, MissaoRepository missaoRepository, UsuarioRecompensaService usuarioRecompensaService, RecompensaRepository recompensaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMissaoService = usuarioMissaoService;
        this.missaoRepository = missaoRepository;
        this.usuarioRecompensaService = usuarioRecompensaService;
        this.recompensaRepository = recompensaRepository;
    }

    @Transactional
    public Usuario criar(@Valid UsuarioFormRequest usuarioForm){
        Usuario usuario = usuarioForm.toModel();

        String nomeFormatado = formatarNome(usuario.getNome());

        usuario.setNome(nomeFormatado);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setAvatar("https://avatar.iran.liara.run/username?username=" + usuario.getNome());
        usuarioRepository.save(usuario);

        associarMissaoParaNovoUsuario(usuario);

        associarRecompensaParaNovoUsuario(usuario);

        return usuario;
    }

    public Usuario buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado")
        );

    }

    public Usuario atualizar(Long id, @Valid UsuarioUpdateFormRequest usuarioForm){
        verficicarId(id);

        Usuario usuarioAtual = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (usuarioForm.nome() != null) {
            String nomeFormatado = formatarNome(usuarioForm.nome());
            usuarioAtual.setNome(nomeFormatado);
        }

        if (usuarioForm.telefone() != null) {
            usuarioAtual.setTelefone(usuarioForm.telefone());
        }

        return usuarioRepository.save(usuarioAtual);

    }

    public void atualizarAvatar(Long id, MultipartFile file){
        if(file.isEmpty()){
            throw new RuntimeException("Arquivo Invalido");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new RuntimeException("O arquivo é muito grande. O tamanho máximo permitido é 10MB.");
        }

        try (InputStream is = file.getInputStream()) {
            BufferedImage image = ImageIO.read(is);
            if (image == null) {
                throw new RuntimeException("O arquivo enviado não é uma imagem válida.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao validar o arquivo de imagem: " + e.getMessage());
        }

        Path destinoDir = Paths.get("src/main/resources/static/avatars");
        if (!Files.exists(destinoDir)) {
            try {
                Files.createDirectories(destinoDir);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao criar o diretório de destino para o avatar.");
            }
        }

        try(InputStream is = file.getInputStream()){

            Path destinoFile = destinoDir
                    .resolve(System.currentTimeMillis() + file.getOriginalFilename())
                    .normalize()
                    .toAbsolutePath();

            Files.copy(is, destinoFile);
            System.out.println("Arquivo salvo com sucesso");

            var user = usuarioRepository.findById(id).orElseThrow(
                    () -> new UsernameNotFoundException("Usuario não encontrado")
            );
            var baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            var avatarUrl = baseUrl + "/avatars/" + destinoFile.getFileName().toString();
            user.setAvatar(avatarUrl);
            usuarioRepository.save(user);

        }catch (IOException e){
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar o avatar. Tente novamente.");
        }
    }

    private void verficicarId(Long id){
        usuarioRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado usuario com o id " + id)
        );
    }

    private void associarMissaoParaNovoUsuario(Usuario usuario) {
        List<Missao> missoes = missaoRepository.findAll();
        for (Missao missao : missoes) {
            usuarioMissaoService.associarMissaoUsuario(usuario, missao);
        }
    }

    private void associarRecompensaParaNovoUsuario(Usuario usuario) {
        List<Recompensa> recompensas = recompensaRepository.findAll();
        if (recompensas == null || recompensas.isEmpty()) {
            log.warn("Nenhuma recompensa encontrada para associar ao usuário.");
        } else {
            log.info("Recompensas encontradas: {}", recompensas.size());
        }
        for (Recompensa recompensa : recompensas) {
            usuarioRecompensaService.associarRecompensaUsuario(usuario, recompensa);
        }
    }

    private String formatarNome(String nome) {
        if (nome != null && !nome.isEmpty()) {
            String[] palavras = nome.split("\\s+");
            StringBuilder nomeFormatado = new StringBuilder();

            for (String palavra : palavras) {
                if (!palavra.isEmpty()) {
                    nomeFormatado.append(palavra.substring(0, 1).toUpperCase())
                            .append(palavra.substring(1).toLowerCase())
                            .append(" ");
                }
            }
            return nomeFormatado.toString().trim();
        }
        return nome;
    }


}
