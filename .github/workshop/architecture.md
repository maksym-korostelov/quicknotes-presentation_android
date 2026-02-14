# QuickNotes Android — Architecture Reference

This document is a reference for the GitHub Copilot Workshop Assistant. It contains the full Android architecture overview, folder structure, and architecture rules for the QuickNotes project.

---

## Project Overview

A Notes application built progressively throughout the workshop series using **Clean Architecture**. The Android app mirrors the iOS app's architecture using Kotlin and Jetpack Compose.

## Folder Structure

```
app/src/main/java/com/example/quicknotes/
├── presentation/
│   ├── notes/              # NotesScreen, NoteDetailScreen, NoteEditorScreen + ViewModels
│   ├── categories/         # CategoriesScreen, CategoryEditorScreen + ViewModels
│   ├── search/             # SearchScreen + ViewModel
│   ├── settings/           # SettingsScreen
│   ├── profile/            # ProfileScreen + ViewModel
│   ├── onboarding/         # OnboardingScreen
│   ├── about/              # AboutScreen
│   └── help/               # HelpScreen
├── domain/
│   ├── entity/             # Data classes (Note, Category, UserProfile)
│   ├── usecase/            # Business rules (mirrors iOS UseCases)
│   └── repository/         # Interface definitions
├── data/
│   ├── local/              # Local data source implementations
│   ├── repository/         # Repository implementations
│   └── SeedData.kt         # Sample data
├── core/
│   ├── AppDependencies.kt      # DI container
│   ├── AppPreferences.kt       # SharedPreferences wrapper
│   ├── *ViewModelFactory.kt    # ViewModel factory classes (Android-specific DI)
│   └── ColorUtils.kt           # Utility functions
└── ui/
    └── theme/
        ├── AppTypography.kt    # Design system typography tokens
        ├── AppColors.kt        # Design system color tokens
        ├── Color.kt            # Color definitions
        ├── Theme.kt            # Material theme setup
        └── Type.kt             # Base typography
```

## Architecture Rules

### 1. Presentation Layer

- **Screens**: Jetpack Compose `@Composable` functions
- **ViewModels**: Manage presentation logic, use `ViewModel` + `StateFlow` for state management
- One screen per package in `presentation/`
- ViewModel + Screen composable in same package
- `ViewModelFactory` classes in `core/` for DI

### 2. Domain Layer

- **UseCases**: Encapsulate business rules, coordinate Repository ↔ ViewModel data flow. Single public `execute()`/`invoke()` method.
- **Entities**: Core business models — Kotlin data classes, no framework dependencies
- **Repository Interfaces**: Define data access contracts

### 3. Data Layer

- **Repositories**: Implement Domain interfaces
- Local data source implementations with in-memory storage

### 4. Design System (ui/theme)

- `AppTypography`: Semantic typography tokens (displayLarge, headingSmall, bodyLarge, etc.)
- `AppColors`: Semantic color tokens (textPrimary, textSecondary, textDestructive, etc.)
- Use `style = AppTypography.xxx` for text style
- Use `color = AppColors.xxx` for semantic colors
- Semantic composable functions: `AppTypography.bodyLargeAction()` returns `Pair<TextStyle, Color>`
- Icon sizes: `AppTypography.iconHeroLarge`, `iconHeroXLarge`, etc.

### 5. Dependency Rule

Dependencies point inward: **Presentation → Domain ← Data**

---

## Architecture Mapping (Android ↔ Other Platforms)

| Android (Demo) | iOS | Backend | Frontend |
|----------------|-----|---------|----------|
| Jetpack Compose Screen | SwiftUI View | REST Controller / GraphQL Resolver | React/Vue/Angular Component |
| ViewModel (`StateFlow`) | ViewModel (`@Observable`) | Service Layer | Hooks / Store / Service |
| UseCase | UseCase | Service / UseCase | Custom Hook / Service |
| Entity (data class) | Entity (struct) | Entity / Model / DTO | Interface / Type / Model |
| Repository Interface | Repository Protocol | Repository Interface | API Service Interface |
| Repository Impl | Repository Impl | Repository Impl | API Service Impl |
| Room Entity | SwiftData Model | JPA Entity / ORM | N/A |
| Local DataSource | InMemory Repository | JPA Repository / ORM | LocalStorage / IndexedDB |
| AppTypography (TextStyle object) | AppTypography (ViewModifier) | N/A | Design tokens / CSS variables |
| AppColors (semantic) | AppColors (semantic) | N/A | Theme colors / CSS custom properties |
