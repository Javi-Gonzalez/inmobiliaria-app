package com.inmobiliaria.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class Inquilino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido1;
    private String apellido2;

    @Column(unique = true)
    private String dniNie;

    private String email;
    private String telefono;
    private String iban;
    private String fechaInicioContrato;
    private boolean contratoActivo = true;

    @OneToOne
    @JoinColumn(name = "inmueble_id")
    @JsonIgnoreProperties("inquilino")
    private Inmueble inmueble;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido1() { return apellido1; }
    public void setApellido1(String apellido1) { this.apellido1 = apellido1; }
    public String getApellido2() { return apellido2; }
    public void setApellido2(String apellido2) { this.apellido2 = apellido2; }
    public String getDniNie() { return dniNie; }
    public void setDniNie(String dniNie) { this.dniNie = dniNie; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }
    public String getFechaInicioContrato() { return fechaInicioContrato; }
    public void setFechaInicioContrato(String fechaInicioContrato) { this.fechaInicioContrato = fechaInicioContrato; }
    public boolean isContratoActivo() { return contratoActivo; }
    public void setContratoActivo(boolean contratoActivo) { this.contratoActivo = contratoActivo; }
    public Inmueble getInmueble() { return inmueble; }
    public void setInmueble(Inmueble inmueble) { this.inmueble = inmueble; }

    public String getNombreCompleto() {
        return nombre + " " + (apellido1 != null ? apellido1 : "") + " " + (apellido2 != null ? apellido2 : "");
    }
}
