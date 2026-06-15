package ir.bank.loan.config;

import ir.bank.loan.calculation.strategy.rounding.NearestUnitRoundingStrategy;
import ir.bank.loan.calculation.strategy.rounding.RoundingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wires the active money policy beans. The rounding unit (nearest 10 Rial)
 * is read from configuration so it can be changed without code edits.
 */
@Configuration
public class MoneyConfig {

    @Bean
    public RoundingStrategy roundingStrategy(
            @Value("${loan.money.rounding-unit:10}") long roundingUnit) {
        return new NearestUnitRoundingStrategy(roundingUnit);
    }
}
