package ru.ivanov.vinitro.controller;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.vinitro.dto.VinitroUserDetails;
import ru.ivanov.vinitro.model.AppointmentForAnalysis;
import ru.ivanov.vinitro.model.User;
import ru.ivanov.vinitro.service.AnalysisService;
import ru.ivanov.vinitro.service.AppointmentService;
import ru.ivanov.vinitro.service.UserService;
import ru.ivanov.vinitro.util.ResultsKeeper;
import ru.ivanov.vinitro.util.TagKeeper;

@Controller
@RequestMapping("/vinitro/analyses")
public class AppointmentController {

    private final AnalysisService analysisService;
    private final AppointmentService appointmentService;
    private final UserService userService;

    @Autowired
    public AppointmentController(AnalysisService analysisService, AppointmentService appointmentService, UserService userService) {
        this.analysisService = analysisService;
        this.appointmentService = appointmentService;
        this.userService = userService;
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
                                     Authentication authentication){
        User user = userService.findById(((VinitroUserDetails)authentication.getPrincipal()).getId()).orElse(null);
        appointmentService.appointUserToAnalysis(id, user.getId() , appointment.getDate(), appointment.getTime());
        return "redirect:/vinitro/analyses/" + id;
    }

    @GetMapping("/waiting")
    public String showNurseActivityPage(Model model){
        model.addAttribute("appointments", appointmentService.getAllWaitingAnalyses());
        model.addAttribute("tagKeeper", new TagKeeper());
        return "nurse";
    }

    @PatchMapping("/confirm/{appointmentId}")
    public String confirmAnalysisAppointment(@PathVariable("appointmentId") String id,
                                             @ModelAttribute("tagKeeper") TagKeeper tagKeeper){
        appointmentService.moveAppointmentStatusFromWaitingToProcessing(id, tagKeeper.getTag());
        return "redirect:/vinitro/analyses/waiting";
    }

    @GetMapping("/waiting/personal")
    public String pageOfPersonalConfirmingAnalysisAppointment(@ModelAttribute("appointment") AppointmentForAnalysis appointmentForAnalysis,
                                                              @ModelAttribute("tagKeeper") TagKeeper tagKeeper,
                                                              Model model){
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("analyses", analysisService.findAllAnalyses());
        return "appointment_confirming_page";
    }

    @PostMapping("/waiting/personal/confirm")
    public String personalConfirmAnalysisAppointment(@ModelAttribute("appointment") AppointmentForAnalysis appointment){
        appointmentService.save(appointment);
        appointmentService.moveAppointmentStatusFromWaitingToProcessing(appointment.getId(), appointment.getTag());
        return "redirect:/vinitro/analyses/waiting";
    }

    @GetMapping("/processing")
    public String showAssistantActivityPage(Model model){
        model.addAttribute("appointments", appointmentService.getAllProcessingAnalyses());
        return "assistant";
    }

    @GetMapping("/processing/fill/{appointmentId}")
    public String showAppointmentPageCompleting(@PathVariable("appointmentId") String id, Model model){
        model.addAttribute("appointment", appointmentService.findById(id).orElse(null));
        model.addAttribute("results", new ResultsKeeper());
        return "filling_analysis_results";
    }

    @PostMapping("/processing/fill/{appointmentId}")
    public String fillAnalysisResults(@PathVariable("appointmentId") String id,
                                      @ModelAttribute("results") ResultsKeeper resultsKeeper){
        AppointmentForAnalysis appointment = appointmentService.findById(id).orElse(null);
        appointmentService.saveResults(appointment, resultsKeeper.getResults());
        return "redirect:/vinitro/analyses/processing";
    }
}
