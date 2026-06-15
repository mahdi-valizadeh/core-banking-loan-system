# CLAUDE.md — Claude Code Standards

Purpose
-------
This document defines shared standards for "Claude Code" artifacts — small, focused agent/subagent configurations, prompt templates, and role-based helpers used by the team when interacting with Claude family models.

Naming & Placement
------------------
- Place Claude-related agent files under `agents/claude/`.
- Use lowercase kebab-case for filenames: `role-name.md`, `prompt-name.md`.

Prompt & Template Rules
-----------------------
- Keep prompts deterministic: include explicit instructions, expected output format, and an examples section.
- Provide a short `context` block and a `constraints` block for safety and allowed actions.
- When asking for code, include language and required entry point (e.g., `Java class X`, `Python: function compute()`).

Versioning
----------
- Update file header with `version: YYYY.MM.DD` when making semantic changes to prompt intent.

Testing & Validation
--------------------
- Add an `examples/` section with at least 2 test cases per prompt showing input → expected output.
- Store unit test cases under `agents/claude/tests/` as small JSON fixtures when practical.

Review Process
--------------
- New or changed Claude artifacts must be reviewed by a peer before merging to `develop`.
- Use the commit message prefix `docs(claude):` for documentation changes and `chore(agents):` for agent orchestration changes.

Security & Privacy
------------------
- Never embed secrets, API keys, or personal data in prompts or agent files.
- Prefer placeholders and instruct engineers to inject secrets at runtime.

Example file layout
-------------------
```
agents/
  claude/
    roles/
      developer.md
      reviewer.md
    prompts/
      summarize-issue.md
    tests/
      summarize-issue.json
CLAUDE.md
```

Contact
-------
For questions about these standards, contact the Tech Lead or the NLP owner.
