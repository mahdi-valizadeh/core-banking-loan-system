package ir.bank.loan.calculation.engine;

import ir.bank.loan.calculation.strategy.profit.FixedReturnProfitStrategy;
import ir.bank.loan.calculation.strategy.profit.NoReturnProfitStrategy;
import ir.bank.loan.calculation.strategy.profit.ProfitStrategy;
import ir.bank.loan.calculation.strategy.profit.VariableReturnProfitStrategy;
import ir.bank.loan.product.domain.ContractCategory;
import ir.bank.loan.product.domain.ContractType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

/**
 * Factory pattern: resolves the correct calculation strategy for a contract.
 *
 * Adding a new return model means registering a strategy here — the engine
 * core stays free of contract-specific branching.
 */
@Component
public class EngineFactory {

    private final Map<ContractCategory, ProfitStrategy> profitStrategies =
            new EnumMap<>(ContractCategory.class);

    public EngineFactory() {
        register(new FixedReturnProfitStrategy());
        register(new VariableReturnProfitStrategy());
        register(new NoReturnProfitStrategy());
    }

    private void register(ProfitStrategy strategy) {
        profitStrategies.put(strategy.category(), strategy);
    }

    public ProfitStrategy profitStrategyFor(ContractType contractType) {
        ProfitStrategy strategy = profitStrategies.get(contractType.category());
        if (strategy == null) {
            throw new IllegalStateException(
                    "no profit strategy for category " + contractType.category());
        }
        return strategy;
    }
}
