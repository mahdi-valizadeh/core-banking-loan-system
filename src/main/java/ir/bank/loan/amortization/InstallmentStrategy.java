package ir.bank.loan.amortization;

import ir.bank.loan.calculation.strategy.profit.ProfitStrategy;
import ir.bank.loan.calculation.strategy.rounding.RoundingStrategy;
import ir.bank.loan.product.domain.InstallmentType;

/**
 * Strategy for generating a repayment schedule of a given shape
 * (equal | stepped | balloon | grace). Selected by the engine via type.
 */
public interface InstallmentStrategy {

    AmortizationSchedule generate(AmortizationRequest req,
                                  ProfitStrategy profitStrategy,
                                  RoundingStrategy rounding);

    InstallmentType type();
}
