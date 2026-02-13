---
name: applyChangesFromIOS
description: Apply changes from iOS repository to Android based on diff/patch files
---

# iOS to Android Change Migration

You are tasked with applying changes from an iOS repository to this Android codebase based on a provided diff or patch file.

## CRITICAL: File Validation

**STOP IMMEDIATELY** if no diff or patch file is provided. Do not proceed without a diff/patch file attached or pasted by the user.

## Workflow

### 1. Parse the Diff/Patch File

- Analyze the provided diff/patch file to understand:
  - Which files were modified in iOS
  - What changes were made (additions, deletions, modifications)
  - The context and purpose of each change
  - Any new features, bug fixes, or refactorings

### 2. Identify Target Files

**If user provides specific file names or modules:**
- Use the provided file names/modules as the target for changes
- Verify these files exist in the Android codebase
- If they don't exist, ask user for clarification

**If no files are specified:**
- Search the Android codebase for corresponding files:
  - Map iOS file names to likely Android equivalents
  - Example mappings:
    - iOS: `PresentationView.swift` → Android: `PresentationView.kt` or `PresentationActivity.kt`
    - iOS: `NoteManager.swift` → Android: `NoteManager.kt` or `NoteRepository.kt`
    - iOS: `Models/Note.swift` → Android: `models/Note.kt` or `data/model/Note.kt`
  - Use semantic search to find files with similar functionality
  - Look for matching class names, function names, or concepts
  - Check both `app/src/main/java/` and nested package directories

### 3. Analyze and Translate Changes

For each change in the iOS diff:

- **Understand the intent**: What problem does this change solve?
- **Identify iOS-specific code**: UIKit, SwiftUI, iOS APIs, Swift syntax
- **Find Android equivalents**:
  - UIKit/SwiftUI → Jetpack Compose, View system, or XML layouts
  - Swift syntax → Kotlin syntax
  - iOS APIs → Android SDK equivalents
  - CocoaPods/Swift Package Manager dependencies → Gradle dependencies
  - UserDefaults → SharedPreferences or DataStore
  - Combine/async-await → Coroutines/Flow
  - UIViewController → Activity or Fragment or Composable

### 4. Apply Changes

- Translate each iOS change to its Android equivalent
- Maintain the same logic and behavior
- Follow Android/Kotlin best practices and conventions
- Preserve existing code structure and patterns in the Android project
- Add necessary imports and dependencies
- Update build.gradle.kts if new dependencies are needed

### 5. Verification

After applying changes:
- Check for compilation errors
- Ensure all imports are correct
- Verify that the changes maintain the same functionality as iOS
- Look for any platform-specific edge cases that need handling

## Important Considerations

- **Architecture Patterns**: iOS may use MVVM, Coordinators, etc. Adapt to Android's existing architecture (MVVM, MVI, etc.)
- **Threading**: iOS's MainActor/GCD → Android's Main thread/Coroutines
- **UI Patterns**: Adapt iOS UI patterns to Android Material Design guidelines
- **Lifecycle**: Account for differences in app/view lifecycle between iOS and Android
- **Data Persistence**: Map iOS persistence solutions to Android equivalents
- **Dependency Injection**: If iOS uses a DI framework, ensure Android's DI is updated similarly

## Error Handling

- If you cannot find a corresponding Android file, list potential matches and ask the user
- If a change is iOS-specific and has no Android equivalent, explain why and suggest alternatives
- If dependencies are missing, list them clearly with Gradle dependency strings

## Output Format

Provide a clear summary of:
1. Files analyzed from the iOS diff
2. Corresponding Android files identified/modified
3. Key changes applied
4. Any new dependencies added
5. Any iOS-specific changes that couldn't be directly translated (with explanations)

Begin by confirming you have received the diff/patch file, then proceed with the analysis and implementation.