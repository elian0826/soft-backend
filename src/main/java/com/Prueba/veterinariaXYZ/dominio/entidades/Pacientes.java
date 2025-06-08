package com.Prueba.veterinariaXYZ.dominio.entidades;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Pacientes {
    private Integer  id;
    private String nombre_mascota;
    private String especie;
    private String raza;
    private LocalDate fecha_nacimiento;
    private String tipo_identificacion_dueno;
    private String identificacion_dueno;
    private String nombre_dueno;
    private String ciudad;
    private String direccion;
    private String telefono;
    private LocalDate fecha_registro;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getNombre_mascota() {
        return nombre_mascota;
    }

    public void setNombre_mascota(String nombre_mascota) {
        this.nombre_mascota = nombre_mascota;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getTipo_identificacion_dueno() {
        return tipo_identificacion_dueno;
    }

    public void setTipo_identificacion_dueno(String tipo_identificacion_dueno) {
        this.tipo_identificacion_dueno = tipo_identificacion_dueno;
    }

    public String getIdentificacion_dueno() {
        return identificacion_dueno;
    }

    public void setIdentificacion_dueno(String identificacion_dueno) {
        this.identificacion_dueno = identificacion_dueno;
    }

    public String getNombre_dueno() {
        return nombre_dueno;
    }

    public void setNombre_dueno(String nombre_dueno) {
        this.nombre_dueno = nombre_dueno;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(LocalDate fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }



}
