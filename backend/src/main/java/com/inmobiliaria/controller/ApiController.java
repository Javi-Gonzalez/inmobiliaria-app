package com.inmobiliaria.controller;

import com.inmobiliaria.model.Inmueble;
import com.inmobiliaria.model.Inquilino;
import com.inmobiliaria.model.Propietario;
import com.inmobiliaria.repository.InmuebleRepository;
import com.inmobiliaria.repository.InquilinoRepository;
import com.inmobiliaria.repository.PropietarioRepository;
import com.inmobiliaria.service.FacturacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    private final InmuebleRepository inmuebleRepo;
    private final PropietarioRepository propietarioRepo;
    private final InquilinoRepository inquilinoRepo;
    private final FacturacionService facturacionService;

    public ApiController(InmuebleRepository inmuebleRepo, 
                         PropietarioRepository propietarioRepo, 
                         InquilinoRepository inquilinoRepo, 
                         FacturacionService facturacionService) {
        this.inmuebleRepo = inmuebleRepo;
        this.propietarioRepo = propietarioRepo;
        this.inquilinoRepo = inquilinoRepo;
        this.facturacionService = facturacionService;
    }

    // ==========================================
    // --- PROPIETARIOS ---
    // ==========================================
    @GetMapping("/propietarios")
    public List<Propietario> getPropietarios() { return propietarioRepo.findAll(); }

    @PostMapping("/propietarios")
    public Propietario createPropietario(@RequestBody Propietario p) { return propietarioRepo.save(p); }

    @PutMapping("/propietarios/{id}")
    public ResponseEntity<Propietario> updatePropietario(@PathVariable Long id, @RequestBody Propietario p) {
        return propietarioRepo.findById(id).map(existing -> {
            existing.setNombre(p.getNombre());
            existing.setApellido1(p.getApellido1());
            existing.setApellido2(p.getApellido2());
            existing.setDniNie(p.getDniNie());
            existing.setEmail(p.getEmail());
            existing.setTelefono(p.getTelefono());
            existing.setIban(p.getIban());
            existing.setDireccionPostal(p.getDireccionPostal());
            existing.setNotas(p.getNotas());
            return ResponseEntity.ok(propietarioRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/propietarios/{id}")
    public ResponseEntity<Void> deletePropietario(@PathVariable Long id) {
        if (propietarioRepo.existsById(id)) {
            propietarioRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==========================================
    // --- INMUEBLES ---
    // ==========================================
    @GetMapping("/inmuebles")
    public List<Inmueble> getInmuebles() { return inmuebleRepo.findAll(); }

    @PostMapping("/inmuebles")
    public Inmueble createInmueble(@RequestBody Map<String, Object> body) {
        Inmueble inm = new Inmueble();
        maperInmueble(inm, body);
        return inmuebleRepo.save(inm);
    }

    @PutMapping("/inmuebles/{id}")
    public ResponseEntity<Inmueble> updateInmueble(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return inmuebleRepo.findById(id).map(existing -> {
            maperInmueble(existing, body);
            return ResponseEntity.ok(inmuebleRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    private void maperInmueble(Inmueble inm, Map<String, Object> body) {
        inm.setTipo((String) body.get("tipo"));
        inm.setDireccion((String) body.get("direccion"));
        inm.setPoblacion((String) body.get("poblacion"));
        inm.setCodigoPostal((String) body.get("codigoPostal"));
        inm.setM2(body.get("m2") != null && !body.get("m2").toString().isEmpty() ? Double.parseDouble(body.get("m2").toString()) : null);
        inm.setHabitaciones(body.get("habitaciones") != null && !body.get("habitaciones").toString().isEmpty() ? Integer.parseInt(body.get("habitaciones").toString()) : null);
        inm.setBaños(body.get("baños") != null && !body.get("baños").toString().isEmpty() ? Integer.parseInt(body.get("baños").toString()) : null);
        inm.setPrecioAlquiler(body.get("precioAlquiler") != null && !body.get("precioAlquiler").toString().isEmpty() ? Double.parseDouble(body.get("precioAlquiler").toString()) : 0.0);
        inm.setHonorariosAgencia(body.get("honorariosAgencia") != null && !body.get("honorariosAgencia").toString().isEmpty() ? Double.parseDouble(body.get("honorariosAgencia").toString()) : 0.0);
        inm.setEstado(body.get("estado") != null ? body.get("estado").toString() : "DISPONIBLE");
        inm.setNotas((String) body.get("notas"));

        if (body.get("propietarioId") != null && !body.get("propietarioId").toString().isEmpty()) {
            Long propId = Long.parseLong(body.get("propietarioId").toString());
            Propietario prop = propietarioRepo.findById(propId).orElse(null);
            inm.setPropietario(prop);
        } else {
            inm.setPropietario(null);
        }
    }

    @DeleteMapping("/inmuebles/{id}")
    public ResponseEntity<Void> deleteInmueble(@PathVariable Long id) {
        return inmuebleRepo.findById(id).map(inm -> {
            if (inm.getInquilino() != null) {
                Inquilino inq = inm.getInquilino();
                inq.setInmueble(null);
                inquilinoRepo.save(inq);
            }
            inmuebleRepo.delete(inm);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // ==========================================
    // --- INQUILINOS ---
    // ==========================================
    @GetMapping("/inquilinos")
    public List<Inquilino> getInquilinos() { return inquilinoRepo.findAll(); }

    @PostMapping("/inquilinos")
    public Inquilino createInquilino(@RequestBody Map<String, Object> body) {
        Inquilino inq = new Inquilino();
        maperInquilino(inq, body);
        return inquilinoRepo.save(inq);
    }

    @PutMapping("/inquilinos/{id}")
    public ResponseEntity<Inquilino> updateInquilino(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return inquilinoRepo.findById(id).map(existing -> {
            if (existing.getInmueble() != null) {
                Inmueble oldInm = existing.getInmueble();
                oldInm.setEstado("DISPONIBLE");
                inmuebleRepo.save(oldInm);
                existing.setInmueble(null);
            }
            maperInquilino(existing, body);
            return ResponseEntity.ok(inquilinoRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    private void maperInquilino(Inquilino inq, Map<String, Object> body) {
        inq.setNombre((String) body.get("nombre"));
        inq.setApellido1((String) body.get("apellido1"));
        inq.setApellido2((String) body.get("apellido2"));
        inq.setDniNie((String) body.get("dniNie"));
        inq.setEmail((String) body.get("email"));
        inq.setTelefono((String) body.get("telefono"));
        inq.setIban((String) body.get("iban"));
        inq.setFechaInicioContrato((String) body.get("fechaInicioContrato"));
        inq.setContratoActivo(body.get("contratoActivo") != null ? Boolean.parseBoolean(body.get("contratoActivo").toString()) : true);

        if (body.get("inmuebleId") != null && !body.get("inmuebleId").toString().isEmpty()) {
            Long inmId = Long.parseLong(body.get("inmuebleId").toString());
            Inmueble newInm = inmuebleRepo.findById(inmId).orElse(null);
            if (newInm != null) {
                newInm.setEstado("ALQUILADO");
                inmuebleRepo.save(newInm);
                inq.setInmueble(newInm);
            }
        }
    }

    @DeleteMapping("/inquilinos/{id}")
    public ResponseEntity<Void> deleteInquilino(@PathVariable Long id) {
        return inquilinoRepo.findById(id).map(inq -> {
            if (inq.getInmueble() != null) {
                Inmueble inm = inq.getInmueble();
                inm.setEstado("DISPONIBLE");
                inmuebleRepo.save(inm);
            }
            inquilinoRepo.delete(inq);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // ==========================================
    // --- FACTURACIÓN AUTOMÁTICA ---
    // ==========================================
    @PostMapping("/facturacion/enviar-inquilinos")
    public Map<String, Object> enviarInquilinos() {
        int total = facturacionService.enviarFacturasInquilinos();
        return Map.of("status", "OK", "enviados", total);
    }

    @PostMapping("/facturacion/enviar-propietarios")
    public Map<String, Object> enviarPropietarios() {
        int total = facturacionService.enviarLiquidacionesPropietarios();
        return Map.of("status", "OK", "enviados", total);
    }
}
