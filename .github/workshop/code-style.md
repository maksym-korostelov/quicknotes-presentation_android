# QuickNotes Android — Kotlin Code Style Guidelines

This document is a reference for the GitHub Copilot Workshop Assistant. It contains Kotlin/Android code style rules for the QuickNotes project.

---

## Naming Conventions

- **Types**: PascalCase — `NoteListViewModel`, `GetNotesUseCase`
- **Properties/Methods**: camelCase — `fetchNotes()`, `noteTitle`
- **Interfaces**: Descriptive name — `NoteRepository`
- **Packages**: lowercase — `domain.entity`, `data.repository`

## Code Structure

- One screen per package in `presentation/`
- ViewModel + Screen composable in same package
- `ViewModelFactory` classes in `core/` for DI

## Jetpack Compose Patterns

- ViewModel uses `StateFlow` for state management
- Screens are `@Composable` functions
- Use `MaterialTheme` for base theming

## Design System Usage

```kotlin
// Typography with style
Text(
    text = "Title",
    style = AppTypography.headingLarge
)

// Typography with semantic color
Text(
    text = "Subtitle",
    style = AppTypography.bodyMedium,
    color = AppColors.textSecondary
)

// Semantic composable function (returns Pair<TextStyle, Color>)
val (style, color) = AppTypography.bodyLargeAction()
Text(
    text = "Action",
    style = style,
    color = color
)

// Icon sizes
Icon(
    imageVector = Icons.Default.Star,
    modifier = Modifier.size(AppTypography.iconHeroLarge)
)
```

## Clean Architecture Patterns

### UseCases

```kotlin
class GetNotesUseCase(
    private val repository: NoteRepository
) {
    suspend fun execute(): List<Note> {
        return repository.fetchAll()
    }
}
```

### Entities

```kotlin
// Kotlin data class, no framework dependencies
data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val category: Category? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
```

### Repository Interface

```kotlin
interface NoteRepository {
    suspend fun fetchAll(): List<Note>
    suspend fun save(note: Note)
    suspend fun delete(note: Note)
}
```

### ViewModel

```kotlin
class NoteListViewModel(
    private val getNotesUseCase: GetNotesUseCase
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun fetchNotes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _notes.value = getNotesUseCase.execute()
            } catch (e: Exception) {
                // Handle error
            }
            _isLoading.value = false
        }
    }
}
```

### ViewModelFactory

```kotlin
class NoteListViewModelFactory(
    private val appDependencies: AppDependencies
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteListViewModel(
            getNotesUseCase = appDependencies.getNotesUseCase
        ) as T
    }
}
```
