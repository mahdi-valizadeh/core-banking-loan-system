# Wireframes / وایرفریم‌ها

Descriptive wireframes for key modules (RTL in implementation).

```text
WIREFRAME A - Product Factory Wizard (Step 2/4)
+----------------------------------------------------------+
| Sidebar (grey-50)        | Content (white card)          |
|  o 1 Basic Info  (done)  |  New Product - Financial Terms |
|  o 2 Financial   <active |  [ Min Amount(IRR) | Max ]     |
|  o 3 Formula             |  [ Min Tenor | Max Tenor(mo) ] |
|  o 4 Eligibility         |  [ Installment Type v ]        |
|                          |  [ Payment Interval v ]        |
|                          |   [ Back ]    [ Next > ]       |
|                          |   (grey)     (#777BBD)         |
+----------------------------------------------------------+

WIREFRAME B - Formula Builder (Calculation Engine)
+----------------------------------------------------------+
| Variables: [principal][rate][tenor][n][remainingBalance] |
| Expression: principal * rate / 12 ...   (sandbox)        |
| Type: ( ) equal (o) stepped ( ) balloon ( ) grace        |
| -- Test Calculation -----------------------------------   |
| principal=100,000,000 tenor=24 -> Result: x,xxx,xxx IRR  |
| [ Save as new version ] (#777BBD)   v3 | history >        |
+----------------------------------------------------------+

WIREFRAME C - Eligibility Rule Builder
+----------------------------------------------------------+
| IF  [creditScore] [ >= ] [700]                  [ x ]    |
| AND [age]         [ >= ] [18]                   [ x ]    |
| AND [installment/income] [ <= ] [30%]           [ x ]    |
| [ + Add condition ]   [ Guarantors required: 2 ]         |
| Preview: test customer > Eligible / Reasons              |
| [ Activate rule set ] (#777BBD)                          |
+----------------------------------------------------------+

WIREFRAME D - Amortization Schedule Preview
+----------------------------------------------------------+
| Product: Marriage Loan | 100,000,000 IRR | 24 mo         |
| # | DueDate(Jalali) | Principal | Profit | Fee | Remaining|
| 1 | 1404/04/15      | 3,xxx,xxx | x,xxx  |  0  | 96,xxx,xxx|
| ..|     ...         |    ...    |  ...   | ... |    ...    |
| Total Principal = 100,000,000 (ok)  [ Print ] [ Export ] |
+----------------------------------------------------------+
```

## Pages list / فهرست صفحات
1. Login
2. Admin Dashboard (role-aware)
3. Product List
4. Product Wizard (4 steps) + Review
5. Formula Builder (+ version history)
6. Eligibility Rule Builder
7. Custom Fields Manager
8. Loan Application -> Eligibility Result
9. Amortization Schedule Preview
10. Loan Lifecycle (payments / overdue / default / settlement)
11. Audit / Change-Log viewer
