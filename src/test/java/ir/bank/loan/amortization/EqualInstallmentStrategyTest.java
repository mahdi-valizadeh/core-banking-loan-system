package ir.bank.loan.amortization;

import ir.bank.loan.calculation.strategy.profit.FixedReturnProfitStrategy;
import ir.bank.loan.calculation.strategy.profit.ProfitStrategy;
import ir.bank.loan.calculation.strategy.rounding.NearestUnitRoundingStrategy;
import ir.bank.loan.calculation.strategy.rounding.RoundingStrategy;
import ir.bank.loan.common.calendar.JalaliDate;
import ir.bank.loan.common.money.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EqualInstallmentStrategyTest {

    private final ProfitStrategy profit = new FixedReturnProfitStrategy();
    private final RoundingStrategy rounding = new NearestUnitRoundingStrategy(10);
    private final EqualInstallmentStrategy strategy = new EqualInstallmentStrategy();

    private AmortizationSchedule sampleSchedule() {
        AmortizationRequest req = new AmortizationRequest(
                Money.ofRial(100_000_000L),
                new BigDecimal("0.18"),
                1,                       // monthly
                24,                      // 24 installments
                new JalaliDate(1404, 4, 15),
                Money.ZERO);
        return strategy.generate(req, profit, rounding);
    }

    @Test
    void generatesExpectedNumberOfInstallments() {
        assertEquals(24, sampleSchedule().size());
    }

    @Test
    void principalSumEqualsLoanPrincipal() {
        // also enforced by AmortizationSchedule constructor
        assertEquals(Money.ofRial(100_000_000L), sampleSchedule().sumPrincipal());
    }

    @Test
    void allPrincipalSharesArePositive() {
        for (AmortizationLine line : sampleSchedule().lines()) {
            assertTrue(line.principalShare().compareTo(Money.ZERO) > 0,
                    "principal share must be positive at #" + line.number());
        }
    }

    @Test
    void lastRemainingBalanceIsZero() {
        var lines = sampleSchedule().lines();
        Money lastRemaining = lines.get(lines.size() - 1).remainingBalance();
        assertEquals(Money.ZERO, lastRemaining);
    }

    @Test
    void dueDatesAdvanceMonthly() {
        var lines = sampleSchedule().lines();
        assertEquals(new JalaliDate(1404, 4, 15), lines.get(0).dueDate());
        assertEquals(new JalaliDate(1404, 5, 15), lines.get(1).dueDate());
    }
}
