package ir.bank.loan.amortization;

import ir.bank.loan.calculation.engine.CalculationContext;
import ir.bank.loan.calculation.strategy.profit.ProfitStrategy;
import ir.bank.loan.calculation.strategy.rounding.RoundingStrategy;
import ir.bank.loan.common.calendar.JalaliDate;
import ir.bank.loan.common.money.Money;
import ir.bank.loan.product.domain.InstallmentType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Equal-installment (annuity) schedule.
 *
 * Profit per period comes from the {@link ProfitStrategy} on the reducing
 * balance. Principal shares for installments 1..n-1 are rounded to the bank
 * unit; the final installment absorbs the rounding residual so that
 * Σ principal == loan principal exactly (US-03.07).
 */
public class EqualInstallmentStrategy implements InstallmentStrategy {

    @Override
    public AmortizationSchedule generate(AmortizationRequest req,
                                         ProfitStrategy profitStrategy,
                                         RoundingStrategy rounding) {
        int n = req.installmentCount();
        if (n <= 0) {
            throw new IllegalArgumentException("installmentCount must be > 0");
        }

        BigDecimal periodRate = req.annualRate()
                .multiply(BigDecimal.valueOf(req.intervalMonths()))
                .divide(BigDecimal.valueOf(12), MathContext.DECIMAL64);

        Money levelPayment = annuityPayment(req.principal(), periodRate, n);

        List<AmortizationLine> lines = new ArrayList<>(n);
        Money remaining = req.principal();
        JalaliDate due = req.firstDueDate();

        for (int i = 1; i <= n; i++) {
            CalculationContext ctx = new CalculationContext(
                    req.principal(), req.annualRate(), req.intervalMonths(),
                    i, remaining, n);

            Money profit = rounding.round(profitStrategy.profitFor(ctx));
            Money fee = (i == 1) ? req.totalFee() : Money.ZERO;

            Money principalShare;
            if (i < n) {
                principalShare = rounding.round(levelPayment.subtract(profit));
                // guard: never amortize more than what remains
                if (principalShare.compareTo(remaining) > 0) {
                    principalShare = remaining;
                }
            } else {
                // last installment absorbs the residual
                principalShare = remaining;
            }

            remaining = remaining.subtract(principalShare);
            Money total = principalShare.add(profit).add(fee);

            lines.add(new AmortizationLine(
                    i, due, principalShare, profit, fee, total, remaining));

            due = due.plusMonths(req.intervalMonths());
        }

        return new AmortizationSchedule(req.principal(), lines);
    }

    /**
     * Annuity level payment:
     *   rate == 0 -> principal / n
     *   else      -> P * r / (1 - (1+r)^-n)
     * Result rounded to whole Rial (schedule rounding applies later).
     */
    private Money annuityPayment(Money principal, BigDecimal rate, int n) {
        BigDecimal p = new BigDecimal(principal.toRial());
        if (rate.signum() == 0) {
            BigInteger rial = p.divide(BigDecimal.valueOf(n), 0, RoundingMode.HALF_UP)
                    .toBigInteger();
            return Money.ofRial(rial);
        }
        BigDecimal onePlusR = BigDecimal.ONE.add(rate);
        BigDecimal denom = BigDecimal.ONE.subtract(
                pow(onePlusR, -n));
        BigDecimal payment = p.multiply(rate)
                .divide(denom, MathContext.DECIMAL64);
        return Money.ofRial(payment.setScale(0, RoundingMode.HALF_UP).toBigInteger());
    }

    /** (base)^exp for integer exp (supports negative exponent). */
    private BigDecimal pow(BigDecimal base, int exp) {
        if (exp >= 0) {
            return base.pow(exp, MathContext.DECIMAL64);
        }
        return BigDecimal.ONE.divide(
                base.pow(-exp, MathContext.DECIMAL64), MathContext.DECIMAL64);
    }

    @Override
    public InstallmentType type() {
        return InstallmentType.EQUAL;
    }
}
