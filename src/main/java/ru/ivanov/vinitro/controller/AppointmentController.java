package ru.ivanov.vinitro.controller;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.vinitro.dto.VinitroUserDetails;
import ru.ivanov.vinitro.model.AppointmentForAnalysis;
import ru.ivanov.vinitro.model.User;
import ru.ivanov.vinitro.repository.AnalysisRepository;
import ru.ivanov.vinitro.service.AnalysisService;
import ru.ivanov.vinitro.service.AppointmentService;
import ru.ivanov.vinitro.service.UserService;
import ru.ivanov.vinitro.util.AppointmentValidator;
import ru.ivanov.vinitro.util.ResultsKeeper;
import ru.ivanov.vinitro.util.TagKeeper;
import ru.ivanov.vinitro.util.TagValidator;

@Controller
@RequestMapping("/vinitro/analyses")
public class AppointmentController {

    private final AnalysisService analysisService;
    private final AppointmentService appointmentService;
    private final UserService userService;
    private final AppointmentValidator appointmentValidator;
    private final TagValidator tagValidator;
    private final AnalysisRepository analysisRepository;

    @Autowired
    public AppointmentController(AnalysisService analysisService, AppointmentService appointmentService, UserService userService, AppointmentValidator appointmentValidator, TagValidator tagValidator, AnalysisRepository analysisRepository) {
        this.analysisService = analysisService;
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.appointmentValidator = appointmentValidator;
        this.tagValidator = tagValidator;
        this.analysisRepository = analysisRepository;
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
                                     Model model,
                                     @ModelAttribute("new_appointment") AppointmentForAnalysis appointment,
                                     Authentication authentication,
                                     BindingResult bindingResult){

        // сет для валидации, тк в записи ток дата и время, а нам нужно сверить анализы
        appointment.setAnalysis(analysisRepository.findById(id).orElse(null));
        appointmentValidator.validate(appointment, bindingResult);
        if (bindingResult.hasErrors()){
            model.addAttribute("analysis", analysisService.findById(id).orElse(null));
            System.out.println("errors in date in appointForAnalysis");
            return "analysis_appointment_page";
        }

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
                                             @ModelAttribute("tagKeeper") TagKeeper tagKeeper,
                                             Model model,
                                             BindingResult bindingResult){
        tagValidator.validate(tagKeeper, bindingResult);
        if (bindingResult.hasErrors()){
            System.out.println("errors in tag in confirmAnalysisAppointment");
            model.addAttribute("appointments", appointmentService.getAllWaitingAnalyses());
            return "nurse";
        }

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
    public String personalConfirmAnalysisAppointment(@ModelAttribute("appointment") AppointmentForAnalysis appointment,
                                                     BindingResult bindingResult1,
                                                     BindingResult bindingResult2,
                                                     Model model){
        appointmentValidator.validate(appointment, bindingResult1);
        tagValidator.validate(new TagKeeper(appointment.getTag()), bindingResult2);
        if (bindingResult1.hasErrors() || bindingResult2.hasErrors()){
            System.out.println("errors in date/tag in personalConfirmAnalysisAppointment");
            model.addAttribute("users", userService.findAllUsers());
            model.addAttribute("analyses", analysisService.findAllAnalyses());
            return "appointment_confirming_page";
        }

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
