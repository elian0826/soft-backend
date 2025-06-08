package com.Prueba.veterinariaXYZ.manager;

import com.Prueba.veterinariaXYZ.dominio.dao.PacientesDaoInterfaz;
import com.Prueba.veterinariaXYZ.dominio.entidades.Pacientes;
import com.Prueba.veterinariaXYZ.dominio.manager.PacientesDm;
import com.Prueba.veterinariaXYZ.exception.DaoException;
import com.Prueba.veterinariaXYZ.exception.DmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacientesDmTest {

    @Mock
    private PacientesDaoInterfaz pacientesDao;

    @InjectMocks
    private PacientesDm pacientesDm;

    private Pacientes paciente;

    @BeforeEach
    void setUp() {
        paciente = new Pacientes();
        paciente.setId(1);
        paciente.setNombre_mascota("Buddy");
        paciente.setEspecie("Perro");
        paciente.setRaza("Golden Retriever");
        paciente.setFecha_nacimiento(LocalDate.of(2019, 5, 10));
        paciente.setTipo_identificacion_dueno("CC");
        paciente.setIdentificacion_dueno("123456789");
        paciente.setNombre_dueno("Carlos Sanchez");
        paciente.setCiudad("Medellín");
        paciente.setDireccion("Calle Falsa 123");
        paciente.setTelefono("3001234567");
        paciente.setFecha_registro(LocalDate.now());
        paciente.setCreated_at(LocalDateTime.now());
        paciente.setUpdated_at(LocalDateTime.now());
    }


    @Test
    void createPaciente_Success() throws DaoException, DmException {
        doNothing().when(pacientesDao).insert(paciente);

        pacientesDm.createPaciente(paciente);

        verify(pacientesDao).insert(paciente);
    }

    @Test
    void createPaciente_DaoException() throws DaoException {
        doThrow(new DaoException("Error DAO")).when(pacientesDao).insert(paciente);

        DmException ex = assertThrows(DmException.class, () -> pacientesDm.createPaciente(paciente));

        assertTrue(ex.getMessage().contains("Error al crear el paciente"));
        verify(pacientesDao).insert(paciente);
    }

    @Test
    void updatePaciente_Success() throws DaoException, DmException {
        doNothing().when(pacientesDao).update(paciente);

        pacientesDm.updatePaciente(paciente);

        verify(pacientesDao).update(paciente);
    }

    @Test
    void updatePaciente_DaoException() throws DaoException {
        doThrow(new DaoException("Error DAO")).when(pacientesDao).update(paciente);

        DmException ex = assertThrows(DmException.class, () -> pacientesDm.updatePaciente(paciente));

        assertTrue(ex.getMessage().contains("Error al actualizar el paciente"));
        verify(pacientesDao).update(paciente);
    }

    @Test
    void deletePaciente_Success() throws DaoException, DmException {
        doNothing().when(pacientesDao).delete(paciente.getId());

        pacientesDm.deletePaciente(paciente.getId());

        verify(pacientesDao).delete(paciente.getId());
    }

    @Test
    void deletePaciente_DaoException() throws DaoException {
        doThrow(new DaoException("Error DAO")).when(pacientesDao).delete(paciente.getId());

        DmException ex = assertThrows(DmException.class, () -> pacientesDm.deletePaciente(paciente.getId()));

        assertTrue(ex.getMessage().contains("Error al eliminar el paciente"));
        verify(pacientesDao).delete(paciente.getId());
    }

    @Test
    void getPacienteById_Success() throws DaoException, DmException {
        when(pacientesDao.findById(paciente.getId())).thenReturn(paciente);

        Pacientes result = pacientesDm.getPacienteById(paciente.getId());

        assertNotNull(result);
        assertEquals(paciente.getNombre_mascota(), result.getNombre_mascota());
        verify(pacientesDao).findById(paciente.getId());
    }

    @Test
    void getPacienteById_DaoException() throws DaoException {
        when(pacientesDao.findById(paciente.getId())).thenThrow(new DaoException("Error DAO"));

        DmException ex = assertThrows(DmException.class, () -> pacientesDm.getPacienteById(paciente.getId()));

        assertTrue(ex.getMessage().contains("Error al obtener el paciente"));
        verify(pacientesDao).findById(paciente.getId());
    }

    @Test
    void getAllPacientes_Success() throws DaoException, DmException {
        List<Pacientes> lista = Collections.singletonList(paciente);
        when(pacientesDao.findAll()).thenReturn(lista);

        List<Pacientes> result = pacientesDm.getAllPacientes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(paciente.getNombre_mascota(), result.get(0).getNombre_mascota());
        verify(pacientesDao).findAll();
    }

    @Test
    void getAllPacientes_DaoException() throws DaoException {
        when(pacientesDao.findAll()).thenThrow(new DaoException("Error DAO"));

        DmException ex = assertThrows(DmException.class, () -> pacientesDm.getAllPacientes());

        assertTrue(ex.getMessage().contains("Error al obtener la lista de pacientes"));
        verify(pacientesDao).findAll();
    }
}
