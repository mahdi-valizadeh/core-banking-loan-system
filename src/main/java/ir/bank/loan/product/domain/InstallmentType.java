package ir.bank.loan.product.domain;

/**
 * Repayment schedule shape for a product.
 */
public enum InstallmentType {
    EQUAL,     // اقساط مساوی
    STEPPED,   // اقساط پلکانی
    BALLOON,   // پرداخت بالنی
    GRACE      // با دوره تنفس
}
