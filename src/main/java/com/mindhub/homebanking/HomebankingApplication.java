package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	LocalDateTime today =  LocalDateTime.now();
	LocalDateTime tomorrow = today.plusDays(1);
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return(arg) -> {
			Client client1 = new Client("Melba", "Morel", "melba@minhub.com");
			clientRepository.save(client1);

			Account account1 = new Account("VINN001", today, 5000, client1);
			accountRepository.save(account1);

			Account account2 = new Account("VINN002", tomorrow ,7500, client1);
			accountRepository.save(account2);

			Client client2 = new Client("Jennifer", "Mota", "jennifer@hotmail.com");
			clientRepository.save(client2);

			Account account3 = new Account("VINN003", today, 6000, client2);
			accountRepository.save(account3);
		};
	}

}
