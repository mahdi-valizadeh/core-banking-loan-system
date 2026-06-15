package ir.bank.loan.common.money;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Immutable monetary value in Iranian Rial (IRR).
 *
 * Rial has no minor unit; amounts are whole Rial held as BigInteger to avoid
 * floating-point error in financial math. Use {@link RoundingStrategy} to apply
 * the bank's "nearest 10 Rial" policy on engine outputs.
 */
public final class Money implements Comparable<Money> {

    public static final Money ZERO = new Money(BigInteger.ZERO);

    private final BigInteger rial;

    private Money(BigInteger rial) {
        this.rial = Objects.requireNonNull(rial, "rial must not be null");
    }

    public static Money ofRial(long rial) {
        return new Money(BigInteger.valueOf(rial));
    }

    public static Money ofRial(BigInteger rial) {
        return new Money(rial);
    }

    public BigInteger toRial() {
        return rial;
    }

    public Money add(Money other) {
        return new Money(this.rial.add(other.rial));
    }

    public Money subtract(Money other) {
        return new Money(this.rial.subtract(other.rial));
    }

    public Money multiply(BigInteger factor) {
        return new Money(this.rial.multiply(factor));
    }

    /** Integer division (used for splitting an amount into installments). */
    public Money divide(BigInteger divisor) {
        return new Money(this.rial.divide(divisor));
    }

    public boolean isNegative() {
        return rial.signum() < 0;
    }

    public boolean isZero() {
        return rial.signum() == 0;
    }

    @Override
    public int compareTo(Money other) {
        return this.rial.compareTo(other.rial);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return rial.equals(money.rial);
    }

    @Override
    public int hashCode() {
        return rial.hashCode();
    }

    @Override
    public String toString() {
        return rial.toString() + " IRR";
    }
}
