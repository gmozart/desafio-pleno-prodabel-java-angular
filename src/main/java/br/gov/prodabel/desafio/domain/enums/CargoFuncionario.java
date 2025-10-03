package br.gov.prodabel.desafio.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CargoFuncionario {
    ATENDENTE,
    SUPORTE,
    GERENTE;

    @JsonCreator
    public static CargoFuncionario from(String valor) {
        return CargoFuncionario.valueOf(valor.toUpperCase());
    }

    @JsonValue
    public String getValor() {
        return this.name();
    }
}
