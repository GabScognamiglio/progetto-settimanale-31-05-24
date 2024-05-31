package it.epicode.gestione_eventi.controller;

import it.epicode.gestione_eventi.dto.UserDto;
import it.epicode.gestione_eventi.exception.BadRequestException;
import it.epicode.gestione_eventi.exception.NotFoundException;
import it.epicode.gestione_eventi.model.User;
import it.epicode.gestione_eventi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

    //***Solo gli ADMIN possono manipolare gli utenti***

    @Autowired
    private UserService userService;

    @GetMapping("api/event-manager/users")
    @PreAuthorize("hasAuthority('ADMIN')") //solo chi è ADMIN è autorizzato
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sortBy) {
        return userService.getUsers(page, size, sortBy);
    }

    @GetMapping("api/event-manager/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserById(@PathVariable int id) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new NotFoundException("User with id " + id + " not found");
        }
    }

    @PutMapping("/api/event-manager/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable int id, @RequestBody @Validated UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s + " - " + s2));
            // " - " (riga sopra) serve per separare gli errori con un trattino tra uno e l'altro
        }
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/api/event-manager/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

}
