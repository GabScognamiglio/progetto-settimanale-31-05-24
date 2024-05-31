package it.epicode.gestione_eventi.service;

import it.epicode.gestione_eventi.dto.UserDto;
import it.epicode.gestione_eventi.exception.NotFoundException;
import it.epicode.gestione_eventi.model.User;
import it.epicode.gestione_eventi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String saveUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(user);
        return "User with id " + user.getId() + " correctly saved";
    }

    public Page<User> getUsers(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public User updateUser (int id, UserDto userDto) {
        Optional<User> userOptional = getUserById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            userRepository.save(user);
            return user;
        }
        else {
            throw new NotFoundException("User with id " + id + " not found");
        }
    }

   public String deleteUser(int id) {
        Optional<User> userOptional = getUserById(id);

        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            return "User with id " + id + " correctly deleted";
        }
        else {
            throw new NotFoundException("User with id " + id + " not found");
        }
   }

    public User getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isPresent()) {
            return userOptional.get();
        }
        else {
            throw new NotFoundException("User with email " + email + " not found.");
        }
    }

}
