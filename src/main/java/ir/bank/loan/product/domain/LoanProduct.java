package ir.bank.loan.product.domain;

import ir.bank.loan.common.money.Money;

import java.util.Objects;

/**
 * A configurable loan product. Immutable snapshot of one product version.
 *
 * Validation guards the financial boundaries (US-01.03). Activation rules
 * (US-01.05) are enforced at the service layer, not here.
 */
public final class LoanProduct {

    public enum Status { DRAFT, ACTIVE, ARCHIVED }

    private final String code;
    private final String name;
    private final ContractType contractType;
    private final Money minAmount;
    private final Money maxAmount;
    private final int minTenorMonths;
    private final int maxTenorMonths;
    private final InstallmentType installmentType;
    private final PaymentInterval paymentInterval;
    private final int gracePeriodMonths;
    private final int version;
    private final Status status;

    private LoanProduct(Builder b) {
        this.code = Objects.requireNonNull(b.code, "code");
        this.name = Objects.requireNonNull(b.name, "name");
        this.contractType = Objects.requireNonNull(b.contractType, "contractType");
        this.minAmount = Objects.requireNonNull(b.minAmount, "minAmount");
        this.maxAmount = Objects.requireNonNull(b.maxAmount, "maxAmount");
        this.minTenorMonths = b.minTenorMonths;
        this.maxTenorMonths = b.maxTenorMonths;
        this.installmentType = Objects.requireNonNull(b.installmentType, "installmentType");
        this.paymentInterval = Objects.requireNonNull(b.paymentInterval, "paymentInterval");
        this.gracePeriodMonths = b.gracePeriodMonths;
        this.version = b.version;
        this.status = b.status;
        validate();
    }

    private void validate() {
        if (minAmount.isNegative() || minAmount.isZero()) {
            throw new IllegalArgumentException("minAmount must be > 0");
        }
        if (minAmount.compareTo(maxAmount) > 0) {
            throw new IllegalArgumentException("minAmount must be <= maxAmount");
        }
        if (minTenorMonths <= 0 || minTenorMonths > maxTenorMonths) {
            throw new IllegalArgumentException("invalid tenor range");
        }
        if (gracePeriodMonths < 0) {
            throw new IllegalArgumentException("gracePeriodMonths must be >= 0");
        }
    }

    /** Whether a requested amount/tenor is within this product's limits. */
    public boolean accepts(Money amount, int tenorMonths) {
        return amount.compareTo(minAmount) >= 0
                && amount.compareTo(maxAmount) <= 0
                && tenorMonths >= minTenorMonths
                && tenorMonths <= maxTenorMonths;
    }

    public String code() { return code; }
    public String name() { return name; }
    public ContractType contractType() { return contractType; }
    public Money minAmount() { return minAmount; }
    public Money maxAmount() { return maxAmount; }
    public int minTenorMonths() { return minTenorMonths; }
    public int maxTenorMonths() { return maxTenorMonths; }
    public InstallmentType installmentType() { return installmentType; }
    public PaymentInterval paymentInterval() { return paymentInterval; }
    public int gracePeriodMonths() { return gracePeriodMonths; }
    public int version() { return version; }
    public Status status() { return status; }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String code;
        private String name;
        private ContractType contractType;
        private Money minAmount;
        private Money maxAmount;
        private int minTenorMonths;
        private int maxTenorMonths;
        private InstallmentType installmentType = InstallmentType.EQUAL;
        private PaymentInterval paymentInterval = PaymentInterval.MONTHLY;
        private int gracePeriodMonths = 0;
        private int version = 1;
        private Status status = Status.DRAFT;

        public Builder code(String v) { this.code = v; return this; }
        public Builder name(String v) { this.name = v; return this; }
        public Builder contractType(ContractType v) { this.contractType = v; return this; }
        public Builder minAmount(Money v) { this.minAmount = v; return this; }
        public Builder maxAmount(Money v) { this.maxAmount = v; return this; }
        public Builder minTenorMonths(int v) { this.minTenorMonths = v; return this; }
        public Builder maxTenorMonths(int v) { this.maxTenorMonths = v; return this; }
        public Builder installmentType(InstallmentType v) { this.installmentType = v; return this; }
        public Builder paymentInterval(PaymentInterval v) { this.paymentInterval = v; return this; }
        public Builder gracePeriodMonths(int v) { this.gracePeriodMonths = v; return this; }
        public Builder version(int v) { this.version = v; return this; }
        public Builder status(Status v) { this.status = v; return this; }

        public LoanProduct build() {
            return new LoanProduct(this);
        }
    }
}
