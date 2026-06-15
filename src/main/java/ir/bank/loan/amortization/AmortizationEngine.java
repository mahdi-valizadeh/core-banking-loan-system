package ir.bank.loan.amortization;

import ir.bank.loan.calculation.engine.EngineFactory;
import ir.bank.loan.calculation.strategy.profit.ProfitStrategy;
import ir.bank.loan.calculation.strategy.rounding.RoundingStrategy;
import ir.bank.loan.product.domain.InstallmentType;
import ir.bank.loan.product.domain.LoanProduct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

/**
 * Orchestrates schedule generation: resolves the profit strategy by contract
 * (via {@link EngineFactory}) and the installment strategy by type, then
 * delegates. Stepped/balloon/grace strategies are registered in later sections.
 */
@Component
public class AmortizationEngine {

    private final EngineFactory engineFactory;
    private final RoundingStrategy rounding;
    private final Map<InstallmentType, InstallmentStrategy> installmentStrategies =
            new EnumMap<>(InstallmentType.class);

    public AmortizationEngine(EngineFactory engineFactory, RoundingStrategy rounding) {
        this.engineFactory = engineFactory;
        this.rounding = rounding;
        register(new EqualInstallmentStrategy());
    }

    private void register(InstallmentStrategy strategy) {
        installmentStrategies.put(strategy.type(), strategy);
    }

    public AmortizationSchedule generate(LoanProduct product,
                                         AmortizationRequest request) {
        ProfitStrategy profitStrategy =
                engineFactory.profitStrategyFor(product.contractType());
        InstallmentStrategy installmentStrategy =
                installmentStrategies.get(product.installmentType());
        if (installmentStrategy == null) {
            throw new UnsupportedOperationException(
                    "installment type not yet supported: " + product.installmentType());
        }
        return installmentStrategy.generate(request, profitStrategy, rounding);
    }
}
