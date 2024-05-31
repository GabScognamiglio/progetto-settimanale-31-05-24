package it.epicode.gestione_eventi.controller;

import it.epicode.gestione_eventi.dto.EventDto;
import it.epicode.gestione_eventi.exception.BadRequestException;
import it.epicode.gestione_eventi.exception.NotFoundException;
import it.epicode.gestione_eventi.model.Event;
import it.epicode.gestione_eventi.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping("api/event-manager/events")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')") //solo gli organizzatori possono creare eventi
    public String saveEvent(@RequestBody @Validated EventDto eventDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
        }
        return eventService.saveEvent(eventDto);
    }

    @GetMapping("api/event-manager/events")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')") //chiunque è loggato può vedere gli eventi
    public Page<Event> getEvents(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "id") String sortBy) {
        return eventService.getEvents(page, size, sortBy);
    }

    @GetMapping("api/event-manager/events/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Event getEventById(@PathVariable int id) {
        Optional<Event> eventOptional = eventService.getEventById(id);
        if (eventOptional.isPresent()) {
            return eventOptional.get();
        } else {
            throw new NotFoundException("Event with id " + id + " not found.");
        }
    }

    @PutMapping("/api/event-manager/events/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")//solo gli organizzatori possono modificare eventi
    public Event updateEvent (@PathVariable int id, @RequestBody @Validated EventDto eventDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
        }
        return eventService.updateEvent(id, eventDto);
    }

    @DeleteMapping("/api/event-manager/events/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")//solo gli organizzatori possono cancellare eventi
    public String deleteEvent(@PathVariable int id){
        return eventService.deleteEvent(id);
    }

    //PARTECIPARE A UN EVENTO
    @PatchMapping("/api/{eventId}/participate/{userId}")
    @PreAuthorize("hasAuthority('USER')")
    public String participateInEvent(@PathVariable int eventId, @PathVariable int userId) {
        return eventService.participateInEvent(eventId, userId);
    }

    //VISUALIZZARE EVENTI A CUI PARTECIPA UN UTENTE
    @GetMapping("/api/users/{userId}/events")
    @PreAuthorize("hasAuthority('USER')")
    public List<Event> getEventsByUserId(@PathVariable int userId) {
        return eventService.getEventsByUserId(userId);
    }

    @PatchMapping("/api/{eventId}/cancel/{userId}")
    @PreAuthorize("hasAuthority('USER')")
    public String deletePartecipation(@PathVariable int eventId, @PathVariable int userId) {
        return eventService.deleteEventFromUserEvents(eventId, userId);
    }




}











