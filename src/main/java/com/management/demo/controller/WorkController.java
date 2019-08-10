package com.management.demo.controller;

import com.management.demo.entity.WorkEntity;
import com.management.demo.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class WorkController {

    @Autowired
    private final WorkService workService;

    public WorkController(WorkService workService) {
        this.workService = workService;
    }

    @GetMapping("/workAddView")
    public ModelAndView workAdd() {
        ModelAndView modelAndView = new ModelAndView();
        WorkEntity workEntity = new WorkEntity();
        modelAndView.addObject("workEntity", workEntity);
        return modelAndView;
    }

    @PostMapping("/workAdd")
    public ModelAndView createNewWork(@ModelAttribute("workEntity")
                                      @Valid WorkEntity workEntity,
                                      BindingResult bindingResult) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Optional<WorkEntity> workExists = workService.getById(workEntity.getId());
        if (workExists.isPresent()) {
            bindingResult
                    .rejectValue("title", "error.workEntity",
                            "This title already exist");
        } else {
            if (!bindingResult.hasErrors()) {
                modelAndView.addObject("title", workEntity.getTitle());
                modelAndView.addObject("successMessage", "Successfully create work");
                modelAndView.addObject("workEntity", new WorkEntity());
                modelAndView.setViewName("successfullyCreateWork");
            }
        }
        return modelAndView;
    }

    @DeleteMapping("/deleteWork{id}")
    public ModelAndView removeWorkById(@PathVariable("id") int id) {
        workService.deleteById(id);
        return new ModelAndView("redirect:/workAddView");
    }

}
