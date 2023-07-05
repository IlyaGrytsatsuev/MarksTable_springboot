package gratchuvalsky.spring_boot.Controller;

import gratchuvalsky.spring_boot.Model.Person;
import gratchuvalsky.spring_boot.service.RegistrationService;
import gratchuvalsky.spring_boot.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final RegistrationService registrationService;
    private final PersonValidator validator;

    @Autowired
    public AuthController(RegistrationService registrationService, PersonValidator validator) {
        this.registrationService = registrationService;
        this.validator = validator;
    }
    @GetMapping("/auth")
    public String getLoginPage(){
        return "loginPage";
    }

//    @GetMapping("/signUp")
//    public String getSignUpPage(@ModelAttribute("person") Person person){
//        return "signUpForm";
//    }
//    @PostMapping("/signUp")
//    public String doSignUp(@ModelAttribute("person") @Valid Person person,
//                           BindingResult bindingResult){
//        validator.validate(person, bindingResult);
//
//        if(bindingResult.hasErrors())
//            return "signUpForm";
//
//        registrationService.register(person);
//
//        return "redirect:/auth";
//    }
}
