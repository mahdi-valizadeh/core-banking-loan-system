package ir.bank.loan.product.factory;

import ir.bank.loan.product.domain.ContractType;
import ir.bank.loan.product.domain.InstallmentType;
import ir.bank.loan.product.domain.PaymentInterval;

import java.math.BigInteger;

/**
 * Raw, zero-code configuration that a business manager defines for a product.
 * The {@link ProductFactory} turns this into a validated domain product.
 *
 * Amounts are given in Rial as BigInteger to stay exact.
 */
public record ProductDefinition(
        String code,
        String name,
        ContractType contractType,
        BigInteger minAmountRial,
        BigInteger maxAmountRial,
        int minTenorMonths,
        int maxTenorMonths,
        InstallmentType installmentType,
        PaymentInterval paymentInterval,
        int gracePeriodMonths
) {
}
