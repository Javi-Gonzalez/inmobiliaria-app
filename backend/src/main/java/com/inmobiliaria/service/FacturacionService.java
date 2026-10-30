package com.inmobiliaria.service;

import com.inmobiliaria.model.Inmueble;
import com.inmobiliaria.model.Inquilino;
import com.inmobiliaria.model.Propietario;
import com.inmobiliaria.repository.InquilinoRepository;
import com.inmobiliaria.repository.PropietarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacturacionService {

    private final InquilinoRepository inquilinoRepository;
    private final PropietarioRepository propietarioRepository;
    private final PdfGeneratorService pdfService;
    private final EmailService emailService;

    public FacturacionService(InquilinoRepository inquilinoRepository,
                              PropietarioRepository propietarioRepository,
                              PdfGeneratorService pdfService,
                              EmailService emailService) {
        this.inquilinoRepository = inquilinoRepository;
        this.propietarioRepository = propietarioRepository;
        this.pdfService = pdfService;
        this.emailService = emailService;
    }

    public int enviarFacturasInquilinos() {
        List<Inquilino> inquilinos = inquilinoRepository.findByContratoActivoTrue();
        int enviadas = 0;
        for (Inquilino inq : inquilinos) {
            if (inq.getInmueble() != null && inq.getEmail() != null && !inq.getEmail().isBlank()) {
                byte[] pdf = pdfService.generarFacturaInquilino(
                        inq.getNombreCompleto(),
                        inq.getDniNie(),
                        inq.getInmueble().getDireccion(),
                        inq.getInmueble().getPrecioAlquiler()
                );
                emailService.enviarFacturaEmail(
                        inq.getEmail(),
                        "Factura Mensual de Alquiler",
                        "Estimado/a " + inq.getNombreCompleto() + ",\n\nAdjuntamos la factura correspondiente al alquiler de este mes.",
                        pdf,
                        "Factura_" + inq.getDniNie() + ".pdf"
                );
                enviadas++;
            }
        }
        return enviadas;
    }

    public int enviarLiquidacionesPropietarios() {
        List<Propietario> propietarios = propietarioRepository.findAll();
        int enviadas = 0;
        for (Propietario prop : propietarios) {
            if (prop.getEmail() != null && !prop.getEmail().isBlank()) {
                double totalCobrado = 0.0;
                double honorarios = 0.0;
                for (Inmueble inm : prop.getInmuebles()) {
                    if ("ALQUILADO".equalsIgnoreCase(inm.getEstado()) && inm.getPrecioAlquiler() != null) {
                        totalCobrado += inm.getPrecioAlquiler();
                        if (inm.getHonorariosAgencia() != null) {
                            honorarios += inm.getHonorariosAgencia();
                        }
                    }
                }
                
                byte[] pdf = pdfService.generarLiquidacionPropietario(
                        prop.getNombreCompleto(),
                        prop.getDniNie(),
                        prop.getIban(),
                        totalCobrado,
                        honorarios
                );
                emailService.enviarFacturaEmail(
                        prop.getEmail(),
                        "Liquidación Mensual de Inmuebles",
                        "Estimado/a " + prop.getNombreCompleto() + ",\n\nAdjuntamos el desglose de la liquidación mensual de sus inmuebles.",
                        pdf,
                        "Liquidacion_" + prop.getDniNie() + ".pdf"
                );
                enviadas++;
            }
        }
        return enviadas;
    }
}
