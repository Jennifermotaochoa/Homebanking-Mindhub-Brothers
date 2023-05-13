package com.mindhub.homebanking.service;

import com.mindhub.homebanking.DTOs.CardDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CardService {
    List<CardDTO> getCards(Authentication authentication);
    void saveCard(Card card);
    Card findByNumber(String number);

}
