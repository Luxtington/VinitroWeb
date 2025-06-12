package ru.ivanov.vinitro.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ivanov.vinitro.model.AppointmentForAnalysis;
import ru.ivanov.vinitro.service.AppointmentService;
import ru.ivanov.vinitro.service.PdfService;

import java.io.IOException;

@Controller
@RequestMapping("/vinitro/analyses/pdf")
public class PdfController {
    private final PdfService pdfService;
    private final AppointmentService appointmentService;

    @Autowired
    public PdfController(PdfService pdfService, AppointmentService appointmentService) {
        this.pdfService = pdfService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/download/{id}")
    public void downloadPdf(@PathVariable String id,
                            HttpServletResponse response) throws IOException {

        AppointmentForAnalysis appointment = appointmentService.findById(id).orElse(null);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=appointment_" + id + ".pdf");

        try (PDDocument document = PDDocument.load(pdfService.generateAppointmentPdf(appointment))) {
            document.save(response.getOutputStream());
        }
    }
}
