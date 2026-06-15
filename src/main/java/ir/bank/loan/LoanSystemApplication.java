package ir.bank.loan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Core Banking Loan System entry point.
 *
 * Zero-Code philosophy: products, formulas and rules are configuration,
 * resolved at runtime through Factory + Strategy patterns.
 */
@SpringBootApplication
public class LoanSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanSystemApplication.class, args);
    }
}
