package gratchuvalsky.spring_boot.Controller;

import gratchuvalsky.spring_boot.DAO.MarksTablesDao;
import gratchuvalsky.spring_boot.Model.Person;
import gratchuvalsky.spring_boot.security.PersonDetails;
import gratchuvalsky.spring_boot.service.PersonDetailsService;
import gratchuvalsky.spring_boot.service.RegistrationService;
import gratchuvalsky.spring_boot.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.List;

@Controller
public class AuthController {

    private final RegistrationService registrationService;
    private final PersonValidator validator;
    private final PersonDetailsService personDetailsService;
    private final MarksTablesDao dao;

    @Autowired
    public AuthController(RegistrationService registrationService, PersonValidator validator, PersonDetailsService personDetailsService, MarksTablesDao dao) {
        this.registrationService = registrationService;
        this.validator = validator;
        this.personDetailsService = personDetailsService;
        this.dao = dao;
    }
    @GetMapping("/auth")
    public String getLoginPage(){
        return "loginPage";
    }

    @GetMapping("/login_success")
    public String loginResult(){
        Collection<? extends GrantedAuthority> authorities;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        authorities = auth.getAuthorities();
        String myRole = authorities.toArray()[0].toString();
        String admin = "ROLE_ADMIN";
        if (myRole.equals(admin)) {
            System.out.println("admin");
            return "redirect:/Forms";
        }
        PersonDetails personDetails = (PersonDetails) SecurityContextHolder
                .getContext()
                .getAuthentication().getPrincipal();
        Person person = personDetails.getPerson();
        int authId = person.getId();
        List<Integer> ids = dao.getStudentIdAndFormIdByAuthId(authId);

        return "redirect:/SubMarks/form=" +
                ids.get(1) + ":student=" + ids.get(0);
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
