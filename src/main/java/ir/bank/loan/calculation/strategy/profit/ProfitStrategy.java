package ir.bank.loan.calculation.strategy.profit;

import ir.bank.loan.calculation.engine.CalculationContext;
import ir.bank.loan.common.money.Money;
import ir.bank.loan.product.domain.ContractCategory;

/**
 * Strategy for computing the profit portion of one installment.
 * Selected by contract category via the EngineFactory (Zero-Code).
 */
public interface ProfitStrategy {

    /** Profit for the installment described by the context (whole Rial). */
    Money profitFor(CalculationContext ctx);

    /** Contract category this strategy serves. */
    ContractCategory category();
}
