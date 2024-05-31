package it.epicode.gestione_eventi.service;

import it.epicode.gestione_eventi.dto.EventDto;
import it.epicode.gestione_eventi.exception.BadRequestException;
import it.epicode.gestione_eventi.exception.NotFoundException;
import it.epicode.gestione_eventi.model.Event;
import it.epicode.gestione_eventi.model.User;
import it.epicode.gestione_eventi.repository.EventRepository;
import it.epicode.gestione_eventi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

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
        } else {
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

    //PARTECIPARE A UN EVENTO
    public String participateInEvent(int eventId, int userId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (eventOptional.isPresent() && userOptional.isPresent()) {
            Event event = eventOptional.get();
            User user = userOptional.get();

            if (event.getUsers().size() < event.getMaxPartecipants()) {
                event.getUsers().add(user);
                user.getEvents().add(event);
                eventRepository.save(event);
                userRepository.save(user);
                return "User " + user.getFirstName() + " " + user.getLastName() + " successfully registered for the event " + event.getTitle() + "(id: " + event.getId() + ").";
            } else {
                throw new BadRequestException("The event is already at maximum capacity.");
            }
        } else {
            throw new NotFoundException("Event or User not found.");
        }
    }

    //VISUALIZZARE EVENTI A CUI PARTECIPA UN UTENTE
    public List<Event> getEventsByUserId(int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get().getEvents();
        } else {
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }

    //CANCELLARE UNA PARTECIPAZIONE A EVENTO
    public String deleteEventFromUserEvents(int eventId, int userId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (eventOptional.isPresent() && userOptional.isPresent()) {
            Event event = eventOptional.get();
            User user = userOptional.get();
            if (event.getUsers().stream().anyMatch(u->u.getId()==userId)) {
                event.getUsers().remove(user);
            }else {
                throw new BadRequestException("User with id: " + userId + " is not in participation list");
            }
            user.getEvents().remove(event);
            eventRepository.save(event);
            userRepository.save(user);
            return "Participation in event " + event.getTitle() + "(id: " + event.getId() + ") of user " + user.getFirstName() + " " + user.getLastName() + " successfully cancelled.";
        } else {
            throw new NotFoundException("Event or User not found.");
        }

    }
}



