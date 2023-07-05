package gratchuvalsky.spring_boot.util;

import gratchuvalsky.spring_boot.Model.Person;
import gratchuvalsky.spring_boot.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final RegistrationService registrationService;

    @Autowired
    public PersonValidator(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> res
                = registrationService.loadUserByNameForSignUp(person);

        if(res.isEmpty()){
            System.out.println("empty");
            return;
        }
        System.out.println("exists");
        errors.rejectValue("login","",
                    "Account with this login already exists!!");

    }
}
