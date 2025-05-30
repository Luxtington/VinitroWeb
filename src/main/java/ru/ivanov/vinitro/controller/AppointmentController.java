package ru.ivanov.vinitro.controller;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.vinitro.dto.VinitroUserDetails;
import ru.ivanov.vinitro.model.AppointmentForAnalysis;
import ru.ivanov.vinitro.service.AnalysisService;
import ru.ivanov.vinitro.service.AppointmentService;

@Controller
@RequestMapping("/vinitro/analyses")
public class AppointmentController {

    private final AnalysisService analysisService;
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AnalysisService analysisService, AppointmentService appointmentService) {
        this.analysisService = analysisService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/{analysis_id}/appoint")
    public String showAppointmentPage(@PathVariable("analysis_id") String id, Model model){
        model.addAttribute("analysis", analysisService.findById(id).orElse(null));
        model.addAttribute("new_appointment", new AppointmentForAnalysis());
        return "analysis_appointment_page";
    }

    // создаем заново запись и сейвим именно ее,
    // тк при пост-запросе теряется айди старой записи
    // но старую в бд не сохраняем, поэтому по памяти все ок
    @PostMapping("/{analysis_id}/appoint")
    public String appointForAnalysis(@PathVariable("analysis_id") String id,
                                     @ModelAttribute("new_appointment") AppointmentForAnalysis appointment,
                                     Authentication authentication,
                                     Model model){
        model.addAttribute("analysis", analysisService.findById(id).orElse(null));
        appointmentService.appointUserToAnalysis(id, ((VinitroUserDetails)authentication.getPrincipal()).getId() , appointment.getDate(), appointment.getTime());
        return "concrete_analysis";
    }
}
