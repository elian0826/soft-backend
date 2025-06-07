package com.Prueba.veterinariaXYZ.controladores;

import com.Prueba.veterinariaXYZ.cu.AdmonPacientes;
import com.Prueba.veterinariaXYZ.cu.ExcelService;
import com.Prueba.veterinariaXYZ.dominio.entidades.Pacientes;
import com.Prueba.veterinariaXYZ.dto.PacientesDto;
import com.Prueba.veterinariaXYZ.dto.Mensaje;
import com.Prueba.veterinariaXYZ.mapper.PacientesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacientes")
public class PacientesController {

    @Autowired
    private AdmonPacientes admonPacientes;

    @Autowired
    private ExcelService excelService;

    @PostMapping("/registrar")
    public ResponseEntity<Mensaje> insertar(@RequestBody PacientesDto pacienteDto) {
        Mensaje msg = new Mensaje();
        try {
            admonPacientes.insert(pacienteDto);
            msg.setId("0");
            msg.setMensaje("Paciente registrado exitosamente");
        } catch (RuntimeException e) {
            msg.setId("1");
            msg.setMensaje("Error al registrar: " + e.getMessage());
        }
        return ResponseEntity.ok(msg);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<Mensaje> actualizar(@RequestBody PacientesDto pacienteDto) {
        Mensaje msg = new Mensaje();
        try {
            admonPacientes.update(pacienteDto);
            msg.setId("0");
            msg.setMensaje("Paciente actualizado exitosamente");
        } catch (RuntimeException e) {
            msg.setId("1");
            msg.setMensaje("Error al actualizar: " + e.getMessage());
        }
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Mensaje> eliminar(@PathVariable int id) {
        Mensaje msg = new Mensaje();
        try {
            admonPacientes.delete(id);
            msg.setId("0");
            msg.setMensaje("Paciente eliminado exitosamente");
        } catch (RuntimeException e) {
            msg.setId("1");
            msg.setMensaje("Error al eliminar: " + e.getMessage());
        }
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mensaje> obtener(@PathVariable int id) {
        Mensaje msg = new Mensaje();
        try {
            PacientesDto dto = admonPacientes.findById(id);
            msg.setId("0");
            msg.setData(dto);
            msg.setMensaje("Paciente obtenido exitosamente");
        } catch (RuntimeException e) {
            msg.setId("1");
            msg.setMensaje("Error al obtener: " + e.getMessage());
        }
        return ResponseEntity.ok(msg);
    }

    @GetMapping
    public ResponseEntity<Mensaje> listar() {
        Mensaje msg = new Mensaje();
        try {
            List<PacientesDto> lista = admonPacientes.findAll();
            msg.setId("0");
            msg.setData(lista);
            msg.setMensaje("Listado exitoso");
        } catch (RuntimeException e) {
            msg.setId("1");
            msg.setMensaje("Error al listar: " + e.getMessage());
        }
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/exportar")
    public ResponseEntity<ByteArrayResource> exportarPacientes() {
        try {
            byte[] excelFile = admonPacientes.exportarPacientes();
            
            ByteArrayResource resource = new ByteArrayResource(excelFile);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pacientes.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(excelFile.length)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importarPacientes(@RequestPart("file") MultipartFile file) {
        try {
            admonPacientes.importarPacientes(file);
            return ResponseEntity.ok("Archivo importado correctamente");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al procesar el archivo: " + e.getMessage());
        }
    }
}
