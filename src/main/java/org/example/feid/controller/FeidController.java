package org.example.feid.controller;

import org.example.feid.entity.FeidEntity;
import org.example.feid.service.FeidService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/feid")
@Validated
public class FeidController {

    private final FeidService feidService;

    public FeidController(FeidService feidService) {
        this.feidService = feidService;
    }

    @GetMapping
    public ResponseEntity<?> getAllSongs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "nombreCancion,asc") String[] sort) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
            return feidService.getAllSongs(pageable);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Dirección de ordenamiento inválida. Use 'asc' o 'desc'.");
        }
    }

    private Sort.Order parseSort(String[] sort) {
        if (sort.length < 2) {
            throw new IllegalArgumentException("El parámetro de ordenamiento debe tener campo y dirección (ej., 'nombreCancion,desc').");
        }

        String property = sort[0];
        String direction = sort[1].toLowerCase();

        List<String> validDirections = Arrays.asList("asc", "desc");
        if (!validDirections.contains(direction)) {
            throw new IllegalArgumentException("Dirección de ordenamiento inválida. Use 'asc' o 'desc'.");
        }

        return new Sort.Order(Sort.Direction.fromString(direction), property);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable UUID id) {
        return feidService.getSongById(id);
    }

    @GetMapping("/search/nombre")
    public ResponseEntity<?> getSongsByName(
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "nombreCancion,asc") String[] sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
        return feidService.getSongsByName(nombre, pageable);
    }

    @GetMapping("/search/artista")
    public ResponseEntity<?> getSongsByArtist(
            @RequestParam String artista,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "nombreCancion,asc") String[] sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
        return feidService.getSongsByArtist(artista, pageable);
    }

    @PostMapping
    public ResponseEntity<?> addSong(@Valid @RequestBody FeidEntity feidEntity) {
        return feidService.addSong(feidEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSong(@PathVariable UUID id, @Valid @RequestBody FeidEntity feidEntity) {
        return feidService.updateSong(id, feidEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable UUID id) {
        return feidService.deleteSong(id);
    }
}