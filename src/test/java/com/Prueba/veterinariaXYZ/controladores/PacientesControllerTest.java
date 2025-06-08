package com.Prueba.veterinariaXYZ.controladores;

import com.Prueba.veterinariaXYZ.cu.AdmonPacientes;
import com.Prueba.veterinariaXYZ.dto.PacientesDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; 
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PacientesController.class)
@AutoConfigureMockMvc(addFilters = false)
class PacientesControllerTest {

    @Autowired  private MockMvc mockMvc;
    @Autowired  private ObjectMapper objectMapper;

    @MockBean   private AdmonPacientes admonPacientes;


    @Test
    void insertarPaciente() throws Exception {
        PacientesDto dto = new PacientesDto();
        dto.setId(1);
        dto.setNombre_mascota("Firulais");

        doNothing().when(admonPacientes).insert(any(PacientesDto.class));

        mockMvc.perform(post("/api/pacientes/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("0"))
                .andExpect(jsonPath("$.mensaje")
                        .value("Paciente registrado exitosamente"));
    }


    @Test
    void actualizarPaciente() throws Exception {
        PacientesDto dto = new PacientesDto();
        dto.setId(1);
        dto.setNombre_mascota("Luna");

        doNothing().when(admonPacientes).update(any(PacientesDto.class));

        mockMvc.perform(put("/api/pacientes/actualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("0"))
                .andExpect(jsonPath("$.mensaje")
                        .value("Paciente actualizado exitosamente"));
    }


    @Test
    void eliminarPaciente() throws Exception {
        int id = 1;
        doNothing().when(admonPacientes).delete(id);

        mockMvc.perform(delete("/api/pacientes/eliminar/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("0"))
                .andExpect(jsonPath("$.mensaje")
                        .value("Paciente eliminado exitosamente"));
    }


    @Test
    void obtenerPaciente() throws Exception {
        PacientesDto dto = new PacientesDto();
        dto.setId(1);
        dto.setNombre_mascota("Rocky");

        Mockito.when(admonPacientes.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/pacientes/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("0"))
                .andExpect(jsonPath("$.data.nombre_mascota").value("Rocky"));
    }


    @Test
    void listarPacientes() throws Exception {
        PacientesDto dto = new PacientesDto();
        dto.setId(1);
        dto.setNombre_mascota("Pepe");

        Mockito.when(admonPacientes.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("0"))
                .andExpect(jsonPath("$.data[0].nombre_mascota").value("Pepe"));
    }


    @Test
    void exportarPacientes() throws Exception {
        byte[] excel = "dummy-excel-content".getBytes();
        Mockito.when(admonPacientes.exportarPacientes()).thenReturn(excel);

        mockMvc.perform(get("/api/pacientes/exportar"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=pacientes.xlsx"))
                .andExpect(content().bytes(excel));
    }


    @Test
    void importarPacientes() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "pacientes.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "excel-content".getBytes()
        );
        doNothing().when(admonPacientes).importarPacientes(any());

        mockMvc.perform(multipart("/api/pacientes/importar").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("0"))
                .andExpect(jsonPath("$.mensaje")
                        .value("Archivo importado correctamente"));
    }
}
