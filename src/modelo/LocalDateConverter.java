package modelo;

import jakarta.persistence.AttributeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter implements AttributeConverter<String, LocalDate> {


    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public String convertToEntityAttribute(LocalDate attribute) {
        return (attribute == null) ? null : attribute.format(formatter);
    }

    @Override
    public LocalDate convertToDatabaseColumn(String dbData) {
        return (dbData == null) ? null : LocalDate.parse(dbData, formatter);
    }
}
