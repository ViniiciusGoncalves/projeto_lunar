package com.lunarbase.controller;

import com.lunarbase.dto.ApiResponse;
import com.lunarbase.dto.LeituraDTO;
import com.lunarbase.dto.SensorDTO;
import com.lunarbase.model.StatusSensor;
import com.lunarbase.model.TipoSensor;
import com.lunarbase.service.SensorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SensorController {

    private final SensorService sensorService;

    // POST /api/sensores
    @PostMapping
    public ResponseEntity<ApiResponse<SensorDTO>> criar(@Valid @RequestBody SensorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Sensor cadastrado com sucesso", sensorService.criar(dto)));
    }

    // GET /api/sensores
    @GetMapping
    public ResponseEntity<ApiResponse<List<SensorDTO>>> listarTodos(
            @RequestParam(required = false) TipoSensor tipo,
            @RequestParam(required = false) StatusSensor status,
            @RequestParam(required = false) String localizacao) {

        List<SensorDTO> resultado;
        if (tipo != null && status != null)
            resultado = sensorService.listarTodos().stream()
                    .filter(s -> s.getTipo() == tipo && s.getStatus() == status).toList();
        else if (tipo != null)
            resultado = sensorService.listarPorTipo(tipo);
        else if (status != null)
            resultado = sensorService.listarPorStatus(status);
        else if (localizacao != null)
            resultado = sensorService.listarPorLocalizacao(localizacao);
        else
            resultado = sensorService.listarTodos();

        return ResponseEntity.ok(ApiResponse.ok(resultado));
    }

    // GET /api/sensores/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SensorDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(sensorService.buscarPorId(id)));
    }

    // GET /api/sensores/alertas
    @GetMapping("/alertas")
    public ResponseEntity<ApiResponse<List<SensorDTO>>> listarEmAlerta() {
        return ResponseEntity.ok(ApiResponse.ok(sensorService.listarSensoresEmAlerta()));
    }

    // PUT /api/sensores/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SensorDTO>> atualizar(
            @PathVariable Long id, @Valid @RequestBody SensorDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok("Sensor atualizado com sucesso", sensorService.atualizar(id, dto)));
    }

    // PATCH /api/sensores/{id}/leitura
    @PatchMapping("/{id}/leitura")
    public ResponseEntity<ApiResponse<SensorDTO>> registrarLeitura(
            @PathVariable Long id, @Valid @RequestBody LeituraDTO leitura) {
        return ResponseEntity.ok(ApiResponse.ok("Leitura registrada", sensorService.registrarLeitura(id, leitura)));
    }

    // DELETE /api/sensores/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        sensorService.deletar(id);
        return ResponseEntity.ok(ApiResponse.ok("Sensor removido com sucesso", null));
    }
}
