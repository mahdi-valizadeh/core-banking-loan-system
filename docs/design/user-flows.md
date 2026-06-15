# User Flows / فلوهای کاربری

## System Flow / فلوی کلی سیستم

```text
[Login / RBAC]
    |
    v
[Admin Dashboard]  (role decides visible modules)
    |
    +-> Product Manager -> Product Factory -> Define Product (draft)
    |                              |
    |                              +-> Attach Formula (Calc Engine)
    |                              +-> Attach Eligibility Rules
    |                              +-> Activate Product
    |
    +-> Finance Analyst -> Formula Builder -> Test -> Version & Save
    |
    +-> Risk Officer -> Rule Builder + Custom Fields Manager
    |
    +-> Loan Officer -> New Application
                              |
                              +-> Eligibility Check -> (Pass / Reject+reasons)
                              +-> Amortization Preview -> Print/Export
                              +-> Lifecycle (pay / overdue / default / settle)
```

## Product Factory — Detailed Flow / فلوی تفصیلی کارخانه محصول

```text
START
  |
  v
(1) Product List  [ +New ] [ Search ] [ Filter: status ]
  |
  v
(2) Step 1/4 - Basic Info    : name, contract type, description
  |
  v
(3) Step 2/4 - Financial Terms: min/max amount (IRR), min/max tenor,
                                installment type, payment interval
  |
  v
(4) Step 3/4 - Formula Binding -> [Calculation Engine] + Test panel
  |
  v
(5) Step 4/4 - Eligibility Binding -> [Rule Engine]
  |
  v
(6) Review & Save
     +- Save Draft -> status=draft -> back to (1)
     +- Activate   -> confirm modal -> status=active (runtime, no redeploy)
END
```
