# Architecture Notes / یادداشت معماری

## Patterns / الگوها

- **Factory Pattern**
  - `ProductFactory` builds product instances from configuration.
  - `EngineFactory` resolves the correct Calculation/Installment strategy
    based on the product's contract type.
- **Strategy Pattern** (swappable, config-selected — keeps the core free of
  hard-coded formulas → Zero-Code):
  - `ProfitStrategy` — fixed vs. variable (provisional + settlement)
  - `InstallmentStrategy` — equal | stepped | balloon | grace
  - `PenaltyStrategy` — late-payment penalty accrual
  - `FeeStrategy` — karmozd calculation
  - `RoundingStrategy` — nearest 10 Rial + residual reconciliation

## Key invariants / ثابت‌های کلیدی
- Σ(principal shares) == loan principal after rounding reconciliation.
- All monetary outputs rounded to nearest 10 Rial.
- Loans snapshot the formula/rule versions used at creation (no retro-change).

## Safety / ایمنی
- Formula evaluation is sandboxed with a variable/operator whitelist.
- No arbitrary code, IO, loops, or system calls in formulas.
- Sensitive config changes pass maker-checker before activation.

## Runtime config / پیکربندی در لحظه
- Activating product/formula/rule versions takes effect at runtime.
- No redeploy is required to launch or change a product (G1, G3).

## High-level layering / لایه‌بندی
```
[ Admin UI (RTL, Persian) ]
        |
[ Config & Versioning Service ]  --(runtime)-->  [ Engines ]
        |                                          |- ProfitStrategy
[ Audit Log (append-only) ]                        |- InstallmentStrategy
                                                   |- PenaltyStrategy
[ Customer Master (local + inquiry) ] <---- [ Inquiry Integration ]
        |
[ Loan Lifecycle + State Machine ]
```
