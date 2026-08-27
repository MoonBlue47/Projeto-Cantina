package com.senai.projetoCantina.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoPagamentoConverter implements AttributeConverter<FormaPagamento.TipoPagamento, String> {

    @Override
    public String convertToDatabaseColumn(FormaPagamento.TipoPagamento attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public FormaPagamento.TipoPagamento convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }

        for (FormaPagamento.TipoPagamento tp : FormaPagamento.TipoPagamento.values()) {
            if (tp.name().equalsIgnoreCase(dbData.trim()) ||
                tp.getDescricao().equalsIgnoreCase(dbData.trim())) {
                return tp;
            }
        }

        return FormaPagamento.TipoPagamento.DINHEIRO;
    }
}
