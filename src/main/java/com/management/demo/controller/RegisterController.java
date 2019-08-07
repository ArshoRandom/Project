package com.management.demo.controller;

import com.management.demo.entity.ConfirmationTokenEntity;
import com.management.demo.entity.UserEntity;
import com.management.demo.repository.ConfirmationTokenRepo;
import com.management.demo.service.MailSenderService;
import com.management.demo.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;


@Controller
public class RegisterController {

    private final UserService userService;
    private final ConfirmationTokenRepo confirmationTokenRepo;
    private final MailSenderService mailSenderService;

    @Value("${spring.confirmation.link}")
    private String confirmationLink;


    public RegisterController(UserService userService, ConfirmationTokenRepo confirmationTokenRepo, MailSenderService mailSenderService) {
        this.userService = userService;
        this.confirmationTokenRepo = confirmationTokenRepo;
        this.mailSenderService = mailSenderService;
    }

    @GetMapping("/register")
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView("register");
        UserEntity user = new UserEntity();
        modelAndView.addObject("userEntity", user);
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView createNewUser(@ModelAttribute("userEntity")
                                      @Valid UserEntity user,
                                      @RequestParam("file") MultipartFile file,
                                      BindingResult bindingResult) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Optional<UserEntity> userExists = userService.getByEmail(user.getEmail());
        if (userExists.isPresent()) {
            bindingResult
                    .rejectValue("email", "error.userEntity",
                            "This email already exist");
        } else {
            if (!bindingResult.hasErrors()) {
                userService.add(user, "USER",file);
                ConfirmationTokenEntity confirmationTokenEntity = new ConfirmationTokenEntity(user);
                confirmationTokenRepo.save(confirmationTokenEntity);

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Complete Registration!");
                mailMessage.setFrom("papoyanarshak@gmail.com");
                mailMessage.setText(confirmationLink + confirmationTokenEntity.getConfirmationToken());

                this.mailSenderService.sendEmail(mailMessage);

                modelAndView.addObject("email", user.getEmail());
                modelAndView.addObject("successMessage", "Successfully register");
                modelAndView.addObject("userEntity", new UserEntity());
                modelAndView.setViewName("successfullyRegister");
            }
        }
        return modelAndView;
    }

    @GetMapping(value = "/confirm-account")
    public ModelAndView confirmUser(@RequestParam("token") String token) {
        ModelAndView modelAndView = new ModelAndView();
        ConfirmationTokenEntity confirmationToken = confirmationTokenRepo.findByConfirmationToken(token);
        if (token != null) {
            userService.setActivated(true,confirmationToken.getUserEntity().getEmail());
            modelAndView.setViewName("accountVerified");

        } else {
            modelAndView.addObject("errorMessage", "The link is invalid or broken!");
            modelAndView.setViewName("error");
        }
            return modelAndView;
    }


}
