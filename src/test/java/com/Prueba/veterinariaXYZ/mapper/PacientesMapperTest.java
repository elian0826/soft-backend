package com.Prueba.veterinariaXYZ.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.Prueba.veterinariaXYZ.dominio.entidades.Pacientes;
import com.Prueba.veterinariaXYZ.dto.PacientesDto;
import com.Prueba.veterinariaXYZ.mapper.PacientesMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

class PacientesMapperTest {

    private PacientesMapper mapper = PacientesMapper.mapper;

    @Test
    void testToPacientesDto() {
        Pacientes paciente = new Pacientes();
        paciente.setId(1);
        paciente.setNombre_mascota("Firulais");

        PacientesDto dto = mapper.toPacientesDto(paciente);

        assertEquals(1, dto.getId());
        assertEquals("Firulais", dto.getNombre_mascota());
    }

    @Test
    void testToPacientes() {
        PacientesDto dto = new PacientesDto();
        dto.setId(2);
        dto.setNombre_mascota("Luna");

        Pacientes paciente = mapper.toPacientes(dto);

        assertEquals(2, paciente.getId());
        assertEquals("Luna", paciente.getNombre_mascota());
    }

    @Test
    void testListConversion() {
        List<Pacientes> pacientesList = List.of(new Pacientes() {{
            setId(1);
            setNombre_mascota("Firulais");
        }});

        List<PacientesDto> dtoList = mapper.toListEntityDto(pacientesList);
        assertEquals(1, dtoList.size());
        assertEquals("Firulais", dtoList.get(0).getNombre_mascota());

        List<Pacientes> pacientesList2 = mapper.toListDtoEntity(dtoList);
        assertEquals(1, pacientesList2.size());
        assertEquals("Firulais", pacientesList2.get(0).getNombre_mascota());
    }
}
