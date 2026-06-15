# Contributing Guide / راهنمای مشارکت

## 1. Git Standards / استانداردهای گیت

### Branch naming / نام‌گذاری شاخه
```
feature/TASK-XXX-short-description
bugfix/BUG-XXX-short-description
hotfix/TASK-XXX-short-description
```

### Commit messages / پیام کامیت
```
type(scope): short description

Types: feat | fix | refactor | test | docs | chore
Example: feat(loan): add balloon payment calculator
```

### Branching model / مدل شاخه‌بندی
- Branch from `develop` for features and bugfixes.
- Open a Pull Request into `develop`; Tech Lead reviews (Approve / Request Changes).
- Release-ready work is merged from `develop` into `main`.
- `hotfix/*` branches may be cut from `main` directly.

## 2. Scrum Workflow / فرآیند اسکرام
```
Discovery → Refinement → Sprint Planning → Design → Development
→ Code Review → QA Testing → Accepted
```

### Definition of Ready (DoR)
- [ ] User Story clear and complete
- [ ] Acceptance Criteria defined
- [ ] Mockup/wireframe approved (if UI)
- [ ] Dependencies identified
- [ ] Story Point estimated

### Definition of Done (DoD)
- [ ] Code written and code-reviewed
- [ ] Unit + integration tests written (Selenium tests if UI)
- [ ] QA test passed
- [ ] Documentation updated
- [ ] Deployed to Staging

## 3. Story / Task / Bug formats / قالب‌ها

User Story:
```
As a [role]
I want [need]
So that [reason]

Acceptance Criteria:
[ ] ...
```

Task:
```yaml
id: TASK-XXX
title: ""
type: feature | bug | chore
story_points: 1-13
assignee: ""
status: todo | in_progress | review | done
```

Bug Report:
```
ID: BUG-XXX
Title:
Severity: critical | high | medium | low
Steps to Reproduce:
Expected:
Actual:
```
