package ir.bank.loan.common.calendar;

import java.util.Set;

/**
 * Minimal Jalali (Shamsi) date for installment due-dates.
 *
 * Month arithmetic is exact. Leap years use the common 33-year cycle rule
 * (accurate for all realistic loan dates). Gregorian<->Jalali conversion is
 * out of scope here and will be added when wiring disbursement dates.
 */
public record JalaliDate(int year, int month, int day) {

    private static final Set<Integer> LEAP_REMAINDERS =
            Set.of(1, 5, 9, 13, 17, 22, 26, 30);

    public JalaliDate {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("month must be 1..12");
        }
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("day must be 1..31");
        }
    }

    public static boolean isLeapYear(int year) {
        int r = ((year % 33) + 33) % 33;
        return LEAP_REMAINDERS.contains(r);
    }

    public static int monthLength(int year, int month) {
        if (month <= 6) return 31;
        if (month <= 11) return 30;
        return isLeapYear(year) ? 30 : 29;
    }

    /** Add n months, rolling the year and clamping the day to month length. */
    public JalaliDate plusMonths(int n) {
        int zeroBased = (month - 1) + n;
        int newYear = year + Math.floorDiv(zeroBased, 12);
        int newMonth = Math.floorMod(zeroBased, 12) + 1;
        int newDay = Math.min(day, monthLength(newYear, newMonth));
        return new JalaliDate(newYear, newMonth, newDay);
    }

    @Override
    public String toString() {
        return String.format("%04d/%02d/%02d", year, month, day);
    }
}
