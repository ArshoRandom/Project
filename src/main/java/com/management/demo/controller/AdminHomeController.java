package com.management.demo.controller;


import com.management.demo.entity.UserEntity;
import com.management.demo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class AdminHomeController {

    private final UserService userService;

    public AdminHomeController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/home")
    public ModelAndView adminHome() {
        ModelAndView modelAndView = new ModelAndView("home/admin");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userService.getByEmail(authentication.getName());
        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            String imageUrl = userEntity.getImageUrl().substring(0, userEntity.getImageUrl().length() - userEntity.getEmail().length());
            modelAndView.addObject("imageUrl",imageUrl);
            modelAndView.addObject("userName", String.format("Welcome %s %s !!!", userEntity.getName(), userEntity.getLastName()));
            modelAndView.addObject("adminMessage", "Content Available Only for Admins");
        }

        return modelAndView;
    }

}
