# Core Banking Loan System — Detailed User Stories (Bilingual)
# سیستم جامع تسهیلات بانکی — یوزر استوری‌های تفصیلی (دوزبانه)

| Field | Value |
|-------|-------|
| Version | 0.2 (Refinement) |
| Owner | Product Owner |
| Currency | IRR (Rial) — round to nearest **10 Rial** |
| Calendar | Jalali (Shamsi) |
| UI | Persian only, RTL |
| Architecture | Factory Pattern + Strategy Pattern |
| Approach | Zero-Code Configuration |

> Convention / قرارداد: each story is given in English first (engineering
> standard) then mirrored in Persian. IDs follow `US-EE.NN` (Epic.Number).
> هر استوری اول انگلیسی و سپس فارسی ارائه شده است.

---

## 0. Updated Standards / استانداردهای به‌روزشده

### 0.1 Contract Taxonomy / دسته‌بندی عقود

| Category | Contract (EN) | عقد (FA) | Return model |
|----------|---------------|----------|--------------|
| Fixed Return | Murabaha | مرابحه | Predetermined profit |
| Fixed Return | Ijarah-to-own | اجاره به‌شرط تملیک | Predetermined profit |
| Fixed Return | Installment Sale | فروش اقساطی | Predetermined profit |
| Fixed Return | Jualah | جعاله | Predetermined profit |
| Variable Return | Mudarabah | مضاربه | Provisional + final settlement |
| Variable Return | Civil Partnership | مشارکت مدنی | Provisional + final settlement |
| Variable Return | Legal Partnership | مشارکت حقوقی | Provisional + final settlement |
| Variable Return | Salaf (Forward) | سلف | Provisional + final settlement |
| No Return | Qard al-Hasan | قرض‌الحسنه | Fee only (کارمزد) |

### 0.2 Rounding Policy / سیاست گرد کردن
- All monetary amounts are integer Rial, rounded to the nearest **10 Rial**.
- Rounding residuals are reconciled into the **last installment** so that
  `Σ principal = loan principal` exactly.
- همه مبالغ به نزدیک‌ترین ۱۰ ریال گرد می‌شوند و باقی‌مانده گرد کردن در «آخرین قسط» تطبیق داده می‌شود.

### 0.3 Definition of Ready (shared) / تعریف آمادگی (مشترک)
`[x] Story clear  [x] AC defined  [PENDING] Wireframe approved (Designer→PO)  [x] Dependencies  [x] Story Point`
> All P0/P1 stories share one open DoR item: **wireframe sign-off** (Designer phase).

---

## Epic Index / فهرست اپیک‌ها

```yaml
epics:
  - { id: EPIC-01, title: "Product Factory (Dynamic Definition)",        priority: Must,   rank: P0 }
  - { id: EPIC-02, title: "Calculation Engine (Editable Formulas)",      priority: Must,   rank: P0 }
  - { id: EPIC-03, title: "Amortization & Installment Engine",           priority: Must,   rank: P0 }
  - { id: EPIC-04, title: "Eligibility & Risk Rule Engine",              priority: Must,   rank: P0 }
  - { id: EPIC-05, title: "Dynamic Customer Master Data",                priority: Must,   rank: P1 }
  - { id: EPIC-06, title: "Loan Lifecycle Management",                   priority: Must,   rank: P0 }
  - { id: EPIC-07, title: "Zero-Code Admin Configuration Panel",         priority: Must,   rank: P1 }
  - { id: EPIC-08, title: "Config Versioning, Audit & Governance",       priority: Should, rank: P1 }
  - { id: EPIC-09, title: "External Inquiry (Web Service / استعلام)",    priority: Should, rank: P2 }
  - { id: EPIC-10, title: "AuthN/AuthZ & RBAC",                          priority: Must,   rank: P1 }
```

---

# EPIC-01 — Product Factory / کارخانه محصول

### US-01.01 — Create product basic info / ثبت اطلاعات پایه محصول

```text
EN
As a Product Manager
I want to create a new loan product with its basic identity
So that the product exists as a configurable entity without any code

Acceptance Criteria:
[ ] Fields: name (unique), product code (unique), description, status.
[ ] Saving creates the product in status=draft, version=1.
[ ] Duplicate name or code is rejected with a clear validation message.
[ ] Currency is system-fixed to IRR and is not editable.
[ ] Created/updated audit metadata (user, Jalali timestamp) is stored.
```
```text
FA
به‌عنوان مدیر محصول
می‌خواهم یک محصول وام جدید با هویت پایه آن ایجاد کنم
تا محصول به‌عنوان یک موجودیت قابل‌پیکربندی و بدون کدنویسی ساخته شود

معیار پذیرش:
[ ] فیلدها: نام (یکتا)، کد محصول (یکتا)، توضیحات، وضعیت.
[ ] ذخیره، محصول را در وضعیت draft و نسخه ۱ ایجاد می‌کند.
[ ] نام یا کد تکراری با پیام اعتبارسنجی شفاف رد می‌شود.
[ ] واحد پول روی ریال قفل است و قابل‌ویرایش نیست.
[ ] متادیتای ممیزی (کاربر، تاریخ شمسی) ذخیره می‌شود.
```
`meta: story_points=5 | priority=P0 | deps=EPIC-10`

---

### US-01.02 — Select contract type / انتخاب نوع عقد

```text
EN
As a Product Manager
I want to assign one of the supported Islamic contracts to a product
So that the engine knows which return model and rules apply

Acceptance Criteria:
[ ] Contract is chosen from a managed list grouped by category
    (Fixed / Variable / No-Return).
[ ] Choosing a Variable contract enables "provisional + settlement"
    profit configuration downstream (EPIC-02).
[ ] Choosing Qard al-Hasan disables profit config and enables fee-only.
[ ] Contract type cannot be changed after the product is activated.
[ ] Each contract carries metadata: category, requires_settlement (bool).
```
```text
FA
به‌عنوان مدیر محصول
می‌خواهم یکی از عقود اسلامی پشتیبانی‌شده را به محصول اختصاص دهم
تا موتور بداند کدام مدل سود و قوانین اعمال می‌شود

معیار پذیرش:
[ ] عقد از فهرست مدیریت‌شده و گروه‌بندی‌شده (ثابت/متغیر/بدون سود) انتخاب می‌شود.
[ ] انتخاب عقد متغیر، پیکربندی «سود علی‌الحساب + تسویه قطعی» را در ادامه فعال می‌کند.
[ ] انتخاب قرض‌الحسنه، پیکربندی سود را غیرفعال و حالت فقط‌کارمزد را فعال می‌کند.
[ ] پس از فعال‌سازی محصول، نوع عقد قابل‌تغییر نیست.
[ ] هر عقد متادیتا دارد: دسته، نیاز به تسویه قطعی (بولین).
```
`meta: story_points=8 | priority=P0 | deps=US-01.01`

---

### US-01.03 — Define financial terms & ranges / تعریف شرایط و بازه‌های مالی

```text
EN
As a Product Manager
I want to set the financial boundaries of a product
So that loans can only be created within allowed limits

Acceptance Criteria:
[ ] Fields: min/max amount (IRR), min/max tenor (months), max guarantors.
[ ] Validation: min <= max for every range; tenor > 0; amounts > 0.
[ ] Amounts are entered/displayed with a thousands separator.
[ ] Out-of-range values entered later at loan creation are blocked.
```
```text
FA
به‌عنوان مدیر محصول
می‌خواهم مرزهای مالی محصول را تعیین کنم
تا وام‌ها فقط در محدوده مجاز ایجاد شوند

معیار پذیرش:
[ ] فیلدها: حداقل/حداکثر مبلغ (ریال)، حداقل/حداکثر مدت (ماه)، حداکثر تعداد ضامن.
[ ] اعتبارسنجی: min ≤ max برای هر بازه؛ مدت > 0؛ مبالغ > 0.
[ ] مبالغ با جداکننده هزارگان نمایش/ورود داده می‌شوند.
[ ] مقادیر خارج از بازه هنگام ایجاد وام مسدود می‌شوند.
```
`meta: story_points=5 | priority=P0 | deps=US-01.01`

---

### US-01.04 — Payment interval & schedule policy / بازه پرداخت و سیاست زمان‌بندی

