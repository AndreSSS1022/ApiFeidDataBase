package org.example.feid.service;

import org.example.feid.entity.FeidEntity;
import org.example.feid.repository.FeidRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FeidService {

    private final FeidRepository feidRepository;

    public FeidService(FeidRepository feidRepository) {
        this.feidRepository = feidRepository;
    }

    public ResponseEntity<?> getAllSongs(Pageable pageable) {
        Page<FeidEntity> songs = feidRepository.findAll(pageable);
        return getResponseEntity(songs);
    }

    public ResponseEntity<?> getSongById(UUID id) {
        Optional<FeidEntity> song = feidRepository.findById(id);
        if (song.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("Status", String.format("Canción con ID %s no encontrada.", id));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(Collections.singletonMap("Cancion", song.get()));
    }

    public ResponseEntity<?> getSongsByName(String songName, Pageable pageable) {
        Page<FeidEntity> songs = feidRepository.findAllByNombreCancionContainingIgnoreCase(songName, pageable);
        return getResponseEntity(songs);
    }

    public ResponseEntity<?> getSongsByArtist(String artist, Pageable pageable) {
        Page<FeidEntity> songs = feidRepository.findAllByArtistaContainingIgnoreCase(artist, pageable);
        return getResponseEntity(songs);
    }

    private ResponseEntity<?> getResponseEntity(Page<FeidEntity> songs) {
        Map<String, Object> response = new HashMap<>();
        response.put("TotalElementos", songs.getTotalElements());
        response.put("TotalPaginass", songs.getTotalPages());
        response.put("Paginas", songs.getNumber());
        response.put("NumeroDeElementos", songs.getNumberOfElements());
        response.put("Canciones", songs.getContent());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> addSong(FeidEntity songToAdd) {
        Page<FeidEntity> songs = feidRepository.findAllByNombreCancionContainingIgnoreCase(
                songToAdd.getNombreCancion(),
                Pageable.unpaged());

        if (songs.getTotalElements() > 0) {
            return new ResponseEntity<>(
                    Collections.singletonMap("Status",
                            String.format("La canción ya existe con %d coincidencias.", songs.getTotalElements())),
                    HttpStatus.CONFLICT);
        } else {
            FeidEntity savedSong = feidRepository.save(songToAdd);
            return new ResponseEntity<>(
                    Collections.singletonMap("Status",
                            String.format("Canción añadida con ID %s", savedSong.getId())),
                    HttpStatus.CREATED);
        }
    }

    public ResponseEntity<?> updateSong(UUID id, FeidEntity songToUpdate) {
        Optional<FeidEntity> song = feidRepository.findById(id);
        if (song.isEmpty()) {
            return new ResponseEntity<>(
                    Collections.singletonMap("Status",
                            String.format("Canción con ID %s no encontrada.", id)),
                    HttpStatus.NOT_FOUND);
        }

        FeidEntity existingSong = song.get();
        existingSong.setNombreCancion(songToUpdate.getNombreCancion());
        existingSong.setDuracion(songToUpdate.getDuracion());
        existingSong.setArtista(songToUpdate.getArtista());

        feidRepository.save(existingSong);

        return ResponseEntity.ok(
                Collections.singletonMap("Status",
                        String.format("Canción actualizada con ID %s", existingSong.getId())));
    }

    public ResponseEntity<?> deleteSong(UUID id) {
        Optional<FeidEntity> song = feidRepository.findById(id);
        if (song.isEmpty()) {
            return new ResponseEntity<>(
                    Collections.singletonMap("Status",
                            String.format("La canción con ID %s no existe.", id)),
                    HttpStatus.NOT_FOUND);
        }

        feidRepository.deleteById(id);
        return ResponseEntity.ok(
                Collections.singletonMap("Status",
                        String.format("Canción eliminada con ID %s", id)));
    }
}