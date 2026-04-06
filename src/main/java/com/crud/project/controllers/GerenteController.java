package com.crud.project.controllers;

import com.crud.project.models.Gerente;
import com.crud.project.repositories.GerenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gerentes")
public class GerenteController {

    @Autowired
    private GerenteRepository gerenteRepository;

    @GetMapping
    public ResponseEntity<List<Gerente>> listAll() {
        return ResponseEntity.ok(gerenteRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Gerente> create(@RequestBody Gerente gerente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gerenteRepository.save(gerente));
    }
}