```text
EN
As a Product Manager
I want to configure the repayment interval of a product
So that installments are scheduled at the correct frequency

Acceptance Criteria:
[ ] Interval options: monthly | quarterly | semi-annual | annual | custom-days.
[ ] Installment type options: equal | stepped | balloon | grace.
[ ] Optional grace period (months) before the first principal payment.
[ ] First due-date policy: from disbursement date or fixed day-of-month (Jalali).
[ ] Selected policy is passed to the Amortization Engine (EPIC-03).
```
```text
FA
به‌عنوان مدیر محصول
می‌خواهم بازه بازپرداخت محصول را پیکربندی کنم
تا اقساط با تناوب درست زمان‌بندی شوند

معیار پذیرش:
[ ] گزینه‌های بازه: ماهانه | فصلی | شش‌ماهه | سالانه | تعداد روز دلخواه.
[ ] گزینه‌های نوع قسط: مساوی | پلکانی | بالن | با تنفس.
[ ] دوره تنفس اختیاری (ماه) پیش از اولین پرداخت اصل.
[ ] سیاست اولین سررسید: از تاریخ پرداخت یا روز ثابت ماه (شمسی).
[ ] سیاست انتخاب‌شده به موتور استهلاک (EPIC-03) ارسال می‌شود.
```
`meta: story_points=8 | priority=P0 | deps=US-01.01`

---

### US-01.05 — Draft / Activate with versioning / پیش‌نویس/فعال‌سازی نسخه‌دار

```text
EN
As a Product Manager
I want to activate a product through configuration only
So that it becomes usable at runtime with no software release

Acceptance Criteria:
[ ] Activation requires: contract, financial terms, ≥1 bound formula,
    ≥1 bound eligibility rule set.
[ ] Activation creates an immutable, versioned product snapshot.
[ ] Activated product is selectable for new loans without redeploy.
[ ] Editing an active product creates a new draft version (active stays live).
[ ] Activation is blocked with a checklist if any prerequisite is missing.
```
```text
FA
به‌عنوان مدیر محصول
می‌خواهم محصول را فقط از طریق پیکربندی فعال کنم
تا بدون انتشار نسخه نرم‌افزار، در لحظه قابل‌استفاده شود

معیار پذیرش:
[ ] فعال‌سازی نیازمند: عقد، شرایط مالی، حداقل یک فرمول و یک مجموعه قانون اعتبارسنجی متصل.
[ ] فعال‌سازی یک snapshot نسخه‌دار و تغییرناپذیر می‌سازد.
[ ] محصول فعال بدون استقرار مجدد برای وام جدید قابل‌انتخاب است.
[ ] ویرایش محصول فعال، نسخه draft جدید می‌سازد (نسخه فعال دست‌نخورده می‌ماند).
[ ] اگر پیش‌نیازی نباشد، فعال‌سازی با چک‌لیست مسدود می‌شود.
```
`meta: story_points=8 | priority=P0 | deps=US-01.02,US-02.*,US-04.*`

---

### US-01.06 — Clone product / کپی محصول

```text
EN
As a Product Manager
I want to clone an existing product as a new draft
So that I can launch a similar product faster

Acceptance Criteria:
[ ] Clone copies terms, bound formulas and rule sets into a new draft.
[ ] Cloned product needs a new unique name and code before saving.
[ ] The clone has no link back to the source after creation.
```
```text
FA
به‌عنوان مدیر محصول
می‌خواهم یک محصول موجود را به‌عنوان draft جدید کپی کنم
تا محصول مشابه را سریع‌تر راه‌اندازی کنم

معیار پذیرش:
[ ] کپی، شرایط و فرمول‌ها و مجموعه‌قوانین متصل را در draft جدید می‌آورد.
[ ] محصول کپی‌شده پیش از ذخیره به نام و کد یکتای جدید نیاز دارد.
[ ] پس از ایجاد، هیچ وابستگی به محصول مبدأ ندارد.
```
`meta: story_points=3 | priority=P2 | deps=US-01.05`

---

# EPIC-02 — Calculation Engine / موتور محاسبات

### US-02.01 — Author profit formula (fixed return) / تعریف فرمول سود (سود ثابت)

```text
EN
As a Finance Analyst
I want to author a product's profit formula as an editable, sandboxed rule
So that no financial formula is hard-coded in the system core

Acceptance Criteria:
[ ] Formula uses only whitelisted variables: principal, rate, tenor,
    installmentNo, remainingBalance, intervalCount.
[ ] Only whitelisted operators/functions are allowed; arbitrary code,
    loops, IO and system calls are rejected by the sandbox.
[ ] Invalid syntax is reported with line/column before saving.
[ ] Division-by-zero and overflow are caught and surfaced as errors.
[ ] Saved formula is linked to a contract type and product, and versioned.
```
```text
FA
به‌عنوان کارشناس مالی
می‌خواهم فرمول سود محصول را به‌صورت قانون قابل‌ویرایش و sandbox شده بنویسم
تا هیچ فرمول مالی در هسته سیستم hard-code نشود

معیار پذیرش:
[ ] فرمول فقط از متغیرهای مجاز استفاده می‌کند: principal, rate, tenor,
    installmentNo, remainingBalance, intervalCount.
[ ] فقط عملگر/توابع مجاز پذیرفته می‌شوند؛ کد دلخواه، حلقه، IO و فراخوانی سیستمی
    توسط sandbox رد می‌شود.
[ ] خطای نحوی پیش از ذخیره با شماره خط/ستون گزارش می‌شود.
[ ] تقسیم بر صفر و سرریز گرفته و به‌صورت خطا نمایش داده می‌شوند.
[ ] فرمول ذخیره‌شده به نوع عقد و محصول متصل و نسخه‌دار می‌شود.
```
`meta: story_points=13 | priority=P0 | deps=US-01.02`

---

### US-02.02 — Variable-return profit (provisional + settlement) / سود متغیر (علی‌الحساب + قطعی)

```text
EN
As a Finance Analyst
I want to define provisional and final-settlement profit for variable contracts
So that Mudarabah/Musharakah products price interim and final profit correctly

Acceptance Criteria:
[ ] For Variable contracts, two formulas are required: provisional rate
    and final-settlement adjustment.
[ ] Provisional profit drives the initial amortization schedule.
[ ] A settlement run recomputes profit and produces an adjustment
    (debit/credit) entry at maturity or on a settlement date.
[ ] Fixed-return and Qard products hide this configuration.
```
```text
FA
به‌عنوان کارشناس مالی
می‌خواهم سود علی‌الحساب و تسویه قطعی را برای عقود متغیر تعریف کنم
تا محصولات مضاربه/مشارکت سود میان‌دوره و نهایی را درست قیمت‌گذاری کنند

معیار پذیرش:
[ ] برای عقود متغیر دو فرمول لازم است: نرخ علی‌الحساب و اصلاح تسویه قطعی.
[ ] سود علی‌الحساب، جدول استهلاک اولیه را می‌سازد.
[ ] اجرای تسویه، سود را بازمحاسبه و در سررسید/تاریخ تسویه یک رکورد اصلاحی
    (بدهکار/بستانکار) تولید می‌کند.
[ ] عقود سود ثابت و قرض‌الحسنه این پیکربندی را پنهان می‌کنند.
```
`meta: story_points=13 | priority=P1 | deps=US-02.01`

---

### US-02.03 — Fee (کارمزد) formula / فرمول کارمزد

```text
EN
As a Finance Analyst
I want to define the fee (Karmozd) formula for a product
So that products (including Qard al-Hasan) can charge fees correctly

Acceptance Criteria:
[ ] Fee can be a flat amount or a formula over principal/tenor.
[ ] Fee timing is configurable: upfront, per-installment, or at maturity.
[ ] For Qard al-Hasan the fee is the only charge (no profit).
[ ] Fee result is rounded to nearest 10 Rial.
```
```text
FA
به‌عنوان کارشناس مالی
می‌خواهم فرمول کارمزد محصول را تعریف کنم
تا محصولات (از جمله قرض‌الحسنه) کارمزد را درست دریافت کنند

معیار پذیرش:
[ ] کارمزد می‌تواند مبلغ ثابت یا فرمولی بر اساس اصل/مدت باشد.
[ ] زمان دریافت کارمزد قابل‌تنظیم است: ابتدای کار، هر قسط، یا سررسید.
[ ] برای قرض‌الحسنه، کارمزد تنها مبلغ دریافتی است (بدون سود).
[ ] نتیجه کارمزد به نزدیک‌ترین ۱۰ ریال گرد می‌شود.
```
`meta: story_points=8 | priority=P0 | deps=US-02.01`

---

### US-02.04 — Late penalty (جریمه دیرکرد) formula / فرمول جریمه دیرکرد

