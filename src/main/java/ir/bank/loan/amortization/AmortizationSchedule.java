package ir.bank.loan.amortization;

import ir.bank.loan.common.money.Money;

import java.util.Collections;
import java.util.List;

/**
 * A generated repayment schedule.
 *
 * Enforces the core financial invariant (US-03.07): the sum of principal
 * shares equals the loan principal exactly, after rounding reconciliation.
 */
public final class AmortizationSchedule {

    private final Money principal;
    private final List<AmortizationLine> lines;

    AmortizationSchedule(Money principal, List<AmortizationLine> lines) {
        this.principal = principal;
        this.lines = List.copyOf(lines);
        assertPrincipalInvariant();
    }

    List<AmortizationLine> lines() {
        return Collections.unmodifiableList(lines);
    }

    public Money totalProfit() {
        return lines.stream().map(AmortizationLine::profitShare)
                .reduce(Money.ZERO, Money::add);
    }

    public Money totalFee() {
        return lines.stream().map(AmortizationLine::fee)
                .reduce(Money.ZERO, Money::add);
    }

    public Money sumPrincipal() {
        return lines.stream().map(AmortizationLine::principalShare)
                .reduce(Money.ZERO, Money::add);
    }

    public int size() {
        return lines.size();
    }

    private void assertPrincipalInvariant() {
        if (!sumPrincipal().equals(principal)) {
            throw new IllegalStateException(
                    "principal invariant violated: sum=" + sumPrincipal()
                            + " expected=" + principal);
        }
    }
}
