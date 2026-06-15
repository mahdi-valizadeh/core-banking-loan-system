package ir.bank.loan.calculation.strategy.rounding;

import ir.bank.loan.common.money.Money;

/**
 * Strategy for rounding monetary amounts.
 *
 * Centralized so no formula hard-codes its own rounding (Zero-Code).
 * The active strategy is configured globally, not per formula.
 */
public interface RoundingStrategy {

    /** Round a single amount according to the bank policy. */
    Money round(Money amount);

    /** Human-readable name of the policy (for audit/debug). */
    String name();
}
