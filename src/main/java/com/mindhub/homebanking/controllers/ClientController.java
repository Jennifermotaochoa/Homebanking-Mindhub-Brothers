package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTOs.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.time.LocalDateTime;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountService accountService;

    //@RequestMapping("/clients")
    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClients();
    }
    //@RequestMapping("/clients/{id}")
    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClient(id);
    }
    //@RequestMapping("/clients/current")
    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        //ClientDTO  clientDTO= ;
        return clientService.getCurrentClient(authentication);
    }

    //@RequestMapping(path = "/clients", method = RequestMethod.POST)
    @PostMapping("/clients")
    public ResponseEntity<Object> registerClient(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isBlank()) {
            return new ResponseEntity<>("Please, complete your first name", HttpStatus.FORBIDDEN);
        }
        else if(!firstName.matches("^[a-zA-Z]*$")){
            return new ResponseEntity<>("Please, the first name only allows letters", HttpStatus.FORBIDDEN);
        }

        if (lastName.isBlank()) {
            return new ResponseEntity<>("Please, complete your last name", HttpStatus.FORBIDDEN);
        }
        else if(!lastName.matches("^[a-zA-Z]*$")){
            return new ResponseEntity<>("Please, the last name only allows letters", HttpStatus.FORBIDDEN);
        }

        if (email.isBlank()) {
            return new ResponseEntity<>("Please complete your email", HttpStatus.FORBIDDEN);
        }
        else if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            return new ResponseEntity<>("Please, enter a valid email.", HttpStatus.FORBIDDEN);
        }

        if (password.isBlank()) {
            return new ResponseEntity<>("Please, complete your password", HttpStatus.FORBIDDEN);
        }

        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Your email already in use", HttpStatus.FORBIDDEN);
        }

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(newClient);

        String number;

        do{
            number = "VIN"+ getStringNumber();
        }while(accountService.findByNumber(number) != null);

        Account newAccount = new Account(number, LocalDateTime.now(), 0.00, true);
        newClient.addAccount(newAccount);
        accountService.saveAccount(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    int min = 000000;
    int max = 999999;
    private int getNumberRandom(int min, int max){
        Random random = new Random();
        int number = random.nextInt(max) + min;
        return number;
    }
    private String getStringNumber(){
        int numberRandom = getNumberRandom(min, max);
        return String.valueOf(numberRandom);
    }

}
