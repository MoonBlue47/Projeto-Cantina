package com.senai.projetoCantina.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusVendaConverter implements AttributeConverter<Venda.StatusVenda, String> {

    @Override
    public String convertToDatabaseColumn(Venda.StatusVenda attribute) {
        return attribute != null ? attribute.name() : Venda.StatusVenda.EM_ANDAMENTO.name();
    }

    @Override
    public Venda.StatusVenda convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Venda.StatusVenda.EM_ANDAMENTO;
        }

        for (Venda.StatusVenda status : Venda.StatusVenda.values()) {
            if (status.name().equalsIgnoreCase(dbData.trim()) ||
                status.getValorBanco().equalsIgnoreCase(dbData.trim())) {
                return status;
            }
        }

        return Venda.StatusVenda.EM_ANDAMENTO;
    }
}
