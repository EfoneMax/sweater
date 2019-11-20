package com.example.sweater.controller;

import com.example.sweater.domain.User;
import com.example.sweater.domain.dto.CaptchaResponceDTO;
import com.example.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private UserService userService;
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";


    @Value("${recaptcha.secret")
    private String secret;

    private final RestTemplate restTemplate;

    public RegistrationController(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("message") String message ) {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
                          @RequestParam("password2") String passwordConfirm,
                          @RequestParam("g-recaptcha-responce") String captchaResponce,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model
    ) {
        String url = String.format(CAPTCHA_URL, secret, captchaResponce);
        CaptchaResponceDTO responce = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponceDTO.class);
        if (!responce.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
        }

        boolean confirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if (confirmEmpty) {
            model.addAttribute("password2Error", "Password confirmation can not be empty");
        }
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Passwords are different!");
        }
        if (confirmEmpty || bindingResult.hasErrors() || !responce.isSuccess()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);

            return "registration";
        }

        if (userService.addUser(user)) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found");
        }

        return "login";
    }

}
