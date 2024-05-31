package it.epicode.gestione_eventi.service;

import it.epicode.gestione_eventi.dto.EventDto;
import it.epicode.gestione_eventi.exception.NotFoundException;
import it.epicode.gestione_eventi.model.Event;
import it.epicode.gestione_eventi.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public String saveEvent(EventDto eventDto) {
        Event event = new Event();
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setLocation(eventDto.getLocation());
        event.setDate(eventDto.getDate());
        event.setMaxPartecipants(eventDto.getMaxPartecipants());

        eventRepository.save(event);
        return "Event with id: " + event.getId() + " correctly created";
    }

    public Page<Event> getEvents(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return eventRepository.findAll(pageable);
    }

    public Optional<Event> getEventById(int id) {
        return eventRepository.findById(id);
    }

    public Event updateEvent(int id, EventDto eventDto) {
        Optional<Event> eventOptional = getEventById(id);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            event.setTitle(eventDto.getTitle());
            event.setDescription(eventDto.getDescription());
            event.setLocation(eventDto.getLocation());
            event.setDate(eventDto.getDate());
            event.setMaxPartecipants(eventDto.getMaxPartecipants());
            eventRepository.save(event);
            return event;
        }
        else {
            throw new NotFoundException("Event with id " + id + " not found.");
        }
    }

    public String deleteEvent(int id) {
        Optional<Event> eventOptional = getEventById(id);

        if (eventOptional.isPresent()) {
            eventRepository.delete(eventOptional.get());
            return "Event with id" + id + " correctly deleted.";
        } else {
            throw new NotFoundException("Event with id " + id + " not found.");
        }
    }











}


