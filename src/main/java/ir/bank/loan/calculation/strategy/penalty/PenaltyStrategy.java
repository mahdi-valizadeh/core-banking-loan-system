package ir.bank.loan.calculation.strategy.penalty;

import ir.bank.loan.common.money.Money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Strategy for late-payment penalty (jarime-ye dirkard) accrual.
 *
 * A grace window (days) is honored before penalty starts; a regulatory
 * ceiling caps the maximum penalty (US-02.04).
 */
public interface PenaltyStrategy {

    Money penaltyFor(PenaltyContext ctx);

    /** Input for penalty accrual on one overdue installment. */
    record PenaltyContext(
            Money overdueBase,   // base the penalty applies to
            int overdueDays,     // days past due
            int graceDays,       // days before penalty starts
            Money ceiling        // max penalty (Money.ZERO means "no cap")
    ) {}

    /**
     * Penalty = overdueBase * dailyRate * chargeableDays, capped at ceiling.
     * chargeableDays = max(0, overdueDays - graceDays).
     */
    final class DailyRate implements PenaltyStrategy {

        private final BigDecimal dailyRate; // e.g. 0.0006 per day

        public DailyRate(BigDecimal dailyRate) {
            this.dailyRate = dailyRate;
        }

        @Override
        public Money penaltyFor(PenaltyContext ctx) {
            int chargeableDays = Math.max(0, ctx.overdueDays() - ctx.graceDays());
            if (chargeableDays == 0) {
                return Money.ZERO;
            }
            BigDecimal base = new BigDecimal(ctx.overdueBase().toRial());
            BigDecimal raw = base.multiply(dailyRate)
                    .multiply(BigDecimal.valueOf(chargeableDays));
            BigInteger rial = raw.setScale(0, RoundingMode.HALF_UP).toBigInteger();
            Money penalty = Money.ofRial(rial);

            if (!ctx.ceiling().isZero() && penalty.compareTo(ctx.ceiling()) > 0) {
                return ctx.ceiling();
            }
            return penalty;
        }
    }
}
