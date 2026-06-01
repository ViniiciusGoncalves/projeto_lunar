package com.lunarbase.controller;

import com.lunarbase.dto.ApiResponse;
import com.lunarbase.dto.ReservatorioDTO;
import com.lunarbase.model.StatusReservatorio;
import com.lunarbase.service.ReservatorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservatorios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservatorioController {

    private final ReservatorioService reservatorioService;

    // POST /api/reservatorios
    @PostMapping
    public ResponseEntity<ApiResponse<ReservatorioDTO>> criar(@Valid @RequestBody ReservatorioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Reservatório cadastrado com sucesso", reservatorioService.criar(dto)));
    }

    // GET /api/reservatorios
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservatorioDTO>>> listarTodos(
            @RequestParam(required = false) StatusReservatorio status) {
        List<ReservatorioDTO> resultado = status != null
                ? reservatorioService.listarPorStatus(status)
                : reservatorioService.listarTodos();
        return ResponseEntity.ok(ApiResponse.ok(resultado));
    }

    // GET /api/reservatorios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservatorioDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(reservatorioService.buscarPorId(id)));
    }

    // GET /api/reservatorios/nivel-baixo
    @GetMapping("/nivel-baixo")
    public ResponseEntity<ApiResponse<List<ReservatorioDTO>>> listarNivelBaixo() {
        return ResponseEntity.ok(ApiResponse.ok(reservatorioService.listarNivelBaixo()));
    }

    // PUT /api/reservatorios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservatorioDTO>> atualizar(
            @PathVariable Long id, @Valid @RequestBody ReservatorioDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok("Reservatório atualizado", reservatorioService.atualizar(id, dto)));
    }

    // PATCH /api/reservatorios/{id}/nivel
    @PatchMapping("/{id}/nivel")
    public ResponseEntity<ApiResponse<ReservatorioDTO>> atualizarNivel(
            @PathVariable Long id, @RequestBody Map<String, Double> body) {
        Double nivel = body.get("nivelAtual");
        if (nivel == null) throw new IllegalArgumentException("Campo 'nivelAtual' é obrigatório");
        return ResponseEntity.ok(ApiResponse.ok("Nível atualizado", reservatorioService.atualizarNivel(id, nivel)));
    }

    // DELETE /api/reservatorios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        reservatorioService.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok("Reservatório removido", null));
    }
}
