package ir.bank.loan.calculation;

import ir.bank.loan.calculation.strategy.rounding.NearestUnitRoundingStrategy;
import ir.bank.loan.calculation.strategy.rounding.RoundingStrategy;
import ir.bank.loan.common.money.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NearestUnitRoundingStrategyTest {

    private final RoundingStrategy strategy = new NearestUnitRoundingStrategy(10);

    @Test
    void roundsDownBelowHalf() {
        assertEquals(Money.ofRial(4_215_630), strategy.round(Money.ofRial(4_215_634)));
    }

    @Test
    void roundsUpAtHalf() {
        assertEquals(Money.ofRial(4_215_640), strategy.round(Money.ofRial(4_215_635)));
    }

    @Test
    void roundsUpAboveHalf() {
        assertEquals(Money.ofRial(4_215_640), strategy.round(Money.ofRial(4_215_638)));
    }

    @Test
    void exactMultipleIsUnchanged() {
        assertEquals(Money.ofRial(1_000_000), strategy.round(Money.ofRial(1_000_000)));
    }

    @Test
    void rejectsNonPositiveUnit() {
        try {
            new NearestUnitRoundingStrategy(0);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }
}