```text
EN
As a Finance Analyst
I want to define the late-payment penalty rule
So that overdue installments accrue penalties per bank policy

Acceptance Criteria:
[ ] Penalty base (overdue principal vs full installment) is selectable.
[ ] Penalty is a configurable rate applied per overdue day or month.
[ ] A grace window (days) before penalty starts is configurable.
[ ] Penalty result is rounded to nearest 10 Rial and stored per installment.
[ ] A regulatory ceiling parameter caps the maximum penalty.
```
```text
FA
به‌عنوان کارشناس مالی
می‌خواهم قانون جریمه دیرکرد را تعریف کنم
تا اقساط معوق طبق سیاست بانک جریمه بگیرند

معیار پذیرش:
[ ] مبنای جریمه (اصلِ معوق یا کل قسط) قابل‌انتخاب است.
[ ] جریمه نرخی قابل‌تنظیم است که به ازای هر روز/ماه دیرکرد اعمال می‌شود.
[ ] پنجره اغماض (روز) پیش از شروع جریمه قابل‌تنظیم است.
[ ] نتیجه جریمه به ۱۰ ریال گرد و per-installment ذخیره می‌شود.
[ ] پارامتر سقف قانونی، حداکثر جریمه را محدود می‌کند.
```
`meta: story_points=8 | priority=P0 | deps=US-02.01`

---

### US-02.05 — Early settlement (تسویه زودتر از موعد) / تسویه زودتر از موعد

```text
EN
As a Loan Officer
I want to compute an early-settlement payoff amount
So that a customer can repay before maturity with a correct figure

Acceptance Criteria:
[ ] Payoff = remaining principal + accrued profit/fee up to settlement date
    − profit rebate per the configured rebate rule.
[ ] Rebate rule is an editable formula (e.g. unearned-profit discount).
[ ] Result is rounded to nearest 10 Rial and itemized for the customer.
[ ] Works for fixed and variable contracts (variable triggers settlement run).
```
```text
FA
به‌عنوان کارشناس وام
می‌خواهم مبلغ تسویه زودتر از موعد را محاسبه کنم
تا مشتری بتواند پیش از سررسید با رقم درست بازپرداخت کند

معیار پذیرش:
[ ] مبلغ تسویه = اصل باقی‌مانده + سود/کارمزد تعهدشده تا تاریخ تسویه − تخفیف سود
    طبق قانون تخفیف پیکربندی‌شده.
[ ] قانون تخفیف یک فرمول قابل‌ویرایش است (مثل تخفیف سود کسب‌نشده).
[ ] نتیجه به ۱۰ ریال گرد و برای مشتری ریز می‌شود.
[ ] برای عقود ثابت و متغیر کار می‌کند (متغیر، اجرای تسویه را تریگر می‌کند).
```
`meta: story_points=8 | priority=P0 | deps=US-02.01,US-03.06`

---

### US-02.06 — Rounding policy (nearest 10 Rial) / سیاست گرد کردن (۱۰ ریال)

```text
EN
As a Finance Analyst
I want every monetary result rounded to the nearest 10 Rial
So that figures match the bank's accounting policy

Acceptance Criteria:
[ ] A single Rounding strategy rounds all engine outputs to nearest 10 Rial.
[ ] Rounding residual across installments is reconciled into the last one.
[ ] Σ(principal shares) == loan principal exactly after reconciliation.
[ ] Rounding mode is centrally configured (no per-formula override).
```
```text
FA
به‌عنوان کارشناس مالی
می‌خواهم هر نتیجه پولی به نزدیک‌ترین ۱۰ ریال گرد شود
تا ارقام با سیاست حسابداری بانک منطبق باشند

معیار پذیرش:
[ ] یک استراتژی واحد گرد کردن، همه خروجی‌های موتور را به ۱۰ ریال گرد می‌کند.
[ ] باقی‌مانده گرد کردن بین اقساط در آخرین قسط تطبیق می‌یابد.
[ ] پس از تطبیق، جمع سهم اصل دقیقاً برابر اصل وام می‌شود.
[ ] حالت گرد کردن به‌صورت مرکزی پیکربندی می‌شود (بدون override هر فرمول).
```
`meta: story_points=5 | priority=P0 | deps=US-02.01`

---

### US-02.07 — Test / preview calculation / محاسبه آزمایشی

```text
EN
As a Finance Analyst
I want to test a formula with sample inputs before saving
So that I can verify correctness without affecting real loans

Acceptance Criteria:
[ ] Input sample principal, tenor, rate; output shows full breakdown.
[ ] Test runs against the draft formula, never persisting any loan data.
[ ] Result clearly labels: profit, fee, penalty (if applicable), rounded.
[ ] Errors in the formula are shown in the test panel, not on save only.
```
```text
FA
به‌عنوان کارشناس مالی
می‌خواهم فرمول را پیش از ذخیره با ورودی نمونه تست کنم
تا بدون اثر روی وام‌های واقعی، درستی را بسنجم

معیار پذیرش:
[ ] ورود اصل/مدت/نرخ نمونه؛ خروجی، تفکیک کامل را نشان می‌دهد.
[ ] تست روی فرمول draft اجرا می‌شود و هیچ داده وامی ذخیره نمی‌کند.
[ ] نتیجه به‌وضوح برچسب می‌خورد: سود، کارمزد، جریمه (در صورت وجود)، گرد‌شده.
[ ] خطاهای فرمول در پنل تست نمایش داده می‌شوند، نه فقط هنگام ذخیره.
```
`meta: story_points=5 | priority=P0 | deps=US-02.01`

---

### US-02.08 — Formula versioning & rollback / نسخه‌بندی و بازگشت فرمول

```text
EN
As a Finance Analyst
I want every formula edit versioned with rollback
So that changes are auditable and reversible

Acceptance Criteria:
[ ] Each save creates a new version; prior versions are immutable.
[ ] Loans keep the formula version they were created with (no retro-change).
[ ] I can roll back to a previous version, which creates a new version.
[ ] Version history shows editor, Jalali timestamp, and diff.
```
```text
FA
به‌عنوان کارشناس مالی
می‌خواهم هر ویرایش فرمول نسخه‌دار و قابل‌بازگشت باشد
تا تغییرات قابل‌ممیزی و برگشت‌پذیر باشند

معیار پذیرش:
[ ] هر ذخیره یک نسخه جدید می‌سازد؛ نسخه‌های قبلی تغییرناپذیرند.
[ ] هر وام، نسخه فرمولی را که با آن ساخته شده نگه می‌دارد (بدون تغییر گذشته‌نگر).
[ ] امکان بازگشت به نسخه قبلی وجود دارد که خود نسخه جدیدی می‌سازد.
[ ] تاریخچه نسخه، ویرایش‌گر، تاریخ شمسی و diff را نشان می‌دهد.
```
`meta: story_points=8 | priority=P1 | deps=US-02.01,EPIC-08`

---

# EPIC-03 — Amortization & Installment Engine / موتور استهلاک و اقساط

### US-03.01 — Equal installments / اقساط مساوی

```text
EN
As a Loan Officer
I want an equal-installment schedule
So that the customer pays a constant amount each period

Acceptance Criteria:
[ ] Each installment total is equal (except last for rounding residual).
[ ] Principal/profit split changes per period; sum of principal == loan amount.
[ ] Honors the product's payment interval and grace settings.
```
```text
FA
به‌عنوان کارشناس وام
می‌خواهم جدول اقساط مساوی داشته باشم
تا مشتری هر دوره مبلغ ثابتی بپردازد

معیار پذیرش:
[ ] جمع هر قسط مساوی است (به‌جز آخرین قسط برای باقی‌مانده گرد کردن).
[ ] تفکیک اصل/سود هر دوره تغییر می‌کند؛ جمع اصل = مبلغ وام.
[ ] بازه پرداخت و تنظیمات تنفس محصول رعایت می‌شود.
```
`meta: story_points=8 | priority=P0 | deps=US-02.01`

---

### US-03.02 — Stepped installments / اقساط پلکانی

```text
EN
As a Loan Officer
I want a stepped (increasing/decreasing) installment schedule
So that repayment matches a customer's expected cash-flow

Acceptance Criteria:
[ ] Steps defined as percentage or fixed delta per N periods.
[ ] Schedule still fully amortizes principal by the final installment.
[ ] Step configuration comes from the product, not hard-coded.
```
```text
FA
به‌عنوان کارشناس وام
می‌خواهم جدول اقساط پلکانی (صعودی/نزولی) داشته باشم
تا بازپرداخت با جریان نقدی موردانتظار مشتری بخواند

معیار پذیرش:
[ ] پله‌ها به‌صورت درصد یا دلتای ثابت به ازای هر N دوره تعریف می‌شوند.
[ ] جدول همچنان اصل را تا قسط آخر کاملاً مستهلک می‌کند.
[ ] پیکربندی پله از محصول می‌آید، نه hard-code.
```
`meta: story_points=8 | priority=P0 | deps=US-03.01`

