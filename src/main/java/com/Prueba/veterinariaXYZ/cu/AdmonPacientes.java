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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class AdmonPacientes implements AdmonPacientesInterfaz {

    @Autowired
    private PacientesDmInterfaz pacientesDmInterfaz;
    
    @Override
    public void insert(PacientesDto dto) {
        try {
            pacientesDmInterfaz.createPaciente(
                    PacientesMapper.mapper.toPacientes(dto));
        } catch (DmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PacientesDto dto) {
        try {
            pacientesDmInterfaz.updatePaciente(
                    PacientesMapper.mapper.toPacientes(dto));
        } catch (DmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            pacientesDmInterfaz.deletePaciente(id);
        } catch (DmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PacientesDto findById(int id) {
        try {
            return PacientesMapper.mapper.toPacientesDto(
                    pacientesDmInterfaz.getPacienteById(id));
        } catch (DmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PacientesDto> findAll() {
        try {
            return PacientesMapper.mapper.toListEntityDto(
                    pacientesDmInterfaz.getAllPacientes());
        } catch (DmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] exportarPacientes() throws IOException {
        List<Pacientes> pacientes = findAll().stream()
                .map(PacientesMapper.mapper::toPacientes)
                .collect(Collectors.toList());

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Pacientes");
            String[] col = { "ID", "Nombre Mascota", "Especie", "Raza",
                    "Fecha Nacimiento", "Tipo ID Dueño", "ID Dueño",
                    "Nombre Dueño", "Ciudad", "Dirección", "Teléfono" };

            Row header = sheet.createRow(0);
            for (int i = 0; i < col.length; i++) header.createCell(i).setCellValue(col[i]);

            int rowNum = 1;
            for (Pacientes p : pacientes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(nvl(p.getNombre_mascota()));
                row.createCell(2).setCellValue(nvl(p.getEspecie()));
                row.createCell(3).setCellValue(nvl(p.getRaza()));
                row.createCell(4).setCellValue(p.getFecha_nacimiento() != null ?
                        p.getFecha_nacimiento().toString() : "");
                row.createCell(5).setCellValue(nvl(p.getTipo_identificacion_dueno()));
                row.createCell(6).setCellValue(nvl(p.getIdentificacion_dueno()));
                row.createCell(7).setCellValue(nvl(p.getNombre_dueno()));
                row.createCell(8).setCellValue(nvl(p.getCiudad()));
                row.createCell(9).setCellValue(nvl(p.getDireccion()));
                row.createCell(10).setCellValue(nvl(p.getTelefono()));
            }
            for (int i = 0; i < col.length; i++) sheet.autoSizeColumn(i);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        }
    }


    @Override
    @Transactional
    public void importarPacientes(MultipartFile file) throws IOException {

        /* 1️⃣  Cachear todos los pacientes existentes en un Map
               clave = identificacionDueno|nombreMascota                 */
        Map<String, PacientesDto> existentes = findAll().stream()
                .collect(Collectors.toMap(
                        p -> key(p.getIdentificacion_dueno(), p.getNombre_mascota()),
                        p -> p
                ));

        try (Workbook wb = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next(); // saltar encabezado

            while (rows.hasNext()) {
                Row row = rows.next();

                PacientesDto dto = new PacientesDto();
                dto.setNombre_mascota(getStringCellValue(row.getCell(1)));
                dto.setEspecie        (getStringCellValue(row.getCell(2)));
                dto.setRaza           (getStringCellValue(row.getCell(3)));

                String fNac = getStringCellValue(row.getCell(4));
                if (!fNac.isBlank()) dto.setFecha_nacimiento(LocalDate.parse(fNac));

                dto.setTipo_identificacion_dueno(getStringCellValue(row.getCell(5)));
                dto.setIdentificacion_dueno      (getStringCellValue(row.getCell(6)));
                dto.setNombre_dueno              (getStringCellValue(row.getCell(7)));
                dto.setCiudad                    (getStringCellValue(row.getCell(8)));
                dto.setDireccion                 (getStringCellValue(row.getCell(9)));
                dto.setTelefono                  (getStringCellValue(row.getCell(10)));

                String key = key(dto.getIdentificacion_dueno(), dto.getNombre_mascota());

                if (existentes.containsKey(key)) {
                    dto.setId(existentes.get(key).getId());
                    update(dto);
                } else {

                    insert(dto);
                }
            }
        }
    }

    /* ====================================================== *
     *  Helpers
     * ====================================================== */
    private String nvl(String s) { return s != null ? s : ""; }

    private String key(String identificacion, String nombreMascota) {
        return (identificacion == null ? "" : identificacion.trim()) + "|" +
                (nombreMascota   == null ? "" : nombreMascota.trim()).toLowerCase();
    }

    /** Lee la celda como String, detectando fechas y números. */
    private String getStringCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getLocalDateTimeCellValue()
                    .toLocalDate().toString()
                    : longOrDouble(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default      -> "";
        };
    }

    private String longOrDouble(double num) {
        return num == (long) num ? String.valueOf((long) num) : String.valueOf(num);
    }
}
