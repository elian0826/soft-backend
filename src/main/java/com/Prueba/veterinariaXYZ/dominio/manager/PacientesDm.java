package com.Prueba.veterinariaXYZ.dominio.manager;

import com.Prueba.veterinariaXYZ.dominio.dao.PacientesDaoInterfaz;
import com.Prueba.veterinariaXYZ.dominio.entidades.Pacientes;
import com.Prueba.veterinariaXYZ.exception.DaoException;
import com.Prueba.veterinariaXYZ.exception.DmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PacientesDm implements PacientesDmInterfaz {

    @Autowired
    private PacientesDaoInterfaz pacientesDao;

    @Override
    public void createPaciente(Pacientes paciente) throws DmException {
        try {
            pacientesDao.insert(paciente);
        } catch (DaoException e) {
            throw new DmException("Error al crear el paciente", e);
        }
    }

    @Override
    public void updatePaciente(Pacientes paciente) throws DmException {
        try {
            pacientesDao.update(paciente);
        } catch (DaoException e) {
            throw new DmException("Error al actualizar el paciente", e);
        }
    }

    @Override
    public void deletePaciente(Integer id) throws DmException {
        try {
            pacientesDao.delete(id);
        } catch (DaoException e) {
            throw new DmException("Error al eliminar el paciente con ID: " + id, e);
        }
    }

    @Override
    public Pacientes getPacienteById(Integer id) throws DmException {
        try {
            return pacientesDao.findById(id);
        } catch (DaoException e) {
            throw new DmException("Error al obtener el paciente con ID: " + id, e);
        }
    }

    @Override
    public List<Pacientes> getAllPacientes() throws DmException {
        try {
            return pacientesDao.findAll();
        } catch (DaoException e) {
            throw new DmException("Error al obtener la lista de pacientes", e);
        }
    }
}
