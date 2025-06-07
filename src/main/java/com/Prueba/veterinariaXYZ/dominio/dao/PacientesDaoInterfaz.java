package com.Prueba.veterinariaXYZ.dominio.dao;

import com.Prueba.veterinariaXYZ.dominio.entidades.Pacientes;
import com.Prueba.veterinariaXYZ.exception.DaoException;
import java.util.List;

public interface PacientesDaoInterfaz {
    void insert(Pacientes paciente) throws DaoException;
    void update(Pacientes paciente) throws DaoException;
    void delete(int id) throws DaoException;
    Pacientes findById(int id) throws DaoException;
    List<Pacientes> findAll() throws DaoException;
}
