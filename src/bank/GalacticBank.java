package bank;

import enums.AccountType;
import enums.TransactionType;
import enums.WorkerType;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class GalacticBank {
    private List<GalacticBankAccount> accounts;
    private List<GalacticTransaction> transactions;
    private List<GalacticBankWorker> workers;

    public GalacticBank() {
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.workers = new ArrayList<>();
    }

    private void addAccount(GalacticBankAccount account) {
        accounts.add(account);
    }

    private void addTransaction(GalacticTransaction transaction) {
        transactions.add(transaction);
    }

    private void addWorker(GalacticBankWorker worker) {
        workers.add(worker);
    }

    public void displayAccounts() {
        for (GalacticBankAccount account : accounts) {
            System.out.println(account);
            System.out.println("------------------------");
            System.out.println();
        }
    }

    public void displayTransactions() {
        for (GalacticTransaction transaction : transactions) {
            System.out.println(transaction);
            System.out.println("------------------------");
            System.out.println();
        }
    }

    public void displayWorkers() {
        for (GalacticBankWorker worker : workers) {
            System.out.println(worker);
            System.out.println("------------------------");
            System.out.println();
        }
    }

    public void readAccountsFromFile(String filePath) {
        try (Scanner reader = new Scanner(new FileInputStream(filePath))) {
            while (reader.hasNextLine()) {
                addAccount(parseAccount(reader.nextLine()));
            }
        } catch (IOException err) {
            System.out.println("Error reading file: " + err.getMessage());
        }
    }

    private GalacticBankAccount parseAccount(String line) {
        String[] parts = line.split(",");
        UUID accountId = UUID.fromString(parts[0]);
        int accountNumber = Integer.parseInt(parts[1]);
        String accountHolder = parts[2];
        double balance = Double.parseDouble(parts[3]);
        double interestRate = Double.parseDouble(parts[4]);
        AccountType type = AccountType.valueOf(parts[5]);

        if (type == AccountType.CHECKING) {
            double overdraftLimit = Double.parseDouble(parts[6]);
            return new GalacticCheckingAccount(accountId, accountNumber, accountHolder, balance,
                                               interestRate, overdraftLimit);
        } else if (type == AccountType.INVESTMENT) {
            double investmentReturnRate = Double.parseDouble(parts[6]);
            return new GalacticInvestmentAccount(accountId, accountNumber, accountHolder, balance,
                                                 interestRate, investmentReturnRate);
        } else if (type == AccountType.SAVINGS) {
            return new GalacticSavingsAccount(accountId, accountNumber, accountHolder, balance,
                                              interestRate);
        } else {
            System.out.println("Error: Unrecognized account type or invalid data in the file.");
            return null;
        }
    }

    public void readTransactionsFromFile(String filePath) {
        try (Scanner reader = new Scanner(new FileInputStream(filePath))) {
            while (reader.hasNextLine()) {
                addTransaction(parseTransaction(reader.nextLine()));
            }
        } catch (IOException err) {
            System.out.println("Error reading file: " + err.getMessage());
        }
    }

    private GalacticTransaction parseTransaction(String line) {
        String[] parts = line.split(",");
        UUID transactionId = UUID.fromString(parts[0]);
        int accountId = Integer.parseInt(parts[1]);

        LocalDateTime timestamp = LocalDateTime.parse(parts[2]);
        TransactionType transactionType = TransactionType.valueOf(parts[3]);
        double amount = Double.parseDouble(parts[4]);

        return new GalacticTransaction(transactionId, accountId, timestamp, transactionType, amount);
    }

    public void readWorkersFromFile(String filePath) {
        try (Scanner reader = new Scanner(new FileInputStream(filePath))) {
            while (reader.hasNextLine()) {
                addWorker(parseWorker(reader.nextLine()));
            }
        } catch (IOException err) {
            System.out.println("Error reading file: " + err.getMessage());
        }
    }

    private GalacticBankWorker parseWorker(String line) {
        String[] parts = line.split(",");
        UUID workerId = UUID.fromString(parts[0]);
        String workerName = parts[1];
        WorkerType workerType = WorkerType.valueOf(parts[2]);

        if (workerType == WorkerType.TELLER) {
            int numberOfTransactions = Integer.parseInt(parts[3]);
            return new GalacticTeller(workerId, workerName, numberOfTransactions);
        } else if (workerType == WorkerType.MANAGER) {
            String department = parts[3];
            return new GalacticManager(workerId, workerName, department);
        } else {
            System.out.println("Error: Unrecognized worker type or invalid data in the file.");
            return null;
        }
    }
}