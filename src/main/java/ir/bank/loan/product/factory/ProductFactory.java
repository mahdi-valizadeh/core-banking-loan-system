package ir.bank.loan.product.factory;

import ir.bank.loan.common.money.Money;
import ir.bank.loan.product.domain.LoanProduct;
import org.springframework.stereotype.Component;

/**
 * Factory pattern: builds validated {@link LoanProduct} instances from a
 * zero-code {@link ProductDefinition}. Keeps construction/validation logic in
 * one place so the rest of the system never news-up products directly.
 */
@Component
public class ProductFactory {

    /** Build a new DRAFT product (version 1) from a definition. */
    public LoanProduct createDraft(ProductDefinition def) {
        return LoanProduct.builder()
                .code(def.code())
                .name(def.name())
                .contractType(def.contractType())
                .minAmount(Money.ofRial(def.minAmountRial()))
                .maxAmount(Money.ofRial(def.maxAmountRial()))
                .minTenorMonths(def.minTenorMonths())
                .maxTenorMonths(def.maxTenorMonths())
                .installmentType(def.installmentType())
                .paymentInterval(def.paymentInterval())
                .gracePeriodMonths(def.gracePeriodMonths())
                .version(1)
                .status(LoanProduct.Status.DRAFT)
                .build();
    }

    /**
     * Produce the next version of a product from an edited definition.
     * Editing an active product yields a new DRAFT version (US-01.05).
     */
    public LoanProduct nextVersion(LoanProduct current, ProductDefinition edited) {
        return LoanProduct.builder()
                .code(edited.code())
                .name(edited.name())
                .contractType(edited.contractType())
                .minAmount(Money.ofRial(edited.minAmountRial()))
                .maxAmount(Money.ofRial(edited.maxAmountRial()))
                .minTenorMonths(edited.minTenorMonths())
                .maxTenorMonths(edited.maxTenorMonths())
                .installmentType(edited.installmentType())
                .paymentInterval(edited.paymentInterval())
                .gracePeriodMonths(edited.gracePeriodMonths())
                .version(current.version() + 1)
                .status(LoanProduct.Status.DRAFT)
                .build();
    }
}
