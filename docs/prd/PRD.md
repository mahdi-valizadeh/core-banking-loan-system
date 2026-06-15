# Product Requirements Document (PRD)
# Core Banking Loan System — Product Factory & Engines

| Field        | Value                                   |
|--------------|-----------------------------------------|
| Version      | 0.2 (Refinement)                        |
| Owner        | Product Owner                           |
| Status       | Draft — pending stakeholder sign-off    |
| Currency     | IRR (Rial) — round to nearest 10 Rial   |
| Calendar     | Jalali (Shamsi)                         |
| UI           | Persian only, RTL                       |
| Architecture | Factory Pattern + Strategy Pattern      |
| Approach     | Zero-Code Configuration                 |

## 1. Vision
Enable bank business managers to define, price, and launch new loan
products entirely through configuration — with zero code changes and zero
software releases by the engineering team.

## 2. Problem Statement
Every new loan product currently requires a development cycle (code, test,
deploy), tying business agility to engineering capacity and slowing
time-to-market.

## 3. Goals
- G1: Define a new loan product with no code (config-only).
- G2: Make all financial formulas editable rules, never hard-coded.
- G3: Make eligibility/risk policies editable from an admin panel with no release.
- G4: Generate an accurate, transparent Amortization Schedule before approval.
- G5: Allow dynamic custom fields on customer master data (local + inquiry).
- G6: Manage the full loan lifecycle (create, accrue, penalize, default, settle).

## 4. Success Metrics (KPIs)
- Time-to-market for a new product: < 1 business day.
- % of products launched without code: 100%.
- Formula calculation accuracy vs. finance reference: 100% match.
- Releases required to launch a product: 0.

## 5. Scope
### In Scope (Phase 1)
- Dynamic Product Factory
- Editable Calculation Engine (profit, fee, penalty, early settlement)
- Amortization Engine (equal, stepped, balloon, grace; configurable interval)
- Eligibility & Risk Rule Engine (per product / per customer type)
- Dynamic customer master data (local + inquiry web-service fields)
- Loan lifecycle: creation, payment, late/overdue, default (NPL), early settlement
- Zero-code admin panel, config versioning & audit
- External inquiry integration, RBAC

### Out of Scope (Phase 1)
- General-ledger postings / treasury integration
- Collateral management
- Collections automation
- Customer-facing mobile app

## 6. Islamic Contract Taxonomy
| Category        | Contracts |
|-----------------|-----------|
| Fixed Return    | Murabaha, Ijarah-to-own, Installment Sale, Jualah |
| Variable Return | Mudarabah, Civil Partnership, Legal Partnership, Salaf |
| No Return       | Qard al-Hasan (fee only) |

Variable-return contracts use provisional profit + final settlement.

## 7. Personas
- Product Manager — creates products via wizard.
- Finance Analyst — edits profit/fee/penalty formulas.
- Risk Officer — edits eligibility rules & custom fields.
- Loan Officer — runs eligibility, previews schedule, manages lifecycle.
- Auditor / Compliance — reviews change log, maker-checker.

## 8. Non-Functional Requirements
- NFR1 Rounding to nearest 10 Rial; Σ principal == loan principal.
- NFR2 Config applied at runtime, no redeploy.
- NFR3 Full auditability (who/what/when/old value).
- NFR4 Amortization for 360 installments < 500 ms.
- NFR5 RBAC; only authorized roles edit formulas/rules.
- NFR6 Safe, sandboxed formula evaluation.

## 9. Assumptions & Dependencies
- Customer data is partly local and partly fetched via a bank inquiry web service.
- Bank-Markazi rate ceilings are configurable parameters.
- Islamic-finance contract types are the pricing basis.

## 10. Risks
- R1 Formula misuse → wrong profit (financial/legal). Mitigation: maker-checker.
- R2 Unsafe expression evaluation → security. Mitigation: sandbox + whitelist.
- R3 Scope creep into GL/disbursement.
