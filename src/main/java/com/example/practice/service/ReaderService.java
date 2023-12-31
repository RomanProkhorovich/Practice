package com.example.practice.service;

import com.example.practice.exception.DeletedUserException;
import com.example.practice.exception.UserAlreadyExistException;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.Reader;
import com.example.practice.repository.ReaderRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReaderService implements UserDetailsService {

    private final ReaderRepository readerRepository;

    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }


    public Reader save(Reader reader) {
        if (findByEmail(reader.getEmail()).isPresent()) {
            throw new UserAlreadyExistException(
                    String.format("User with email: '%s' already exist", reader.getEmail()));
        }
        return readerRepository.save(reader);
    }
    public Optional<Reader> findByEmail(String email) {
        return readerRepository.findByEmail(email);
    }
    public Optional<Reader> findById(Long id) {
        return readerRepository.findById(id);
    }
    public List<Reader> findAll(){
        return readerRepository.findAll();
    }
    public List<Reader> findAllActive(){
        return readerRepository.findAllByIsActive(true);
    }

    public Reader update(Reader reader){
        var updated =findById(reader.getId())
                .orElseThrow(()-> new UserNotFoundException(String.format("User with id: '%d' not found", reader.getId())));

        if (!reader.getEmail().isBlank()){
            updated.setEmail(reader.getEmail());
        }

        if (!reader.getFirstname().isBlank()){
            updated.setFirstname(reader.getFirstname());
        }

        if (!reader.getLastname().isBlank()){
            updated.setLastname(reader.getLastname());
        }
        if (reader.getSurname()!=null && !reader.getSurname().isBlank()){
            updated.setSurname(reader.getSurname());
        }

        if (!reader.getPassword().isBlank()){
            updated.setSurname(reader.getSurname());
        }
        return readerRepository.save(updated);
    }



    public void deleteById(Long id){
        findById(id).orElseThrow(
                ()-> new UserNotFoundException(String.format("User with id: '%d' not found", id))
        ) .setIsActive(false);
    }
    public void deleteByEmail(String email){
        findByEmail(email).orElseThrow(
                ()-> new UserNotFoundException(String.format("User with email: '%s' not found", email))
        ) .setIsActive(false);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var reader=findByEmail(email).orElseThrow(()-> new UsernameNotFoundException(
                String.format("User with email: '%s' not found", email)));
        if (!reader.getIsActive()){
            throw new DeletedUserException(String.format("user with email %s was deleted", email));
        }

        return new User(email,
                reader.getPassword(),
                Set.of(new SimpleGrantedAuthority(reader.getRole().name())));
    }
}
