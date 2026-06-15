package ir.bank.loan.calculation.strategy.rounding;

import ir.bank.loan.common.money.Money;

import java.math.BigInteger;

/**
 * Rounds amounts to the nearest configured unit (default: 10 Rial),
 * using round-half-up. Configured via loan.money.rounding-unit.
 *
 * Example (unit=10): 4_215_634 -> 4_215_630 ; 4_215_635 -> 4_215_640
 */
public class NearestUnitRoundingStrategy implements RoundingStrategy {

    private final BigInteger unit;
    private final BigInteger half;

    public NearestUnitRoundingStrategy(long unit) {
        if (unit <= 0) {
            throw new IllegalArgumentException("rounding unit must be positive");
        }
        this.unit = BigInteger.valueOf(unit);
        this.half = this.unit.divide(BigInteger.TWO);
    }

    @Override
    public Money round(Money amount) {
        BigInteger value = amount.toRial();
        BigInteger remainder = value.mod(unit); // 0..unit-1, always non-negative
        BigInteger floored = value.subtract(remainder);
        // round half up
        if (remainder.compareTo(half) >= 0) {
            return Money.ofRial(floored.add(unit));
        }
        return Money.ofRial(floored);
    }

    @Override
    public String name() {
        return "nearest-" + unit + "-rial";
    }
}
