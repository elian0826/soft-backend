package com.Prueba.veterinariaXYZ.cu;

import com.Prueba.veterinariaXYZ.dto.PacientesDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdmonPacientesInterfaz {

    void insert(PacientesDto dto)  throws RuntimeException;
    void update(PacientesDto dto)  throws RuntimeException;
    void delete(Integer id) throws RuntimeException;
    PacientesDto findById(Integer id) throws RuntimeException;

    List<PacientesDto> findAll()         throws RuntimeException;
    
    // Nuevos métodos para importación/exportación
    byte[] exportarPacientes() throws IOException;
    void importarPacientes(MultipartFile file) throws IOException;
}
