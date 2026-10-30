package com.inmobiliaria.service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

@Service
public class PdfGeneratorService {

    public byte[] generarFacturaInquilino(String nombreCompleto, String dniNie, String inmuebleDir, Double monto) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLUE);
            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            
            document.add(new Paragraph("INMOBILIARIA MANAGEMENT - FACTURA ALQUILER", titleFont));
            document.add(new Paragraph("--------------------------------------------------------------------------------"));
            document.add(new Paragraph("DATOS DEL INQUILINO:", headerFont));
            document.add(new Paragraph("Nombre Completo: " + nombreCompleto));
            document.add(new Paragraph("DNI / NIE: " + dniNie));
            document.add(new Paragraph("\nDATOS DEL INMUEBLE:", headerFont));
            document.add(new Paragraph("Dirección: " + inmuebleDir));
            document.add(new Paragraph("\nDESGLOSE DE PAGOS:", headerFont));
            document.add(new Paragraph("Importe Alquiler Mensual: " + monto + " EUR"));
            document.add(new Paragraph("--------------------------------------------------------------------------------"));
            document.add(new Paragraph("TOTAL A PAGAR: " + monto + " EUR", headerFont));
            document.add(new Paragraph("\nGracias por su confianza."));
            
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public byte[] generarLiquidacionPropietario(String nombreCompleto, String dniNie, String iban, Double totalIngresos, Double honorarios) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.DARK_GRAY);
            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);

            document.add(new Paragraph("LIQUIDACIÓN MENSUAL A PROPIETARIO", titleFont));
            document.add(new Paragraph("--------------------------------------------------------------------------------"));
            document.add(new Paragraph("DATOS DEL PROPIETARIO:", headerFont));
            document.add(new Paragraph("Nombre Completo: " + nombreCompleto));
            document.add(new Paragraph("DNI / NIE: " + dniNie));
            document.add(new Paragraph("Cuenta IBAN abonada: " + (iban != null ? iban : "No especificado")));
            document.add(new Paragraph("\nRESUMEN DE LIQUIDACIÓN:", headerFont));
            document.add(new Paragraph("Total Alquileres Cobrados: " + totalIngresos + " EUR"));
            document.add(new Paragraph("Comisión / Honorarios Agencia: -" + honorarios + " EUR"));
            document.add(new Paragraph("--------------------------------------------------------------------------------"));
            document.add(new Paragraph("NETO A TRANSFERIR: " + (totalIngresos - honorarios) + " EUR", headerFont));
            
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
