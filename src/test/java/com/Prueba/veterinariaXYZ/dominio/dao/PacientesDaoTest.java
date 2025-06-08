package com.Prueba.veterinariaXYZ.dominio.dao;

import com.Prueba.veterinariaXYZ.dominio.entidades.Pacientes;
import com.Prueba.veterinariaXYZ.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacientesDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private PacientesDao pacientesDao;

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
    void testInsert_Success() throws DaoException {
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        pacientesDao.insert(paciente);

        verify(jdbcTemplate).update(anyString(), any(Object[].class));
    }

    @Test
    void testInsert_DataAccessException() {
        when(jdbcTemplate.update(anyString(), any(Object[].class)))
                .thenThrow(mock(DataAccessException.class));

        DaoException ex = assertThrows(DaoException.class,
                () -> pacientesDao.insert(paciente));

        assertTrue(ex.getMessage().contains("Error al insertar paciente"));
    }


    @Test
    void testUpdate_Success() throws DaoException {
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        pacientesDao.update(paciente);

        verify(jdbcTemplate).update(anyString(), any(Object[].class));
    }

    @Test
    void testUpdate_DataAccessException() {
        when(jdbcTemplate.update(anyString(), any(Object[].class)))
                .thenThrow(mock(DataAccessException.class));

        DaoException ex = assertThrows(DaoException.class,
                () -> pacientesDao.update(paciente));

        assertTrue(ex.getMessage().contains("Error al actualizar paciente"));
    }


    @Test
    void testDelete_Success() throws DaoException {
        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);

        pacientesDao.delete(paciente.getId());

        verify(jdbcTemplate).update(anyString(), eq(paciente.getId()));
    }

    @Test
    void testDelete_DataAccessException() {
        when(jdbcTemplate.update(anyString(), anyInt()))
                .thenThrow(mock(DataAccessException.class));

        DaoException ex = assertThrows(DaoException.class,
                () -> pacientesDao.delete(paciente.getId()));

        assertTrue(ex.getMessage().contains("Error al eliminar paciente"));
    }

    @Test
    void testFindById_Success() throws DaoException {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(paciente);

        Pacientes found = pacientesDao.findById(paciente.getId());

        assertNotNull(found);
        assertEquals(paciente.getNombre_mascota(), found.getNombre_mascota());
        verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), eq(paciente.getId()));
    }

    @Test
    void testFindById_NotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyInt()))
                .thenThrow(mock(EmptyResultDataAccessException.class));

        DaoException ex = assertThrows(DaoException.class,
                () -> pacientesDao.findById(paciente.getId()));

        assertTrue(ex.getMessage().contains("Error al buscar paciente por ID"));
    }

    @Test
    void testFindById_DataAccessException() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyInt()))
                .thenThrow(mock(DataAccessException.class));

        DaoException ex = assertThrows(DaoException.class,
                () -> pacientesDao.findById(paciente.getId()));

        assertTrue(ex.getMessage().contains("Error al buscar paciente por ID"));
    }


    @Test
    void testFindAll_Success() throws DaoException {
        List<Pacientes> lista = Collections.singletonList(paciente);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(lista);

        List<Pacientes> found = pacientesDao.findAll();

        assertEquals(1, found.size());
        assertEquals(paciente.getNombre_mascota(), found.get(0).getNombre_mascota());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class));
    }

    @Test
    void testFindAll_EmptyList() throws DaoException {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(Collections.emptyList());

        List<Pacientes> found = pacientesDao.findAll();

        assertTrue(found.isEmpty());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class));
    }

    @Test
    void testFindAll_DataAccessException() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenThrow(mock(DataAccessException.class));

        DaoException ex = assertThrows(DaoException.class, () -> pacientesDao.findAll());

        assertTrue(ex.getMessage().contains("Error al listar pacientes"));
    }
}
