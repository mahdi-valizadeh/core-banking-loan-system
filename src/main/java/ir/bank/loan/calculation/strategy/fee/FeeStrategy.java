package ir.bank.loan.calculation.strategy.fee;

import ir.bank.loan.common.money.Money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Strategy for computing the product fee (karmozd). For Qard al-Hasan this is
 * the only charge. Implementations are config-bound (Zero-Code).
 */
public interface FeeStrategy {

    /** Total fee for a loan of the given principal. */
    Money feeFor(Money principal);

    /** Flat fixed fee. */
    final class Flat implements FeeStrategy {
        private final Money fee;
        public Flat(Money fee) { this.fee = fee; }
        @Override public Money feeFor(Money principal) { return fee; }
    }

    /** Percentage-of-principal fee, rounded to whole Rial. */
    final class Percentage implements FeeStrategy {
        private final BigDecimal rate; // e.g. 0.01 for 1%
        public Percentage(BigDecimal rate) { this.rate = rate; }
        @Override public Money feeFor(Money principal) {
            BigDecimal value = new BigDecimal(principal.toRial()).multiply(rate);
            BigInteger rial = value.setScale(0, RoundingMode.HALF_UP).toBigInteger();
            return Money.ofRial(rial);
        }
    }
}
