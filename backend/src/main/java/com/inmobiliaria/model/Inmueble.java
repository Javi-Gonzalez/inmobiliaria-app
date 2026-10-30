package com.inmobiliaria.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class Inmueble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo; // PISO, CASA, LOCAL, GARAJE, TERRENO
    private String direccion;
    private String poblacion;
    private String codigoPostal;
    private Double m2;
    private Integer habitaciones;
    private Integer baños;
    private Double precioAlquiler;
    private Double honorariosAgencia;
    private String estado = "DISPONIBLE"; // DISPONIBLE, ALQUILADO, MANTENIMIENTO
    private String notas;

    @ManyToOne
    @JoinColumn(name = "propietario_id")
    @JsonIgnoreProperties("inmuebles")
    private Propietario propietario;

    @OneToOne(mappedBy = "inmueble")
    @JsonIgnoreProperties("inmueble")
    private Inquilino inquilino;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getPoblacion() { return poblacion; }
    public void setPoblacion(String poblacion) { this.poblacion = poblacion; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    public Double getM2() { return m2; }
    public void setM2(Double m2) { this.m2 = m2; }
    public Integer getHabitaciones() { return habitaciones; }
    public void setHabitaciones(Integer habitaciones) { this.habitaciones = habitaciones; }
    public Integer getBaños() { return baños; }
    public void setBaños(Integer baños) { this.baños = baños; }
    public Double getPrecioAlquiler() { return precioAlquiler; }
    public void setPrecioAlquiler(Double precioAlquiler) { this.precioAlquiler = precioAlquiler; }
    public Double getHonorariosAgencia() { return honorariosAgencia; }
    public void setHonorariosAgencia(Double honorariosAgencia) { this.honorariosAgencia = honorariosAgencia; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public Propietario getPropietario() { return propietario; }
    public void setPropietario(Propietario propietario) { this.propietario = propietario; }
    public Inquilino getInquilino() { return inquilino; }
    public void setInquilino(Inquilino inquilino) { this.inquilino = inquilino; }
}