---

### US-03.03 — Balloon payment / پرداخت بالنی

```text
EN
As a Loan Officer
I want a balloon-payment schedule
So that a large final payment lowers periodic installments

Acceptance Criteria:
[ ] Balloon amount/percentage is configured at product level.
[ ] Periodic installments are reduced; balloon falls on the last period.
[ ] Sum of all payments still equals principal + profit + fees, rounded.
```
```text
FA
به‌عنوان کارشناس وام
می‌خواهم جدول پرداخت بالنی داشته باشم
تا یک پرداخت بزرگ پایانی، اقساط دوره‌ای را کاهش دهد

معیار پذیرش:
[ ] مبلغ/درصد بالن در سطح محصول پیکربندی می‌شود.
[ ] اقساط دوره‌ای کاهش می‌یابند؛ بالن در دوره آخر قرار می‌گیرد.
[ ] جمع همه پرداخت‌ها همچنان برابر اصل + سود + کارمزد (گرد‌شده) است.
```
`meta: story_points=5 | priority=P1 | deps=US-03.01`

---

### US-03.04 — Grace period / دوره تنفس

```text
EN
As a Loan Officer
I want to apply a grace period to a schedule
So that the customer delays principal (and/or profit) for N periods

Acceptance Criteria:
[ ] Grace mode: principal-only deferral or full deferral, configurable.
[ ] During grace, accrued profit is either capitalized or billed, per config.
[ ] First real installment due-date shifts by the grace length (Jalali).
```
```text
FA
به‌عنوان کارشناس وام
می‌خواهم دوره تنفس را روی جدول اعمال کنم
تا مشتری اصل (و/یا سود) را برای N دوره به تعویق اندازد

معیار پذیرش:
[ ] حالت تنفس: تعویق فقط اصل یا تعویق کامل، قابل‌تنظیم.
[ ] در دوره تنفس، سود تعهدشده طبق پیکربندی یا سرمایه‌ای می‌شود یا صورتحساب می‌شود.
[ ] سررسید اولین قسط واقعی به اندازه طول تنفس جابه‌جا می‌شود (شمسی).
```
`meta: story_points=8 | priority=P0 | deps=US-03.01`

---

### US-03.05 — Payment interval frequencies / تناوب بازه پرداخت

```text
EN
As a Loan Officer
I want schedules generated at the product's interval
So that monthly/quarterly/annual products are all supported

Acceptance Criteria:
[ ] Interval drives the number of installments and due-date stepping.
[ ] Jalali due-dates respect month-length and leap-year rules.
[ ] Custom day-count intervals are supported.
```
```text
FA
به‌عنوان کارشناس وام
می‌خواهم جدول‌ها با تناوب تعریف‌شده محصول ساخته شوند
تا محصولات ماهانه/فصلی/سالانه پشتیبانی شوند

معیار پذیرش:
[ ] تناوب، تعداد اقساط و گام سررسیدها را تعیین می‌کند.
[ ] سررسیدهای شمسی، طول ماه و قواعد سال کبیسه را رعایت می‌کنند.
[ ] تناوب با تعداد روز دلخواه پشتیبانی می‌شود.
```
`meta: story_points=5 | priority=P0 | deps=US-03.01`

---

### US-03.06 — Generate full schedule (split + Jalali) / تولید جدول کامل

```text
EN
As a Loan Officer
I want a complete amortization schedule before approval
So that the customer sees a transparent breakdown

Acceptance Criteria:
[ ] Per installment: no, due-date (Jalali), principal, profit, fee,
    total, remaining balance — all IRR with separators.
[ ] Generation for up to 360 installments completes in < 500 ms.
[ ] Σ principal == loan principal; Σ totals == principal+profit+fee (rounded).
[ ] Schedule is read-only and tied to the product/formula versions used.
```
```text
FA
به‌عنوان کارشناس وام
می‌خواهم پیش از تأیید، جدول استهلاک کامل داشته باشم
تا مشتری تفکیک شفاف را ببیند

معیار پذیرش:
[ ] هر قسط: شماره، سررسید (شمسی)، اصل، سود، کارمزد، جمع، مانده —
    همه ریال با جداکننده.
[ ] تولید تا ۳۶۰ قسط در کمتر از ۵۰۰ میلی‌ثانیه انجام می‌شود.
[ ] جمع اصل = اصل وام؛ جمع کل = اصل+سود+کارمزد (گرد‌شده).
[ ] جدول فقط‌خواندنی و متصل به نسخه‌های محصول/فرمول مصرفی است.
```
`meta: story_points=8 | priority=P0 | deps=US-02.06`

---

### US-03.07 — Principal-sum invariant & reconciliation / تطبیق و تضمین جمع اصل

```text
EN
As a Tech Lead
I want a hard invariant on schedule totals
So that rounding never breaks the loan's financial integrity

Acceptance Criteria:
[ ] After rounding, Σ principal == loan principal is enforced (assert/test).
[ ] Reconciliation pushes residual to the last installment only.
[ ] A unit + integration test proves the invariant across all installment types.
```
```text
FA
به‌عنوان Tech Lead
می‌خواهم یک ثابت سخت روی جمع‌های جدول داشته باشم
تا گرد کردن هرگز یکپارچگی مالی وام را نشکند

معیار پذیرش:
[ ] پس از گرد کردن، تساوی جمع اصل با اصل وام تضمین می‌شود (assert/test).
[ ] تطبیق، باقی‌مانده را فقط به آخرین قسط منتقل می‌کند.
[ ] تست واحد + یکپارچه، این ثابت را برای همه انواع اقساط اثبات می‌کند.
```
`meta: story_points=5 | priority=P0 | deps=US-03.06`

---

### US-03.08 — Export / print schedule / خروجی و چاپ جدول

```text
EN
As a Loan Officer
I want to export/print the schedule
So that the customer receives a copy before signing

Acceptance Criteria:
[ ] Export to PDF preserving Jalali dates and IRR separators (RTL layout).
[ ] Printed header shows product, amount, tenor, contract type.
[ ] Export contains only finalized, read-only schedule data.
```
```text
FA
به‌عنوان کارشناس وام
می‌خواهم جدول را خروجی/چاپ بگیرم
تا مشتری پیش از امضا یک نسخه دریافت کند

معیار پذیرش:
[ ] خروجی PDF با حفظ تاریخ شمسی و جداکننده ریال (چیدمان RTL).
[ ] سربرگ چاپ، محصول، مبلغ، مدت و نوع عقد را نشان می‌دهد.
[ ] خروجی فقط داده نهایی و فقط‌خواندنی جدول را دارد.
```
`meta: story_points=5 | priority=P1 | deps=US-03.06`

---

# EPIC-04 — Eligibility & Risk Rule Engine / موتور اعتبارسنجی و ریسک

### US-04.01 — Build eligibility conditions / ساخت شروط اعتبارسنجی

```text
EN
As a Risk Officer
I want to build eligibility conditions over customer fields
So that credit policy is enforced per product

Acceptance Criteria:
[ ] Condition = field + operator (>=,<=,==,!=,in,between) + value.
[ ] Available fields include base + dynamic customer fields (EPIC-05).
[ ] Type-safe: numeric ops only on numeric fields, etc.
[ ] Each rule set is bound to a product (or product+customer-type).
```
```text
FA
به‌عنوان کارشناس ریسک
می‌خواهم شروط اعتبارسنجی روی فیلدهای مشتری بسازم
تا سیاست اعتباری per-product اعمال شود

معیار پذیرش:
[ ] شرط = فیلد + عملگر (>=,<=,==,!=,in,between) + مقدار.
[ ] فیلدهای در دسترس شامل پایه + فیلدهای داینامیک مشتری (EPIC-05).
[ ] نوع‌امن: عملگر عددی فقط روی فیلد عددی و … .
[ ] هر مجموعه‌قانون به یک محصول (یا محصول+نوع‌مشتری) متصل است.
```
`meta: story_points=13 | priority=P0 | deps=US-05.03`

---

### US-04.02 — AND/OR rule groups / گروه‌بندی AND/OR

