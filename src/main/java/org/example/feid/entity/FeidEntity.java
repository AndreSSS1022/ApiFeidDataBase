package org.example.feid.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FEID_ENTITY")
public class FeidEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonProperty("id")
    private UUID id;

    @Column(name = "nombre_cancion")
    @JsonProperty("nombreCancion")
    @NotBlank(message = "El nombre de la canción es obligatorio")
    @Size(min = 1, max = 100, message = "El nombre de la canción debe tener entre 1 y 100 caracteres")
    private String nombreCancion;

    @Column(name = "duracion")
    @JsonProperty("duracion")
    @NotBlank(message = "La duración es obligatoria")
    @Pattern(regexp = "^\\d{1,2}:\\d{2}$", message = "La duración debe tener formato MM:SS")
    private String duracion;

    @Column(name = "artista")
    @JsonProperty("artista")
    @NotBlank(message = "El nombre del artista es obligatorio")
    @Size(min = 1, max = 100, message = "El nombre del artista debe tener entre 1 y 100 caracteres")
    private String artista;

    @PrePersist
    public void generateUUID() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @Override
    public String toString() {
        return "FeidEntity{" +
                "id=" + id +
                ", nombreCancion='" + nombreCancion + '\'' +
                ", duracion='" + duracion + '\'' +
                ", artista='" + artista + '\'' +
                '}';
    }
}