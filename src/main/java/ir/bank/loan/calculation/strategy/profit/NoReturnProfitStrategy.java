package ir.bank.loan.calculation.strategy.profit;

import ir.bank.loan.calculation.engine.CalculationContext;
import ir.bank.loan.common.money.Money;
import ir.bank.loan.product.domain.ContractCategory;

/**
 * Qard al-Hasan: no profit at all; only a fee is charged (see FeeStrategy).
 */
public class NoReturnProfitStrategy implements ProfitStrategy {

    @Override
    public Money profitFor(CalculationContext ctx) {
        return Money.ZERO;
    }

    @Override
    public ContractCategory category() {
        return ContractCategory.NO_RETURN;
    }
}
