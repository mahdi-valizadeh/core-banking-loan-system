package ir.bank.loan.calculation.strategy.profit;

import ir.bank.loan.calculation.engine.CalculationContext;
import ir.bank.loan.common.money.Money;
import ir.bank.loan.product.domain.ContractCategory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Reducing-balance profit for FIXED_RETURN contracts (Murabaha, Ijarah, ...).
 *
 * profit = remainingBalance * periodRate, rounded to whole Rial (HALF_UP).
 * The bank's nearest-10-Rial policy is applied later at schedule assembly.
 */
public class FixedReturnProfitStrategy implements ProfitStrategy {

    @Override
    public Money profitFor(CalculationContext ctx) {
        BigDecimal balance = new BigDecimal(ctx.remainingBalance().toRial());
        BigDecimal profit = balance.multiply(ctx.periodRate());
        BigInteger rial = profit.setScale(0, RoundingMode.HALF_UP).toBigInteger();
        return Money.ofRial(rial);
    }

    @Override
    public ContractCategory category() {
        return ContractCategory.FIXED_RETURN;
    }
}
