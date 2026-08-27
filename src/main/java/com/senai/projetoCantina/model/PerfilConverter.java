package com.senai.projetoCantina.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PerfilConverter implements AttributeConverter<Usuario.Perfil, String> {

    @Override
    public String convertToDatabaseColumn(Usuario.Perfil attribute) {
        return attribute != null ? attribute.name().toLowerCase() : "operador";
    }

    @Override
    public Usuario.Perfil convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Usuario.Perfil.OPERADOR;
        }

        for (Usuario.Perfil p : Usuario.Perfil.values()) {
            if (p.name().equalsIgnoreCase(dbData.trim())) {
                return p;
            }
        }

        return Usuario.Perfil.OPERADOR;
    }
}
