package com.lunarbase.model;

public enum TipoSensor {
    TEMPERATURA,        // °C — monitoramento térmico interno/externo
    UMIDADE,            // %  — controle de umidade do ar
    LUMINOSIDADE,       // lux — iluminação ambiente
    NIVEL_AGUA,         // m³ / % — nível do reservatório
    PRESSAO_ATMOSFERICA,// kPa — pressão interna da base
    QUALIDADE_AR,       // ppm — CO₂, O₂, toxinas
    RADIACAO_SOLAR,     // W/m² — exposição à radiação lunar
    CONSUMO_ENERGIA,    // kWh — consumo elétrico por setor
    VELOCIDADE_VENTO,   // m/s — ventos lunares / ventilação
    NIVEL_BATERIA       // %  — estado das baterias de armazenamento
}
