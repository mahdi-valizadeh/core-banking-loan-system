package ir.bank.loan.calculation.engine;

import ir.bank.loan.common.money.Money;

import java.math.BigDecimal;

/**
 * Immutable input for a single installment's profit calculation.
 *
 * These are exactly the whitelisted variables a formula may use
 * (US-02.01): principal, annual rate, interval, installment number,
 * remaining balance, and total installments.
 */
public record CalculationContext(
        Money principal,
        BigDecimal annualRate,   // e.g. 0.18 for 18%
        int intervalMonths,      // months between due-dates
        int installmentNo,       // 1-based
        Money remainingBalance,
        int totalInstallments
) {
    public BigDecimal periodRate() {
        return annualRate
                .multiply(BigDecimal.valueOf(intervalMonths))
                .divide(BigDecimal.valueOf(12), java.math.MathContext.DECIMAL64);
    }
}
