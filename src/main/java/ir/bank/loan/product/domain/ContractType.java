package ir.bank.loan.product.domain;

import static ir.bank.loan.product.domain.ContractCategory.FIXED_RETURN;
import static ir.bank.loan.product.domain.ContractCategory.NO_RETURN;
import static ir.bank.loan.product.domain.ContractCategory.VARIABLE_RETURN;

/**
 * Supported Islamic finance contracts.
 *
 * Each contract carries its category and whether it requires a final
 * settlement run (variable-return contracts do). The Persian label is kept
 * for UI display.
 */
public enum ContractType {

    // Fixed return
    MURABAHA(FIXED_RETURN, "مرابحه", false),
    IJARAH_TO_OWN(FIXED_RETURN, "اجاره به‌شرط تملیک", false),
    INSTALLMENT_SALE(FIXED_RETURN, "فروش اقساطی", false),
    JUALAH(FIXED_RETURN, "جعاله", false),

    // Variable return (provisional + settlement)
    MUDARABAH(VARIABLE_RETURN, "مضاربه", true),
    CIVIL_PARTNERSHIP(VARIABLE_RETURN, "مشارکت مدنی", true),
    LEGAL_PARTNERSHIP(VARIABLE_RETURN, "مشارکت حقوقی", true),
    SALAF(VARIABLE_RETURN, "سلف", true),

    // No return (fee only)
    QARD_AL_HASAN(NO_RETURN, "قرض‌الحسنه", false);

    private final ContractCategory category;
    private final String persianLabel;
    private final boolean requiresSettlement;

    ContractType(ContractCategory category, String persianLabel, boolean requiresSettlement) {
        this.category = category;
        this.persianLabel = persianLabel;
        this.requiresSettlement = requiresSettlement;
    }

    public ContractCategory category() {
        return category;
    }

    public String persianLabel() {
        return persianLabel;
    }

    public boolean requiresSettlement() {
        return requiresSettlement;
    }

    public boolean isProfitBearing() {
        return category != NO_RETURN;
    }
}
