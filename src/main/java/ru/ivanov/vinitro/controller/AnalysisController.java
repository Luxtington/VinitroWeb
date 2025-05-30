package ru.ivanov.vinitro.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.vinitro.dto.VinitroUserDetails;
import ru.ivanov.vinitro.model.Analysis;
import ru.ivanov.vinitro.model.BooleanKeeper;
import ru.ivanov.vinitro.repository.AnalysisRepository;
import ru.ivanov.vinitro.service.AnalysisService;
import ru.ivanov.vinitro.util.AnalysisValidator;

import java.security.Principal;

@Controller
@RequestMapping("/vinitro/analyses")
public class AnalysisController {

    private final AnalysisService analysisService;
    private final AnalysisValidator analysisValidator;

    @Autowired
    public AnalysisController(AnalysisService analysisService, AnalysisValidator analysisValidator) {
        this.analysisService = analysisService;
        this.analysisValidator = analysisValidator;
    }

    @GetMapping()
    public String showAllAnalyses(Model model){
        model.addAttribute("all_analyses", analysisService.findAllAnalyses());
        model.addAttribute("analyses_quantity", analysisService.findAllAnalyses().size());
        return "all_analyses";
    }

    @GetMapping("/{id}")
    public String showConcreteAnalysis(@PathVariable("id") String id, Model model, Authentication authentication){
        Analysis analysis = analysisService.findById(id).orElse(new Analysis());
        model.addAttribute("analysis", analysis);
        VinitroUserDetails userDetails = (VinitroUserDetails) authentication.getPrincipal();
        model.addAttribute("analysis_status", new BooleanKeeper(analysisService.checkUserAppointmentForAnalysis(analysis, userDetails.getId())));
        return "concrete_analysis";
    }

    @GetMapping("/create")
    public String showCreateAnalysisPage(@ModelAttribute("analysis_to_create") Analysis analysis){
        return "analysis_create_form";
    }

    @PostMapping("/create")
    public String createAnalysis(@ModelAttribute("analysis_to_create") @Valid Analysis analysis,
                             BindingResult bindingResult){

        analysisValidator.validate(analysis, bindingResult);
        if (bindingResult.hasErrors()){
            return "analysis_create_form";
        }
        analysisService.save(analysis);
        return "redirect:/vinitro/analyses";
    }


    @GetMapping("/edit/{id}")
    public String showEditAnalysisPage(@PathVariable("id") String id, Model model){
        model.addAttribute("analysis_to_edit", analysisService.findById(id).orElse(null));
        return "analysis_update_form";
    }

    @PatchMapping("/edit/{id}")
    public String editAnalysis(@PathVariable("id") String id, @ModelAttribute("analysis_to_edit")  @Valid Analysis analysis){
        analysisService.save(id, analysis);
        return "redirect:/vinitro/analyses";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteAnalysis(@PathVariable("id") String id){
        analysisService.deleteById(id);
        return "redirect:/vinitro/analyses";
    }

    //в пост запросе отправим айди анализа на контроллер юзера
}
