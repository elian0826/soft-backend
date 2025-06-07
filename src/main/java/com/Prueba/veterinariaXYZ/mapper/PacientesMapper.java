package com.Prueba.veterinariaXYZ.mapper;

import com.Prueba.veterinariaXYZ.dominio.entidades.Pacientes;
import com.Prueba.veterinariaXYZ.dto.PacientesDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PacientesMapper {

    PacientesMapper mapper = Mappers.getMapper(PacientesMapper.class);

    PacientesDto      toPacientesDto(Pacientes entity);
    Pacientes         toPacientes(PacientesDto dto);

    List<PacientesDto> toListEntityDto(List<Pacientes> entities);
    List<Pacientes>    toListDtoEntity(List<PacientesDto> dtos);
}
