package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTOs.AccountDTO;
import com.mindhub.homebanking.DTOs.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;

    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCards(Authentication authentication) {
        return cardService.getCards(authentication);
    }


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> registerCard(Authentication authentication, @RequestParam CardType cardType,
                                               @RequestParam ColorType colorType) {
        Client client = clientService.findByEmail(authentication.getName());
        Set<Card> cards = client.getCards()
                .stream()
                .filter(card -> card.getType() == cardType && card.getActive())
                .collect(Collectors.toSet());

        String number;
        do{
            number = getStringNumber();
        }while(cardService.findByNumber(number) != null);

        if(cards.size() >= 3) {
            return new ResponseEntity<>("Yo can't have more than three cards of the same type", HttpStatus.FORBIDDEN);
        }

        if(cards.stream().anyMatch(card -> card.getColor() == colorType)){
            return new ResponseEntity<>("You can't have same card", HttpStatus.FORBIDDEN);
        }
        int numberCVV = getRandomCVV();

        Card newCard = new Card(client.getFirstName() + " " + client.getLastName(), cardType, colorType, number, numberCVV, LocalDate.now().plusYears(5), LocalDate.now(), true);
        cardService.saveCard(newCard);
        client.addCard(newCard);
        clientService.saveClient(client);
        cardService.saveCard(newCard);

        return new ResponseEntity<>("Created card",HttpStatus.CREATED);
    }
    int min = 1000;
    int max = 8999;

    int minCVV = 100;
    int maxCVV = 899;
    private int getNumberRandom(int min, int max){
        Random random = new Random();
        int number = random.nextInt(max) + min;
        return number;
    }
    private String getStringNumber(){
        String numberRandom = "";
        for(int i = 0; i < 4; i++){
            numberRandom += String.valueOf(getNumberRandom(min, max)) + " ";
        }
        return numberRandom;
    }
    private int getRandomCVV(){
        return getNumberRandom(minCVV, maxCVV);
    }

    @PutMapping("/clients/current/cards/{id}")
    public ResponseEntity<Object> deleteCard(Authentication authentication, @PathVariable Long id){
        Client client = clientService.findByEmail(authentication.getName());
        Card selectCard = cardService.findById(id).orElse(null);

        if(!selectCard.getActive()){
            return new ResponseEntity<>("This card isn't active", HttpStatus.FORBIDDEN);
        }

        if(!client.getCards().contains(selectCard)){
            new ResponseEntity<>("This card doesn't yours", HttpStatus.FORBIDDEN);
        }

        if(selectCard == null){
            new ResponseEntity<>("This card doesn't exist", HttpStatus.FORBIDDEN);
        }

        selectCard.setActive(false);
        cardService.saveCard(selectCard);

        return new ResponseEntity<>("Delete card", HttpStatus.ACCEPTED);
    }
}
