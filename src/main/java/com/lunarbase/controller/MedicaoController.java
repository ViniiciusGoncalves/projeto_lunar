package com.lunarbase.controller;

import com.lunarbase.dto.ApiResponse;
import com.lunarbase.dto.MedicaoDTO;
import com.lunarbase.service.MedicaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicoes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MedicaoController {

    private final MedicaoService medicaoService;

    // POST /api/medicoes
    @PostMapping
    public ResponseEntity<ApiResponse<MedicaoDTO>> registrar(@Valid @RequestBody MedicaoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Medição registrada com sucesso", medicaoService.registrar(dto)));
    }

    // GET /api/medicoes
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicaoDTO>>> listarTodas() {
        return ResponseEntity.ok(ApiResponse.ok(medicaoService.listarTodas()));
    }

    // GET /api/medicoes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicaoDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(medicaoService.buscarPorId(id)));
    }

    // GET /api/medicoes/sensor/{id}
    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<ApiResponse<List<MedicaoDTO>>> listarPorSensor(@PathVariable Long sensorId) {
        return ResponseEntity.ok(ApiResponse.ok(medicaoService.listarPorSensor(sensorId)));
    }
}