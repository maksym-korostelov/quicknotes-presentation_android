---
name: compareWithIOS
description: Compare current Android implementation with iOS changes from a diff/patch file to ensure feature parity
---
# Compare Android Implementation with iOS Changes

Please analyze the current Android implementation and compare it with the iOS implementation provided in the attached diff or patch file.

## Instructions

1. **Review iOS Changes**: Carefully examine the attached iOS diff/patch file to understand:
   - What features or changes were implemented
   - The architectural approach taken
   - UI/UX patterns used
   - Business logic and data handling
   - Any new dependencies or libraries added

2. **Analyze Android Implementation**: Review the current Android codebase (uncommitted changes included) for:
   - Whether equivalent functionality exists
   - How it's currently implemented
   - Any differences in approach or architecture

3. **Identify Gaps and Differences**:
   - List features present in iOS but missing in Android
   - Highlight implementation differences between platforms
   - Note any platform-specific considerations or constraints
   - Identify inconsistencies in behavior or UI/UX

4. **Provide Recommendations**:
   - Suggest what needs to be implemented in Android to achieve parity
   - Propose Android-appropriate solutions (following Android best practices)
   - Recommend files that need to be created or modified
   - Highlight any potential issues or challenges in porting

5. **Platform-Specific Considerations**:
   - Respect Android design guidelines and patterns
   - Consider Android lifecycle and state management
   - Account for Android-specific UI components and behaviors
   - Note any iOS-specific features that require different approaches on Android

## Expected Output

- **Similarities (Brief)**: A concise summary of what's already aligned between both platforms
- **Discrepancies (Detailed)**:
  - Missing features in Android
  - Implementation differences with detailed analysis
  - Behavioral or UI/UX inconsistencies
  - Architecture or approach differences
- **Action Items**: Specific tasks needed to achieve parity
- **Code Suggestions**: Concrete code examples or file changes for discrepancies only

## Attachment Required

Please attach an iOS diff or patch file showing the changes/implementation to compare against.