# AI Agent Guidelines

The AI agent must follow a planning-first approach when assisting with this project.

## Core Rules

- Always read all documentation inside `/docs` before generating any code.
- Follow the architecture strictly as defined in `docs/06_architecture.md`.
- Generate **Kotlin code only**.
- UI must use **XML Views** — never Jetpack Compose.
- Do **not** generate large files at once — break output into logical chunks.
- Generate code **step-by-step** following `docs/08_implementation_plan.md`.
- When adding new features, follow `docs/09_feature_extensions.md`.

## Workflow to Follow

```
Read Docs → Understand Architecture → Follow Implementation Plan → Generate Code Step by Step
```

## Code Style Rules

- Use `ViewModel` + `LiveData` for all UI state.
- Use `Repository` pattern to abstract data sources.
- All network calls must be wrapped in `try/catch` with error handling.
- All database operations use Room DAOs.
- Use `Glide` for image loading — never load images manually.
- Use `Coroutines` for async operations — no callbacks or AsyncTask.

## File Generation Rules

- Generate one file at a time unless files are trivially small (e.g. data classes).
- Always include imports.
- Always include comments explaining each section.
- After generating a file, wait for confirmation before proceeding to the next step.

## What NOT to Do

- Do not skip steps in the implementation plan.
- Do not use Jetpack Compose.
- Do not use Java.
- Do not hardcode API URLs — use the constants defined in the project.
- Do not generate UI logic inside Activities — use ViewModels.
