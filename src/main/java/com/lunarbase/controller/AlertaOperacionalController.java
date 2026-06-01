package com.lunarbase.controller;

import com.lunarbase.dto.AlertaOperacionalDTO;
import com.lunarbase.dto.ApiResponse;
import com.lunarbase.model.SeveridadeAlerta;
import com.lunarbase.model.StatusAlerta;
import com.lunarbase.model.TipoAlerta;
import com.lunarbase.service.AlertaOperacionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AlertaOperacionalController {

    private final AlertaOperacionalService alertaService;

    // POST /api/alertas
    @PostMapping
    public ResponseEntity<ApiResponse<AlertaOperacionalDTO>> criar(
            @Valid @RequestBody AlertaOperacionalDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Alerta criado", alertaService.criar(dto)));
    }

    // GET /api/alertas
    @GetMapping
    public ResponseEntity<ApiResponse<List<AlertaOperacionalDTO>>> listarTodos(
            @RequestParam(required = false) StatusAlerta status,
            @RequestParam(required = false) SeveridadeAlerta severidade,
            @RequestParam(required = false) TipoAlerta tipo,
            @RequestParam(required = false) String setor) {

        List<AlertaOperacionalDTO> resultado;
        if (status != null)     resultado = alertaService.listarPorStatus(status);
        else if (severidade != null) resultado = alertaService.listarPorSeveridade(severidade);
        else if (tipo != null)  resultado = alertaService.listarPorTipo(tipo);
        else if (setor != null) resultado = alertaService.listarPorSetor(setor);
        else                    resultado = alertaService.listarTodos();

        return ResponseEntity.ok(ApiResponse.ok(resultado));
    }

    // GET /api/alertas/ativos
    @GetMapping("/ativos")
    public ResponseEntity<ApiResponse<List<AlertaOperacionalDTO>>> listarAtivos() {
        return ResponseEntity.ok(ApiResponse.ok(alertaService.listarAtivos()));
    }

    // GET /api/alertas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlertaOperacionalDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(alertaService.buscarPorId(id)));
    }

    // PUT /api/alertas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AlertaOperacionalDTO>> atualizar(
            @PathVariable Long id, @Valid @RequestBody AlertaOperacionalDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok("Alerta atualizado", alertaService.atualizar(id, dto)));
    }

    // PATCH /api/alertas/{id}/reconhecer
    @PatchMapping("/{id}/reconhecer")
    public ResponseEntity<ApiResponse<AlertaOperacionalDTO>> reconhecer(
            @PathVariable Long id, @RequestBody Map<String, String> body) {
        String operador = body.getOrDefault("operador", "Operador");
        return ResponseEntity.ok(ApiResponse.ok("Alerta reconhecido", alertaService.reconhecer(id, operador)));
    }

    // PATCH /api/alertas/{id}/resolver
    @PatchMapping("/{id}/resolver")
    public ResponseEntity<ApiResponse<AlertaOperacionalDTO>> resolver(
            @PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        String obs = body != null ? body.get("observacoes") : null;
        return ResponseEntity.ok(ApiResponse.ok("Alerta resolvido", alertaService.resolver(id, obs)));
    }

    // DELETE /api/alertas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        alertaService.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok("Alerta removido", null));
    }
}
