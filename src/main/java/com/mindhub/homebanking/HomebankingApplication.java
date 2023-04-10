package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
		System.out.println("Hola");
	}

	LocalDateTime today =  LocalDateTime.now();
	LocalDateTime tomorrow = today.plusDays(1);
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return(arg) -> {
			Client client1 = new Client("Melba", "Morel", "melba@minhub.com");
			clientRepository.save(client1);

			Account account1 = new Account("VINN001", today, 5000);
			client1.addAccount(account1);
			accountRepository.save(account1);
			clientRepository.save(client1);

			Account account2 = new Account("VINN002", tomorrow ,7500);
			client1.addAccount(account2);
			accountRepository.save(account2);
			clientRepository.save(client1);

			Transaction transaction1 = new Transaction(DEBIT, -100, "McDonald's", today);
			account1.addTransaction(transaction1);
			transactionRepository.save(transaction1);
			accountRepository.save(account1);

			Transaction transaction2 = new Transaction(CREDIT, +200, "Bazar", today);
			account2.addTransaction(transaction2);
			transactionRepository.save(transaction2);
			accountRepository.save(account2);

			Transaction transaction3 = new Transaction(CREDIT, +300, "Supermercado", tomorrow);
			account1.addTransaction(transaction3);
			transactionRepository.save(transaction3);
			accountRepository.save(account1);


			Client client2 = new Client("Jennifer", "Mota", "jennifer@hotmail.com");
			clientRepository.save(client2);

			Account account3 = new Account("VINN003", today, 6000);
			client2.addAccount(account3);
			accountRepository.save(account3);
			clientRepository.save(client2);

		};
	}

}
