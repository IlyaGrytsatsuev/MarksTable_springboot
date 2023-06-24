package gratchuvalsky.spring_boot.service;


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

    PersonRepository personRepository;

    @Autowired
    PersonDetailsService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByLogin(username);
        if(person.isEmpty())
            throw new UsernameNotFoundException("No such user!");

        return new PersonDetails(person.get());
    }
}
