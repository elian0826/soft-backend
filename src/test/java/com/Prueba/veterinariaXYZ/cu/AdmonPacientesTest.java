package com.Prueba.veterinariaXYZ.cu;

import com.Prueba.veterinariaXYZ.dominio.entidades.Pacientes;
import com.Prueba.veterinariaXYZ.dominio.manager.PacientesDmInterfaz;
import com.Prueba.veterinariaXYZ.dto.PacientesDto;
import com.Prueba.veterinariaXYZ.mapper.PacientesMapper;
import com.Prueba.veterinariaXYZ.exception.DmException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdmonPacientesTest {

    @InjectMocks
    private AdmonPacientes admonPacientes;

    @Mock
    private PacientesDmInterfaz pacientesDmInterfaz;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsertCallsCreatePaciente() throws DmException {
        PacientesDto dto = new PacientesDto();
        dto.setNombre_mascota("Firulais");
        // otros campos...

        doNothing().when(pacientesDmInterfaz).createPaciente(any(Pacientes.class));

        admonPacientes.insert(dto);

        verify(pacientesDmInterfaz, times(1)).createPaciente(any(Pacientes.class));
    }

    @Test
    void testUpdateCallsUpdatePaciente() throws DmException {
        PacientesDto dto = new PacientesDto();
        dto.setId(1);
        dto.setNombre_mascota("Firulais");

        doNothing().when(pacientesDmInterfaz).updatePaciente(any(Pacientes.class));

        admonPacientes.update(dto);

        verify(pacientesDmInterfaz, times(1)).updatePaciente(any(Pacientes.class));
    }

    @Test
    void testDeleteCallsDeletePaciente() throws DmException {
        Integer id = 1;
        doNothing().when(pacientesDmInterfaz).deletePaciente(id);

        admonPacientes.delete(id);

        verify(pacientesDmInterfaz, times(1)).deletePaciente(id);
    }

    @Test
    void testFindByIdReturnsDto() throws DmException {
        Integer id = 1;
        Pacientes paciente = new Pacientes();
        paciente.setId(id);
        paciente.setNombre_mascota("Firulais");

        when(pacientesDmInterfaz.getPacienteById(id)).thenReturn(paciente);

        PacientesDto dto = admonPacientes.findById(id);

        assertNotNull(dto);
        assertEquals("Firulais", dto.getNombre_mascota());
        verify(pacientesDmInterfaz, times(1)).getPacienteById(id);
    }

    @Test
    void testFindAllReturnsList() throws DmException {
        Pacientes paciente = new Pacientes();
        paciente.setId(1);
        paciente.setNombre_mascota("Firulais");

        when(pacientesDmInterfaz.getAllPacientes()).thenReturn(Collections.singletonList(paciente));

        List<PacientesDto> list = admonPacientes.findAll();

        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals("Firulais", list.get(0).getNombre_mascota());
        verify(pacientesDmInterfaz, times(1)).getAllPacientes();
    }

    @Test
    void testExportarPacientesReturnsByteArray() throws DmException, IOException {
        Pacientes paciente = new Pacientes();
        paciente.setId(1);
        paciente.setNombre_mascota("Firulais");
        paciente.setEspecie("Perro");
        paciente.setRaza("Labrador");
        paciente.setFecha_nacimiento(LocalDate.of(2020, 1, 1));
        paciente.setTipo_identificacion_dueno("CC");
        paciente.setIdentificacion_dueno("123456");
        paciente.setNombre_dueno("Juan Perez");
        paciente.setCiudad("CiudadX");
        paciente.setDireccion("Calle 123");
        paciente.setTelefono("5555555");

        when(pacientesDmInterfaz.getAllPacientes()).thenReturn(List.of(paciente));

        byte[] excelBytes = admonPacientes.exportarPacientes();

        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);
    }

    @Test
    void testInsertThrowsRuntimeExceptionOnDmException() throws DmException {
        PacientesDto dto = new PacientesDto();

        doThrow(new DmException("Error")).when(pacientesDmInterfaz).createPaciente(any());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            admonPacientes.insert(dto);
        });

        assertTrue(exception.getMessage().contains("Error"));
    }
}