```text
EN
As a Risk Officer
I want to compose conditions into AND/OR groups
So that complex policies are expressible without code

Acceptance Criteria:
[ ] Nested groups with AND/OR are supported and visually clear.
[ ] Evaluation order/precedence is deterministic and documented.
[ ] An empty group is invalid and blocks activation.
```
```text
FA
به‌عنوان کارشناس ریسک
می‌خواهم شروط را در گروه‌های AND/OR ترکیب کنم
تا سیاست‌های پیچیده بدون کد بیان شوند

معیار پذیرش:
[ ] گروه‌های تودرتو با AND/OR پشتیبانی و به‌صورت بصری شفاف‌اند.
[ ] ترتیب/تقدم ارزیابی قطعی و مستند است.
[ ] گروه خالی نامعتبر است و فعال‌سازی را مسدود می‌کند.
```
`meta: story_points=8 | priority=P0 | deps=US-04.01`

---

### US-04.03 — Guarantor requirements / الزامات ضامن

```text
EN
As a Risk Officer
I want to require a number of qualifying guarantors
So that risk is mitigated per product policy

Acceptance Criteria:
[ ] Required guarantor count is configurable per product/customer-type.
[ ] Optional guarantor-side conditions (e.g. min income) are supported.
[ ] Evaluation fails with a clear reason if guarantors are insufficient.
```
```text
FA
به‌عنوان کارشناس ریسک
می‌خواهم تعداد ضامن واجد شرایط را الزامی کنم
تا ریسک طبق سیاست محصول کاهش یابد

معیار پذیرش:
[ ] تعداد ضامن لازم per-product/customer-type قابل‌تنظیم است.
[ ] شروط سمت ضامن (مثل حداقل درآمد) به‌صورت اختیاری پشتیبانی می‌شوند.
[ ] اگر ضامن کافی نباشد، ارزیابی با دلیل شفاف رد می‌شود.
```
`meta: story_points=8 | priority=P1 | deps=US-04.01`

---

### US-04.04 — Installment-to-income cap / سقف نسبت قسط به درآمد

```text
EN
As a Risk Officer
I want to cap the installment-to-income ratio
So that customers are not over-leveraged

Acceptance Criteria:
[ ] Ratio cap (e.g. <= 30%) is configurable per product.
[ ] Computed installment (from EPIC-03) is checked against income field.
[ ] Failure returns the computed ratio and the cap in the reason.
```
```text
FA
به‌عنوان کارشناس ریسک
می‌خواهم نسبت قسط به درآمد را محدود کنم
تا مشتری بیش از توان بدهکار نشود

معیار پذیرش:
[ ] سقف نسبت (مثلاً ≤ ۳۰٪) per-product قابل‌تنظیم است.
[ ] قسط محاسبه‌شده (از EPIC-03) با فیلد درآمد سنجیده می‌شود.
[ ] در صورت رد، نسبت محاسبه‌شده و سقف در دلیل برگردانده می‌شود.
```
`meta: story_points=5 | priority=P0 | deps=US-04.01,US-03.06`

---

### US-04.05 — Per-product & per-customer-type conditions / شروط per-product و per-customer

```text
EN
As a Risk Officer
I want different rule sets per product and customer type
So that policy can vary (e.g. employee vs general customer)

Acceptance Criteria:
[ ] A product can hold multiple rule sets keyed by customer type.
[ ] The engine selects the rule set matching the applicant's type.
[ ] If no specific set exists, a default product rule set applies.
```
```text
FA
به‌عنوان کارشناس ریسک
می‌خواهم مجموعه‌قوانین متفاوت per-product و per-customer-type داشته باشم
تا سیاست متغیر باشد (مثلاً کارمند در برابر مشتری عادی)

معیار پذیرش:
[ ] یک محصول می‌تواند چند مجموعه‌قانون با کلید نوع‌مشتری داشته باشد.
[ ] موتور، مجموعه‌قانون منطبق با نوع متقاضی را انتخاب می‌کند.
[ ] اگر مجموعه اختصاصی نبود، مجموعه پیش‌فرض محصول اعمال می‌شود.
```
`meta: story_points=8 | priority=P0 | deps=US-04.01`

---

### US-04.06 — Evaluate customer → eligible + reasons / ارزیابی مشتری و دلایل

```text
EN
As a Loan Officer
I want to evaluate an applicant against a product
So that I get an eligible/rejected result with reasons

Acceptance Criteria:
[ ] Result = eligible(bool) + list of failed conditions with messages.
[ ] Each failure shows expected vs actual value, in Persian.
[ ] Evaluation is logged with inputs snapshot for audit.
[ ] Evaluation uses currently active rule version (runtime).
```
```text
FA
به‌عنوان کارشناس وام
می‌خواهم متقاضی را در برابر یک محصول ارزیابی کنم
تا نتیجه واجد/غیرواجد شرایط با دلایل بگیرم

معیار پذیرش:
[ ] نتیجه = eligible(بولین) + فهرست شروط ردشده با پیام.
[ ] هر رد، مقدار موردانتظار در برابر واقعی را به فارسی نشان می‌دهد.
[ ] ارزیابی با snapshot ورودی برای ممیزی ثبت می‌شود.
[ ] ارزیابی از نسخه فعال فعلی قانون (runtime) استفاده می‌کند.
```
`meta: story_points=8 | priority=P0 | deps=US-04.01`

---

### US-04.07 — Rule versioning & runtime apply / نسخه‌بندی و اعمال در لحظه

```text
EN
As a Risk Officer
I want rule changes versioned and applied at runtime
So that policy changes need no software release

Acceptance Criteria:
[ ] Activating a new rule version applies to new evaluations immediately.
[ ] In-flight loans keep the rule version they were approved under.
[ ] Rollback to a previous version is possible and audited.
```
```text
FA
به‌عنوان کارشناس ریسک
می‌خواهم تغییر قوانین نسخه‌دار و در لحظه اعمال شود
تا تغییر سیاست نیازی به انتشار نسخه نرم‌افزار نداشته باشد

معیار پذیرش:
[ ] فعال‌سازی نسخه جدید قانون، فوراً روی ارزیابی‌های جدید اعمال می‌شود.
[ ] وام‌های در جریان، نسخه قانونی را که با آن تأیید شده‌اند نگه می‌دارند.
[ ] بازگشت به نسخه قبلی ممکن و ممیزی‌شده است.
```
`meta: story_points=5 | priority=P1 | deps=US-04.01,EPIC-08`

---

# EPIC-05 — Dynamic Customer Master Data / اطلاعات پایه داینامیک مشتری

### US-05.01 — Define local custom field / تعریف فیلد داینامیک محلی

```text
EN
As a Product Manager
I want to define custom fields on the customer master
So that risk rules can use data we didn't model upfront

Acceptance Criteria:
[ ] Field attrs: key, label(fa), type(number/text/date/enum/bool),
    required, validation(min/max/regex/enum-values).
[ ] New field is immediately available in the rule builder.
[ ] Existing customers default to null for the new field.
[ ] Key is immutable after creation; label is editable.
```
```text
FA
به‌عنوان مدیر محصول
می‌خواهم فیلدهای داینامیک روی پایه مشتری تعریف کنم
تا قوانین ریسک از داده‌هایی که از قبل مدل نشده‌اند استفاده کنند

معیار پذیرش:
[ ] ویژگی فیلد: کلید، برچسب(فارسی)، نوع(عدد/متن/تاریخ/فهرست/بولین)،
    اجباری، اعتبارسنجی(min/max/regex/مقادیر-فهرست).
[ ] فیلد جدید بلافاصله در rule builder در دسترس است.
[ ] مشتریان موجود برای فیلد جدید مقدار null می‌گیرند.
[ ] کلید پس از ایجاد تغییرناپذیر است؛ برچسب قابل‌ویرایش است.
```
`meta: story_points=8 | priority=P1 | deps=none`

---

### US-05.02 — Web-service (استعلام) sourced field / فیلد دریافتی از وب‌سرویس

```text
EN
As a Product Manager
I want some customer fields populated from a bank inquiry web service
So that authoritative data (e.g. credit score) comes from the source

Acceptance Criteria:
[ ] A field can be marked source=inquiry and mapped to a service response path.
[ ] Inquiry fields are read-only in the UI (populated by the service).
[ ] Field stores last-fetched value + fetch timestamp (Jalali) + status.
[ ] If the service is unavailable, the field shows a stale/unknown state,
    handled by EPIC-09 resilience.
```
```text
FA
به‌عنوان مدیر محصول
می‌خواهم برخی فیلدهای مشتری از وب‌سرویس استعلام بانک پر شوند
تا داده مرجع (مثل رتبه اعتباری) از منبع اصلی بیاید

معیار پذیرش:
[ ] یک فیلد می‌تواند source=inquiry علامت بخورد و به مسیر پاسخ سرویس map شود.
[ ] فیلدهای استعلامی در UI فقط‌خواندنی‌اند (با سرویس پر می‌شوند).
[ ] فیلد، آخرین مقدار دریافتی + زمان دریافت (شمسی) + وضعیت را ذخیره می‌کند.
[ ] اگر سرویس در دسترس نباشد، فیلد حالت قدیمی/نامشخص نشان می‌دهد که
    توسط تاب‌آوری EPIC-09 مدیریت می‌شود.
```
`meta: story_points=8 | priority=P1 | deps=EPIC-09`

