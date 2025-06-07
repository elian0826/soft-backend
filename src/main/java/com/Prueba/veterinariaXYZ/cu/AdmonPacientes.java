package com.Prueba.veterinariaXYZ.cu;

import com.Prueba.veterinariaXYZ.dominio.entidades.Pacientes;
import com.Prueba.veterinariaXYZ.dominio.manager.PacientesDmInterfaz;
import com.Prueba.veterinariaXYZ.dto.PacientesDto;
import com.Prueba.veterinariaXYZ.exception.DmException;
import com.Prueba.veterinariaXYZ.mapper.PacientesMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdmonPacientes implements AdmonPacientesInterfaz {

    @Autowired
    private PacientesDmInterfaz pacientesDmInterfaz;

    @Override
    public void insert(PacientesDto pacienteDto) throws RuntimeException {
        Pacientes paciente = PacientesMapper.mapper.toPacientes(pacienteDto);
        try {
            pacientesDmInterfaz.createPaciente(paciente);
        } catch (DmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PacientesDto pacienteDto) throws RuntimeException {
        Pacientes paciente = PacientesMapper.mapper.toPacientes(pacienteDto);
        try {
            pacientesDmInterfaz.updatePaciente(paciente);
        } catch (DmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) throws RuntimeException {
        try {
            pacientesDmInterfaz.deletePaciente(id);
        } catch (DmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PacientesDto findById(int id) throws RuntimeException {
        try {
            Pacientes paciente = pacientesDmInterfaz.getPacienteById(id);
            return PacientesMapper.mapper.toPacientesDto(paciente);
        } catch (DmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PacientesDto> findAll() throws RuntimeException {
        try {
            List<Pacientes> pacientes = pacientesDmInterfaz.getAllPacientes();
            return PacientesMapper.mapper.toListEntityDto(pacientes);
        } catch (DmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] exportarPacientes() throws IOException {
        List<PacientesDto> pacientesDto = findAll();
        List<Pacientes> pacientes = pacientesDto.stream()
            .map(dto -> PacientesMapper.mapper.toPacientes(dto))
            .collect(Collectors.toList());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pacientes");

            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] columns = {
                "ID", "Nombre Mascota", "Especie", "Raza", "Fecha Nacimiento",
                "Tipo ID Dueño", "ID Dueño", "Nombre Dueño", "Ciudad",
                "Dirección", "Teléfono"
            };
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Llenar datos
            int rowNum = 1;
            for (Pacientes paciente : pacientes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(paciente.getId());
                row.createCell(1).setCellValue(paciente.getNombre_mascota() != null ? paciente.getNombre_mascota() : "");
                row.createCell(2).setCellValue(paciente.getEspecie() != null ? paciente.getEspecie() : "");
                row.createCell(3).setCellValue(paciente.getRaza() != null ? paciente.getRaza() : "");
                row.createCell(4).setCellValue(paciente.getFecha_nacimiento() != null ? paciente.getFecha_nacimiento().toString() : "");
                row.createCell(5).setCellValue(paciente.getTipo_identificacion_dueno() != null ? paciente.getTipo_identificacion_dueno() : "");
                row.createCell(6).setCellValue(paciente.getIdentificacion_dueno() != null ? paciente.getIdentificacion_dueno() : "");
                row.createCell(7).setCellValue(paciente.getNombre_dueno() != null ? paciente.getNombre_dueno() : "");
                row.createCell(8).setCellValue(paciente.getCiudad() != null ? paciente.getCiudad() : "");
                row.createCell(9).setCellValue(paciente.getDireccion() != null ? paciente.getDireccion() : "");
                row.createCell(10).setCellValue(paciente.getTelefono() != null ? paciente.getTelefono() : "");
            }

            // Autoajustar columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Convertir a bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    @Override
    public void importarPacientes(MultipartFile file) throws IOException {
        List<Pacientes> pacientes = new ArrayList<>();
        
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Saltar la fila de encabezados
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row row = rows.next();
                Pacientes paciente = new Pacientes();
                
                // Leer datos de cada celda
                paciente.setNombre_mascota(getStringCellValue(row.getCell(1)));
                paciente.setEspecie(getStringCellValue(row.getCell(2)));
                paciente.setRaza(getStringCellValue(row.getCell(3)));
                
                // Manejar fechas
                String fechaNacimiento = getStringCellValue(row.getCell(4));
                if (!fechaNacimiento.isEmpty()) {
                    try {
                        paciente.setFecha_nacimiento(LocalDate.parse(fechaNacimiento));
                    } catch (Exception e) {
                        // Si hay error al parsear la fecha, se deja null
                    }
                }
                
                paciente.setTipo_identificacion_dueno(getStringCellValue(row.getCell(5)));
                paciente.setIdentificacion_dueno(getStringCellValue(row.getCell(6)));
                paciente.setNombre_dueno(getStringCellValue(row.getCell(7)));
                paciente.setCiudad(getStringCellValue(row.getCell(8)));
                paciente.setDireccion(getStringCellValue(row.getCell(9)));
                paciente.setTelefono(getStringCellValue(row.getCell(10)));
                
                // Establecer la fecha de registro como la fecha actual
                paciente.setFecha_registro(LocalDate.now());
                
                // Establecer timestamps de auditoría
                paciente.setCreated_at(LocalDateTime.now());
                paciente.setUpdated_at(LocalDateTime.now());

                // Convertir a DTO y guardar
                PacientesDto dto = PacientesMapper.mapper.toPacientesDto(paciente);
                insert(dto);
            }
        }
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }
}
