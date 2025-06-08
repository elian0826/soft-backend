package com.Prueba.veterinariaXYZ.dominio.manager;

import com.Prueba.veterinariaXYZ.dominio.entidades.Pacientes;
import com.Prueba.veterinariaXYZ.exception.DmException;
import java.util.List;

public interface PacientesDmInterfaz {
    void createPaciente(Pacientes paciente) throws DmException;
    void updatePaciente(Pacientes paciente) throws DmException;
    void deletePaciente(Integer id) throws DmException;
    Pacientes getPacienteById(Integer id) throws DmException;
    List<Pacientes> getAllPacientes() throws DmException;
}