---

### US-05.03 — Field available in rule builder / در دسترس بودن فیلد در سازنده قانون

```text
EN
As a Risk Officer
I want every defined field selectable in the rule builder
So that I can write rules over both local and inquiry data

Acceptance Criteria:
[ ] Both local and inquiry fields appear in the field picker with type.
[ ] Picker shows the field label(fa) and source badge (local/inquiry).
[ ] Type drives the allowed operators offered for that field.
```
```text
FA
به‌عنوان کارشناس ریسک
می‌خواهم هر فیلد تعریف‌شده در سازنده قانون قابل‌انتخاب باشد
تا روی هر دو داده محلی و استعلامی قانون بنویسم

معیار پذیرش:
[ ] هر دو فیلد محلی و استعلامی با نوعشان در انتخابگر فیلد ظاهر می‌شوند.
[ ] انتخابگر، برچسب فارسی و نشان منبع (محلی/استعلامی) را نمایش می‌دهد.
[ ] نوع فیلد، عملگرهای مجاز پیشنهادی را تعیین می‌کند.
```
`meta: story_points=3 | priority=P1 | deps=US-05.01`

---

### US-05.04 — Referential integrity / یکپارچگی ارجاعی فیلد

```text
EN
As a Tech Lead
I want fields protected while referenced by active rules
So that deleting a field never breaks an active policy

Acceptance Criteria:
[ ] Deleting/disabling a field referenced by an active rule is blocked.
[ ] The block message lists the products/rule sets that use the field.
[ ] A field can be soft-disabled only after all references are removed.
```
```text
FA
به‌عنوان Tech Lead
می‌خواهم فیلدهای ارجاع‌شده توسط قوانین فعال محافظت شوند
تا حذف فیلد هرگز یک سیاست فعال را نشکند

معیار پذیرش:
[ ] حذف/غیرفعال‌سازی فیلدی که قانون فعال به آن ارجاع دارد مسدود می‌شود.
[ ] پیام مسدودسازی، محصولات/مجموعه‌قوانین استفاده‌کننده را فهرست می‌کند.
[ ] فیلد فقط پس از حذف همه ارجاع‌ها قابل‌غیرفعال‌سازی نرم است.
```
`meta: story_points=5 | priority=P1 | deps=US-05.01,US-04.01`

---

### US-05.05 — Inquiry refresh & cache / تازه‌سازی و کش استعلام

```text
EN
As a Loan Officer
I want to refresh inquiry data for a customer
So that eligibility uses current authoritative values

Acceptance Criteria:
[ ] A manual "refresh inquiry" action re-fetches and updates fields.
[ ] Cached values have a configurable TTL; stale data is flagged in UI.
[ ] A refresh failure does not overwrite a previously good value.
```
```text
FA
به‌عنوان کارشناس وام
می‌خواهم داده استعلامی یک مشتری را تازه‌سازی کنم
تا اعتبارسنجی از مقادیر مرجع روز استفاده کند

معیار پذیرش:
[ ] اکشن دستی «تازه‌سازی استعلام» فیلدها را مجدد دریافت و به‌روز می‌کند.
[ ] مقادیر کش‌شده TTL قابل‌تنظیم دارند؛ داده قدیمی در UI علامت می‌خورد.
[ ] شکست تازه‌سازی، مقدار خوب قبلی را بازنویسی نمی‌کند.
```
`meta: story_points=5 | priority=P2 | deps=US-05.02,EPIC-09`

---

# EPIC-06 — Loan Lifecycle Management / مدیریت چرخه حیات وام

### US-06.01 — Create loan / ایجاد وام

```text
EN
As a Loan Officer
I want to create a loan from an active product for a customer
So that an approved application becomes a managed loan

Acceptance Criteria:
[ ] Loan inherits the active product/formula/rule versions (snapshotted).
[ ] Amount and tenor must fall within product ranges.
[ ] Eligibility must be passed before a loan can be created.
[ ] Loan starts in status=draft and stores a generated amortization schedule.
```
```text
FA
به‌عنوان کارشناس وام
می‌خواهم برای یک مشتری از محصول فعال یک وام ایجاد کنم
تا یک درخواست تأییدشده به وام تحت‌مدیریت تبدیل شود

معیار پذیرش:
[ ] وام، نسخه‌های فعال محصول/فرمول/قانون را به‌ارث می‌برد (snapshot).
[ ] مبلغ و مدت باید در بازه‌های محصول باشند.
[ ] پیش از ایجاد وام باید اعتبارسنجی پاس شده باشد.
[ ] وام در وضعیت draft شروع می‌شود و جدول استهلاک تولیدشده را ذخیره می‌کند.
```
`meta: story_points=8 | priority=P0 | deps=US-01.05,US-03.06,US-04.06`

---

### US-06.02 — Loan status state machine / ماشین وضعیت وام

```text
EN
As a Tech Lead
I want a well-defined loan status state machine
So that transitions are valid, auditable, and not ad-hoc

Acceptance Criteria:
[ ] States: draft → approved → active → (overdue ⇄ active) →
    settled | defaulted | early_settled.
[ ] Only allowed transitions are permitted; illegal transitions are rejected.
[ ] Every transition records actor, Jalali timestamp, and reason.
[ ] State machine is config-aware but enforced in code (Factory/Strategy).
```
```text
FA
به‌عنوان Tech Lead
می‌خواهم ماشین وضعیت وام به‌خوبی تعریف‌شده باشد
تا گذارها معتبر، قابل‌ممیزی و غیر دلخواه باشند

معیار پذیرش:
[ ] وضعیت‌ها: draft → approved → active → (overdue ⇄ active) →
    settled | defaulted | early_settled.
[ ] فقط گذارهای مجاز ممکن‌اند؛ گذار غیرمجاز رد می‌شود.
[ ] هر گذار، عامل، تاریخ شمسی و دلیل را ثبت می‌کند.
[ ] ماشین وضعیت config-aware اما در کد اعمال می‌شود (Factory/Strategy).
```
`meta: story_points=8 | priority=P0 | deps=US-06.01`

---

### US-06.03 — Record installment payment / ثبت پرداخت قسط

```text
EN
As a Loan Officer
I want to record a payment against an installment
So that the loan balance and schedule status stay accurate

Acceptance Criteria:
[ ] A payment marks the installment paid (full) or partially paid.
[ ] Remaining balance and next-due are recalculated.
[ ] Overpayment handling follows a configured policy (advance vs reject).
[ ] Each payment is immutable once recorded (corrections via reversal).
```
```text
FA
به‌عنوان کارشناس وام
می‌خواهم یک پرداخت را در برابر یک قسط ثبت کنم
تا مانده وام و وضعیت جدول دقیق بماند

معیار پذیرش:
[ ] پرداخت، قسط را پرداخت‌شده (کامل) یا نیمه‌پرداخت علامت می‌زند.
[ ] مانده و سررسید بعدی بازمحاسبه می‌شوند.
[ ] مدیریت اضافه‌پرداخت طبق سیاست پیکربندی‌شده (پیش‌پرداخت یا رد) است.
[ ] هر پرداخت پس از ثبت تغییرناپذیر است (اصلاح از طریق reversal).
```
`meta: story_points=8 | priority=P0 | deps=US-06.02`

---

### US-06.04 — Late payment / دیرکرد tracking / پیگیری دیرکرد

```text
EN
As a Risk Officer
I want overdue installments tracked and penalized
So that late payments are reflected in the loan and customer risk

Acceptance Criteria:
[ ] A daily job flags installments past due-date as overdue.
[ ] Penalty accrues per US-02.04 after the configured grace window.
[ ] Loan transitions active → overdue and back when cleared.
[ ] Overdue days and accrued penalty are visible per installment.
```
```text
FA
به‌عنوان کارشناس ریسک
می‌خواهم اقساط معوق پیگیری و جریمه شوند
تا دیرکرد در وام و ریسک مشتری منعکس شود

معیار پذیرش:
[ ] یک job روزانه، اقساط گذشته از سررسید را معوق علامت می‌زند.
[ ] جریمه طبق US-02.04 پس از پنجره اغماض تعهد می‌شود.
[ ] وام از active به overdue و در صورت تسویه برمی‌گردد.
[ ] روزهای دیرکرد و جریمه تعهدشده per-installment قابل‌مشاهده‌اند.
```
`meta: story_points=8 | priority=P0 | deps=US-06.03,US-02.04`

