package ir.bank.loan.product.domain;

/**
 * Frequency at which installments fall due.
 */
public enum PaymentInterval {
    MONTHLY(1),
    QUARTERLY(3),
    SEMI_ANNUAL(6),
    ANNUAL(12);

    private final int months;

    PaymentInterval(int months) {
        this.months = months;
    }

    /** Number of Jalali months between two consecutive due-dates. */
    public int months() {
        return months;
    }

    /** Number of installments for a tenor expressed in months. */
    public int installmentCount(int tenorMonths) {
        return tenorMonths / months;
    }
}
