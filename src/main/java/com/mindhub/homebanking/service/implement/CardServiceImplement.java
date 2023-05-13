package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.DTOs.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public List<CardDTO> getCards(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName())
                .getCards()
                .stream()
                .map(card -> new CardDTO(card))
                .collect(Collectors.toList());
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

}