---

### US-06.05 — Default / نکول status / وضعیت نکول

```text
EN
As a Risk Officer
I want loans automatically classified as defaulted per policy
So that NPL status is consistent and auditable

Acceptance Criteria:
[ ] Default threshold (e.g. N consecutive overdue installments or X days)
    is configurable, not hard-coded.
[ ] Crossing the threshold transitions the loan to status=defaulted.
[ ] Default classification records the triggering rule and Jalali date.
[ ] A defaulted loan can be cured back to active per a defined transition.
```
```text
FA
به‌عنوان کارشناس ریسک
می‌خواهم وام‌ها طبق سیاست به‌صورت خودکار نکول طبقه‌بندی شوند
تا وضعیت معوق/نکول یکدست و قابل‌ممیزی باشد

معیار پذیرش:
[ ] آستانه نکول (مثلاً N قسط معوق متوالی یا X روز) قابل‌تنظیم است، نه hard-code.
[ ] عبور از آستانه، وام را به وضعیت defaulted منتقل می‌کند.
[ ] طبقه‌بندی نکول، قانون محرک و تاریخ شمسی را ثبت می‌کند.
[ ] وام نکول‌شده طبق گذار تعریف‌شده می‌تواند به active برگردد (cure).
```
`meta: story_points=8 | priority=P0 | deps=US-06.04`

---

### US-06.06 — Early settlement execution / اجرای تسویه زودتر از موعد

```text
EN
As a Loan Officer
I want to execute an early settlement on a loan
So that the loan closes with the correct payoff figure

Acceptance Criteria:
[ ] Payoff is computed via US-02.05 and shown itemized before confirm.
[ ] On confirm, loan transitions to status=early_settled and closes the schedule.
[ ] For variable contracts a settlement run is executed first.
[ ] The settlement is recorded immutably with breakdown and Jalali date.
```
```text
FA
به‌عنوان کارشناس وام
می‌خواهم تسویه زودتر از موعد یک وام را اجرا کنم
تا وام با رقم تسویه درست بسته شود

معیار پذیرش:
[ ] مبلغ تسویه با US-02.05 محاسبه و پیش از تأیید ریز نمایش داده می‌شود.
[ ] با تأیید، وام به وضعیت early_settled می‌رود و جدول بسته می‌شود.
[ ] برای عقود متغیر، ابتدا اجرای تسویه قطعی انجام می‌شود.
[ ] تسویه به‌صورت تغییرناپذیر با تفکیک و تاریخ شمسی ثبت می‌شود.
```
`meta: story_points=8 | priority=P0 | deps=US-02.05,US-06.02`

---

### US-06.07 — Penalty accrual job / فرآیند تعهد جریمه

```text
EN
As a Tech Lead
I want a scheduled job that accrues penalties idempotently
So that penalties are correct even if the job reruns

Acceptance Criteria:
[ ] Job is idempotent: rerunning for the same date does not double-charge.
[ ] Accrual uses each loan's snapshotted penalty formula version.
[ ] Job run produces an auditable summary (loans touched, total accrued).
[ ] Failures are retryable without data corruption.
```
```text
FA
به‌عنوان Tech Lead
می‌خواهم یک job زمان‌بندی‌شده جریمه‌ها را به‌صورت idempotent تعهد کند
تا حتی با اجرای دوباره، جریمه‌ها درست بمانند

معیار پذیرش:
[ ] job مستقل از تکرار است: اجرای دوباره برای یک تاریخ، دوبار شارژ نمی‌کند.
[ ] تعهد از نسخه snapshot فرمول جریمه هر وام استفاده می‌کند.
[ ] اجرای job یک خلاصه قابل‌ممیزی تولید می‌کند (تعداد وام، جمع تعهد).
[ ] خطاها بدون خرابی داده قابل‌retry هستند.
```
`meta: story_points=5 | priority=P1 | deps=US-06.04`

---

# EPIC-07 — Zero-Code Admin Configuration Panel / پنل پیکربندی بدون کد

### US-07.01 — Role-aware dashboard / داشبورد آگاه به نقش

```text
EN
As any authorized user
I want a dashboard showing only the modules my role can access
So that the panel is focused and least-privilege

Acceptance Criteria:
[ ] Dashboard renders modules filtered by the user's permissions.
[ ] Persian, RTL layout using the approved color palette.
[ ] Unauthorized deep-links return a clear "no access" page, not a crash.
```
```text
FA
به‌عنوان هر کاربر مجاز
می‌خواهم داشبوردی ببینم که فقط ماژول‌های مجاز نقش من را نشان دهد
تا پنل متمرکز و کم‌دسترسی باشد

معیار پذیرش:
[ ] داشبورد، ماژول‌ها را بر اساس دسترسی کاربر فیلتر و رندر می‌کند.
[ ] چیدمان فارسی و RTL با پالت رنگ مصوب.
[ ] لینک مستقیم غیرمجاز، صفحه «عدم دسترسی» شفاف می‌دهد، نه crash.
```
`meta: story_points=5 | priority=P1 | deps=EPIC-10`

---

### US-07.02 — Runtime config publish / انتشار پیکربندی در لحظه

```text
EN
As a Product Manager
I want publishing any configuration to take effect at runtime
So that the Zero-Code promise holds with no release

Acceptance Criteria:
[ ] Publishing product/formula/rule changes is effective without redeploy.
[ ] A confirmation step shows what will change and its impact scope.
[ ] Publishing is permission-gated and fully audited.
```
```text
FA
به‌عنوان مدیر محصول
می‌خواهم انتشار هر پیکربندی در لحظه اثرگذار باشد
تا وعده Zero-Code بدون انتشار نسخه محقق شود

معیار پذیرش:
[ ] انتشار تغییرات محصول/فرمول/قانون بدون استقرار مجدد اثر می‌کند.
[ ] یک گام تأیید، آنچه تغییر می‌کند و دامنه اثر را نشان می‌دهد.
[ ] انتشار، دسترسی‌محور و کاملاً ممیزی‌شده است.
```
`meta: story_points=8 | priority=P1 | deps=US-01.05,EPIC-08`

---

# EPIC-08 — Versioning, Audit & Governance / نسخه‌بندی، ممیزی و حاکمیت

### US-08.01 — Audit log / گزارش ممیزی

```text
EN
As an Auditor
I want a complete change log of all configuration
So that every change is traceable

Acceptance Criteria:
[ ] Log captures who, what entity, old value, new value, Jalali timestamp.
[ ] Log is append-only and not editable from the UI.
[ ] Log is filterable by entity, user, and date range.
```
```text
FA
به‌عنوان ممیز
می‌خواهم گزارش کامل تغییرات همه پیکربندی‌ها را داشته باشم
تا هر تغییر قابل‌ردگیری باشد

معیار پذیرش:
[ ] گزارش شامل: چه‌کسی، چه موجودیتی، مقدار قبلی، مقدار جدید، تاریخ شمسی.
[ ] گزارش append-only است و از UI قابل‌ویرایش نیست.
[ ] گزارش با موجودیت، کاربر و بازه تاریخ قابل‌فیلتر است.
```
`meta: story_points=8 | priority=P1 | deps=none`

---

### US-08.02 — Version rollback / بازگشت نسخه

```text
EN
As a Product Manager
I want to roll back any versioned config to a prior version
So that mistakes are quickly reversible

Acceptance Criteria:
[ ] Rollback creates a new version equal to the chosen prior one.
[ ] Rollback never mutates historical versions.
[ ] In-flight loans are unaffected by a later rollback.
```
```text
FA
به‌عنوان مدیر محصول
می‌خواهم هر پیکربندی نسخه‌دار را به نسخه قبلی برگردانم
تا اشتباهات سریع برگشت‌پذیر باشند

معیار پذیرش:
[ ] بازگشت، نسخه جدیدی برابر نسخه انتخابی می‌سازد.
[ ] بازگشت، نسخه‌های تاریخی را تغییر نمی‌دهد.
[ ] وام‌های در جریان از بازگشت بعدی متأثر نمی‌شوند.
```
`meta: story_points=5 | priority=P1 | deps=US-08.01`

---

### US-08.03 — Maker-checker approval / تأیید دوسطحی (Maker-Checker)

