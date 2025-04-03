package org.example.feid.repository;

import org.example.feid.entity.FeidEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeidRepository extends JpaRepository<FeidEntity, UUID> {

    // Buscar canciones por nombre (contiene)
    Page<FeidEntity> findAllByNombreCancionContainingIgnoreCase(String nombreCancion, Pageable pageable);

    // Buscar canciones por artista (contiene)
    Page<FeidEntity> findAllByArtistaContainingIgnoreCase(String artista, Pageable pageable);

    // Implementación de la paginación con findAll
    @Override
    Page<FeidEntity> findAll(Pageable pageable);
}