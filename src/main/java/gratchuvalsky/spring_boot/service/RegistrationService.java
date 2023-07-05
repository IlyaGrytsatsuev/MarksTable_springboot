package gratchuvalsky.spring_boot.service;

import gratchuvalsky.spring_boot.DAO.MarksTablesDao;
import gratchuvalsky.spring_boot.Model.Person;
import gratchuvalsky.spring_boot.repositiory.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Transactional(readOnly = true)
@Service
public class RegistrationService {
    private final PasswordEncoder encoder;
    private final PersonRepository personRepository;
    private final MarksTablesDao dao;

    @Autowired
    public RegistrationService(PasswordEncoder encoder, PersonRepository personRepository, MarksTablesDao dao) {
        this.encoder = encoder;
        this.personRepository = personRepository;
        this.dao = dao;
    }


    public Optional<Person>loadUserByNameForSignUp(Person person){
        return personRepository.findByLogin(person.getLogin());
    }

    @Transactional
    public int register(){
       // String encodedPassword = encoder.encode(person.getPassword());
        Person person = new Person();
        Random random = new Random();
        Optional<Person> opt;
        String login = "st" + random.nextInt(0, 1000000);
        opt = personRepository.findByLogin(login);
        while(opt.isPresent()) {
            login = "st" + random.nextInt(0, 1000000);
            opt =personRepository.findByLogin(login);
        }
        String password = String.valueOf(random.nextInt(0,1000000));
        person.setLogin(login);
        person.setPassword(password);
        person.setRole("ROLE_USER");
        personRepository.save(person);
        return person.getId();
    }
}
