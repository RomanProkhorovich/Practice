package com.example.practice.service;


import com.example.practice.Dto.AuthDto;
import com.example.practice.Dto.RegDto;
import com.example.practice.exception.DeletedUserException;
import com.example.practice.exception.UserAlreadyExistException;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.mapper.ReaderMapper;
import com.example.practice.model.Reader;
import com.example.practice.repository.ReaderRepository;
import com.example.practice.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReaderService  {

    private final ReaderRepository readerRepository;

    private final ReaderMapper readerMapper;
    public ReaderService(ReaderRepository readerRepository, ReaderMapper readerMapper) {
        this.readerRepository = readerRepository;
        this.readerMapper = readerMapper;
    }


    public Reader save(Reader reader,HttpServletRequest req) {

        if (findByEmail(reader.getEmail(),req)!=null) {
            throw new UserAlreadyExistException(
                    String.format("User with email: '%s' already exist", reader.getEmail()));
        }
        return readerRepository.save(reader);
    }

    public Reader findByEmail(String email,HttpServletRequest req) {
        AuthUtil.checkAdminRole(req);
        var a=readerRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException());
        if (!a.getIsActive()){
            throw new UserNotFoundException(String.format("User with email %s not found", email));
        }
        return a ;
    }
    public Optional<Reader> findById(Long id) {
        return readerRepository.findById(id);
    }
    public List<Reader> findAll(HttpServletRequest req){
        AuthUtil.checkAdminRole(req);
        return readerRepository.findAll();
    }
    public List<Reader> findAllActive(HttpServletRequest req){
        AuthUtil.checkAdminRole(req);
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
    public void deleteByEmail(String email,HttpServletRequest req){
        findByEmail(email,req).setIsActive(false);
    }


}
