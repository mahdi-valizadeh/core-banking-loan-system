# Reviewer subagent — role description and example prompt

Role
----
Helps with code reviews, suggests improvements, and verifies adherence to project standards.

Prompt Template
---------------
Context: provide PR diff or code snippet.
Task: produce a short review comment list (issues, suggestions, acceptance criteria).

Constraints: avoid rewriting whole files; focus on actionable review points.

Example
-------
Input: a small PR diff
Output: 1-3 bullets with severity and suggested fixes.
