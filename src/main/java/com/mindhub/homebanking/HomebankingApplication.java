package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.ColorType.GOLD;
import static com.mindhub.homebanking.models.ColorType.TITANIUM;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}

	LocalDateTime today =  LocalDateTime.now();
	LocalDateTime tomorrow = today.plusDays(1);
	LocalDate fromDate = LocalDate.now();
	LocalDate thruDate = fromDate.plusYears(5);

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository) {
		return(arg) -> {
			//Cliente 1 con 2 cuentas, 3 transacciones y 2 prestamos
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba123"));
			clientRepository.save(client1);

			Account account1 = new Account("VINN-001", today, 5000.00);
			client1.addAccount(account1);
			accountRepository.save(account1);
			clientRepository.save(client1);

			Account account2 = new Account("VINN-002", tomorrow ,7500.00);
			client1.addAccount(account2);
			accountRepository.save(account2);
			clientRepository.save(client1);

			Transaction transaction1 = new Transaction(DEBIT, -1000.00, "McDonald's", today);
			account1.addTransaction(transaction1);
			transactionRepository.save(transaction1);
			accountRepository.save(account1);

			Transaction transaction2 = new Transaction(CREDIT, +2000.30, "Bazar", today);
			account2.addTransaction(transaction2);
			transactionRepository.save(transaction2);
			accountRepository.save(account2);

			Transaction transaction3 = new Transaction(CREDIT, +3000.00, "Supermercado", tomorrow);
			account1.addTransaction(transaction3);
			transactionRepository.save(transaction3);
			accountRepository.save(account1);

			Loan typeLoan1 = new Loan("Mortgage", 500000.00, (List.of(12, 24, 36, 48, 60)));
			loanRepository.save(typeLoan1);

			Loan typeLoan2 = new Loan("Personal", 100000.00, (List.of(6, 12, 24)));
			loanRepository.save(typeLoan2);

			Loan typeLoan3 = new Loan("Auto", 300000.00, (List.of(6, 12, 24, 36)));
			loanRepository.save(typeLoan3);

			ClientLoan clientLoan1 = new ClientLoan(400000, 60);
			client1.addClientLoan(clientLoan1);
			typeLoan1.addClientLoan(clientLoan1);
			clientLoanRepository.save(clientLoan1);

			ClientLoan clientLoan2 = new ClientLoan(50000, 12);
			client1.addClientLoan(clientLoan2);
			typeLoan2.addClientLoan(clientLoan2);
			clientLoanRepository.save(clientLoan2);

			Card cardDebit1 = new Card(client1.getFirstName() + " " + client1.getLastName(), CardType.DEBIT, ColorType.GOLD, "1111 2222 3333 4444", 123, thruDate, fromDate);
            client1.addCard(cardDebit1);
            cardRepository.save(cardDebit1);

            Card cardCredit1 = new Card(client1.getFirstName() + " " + client1.getLastName(), CardType.CREDIT, ColorType.TITANIUM, "7777 4444 5555 6666", 456, thruDate, fromDate);
            client1.addCard(cardCredit1);
            cardRepository.save(cardCredit1);

			//Cliente 2 con 1 cuenta y 2 prestamos
			Client client2 = new Client("Jennifer", "Mota", "jennifer@hotmail.com", passwordEncoder.encode("jennifer123"));
			clientRepository.save(client2);

			Account account3 = new Account("VINN-003", today, 6000.00);
			client2.addAccount(account3);
			accountRepository.save(account3);
			clientRepository.save(client2);

			ClientLoan client2Loan1 = new ClientLoan(100000, 24);
			client2.addClientLoan(client2Loan1);
			typeLoan2.addClientLoan(client2Loan1);
			clientLoanRepository.save(client2Loan1);

			ClientLoan client2Loan2 = new ClientLoan(200000, 36);
			client2.addClientLoan(client2Loan2);
			typeLoan3.addClientLoan(client2Loan2);
			clientLoanRepository.save(client2Loan2);

            Card cardCredit2 = new Card(client2.getFirstName() + " " + client2.getLastName(), CardType.CREDIT, ColorType.SILVER, "7777-8888-999", 789, thruDate, fromDate);
            client2.addCard(cardCredit2);
            cardRepository.save(cardCredit2);

		};
	}

}
