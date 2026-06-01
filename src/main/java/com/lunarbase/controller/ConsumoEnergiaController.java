package com.lunarbase.controller;

import com.lunarbase.dto.ApiResponse;
import com.lunarbase.dto.ConsumoEnergiaDTO;
import com.lunarbase.model.StatusEnergia;
import com.lunarbase.service.ConsumoEnergiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/energia")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConsumoEnergiaController {

    private final ConsumoEnergiaService consumoService;

    // POST /api/energia
    @PostMapping
    public ResponseEntity<ApiResponse<ConsumoEnergiaDTO>> criar(@Valid @RequestBody ConsumoEnergiaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Registro de energia criado", consumoService.criar(dto)));
    }

    // GET /api/energia
    @GetMapping
    public ResponseEntity<ApiResponse<List<ConsumoEnergiaDTO>>> listarTodos(
            @RequestParam(required = false) StatusEnergia status) {
        List<ConsumoEnergiaDTO> resultado = status != null
                ? consumoService.listarPorStatus(status)
                : consumoService.listarTodos();
        return ResponseEntity.ok(ApiResponse.ok(resultado));
    }

    // GET /api/energia/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ConsumoEnergiaDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(consumoService.buscarPorId(id)));
    }

    // GET /api/energia/resumo
    @GetMapping("/resumo")
    public ResponseEntity<ApiResponse<Map<String, Double>>> resumo() {
        double consumo = consumoService.getConsumoTotal();
        double geracao = consumoService.getGeracaoTotal();
        return ResponseEntity.ok(ApiResponse.ok(Map.of(
                "consumoTotalKwh",   consumo,
                "geracaoTotalKwh",  geracao,
                "balancoKwh",       geracao - consumo
        )));
    }

    // GET /api/energia/alerta
    @GetMapping("/alerta")
    public ResponseEntity<ApiResponse<List<ConsumoEnergiaDTO>>> setoresAcimaLimite() {
        return ResponseEntity.ok(ApiResponse.ok(consumoService.listarSetoresAcimaLimite()));
    }

    // PUT /api/energia/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ConsumoEnergiaDTO>> atualizar(
            @PathVariable Long id, @Valid @RequestBody ConsumoEnergiaDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok("Registro atualizado", consumoService.atualizar(id, dto)));
    }

    // DELETE /api/energia/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        consumoService.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok("Registro removido", null));
    }
}
