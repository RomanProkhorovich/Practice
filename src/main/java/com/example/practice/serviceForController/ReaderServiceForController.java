package com.example.practice.serviceForController;

import com.example.practice.exception.UserAlreadyExistException;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.Reader;
import com.example.practice.service.ReaderService;
import com.example.practice.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReaderServiceForController {

    private final ReaderService readerService;

    public ReaderServiceForController(ReaderService readerService) {
        this.readerService = readerService;
    }

    public Reader save(Reader reader, HttpServletRequest req) {
        AuthUtil.checkAdminRole(req);
        return readerService.save(reader);
    }

    public Reader findByEmail(String email, HttpServletRequest req) {
        AuthUtil.checkAdminRole(req);
        return readerService.findByEmail(email);
    }

    public Optional<Reader> findById(Long id) {
        return readerService.findById(id);
    }

    public List<Reader> findAll(HttpServletRequest req) {
        AuthUtil.checkAdminRole(req);
        return readerService.findAll();
    }

    public List<Reader> findAllActive(HttpServletRequest req) {
        AuthUtil.checkAdminRole(req);
        return readerService.findAllActive();
    }

    public Reader update(Reader reader, HttpServletRequest req) {
        AuthUtil.checkAdminRole(req);
        return readerService.update(reader);
    }

    public void deleteById(Long id,HttpServletRequest req){
        AuthUtil.checkAdminRole(req);
        readerService.deleteById(id);
    }



    public void deleteByEmail(String email, HttpServletRequest req) {
        AuthUtil.checkAdminRole(req);
        readerService.deleteByEmail(email);
    }

}