```text
EN
As a Compliance Officer
I want sensitive config changes to require a second approver
So that no single person can change critical financial rules alone

Acceptance Criteria:
[ ] Sensitive entities (formulas, penalty, default rules) require approval.
[ ] Maker cannot approve their own change.
[ ] Pending changes are inactive until approved; rejections are logged.
```
```text
FA
به‌عنوان کارشناس انطباق
می‌خواهم تغییرات حساس پیکربندی به تأییدکننده دوم نیاز داشته باشند
تا هیچ فرد واحدی نتواند قوانین مالی حیاتی را به‌تنهایی تغییر دهد

معیار پذیرش:
[ ] موجودیت‌های حساس (فرمول، جریمه، قوانین نکول) نیازمند تأییدند.
[ ] سازنده نمی‌تواند تغییر خودش را تأیید کند.
[ ] تغییرات معلق تا تأیید غیرفعال‌اند؛ ردها ثبت می‌شوند.
```
`meta: story_points=8 | priority=P2 | deps=US-08.01`

---

# EPIC-09 — External Inquiry (Web Service / استعلام) / استعلام بیرونی

### US-09.01 — Configure inquiry service / پیکربندی سرویس استعلام

```text
EN
As an IT Admin
I want to configure external inquiry web services
So that customer data can be fetched from authoritative sources

Acceptance Criteria:
[ ] Endpoint, auth, request/response mapping are configurable (no code).
[ ] A "test connection" validates the mapping with a sample.
[ ] Secrets are stored securely and never shown in plain text.
```
```text
FA
به‌عنوان مدیر فناوری
می‌خواهم سرویس‌های استعلام بیرونی را پیکربندی کنم
تا داده مشتری از منابع مرجع دریافت شود

معیار پذیرش:
[ ] endpoint، احراز هویت و map درخواست/پاسخ قابل‌پیکربندی‌اند (بدون کد).
[ ] «تست اتصال» map را با یک نمونه اعتبارسنجی می‌کند.
[ ] اسرار به‌صورت امن ذخیره و هرگز به‌صورت متن ساده نمایش داده نمی‌شوند.
```
`meta: story_points=8 | priority=P2 | deps=EPIC-10`

---

### US-09.02 — Credit score inquiry / استعلام رتبه اعتباری

```text
EN
As a Loan Officer
I want the customer's credit score fetched via inquiry
So that eligibility uses an authoritative score

Acceptance Criteria:
[ ] Score is fetched and mapped to a customer field (US-05.02).
[ ] The score's source and fetch time are visible.
[ ] If unavailable, the officer is informed and rules treat it as missing.
```
```text
FA
به‌عنوان کارشناس وام
می‌خواهم رتبه اعتباری مشتری از طریق استعلام دریافت شود
تا اعتبارسنجی از رتبه مرجع استفاده کند

معیار پذیرش:
[ ] رتبه دریافت و به یک فیلد مشتری map می‌شود (US-05.02).
[ ] منبع و زمان دریافت رتبه قابل‌مشاهده است.
[ ] در صورت عدم دسترس، کارشناس مطلع و قانون آن را «مفقود» تلقی می‌کند.
```
`meta: story_points=5 | priority=P2 | deps=US-09.01,US-05.02`

---

### US-09.03 — Inquiry resilience / تاب‌آوری استعلام

```text
EN
As a Tech Lead
I want inquiry calls to be resilient
So that an external outage never breaks the loan flow

Acceptance Criteria:
[ ] Timeout, retry-with-backoff, and circuit-breaker are applied.
[ ] On failure, last-known value (with staleness flag) is used if present.
[ ] Failures are logged and surfaced without exposing internal details.
```
```text
FA
به‌عنوان Tech Lead
می‌خواهم فراخوانی‌های استعلام تاب‌آور باشند
تا قطعی سرویس بیرونی هرگز جریان وام را نشکند

معیار پذیرش:
[ ] timeout، retry با backoff و circuit-breaker اعمال می‌شوند.
[ ] در صورت خطا، آخرین مقدار معلوم (با نشان قدیمی‌بودن) در صورت وجود استفاده می‌شود.
[ ] خطاها بدون افشای جزئیات داخلی ثبت و نمایش داده می‌شوند.
```
`meta: story_points=5 | priority=P2 | deps=US-09.01`

---

# EPIC-10 — AuthN/AuthZ & RBAC / احراز هویت و کنترل دسترسی

### US-10.01 — Login / ورود

```text
EN
As a user
I want to authenticate into the admin panel
So that only authorized staff can access the system

Acceptance Criteria:
[ ] Login authenticates against the bank's identity provider/integration.
[ ] Failed attempts are rate-limited and logged.
[ ] Sessions expire after a configured idle timeout.
```
```text
FA
به‌عنوان کاربر
می‌خواهم وارد پنل مدیریت شوم
تا فقط کارکنان مجاز به سیستم دسترسی داشته باشند

معیار پذیرش:
[ ] ورود در برابر هویت‌سنج/یکپارچگی بانک احراز می‌شود.
[ ] تلاش‌های ناموفق محدود و ثبت می‌شوند.
[ ] نشست‌ها پس از زمان بی‌کاری پیکربندی‌شده منقضی می‌شوند.
```
`meta: story_points=5 | priority=P1 | deps=none`

---

### US-10.02 — Role-permission matrix / ماتریس نقش-دسترسی

```text
EN
As a System Admin
I want roles mapped to fine-grained permissions
So that each team only edits what it owns

Acceptance Criteria:
[ ] Roles: ProductManager, FinanceAnalyst, RiskOfficer, LoanOfficer,
    Auditor, ComplianceOfficer, SystemAdmin.
[ ] Permissions gate formula edit, rule edit, publish, and lifecycle actions.
[ ] Changing a role's permissions is audited.
```
```text
FA
به‌عنوان مدیر سیستم
می‌خواهم نقش‌ها به دسترسی‌های ریز map شوند
تا هر تیم فقط آنچه را که مالک آن است ویرایش کند

معیار پذیرش:
[ ] نقش‌ها: مدیر محصول، کارشناس مالی، کارشناس ریسک، کارشناس وام،
    ممیز، کارشناس انطباق، مدیر سیستم.
[ ] دسترسی‌ها، ویرایش فرمول، ویرایش قانون، انتشار و اکشن‌های چرخه حیات را کنترل می‌کنند.
[ ] تغییر دسترسی یک نقش ممیزی می‌شود.
```
`meta: story_points=8 | priority=P1 | deps=US-10.01`

---

# Appendix A — Representative Tasks / پیوست الف — تسک‌های نماینده

```yaml
- id: TASK-001
  title: "Product Factory: create-wizard backend + versioned persistence"
  type: feature
  story_points: 8
  assignee: ""
  status: todo
- id: TASK-014
  title: "Calc Engine: sandboxed formula evaluator (whitelist parser)"
  type: feature
  story_points: 13
  assignee: ""
  status: todo
- id: TASK-022
  title: "Amortization: equal/stepped/balloon/grace strategies (Strategy Pattern)"
  type: feature
  story_points: 13
  assignee: ""
  status: todo
- id: TASK-031
  title: "Rule Engine: AND/OR evaluator + reasons output"
  type: feature
  story_points: 8
  assignee: ""
  status: todo
- id: TASK-040
  title: "Loan lifecycle: status state machine + transition audit"
  type: feature
  story_points: 8
  assignee: ""
  status: todo
```

# Appendix B — Git Standards (reminder) / پیوست ب — استانداردهای Git

```text
Branch:   feature/TASK-014-formula-sandbox
          bugfix/BUG-007-rounding-residual
Commit:   feat(calc): add sandboxed formula evaluator
          fix(amort): reconcile rounding residual into last installment
Types:    feat | fix | refactor | test | docs | chore
```

# Appendix C — Architecture Note / پیوست ج — یادداشت معماری

```text
EN
- Factory Pattern: ProductFactory builds product instances from config;
  EngineFactory yields the right Calculation/Installment strategy by contract.
- Strategy Pattern: ProfitStrategy, InstallmentStrategy, PenaltyStrategy,
  RoundingStrategy are swappable and selected by configuration — keeping the
  core free of hard-coded formulas (Zero-Code).
FA
- الگوی Factory: ProductFactory نمونه محصول را از روی config می‌سازد؛
  EngineFactory استراتژی محاسبه/تقسیط درست را بر اساس عقد برمی‌گرداند.
- الگوی Strategy: استراتژی‌های سود، اقساط، جریمه و گرد کردن قابل‌تعویض‌اند و
  با پیکربندی انتخاب می‌شوند تا هسته بدون فرمول hard-code بماند (Zero-Code).
```
