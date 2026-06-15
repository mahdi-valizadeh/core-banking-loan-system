package ir.bank.loan.amortization;

import ir.bank.loan.common.calendar.JalaliDate;
import ir.bank.loan.common.money.Money;

import java.math.BigDecimal;
import java.util.List;

/** Inputs needed to build a repayment schedule. */
public record AmortizationRequest(
        Money principal,
        BigDecimal annualRate,
        int intervalMonths,
        int installmentCount,
        JalaliDate firstDueDate,
        Money totalFee
) {}

/** One row of the amortization schedule. */
record AmortizationLine(
        int number,
        JalaliDate dueDate,
        Money principalShare,
        Money profitShare,
        Money fee,
        Money total,
        Money remainingBalance
) {}
