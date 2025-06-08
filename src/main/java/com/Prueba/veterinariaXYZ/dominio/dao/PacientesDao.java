package com.Prueba.veterinariaXYZ.dominio.dao;

import com.Prueba.veterinariaXYZ.dominio.entidades.Pacientes;
import com.Prueba.veterinariaXYZ.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PacientesDao implements PacientesDaoInterfaz {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void insert(Pacientes paciente) throws DaoException {
        String sql = "INSERT INTO pacientes (nombre_mascota, especie, raza, fecha_nacimiento, tipo_identificacion_dueno, identificacion_dueno, nombre_dueno, ciudad, direccion, telefono, fecha_registro, created_at, updated_at) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            jdbcTemplate.update(sql,
                    paciente.getNombre_mascota(),
                    paciente.getEspecie(),
                    paciente.getRaza(),
                    paciente.getFecha_nacimiento() != null ? Date.valueOf(paciente.getFecha_nacimiento()) : null,
                    paciente.getTipo_identificacion_dueno(),
                    paciente.getIdentificacion_dueno(),
                    paciente.getNombre_dueno(),
                    paciente.getCiudad(),
                    paciente.getDireccion(),
                    paciente.getTelefono(),
                    paciente.getFecha_registro() != null ? Date.valueOf(paciente.getFecha_registro()) : Date.valueOf(LocalDate.now()),
                    paciente.getCreated_at() != null ? Timestamp.valueOf(paciente.getCreated_at()) : Timestamp.valueOf(LocalDateTime.now()),
                    paciente.getUpdated_at() != null ? Timestamp.valueOf(paciente.getUpdated_at()) : Timestamp.valueOf(LocalDateTime.now())
            );
        } catch (DataAccessException ex) {
            throw new DaoException("Error al insertar paciente: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(Pacientes paciente) throws DaoException {
        String sql = "UPDATE pacientes SET nombre_mascota = ?, especie = ?, raza = ?, fecha_nacimiento = ?, tipo_identificacion_dueno = ?, identificacion_dueno = ?, nombre_dueno = ?, ciudad = ?, direccion = ?, telefono = ?, fecha_registro = ?, updated_at = ? WHERE id = ?";
        try {
            jdbcTemplate.update(sql,
                    paciente.getNombre_mascota(),
                    paciente.getEspecie(),
                    paciente.getRaza(),
                    paciente.getFecha_nacimiento() != null ? Date.valueOf(paciente.getFecha_nacimiento()) : null,
                    paciente.getTipo_identificacion_dueno(),
                    paciente.getIdentificacion_dueno(),
                    paciente.getNombre_dueno(),
                    paciente.getCiudad(),
                    paciente.getDireccion(),
                    paciente.getTelefono(),
                    paciente.getFecha_registro() != null ? Date.valueOf(paciente.getFecha_registro()) : Date.valueOf(LocalDate.now()),
                    paciente.getUpdated_at() != null ? Timestamp.valueOf(paciente.getUpdated_at()) : Timestamp.valueOf(LocalDateTime.now()),
                    paciente.getId()
            );
        } catch (DataAccessException ex) {
            throw new DaoException("Error al actualizar paciente: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(Integer id) throws DaoException {
        String sql = "DELETE FROM pacientes WHERE id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (DataAccessException ex) {
            throw new DaoException("Error al eliminar paciente: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Pacientes findById(int id) throws DaoException {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapPaciente(rs), id);
        } catch (DataAccessException ex) {
            throw new DaoException("Error al buscar paciente por ID: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Pacientes> findAll() throws DaoException {
        String sql = "SELECT * FROM pacientes";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> mapPaciente(rs));
        } catch (DataAccessException ex) {
            throw new DaoException("Error al listar pacientes: " + ex.getMessage(), ex);
        }
    }

    private Pacientes mapPaciente(ResultSet rs) throws SQLException {
        Pacientes p = new Pacientes();
        p.setId(rs.getInt("id"));
        p.setNombre_mascota(rs.getString("nombre_mascota"));
        p.setEspecie(rs.getString("especie"));
        p.setRaza(rs.getString("raza"));

        Date sqlDateNac = rs.getDate("fecha_nacimiento");
        if (sqlDateNac != null) {
            p.setFecha_nacimiento(sqlDateNac.toLocalDate());
        }

        p.setTipo_identificacion_dueno(rs.getString("tipo_identificacion_dueno"));
        p.setIdentificacion_dueno(rs.getString("identificacion_dueno"));
        p.setNombre_dueno(rs.getString("nombre_dueno"));
        p.setCiudad(rs.getString("ciudad"));
        p.setDireccion(rs.getString("direccion"));
        p.setTelefono(rs.getString("telefono"));

        Date sqlDateReg = rs.getDate("fecha_registro");
        if (sqlDateReg != null) {
            p.setFecha_registro(sqlDateReg.toLocalDate());
        }

        Timestamp tsC = rs.getTimestamp("created_at");
        if (tsC != null) {
            p.setCreated_at(tsC.toLocalDateTime());
        }

        Timestamp tsU = rs.getTimestamp("updated_at");
        if (tsU != null) {
            p.setUpdated_at(tsU.toLocalDateTime());
        }

        return p;
    }
}
