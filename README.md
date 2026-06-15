# Core Banking Loan System / سیستم جامع تسهیلات بانکی

A configurable **Core Banking Loan System** built around a **Zero-Code
Configuration** philosophy: business managers define loan products,
financial formulas, and risk rules through configuration — with no code
changes and no software releases by the engineering team.

سیستمی برای تعریف پویای محصولات وام، فرمول‌های مالی و قوانین ریسک به‌صورت
پیکربندی و بدون کدنویسی (Zero-Code).

---

## Project Standards / استانداردهای پروژه

| Item | Value |
|------|-------|
| Currency / واحد پول | IRR (Rial) — round to nearest **10 Rial** |
| Calendar / تقویم | Jalali (Shamsi) |
| UI Language | Persian only, RTL |
| Architecture | Factory Pattern + Strategy Pattern |
| Approach | Zero-Code Configuration |

## Repository Layout / ساختار مخزن

```
core-banking-loan-system/
├── README.md
├── CONTRIBUTING.md            # Git + Scrum standards / استانداردهای گیت و اسکرام
├── .gitignore
└── docs/
    ├── prd/PRD.md             # Product Requirements Document
    ├── backlog/epics.yaml     # Epic list & prioritization
    ├── user-stories/          # Detailed bilingual user stories
    ├── design/                # Color palette, user flows, wireframes
    └── architecture/          # Factory/Strategy architecture notes
```

## Current Phase / فاز جاری

Discovery & Refinement complete. Artifacts produced:
- PRD (v0.2)
- Epic list (10 epics, prioritized)
- ~45 detailed bilingual user stories
- Design tokens, user flows, wireframes
- Architecture notes (Factory + Strategy)

## Backend Skeleton (Java + Spring Boot + Oracle) / اسکلت بک‌اند

Implemented so far (committed section by section):

| Module | Status |
|--------|--------|
| Build scaffold (Spring Boot, Oracle, Flyway, JPA) | done |
| `Money` (Rial) + nearest-10-Rial `RoundingStrategy` | done |
| Contract taxonomy + `LoanProduct` + `ProductFactory` | done |
| Profit/Fee/Penalty strategies + `EngineFactory` | done |
| Amortization engine + equal-installment + invariant test | done |
| Stepped / balloon / grace installment strategies | next |
| Rule engine, customer master, loan lifecycle | next |

### Build & Run / ساخت و اجرا
```bash
# requires JDK 17+ and Maven (and network to fetch dependencies)
mvn clean test          # run unit tests
mvn spring-boot:run     # run the service
```
Configure Oracle via env vars: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.

## Branches / شاخه‌ها

- `main`    — stable, documentation & releases
- `develop` — integration branch for ongoing work

See `CONTRIBUTING.md` for branch naming and commit conventions.
