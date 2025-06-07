package com.Prueba.veterinariaXYZ.cu;

import com.Prueba.veterinariaXYZ.dominio.entidades.Pacientes;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {

    public byte[] exportarPacientes(List<Pacientes> pacientes) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pacientes");

            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] columns = {
                "ID", "Nombre Mascota", "Especie", "Raza", "Fecha Nacimiento",
                "Tipo ID Dueño", "ID Dueño", "Nombre Dueño", "Ciudad",
                "Dirección", "Teléfono", "Fecha Registro"
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
                row.createCell(1).setCellValue(paciente.getNombre_mascota());
                row.createCell(2).setCellValue(paciente.getEspecie());
                row.createCell(3).setCellValue(paciente.getRaza());
                row.createCell(4).setCellValue(paciente.getFecha_nacimiento().toString());
                row.createCell(5).setCellValue(paciente.getTipo_identificacion_dueno());
                row.createCell(6).setCellValue(paciente.getIdentificacion_dueno());
                row.createCell(7).setCellValue(paciente.getNombre_dueno());
                row.createCell(8).setCellValue(paciente.getCiudad());
                row.createCell(9).setCellValue(paciente.getDireccion());
                row.createCell(10).setCellValue(paciente.getTelefono());
                row.createCell(11).setCellValue(paciente.getFecha_registro().toString());
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

    public List<Pacientes> importarPacientes(MultipartFile file) throws IOException {
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
                paciente.setFecha_nacimiento(LocalDate.parse(getStringCellValue(row.getCell(4))));
                paciente.setTipo_identificacion_dueno(getStringCellValue(row.getCell(5)));
                paciente.setIdentificacion_dueno(getStringCellValue(row.getCell(6)));
                paciente.setNombre_dueno(getStringCellValue(row.getCell(7)));
                paciente.setCiudad(getStringCellValue(row.getCell(8)));
                paciente.setDireccion(getStringCellValue(row.getCell(9)));
                paciente.setTelefono(getStringCellValue(row.getCell(10)));
                paciente.setFecha_registro(LocalDate.parse(getStringCellValue(row.getCell(11))));

                pacientes.add(paciente);
            }
        }

        return pacientes;
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