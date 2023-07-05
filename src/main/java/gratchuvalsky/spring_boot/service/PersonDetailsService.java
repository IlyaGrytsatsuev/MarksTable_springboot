package gratchuvalsky.spring_boot.service;


import gratchuvalsky.spring_boot.DAO.MarksTablesDao;
import gratchuvalsky.spring_boot.Model.Person;
import gratchuvalsky.spring_boot.repositiory.PersonRepository;
import gratchuvalsky.spring_boot.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;
    private final MarksTablesDao dao;

    @Autowired
    PersonDetailsService(PersonRepository personRepository, MarksTablesDao dao){
        this.personRepository = personRepository;
        this.dao = dao;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByLogin(username);

        if(person.isEmpty())
            throw new UsernameNotFoundException("No such user!");

        return new PersonDetails(person.get());
    }
    @Transactional
    public void deletePerson(int id){
        personRepository.deleteById(id);
    }
    public String getStudentLoginAndPassword(int accountId){
        Optional<Person> rep = personRepository.findById(accountId);
        Person person = rep.get();
        return "Логин: " + person.getLogin()
                + " Пароль: " + person.getPassword();
    }
}
