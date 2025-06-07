package com.Prueba.veterinariaXYZ.dominio.manager;

import com.Prueba.veterinariaXYZ.dominio.entidades.Pacientes;
import com.Prueba.veterinariaXYZ.exception.DmException;
import java.util.List;

public interface PacientesDmInterfaz {
    void createPaciente(Pacientes paciente) throws DmException;
    void updatePaciente(Pacientes paciente) throws DmException;
    void deletePaciente(int id) throws DmException;
    Pacientes getPacienteById(int id) throws DmException;
    List<Pacientes> getAllPacientes() throws DmException;
}
