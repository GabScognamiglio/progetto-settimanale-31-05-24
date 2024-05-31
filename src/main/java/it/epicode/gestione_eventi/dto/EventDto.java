package it.epicode.gestione_eventi.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @Future(message = "The event date must be in the future")
    private LocalDate date;
    @NotBlank
    private String location;
    @NotBlank
    @Min(value = 2, message = "Partecipants must be at least 2")
    private int maxPartecipants;
}
