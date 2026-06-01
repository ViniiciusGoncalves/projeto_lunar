package com.lunarbase.controller;

import com.lunarbase.dto.ApiResponse;
import com.lunarbase.dto.ClimatizacaoDTO;
import com.lunarbase.model.StatusAtuador;
import com.lunarbase.model.TipoSistema;
import com.lunarbase.service.ClimatizacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/climatizacao")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClimatizacaoController {

    private final ClimatizacaoService climatizacaoService;

    // POST /api/climatizacao
    @PostMapping
    public ResponseEntity<ApiResponse<ClimatizacaoDTO>> criar(@Valid @RequestBody ClimatizacaoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Sistema cadastrado", climatizacaoService.criar(dto)));
    }

    // GET /api/climatizacao
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClimatizacaoDTO>>> listarTodos(
            @RequestParam(required = false) TipoSistema tipo,
            @RequestParam(required = false) StatusAtuador status) {
        List<ClimatizacaoDTO> resultado;
        if (tipo != null) resultado = climatizacaoService.listarPorTipo(tipo);
        else if (status != null) resultado = climatizacaoService.listarPorStatus(status);
        else resultado = climatizacaoService.listarTodos();
        return ResponseEntity.ok(ApiResponse.ok(resultado));
    }

    // GET /api/climatizacao/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClimatizacaoDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(climatizacaoService.buscarPorId(id)));
    }

    // PUT /api/climatizacao/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClimatizacaoDTO>> atualizar(
            @PathVariable Long id, @Valid @RequestBody ClimatizacaoDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok("Sistema atualizado", climatizacaoService.atualizar(id, dto)));
    }

    // PATCH /api/climatizacao/{id}/acionar — ligar/desligar
    @PatchMapping("/{id}/acionar")
    public ResponseEntity<ApiResponse<ClimatizacaoDTO>> acionar(
            @PathVariable Long id, @RequestBody Map<String, Object> body) {
        boolean ligar = Boolean.TRUE.equals(body.get("ligar"));
        Integer intensidade = body.get("intensidade") != null
                ? ((Number) body.get("intensidade")).intValue() : null;
        return ResponseEntity.ok(ApiResponse.ok(
                ligar ? "Sistema ligado" : "Sistema desligado",
                climatizacaoService.acionar(id, ligar, intensidade)));
    }

    // PATCH /api/climatizacao/{id}/intensidade
    @PatchMapping("/{id}/intensidade")
    public ResponseEntity<ApiResponse<ClimatizacaoDTO>> ajustarIntensidade(
            @PathVariable Long id, @RequestBody Map<String, Integer> body) {
        Integer intensidade = body.get("intensidade");
        if (intensidade == null) throw new IllegalArgumentException("Campo 'intensidade' é obrigatório (0-100)");
        return ResponseEntity.ok(ApiResponse.ok("Intensidade ajustada",
                climatizacaoService.ajustarIntensidade(id, intensidade)));
    }

    // DELETE /api/climatizacao/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        climatizacaoService.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok("Sistema removido", null));
    }
}
