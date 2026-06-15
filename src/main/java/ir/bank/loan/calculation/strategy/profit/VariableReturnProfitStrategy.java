package ir.bank.loan.calculation.strategy.profit;

import ir.bank.loan.calculation.engine.CalculationContext;
import ir.bank.loan.common.money.Money;
import ir.bank.loan.product.domain.ContractCategory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Provisional ("ali-al-hesab") profit for VARIABLE_RETURN contracts
 * (Mudarabah, Musharakah, Salaf). Drives the initial amortization schedule;
 * a separate settlement run reconciles to final profit at maturity.
 */
public class VariableReturnProfitStrategy implements ProfitStrategy {

    @Override
    public Money profitFor(CalculationContext ctx) {
        // Provisional uses the same reducing-balance basis on the provisional
        // rate carried in the context; settlement adjustment is computed
        // elsewhere (see SettlementService, added in a later section).
        BigDecimal balance = new BigDecimal(ctx.remainingBalance().toRial());
        BigDecimal profit = balance.multiply(ctx.periodRate());
        BigInteger rial = profit.setScale(0, RoundingMode.HALF_UP).toBigInteger();
        return Money.ofRial(rial);
    }

    @Override
    public ContractCategory category() {
        return ContractCategory.VARIABLE_RETURN;
    }
}
