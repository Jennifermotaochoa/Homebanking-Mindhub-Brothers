package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}

	LocalDateTime today =  LocalDateTime.now();
	LocalDateTime tomorrow = today.plusDays(1);
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
		return(arg) -> {
			//Cliente 1 con 2 cuentas, 3 transacciones y 2 prestamos
			Client client1 = new Client("Melba", "Morel", "melba@minhub.com");
			clientRepository.save(client1);

			Account account1 = new Account("VINN001", today, 5000.00);
			client1.addAccount(account1);
			accountRepository.save(account1);
			clientRepository.save(client1);

			Account account2 = new Account("VINN002", tomorrow ,7500.00);
			client1.addAccount(account2);
			accountRepository.save(account2);
			clientRepository.save(client1);

			Transaction transaction1 = new Transaction(DEBIT, -100.00, "McDonald's", today);
			account1.addTransaction(transaction1);
			transactionRepository.save(transaction1);
			accountRepository.save(account1);

			Transaction transaction2 = new Transaction(CREDIT, +200.00, "Bazar", today);
			account2.addTransaction(transaction2);
			transactionRepository.save(transaction2);
			accountRepository.save(account2);

			Transaction transaction3 = new Transaction(CREDIT, +300.00, "Supermercado", tomorrow);
			account1.addTransaction(transaction3);
			transactionRepository.save(transaction3);
			accountRepository.save(account1);

			Loan typeLoan1 = new Loan("Hipotecario", 500000.00, (List.of(12, 24, 36, 48, 60)));
			loanRepository.save(typeLoan1);

			Loan typeLoan2 = new Loan("Personal", 100000.00, (List.of(6, 12, 24)));
			loanRepository.save(typeLoan2);

			Loan typeLoan3 = new Loan("Automotriz", 300000.00, (List.of(6, 12, 24, 36)));
			loanRepository.save(typeLoan3);

			ClientLoan clientLoan1 = new ClientLoan("Hipotecario",400000, 60);
			client1.addClientLoan(clientLoan1);
			typeLoan1.addClientLoan(clientLoan1);
			clientLoanRepository.save(clientLoan1);

			ClientLoan clientLoan2 = new ClientLoan("Personal",50000, 12);
			client1.addClientLoan(clientLoan2);
			typeLoan2.addClientLoan(clientLoan2);
			clientLoanRepository.save(clientLoan2);

			//Cliente 2 con 1 cuenta y 2 prestamos
			Client client2 = new Client("Jennifer", "Mota", "jennifer@hotmail.com");
			clientRepository.save(client2);

			Account account3 = new Account("VINN003", today, 6000.00);
			client2.addAccount(account3);
			accountRepository.save(account3);
			clientRepository.save(client2);

			ClientLoan client2Loan1 = new ClientLoan("Personal",100000, 24);
			client2.addClientLoan(client2Loan1);
			typeLoan2.addClientLoan(client2Loan1);
			clientLoanRepository.save(client2Loan1);

			ClientLoan client2Loan2 = new ClientLoan("Automotriz",200000, 36);
			client2.addClientLoan(client2Loan2);
			typeLoan3.addClientLoan(client2Loan2);
			clientLoanRepository.save(client2Loan2);

		};
	}

}
