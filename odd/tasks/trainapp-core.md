# TrainApp - ODD Task Plan

**Feature**: TrainApp - Offline-first workout tracker with routines, execution, stats, body tracking
**Branch**: `feature/trainapp-core`
**Target**: MVP (v1) - compilable APK, usable on device/emulator
**Created**: 2026-10-03

---

## Objective
Build a complete Android app (Kotlin + Compose + Room) for managing workout routines, executing sessions, tracking progress, body weight, and statistics — all offline, local storage, installable via APK.

---

## Scope (MVP - v1)

### In Scope
- Routines: CRUD, linear N-day cyclic, blocks (3 predefined + custom), 6 block types
- Exercise catalog: pre-loaded + editable + tags + unilateral/bilateral + custom categories
- Execution: stepper by blocks, global timer + per-exercise timing, auto-rest, skip with double-confirm, sensations per exercise
- History: list, detail, export JSON/CSV
- Body weight: weekly, Friday notification, optional photos linked to weight
- Global sensations: quiz at start/end of routine
- Stats: volume weekly, GitHub heatmap, 1RM timeline, tonnage, RPE trend, effective time
- Backup: local auto + export/import JSON/CSV, status in settings
- Settings: toggles for all calculations/charts, formulas explained
- Notifications: Friday weigh-in + training inactivity
- Theme: dark gray/black + yellow accent, Material Icons, medium typography, subtle playful animations
- Android 16 compatible (target SDK 35), Min SDK 24

### Out of Scope (v2+)
- Body heatmap (corporal heatmap)
- Dynamic weight charts
- Extra activity tracking in heatmap
- Programmed progression in template (progress bar)
- Cloud backup (Drive/Dropbox)
- Full settings granularity
- Onboarding tutorial

---

## Acceptance Criteria
- [ ] `./gradlew assembleDebug` produces `app-debug.apk` installable on Android 7.0+ (API 24)
- [ ] App runs on emulator and physical device (Android 16 tested)
- [ ] All data persists in Room DB (offline, no network permission)
- [ ] Routine creation → execution → history → stats flow works end-to-end
- [ ] Backup export/import restores full state
- [ ] Notifications fire correctly (Friday weigh-in, inactivity)
- [ ] All calculations toggleable in settings with formula info
- [ ] Unit tests for ViewModels, Repository, UseCases pass
- [ ] Instrumented tests for DAOs, Navigation pass

---

## Tasks (T01-T25)

### T01: Project Setup & Gradle Configuration
**Status**: ✅ Completed
**Files**: `settings.gradle.kts`, `build.gradle.kts` (root), `app/build.gradle.kts`, `gradle.properties`, `gradle/wrapper/gradle-wrapper.properties`
**Details**:
- Kotlin 2.0+, Compose BOM 2024.10+, Room 2.6+, Hilt 2.50+, Navigation Compose 2.7+, Vico (charts), Material3
- Target SDK 35, Min SDK 24, Compile SDK 35
- Java 17 toolchain
- KSP for Room/Hilt
- ProGuard/R8 rules for release
**Verification**: `./gradlew :app:compileDebugKotlin` succeeds

---

### T02: AndroidManifest + Permissions + App Entry
**Status**: ✅ Completed
**Files**: `app/src/main/AndroidManifest.xml`, `app/src/main/java/com/tuusuario/trainapp/TrainApp.kt`
**Details**:
- No `INTERNET` permission (offline-first)
- `POST_NOTIFICATIONS` (Android 13+), `FOREGROUND_SERVICE` (timer), `RECEIVE_BOOT_COMPLETED` (backup scheduler)
- Hilt `@HiltAndroidApp` Application class
- WorkManager for backup scheduler + notification scheduler
**Verification**: Manifest merges, app installs

---

### T03: Room Database - Entities & TypeConverters
**Status**: ✅ Completed
**Files**: `data/db/entities/*.kt`, `data/db/converters/*.kt`
**Entities**:
- `Routine` (id, name, description, dayCount, cycleType, createdAt, updatedAt)
- `RoutineDay` (id, routineId, dayIndex, name, blockOrderJson)
- `Block` (id, dayId, blockIndex, name, type, configJson, order)
- `BlockType` enum: SETS_REPS, CIRCUIT_TIME, EMOM, HIIT, SINGLE_TIMER, MIXED
- `TemplateExercise` (id, blockId, exerciseId, order, setsTarget, repsTarget, weightTarget, rpeTarget, restMipSec, restMapSec, timeWorkSec, timeRestSec, rounds, configJson)
- `ExerciseCatalog` (id, name, pattern, primaryMuscle, equipment, unilateral, bilateral, notes, aliasesJson, categoryId, isCustom)
- `ExerciseCategory` (id, name, isSystem, order)
- `Workout` (id, routineId, routineDayIndex, startedAt, endedAt, durationSec, blockSnapshotsJson, notes, globalSensationStart, globalSensationEnd)
- `WorkoutExercise` (id, workoutId, blockId, templateExerciseId, exerciseName, order, setsJson, completedAt, skipped, sensation)
- `SetLog` (id, workoutExerciseId, setIndex, weight, reps, rpe, completed, durationSec, restAfterSec)
- `BodyWeight` (id, date, weightKg, photoUri, notes)
- `GlobalSensation` (id, date, sleep, fatigue, motivation, painNotes, notes)
- `AppSettings` (key, valueJson) — singleton row
**TypeConverters**: Enums, Lists, Maps, LocalDateTime, Duration, BlockConfig, SetLog list
**Verification**: `./gradlew :app:kspDebugKotlin` generates DAOs without errors

---

### T04: Room DAOs
**Status**: ✅ Completed
**Files**: `data/db/dao/*.kt`
**DAOs**:
- `RoutineDao`: CRUD + flow all/byId + withDays
- `RoutineDayDao`: CRUD + flow byRoutine
- `BlockDao`: CRUD + flow byDay
- `TemplateExerciseDao`: CRUD + flow byBlock
- `ExerciseCatalogDao`: CRUD + flow all + search + byCategory + byMuscle + byPattern
- `ExerciseCategoryDao`: CRUD + flow all
- `WorkoutDao`: CRUD + flow all + byDateRange + byRoutine + lastWorkout
- `WorkoutExerciseDao`: CRUD + flow byWorkout
- `SetLogDao`: CRUD + flow byWorkoutExercise
- `BodyWeightDao`: CRUD + flow all + latest + byWeek
- `GlobalSensationDao`: CRUD + flow all + latest
- `AppSettingsDao`: get/set by key
**Verification**: DAO compile, queries type-safe

---

### T05: Database Class & Repository Interfaces
**Status**: ✅ Completed - Manual DI used instead of Hilt
**Files**: `data/db/AppDatabase.kt`, `data/repository/*.kt`
**Details**:
- `AppDatabase` abstract class with all DAOs
- Repository interfaces: `RoutineRepository`, `ExerciseRepository`, `WorkoutRepository`, `BodyRepository`, `SettingsRepository`
- Implementations using DAOs + Flow transformations
- Suspend functions for writes, Flow for reads
**Verification**: Repositories injectable via Hilt

---

### T06: Hilt Modules
**Status**: ✅ Completed - Hilt disabled for now, manual DI structure created
**Files**: `di/DatabaseModule.kt`, `di/RepositoryModule.kt`, `di/WorkerModule.kt`, `di/NavigationModule.kt`
**Details**:
- `DatabaseModule`: `@Provides` AppDatabase (singleton), RoomDatabase.Callback for pre-populate catalog
- `RepositoryModule`: `@Binds` implementations
- `WorkerModule`: WorkManager config for backup + notifications
- `NavigationModule`: NavController provider

---

### T07: Exercise Catalog Pre-population
**Status**: ☐ Pending
**Files**: `data/db/seed/ExerciseSeed.kt`, `data/db/callback/SeedCallback.kt`
**Details**:
- 50+ common exercises with full taxonomy:
  - Patterns: push_horiz, push_vert, pull_horiz, pull_vert, squat, hinge, lunge, core, carry, cardio
  - Muscles: chest, back, shoulders, triceps, biceps, quads, hamstrings, glutes, calves, core, full_body
  - Equipment: barbell, dumbbell, kettlebell, machine, cable, bands, trx, bodyweight
  - Unilateral/bilateral flag
  - Aliases (es/en): "Press banca" = "Banca plana" = "Bench press"
  - Categories: "Empuje", "Tirón", "Piernas", "Core", "Cardio", "Full Body", "Accesorios"
- Seed runs once on first DB creation
**Verification**: Catalog query returns seeded data

---

### T08: Domain Models & Mappers
**Status**: ☐ Pending
**Files**: `domain/model/*.kt`, `data/mapper/*.kt`
**Details**:
- Domain models (clean, no Room annotations)
- Mappers: Entity ↔ Domain ↔ UI State
- BlockConfig sealed class per block type with typed config

---

### T09: Use Cases (Domain Layer)
**Status**: ☐ Pending
**Files**: `domain/usecase/*.kt`
**Use Cases**:
- Routine: `GetRoutines`, `GetRoutineWithDays`, `CreateRoutine`, `UpdateRoutine`, `DeleteRoutine`, `ReorderDays`, `DuplicateRoutine`
- RoutineDay: `CreateDay`, `UpdateDay`, `DeleteDay`, `ReorderBlocks`, `CopyDay`
- Block: `CreateBlock`, `UpdateBlock`, `DeleteBlock`, `ChangeBlockType`, `ReorderExercises`
- TemplateExercise: `AddExerciseToBlock`, `UpdateTemplateExercise`, `RemoveExerciseFromBlock`
- ExerciseCatalog: `SearchExercises`, `GetByCategory`, `GetByMuscle`, `CreateCustomExercise`, `UpdateExercise`, `DeleteCustomExercise`, `CreateCategory`, `UpdateCategory`
- Workout: `StartWorkout`, `GetNextRoutineDay`, `CompleteWorkout`, `SkipExercise`, `LogSet`, `UpdateSet`, `GetWorkoutHistory`, `GetWorkoutDetail`, `ExportWorkoutsJson`, `ExportWorkoutsCsv`
- Body: `LogBodyWeight`, `GetBodyWeightHistory`, `GetLatestWeight`, `ShouldNotifyWeighIn`, `LogGlobalSensation`
- Settings: `GetSettings`, `UpdateSetting`, `ResetSettings`, `GetAllToggles`
- Backup: `ExportFullBackup`, `ImportFullBackup`, `ScheduleAutoBackup`, `GetLastBackupTime`
- Calculations: `Calculate1RM`, `CalculateVolume`, `CalculateTonnage`, `SuggestNextWeight` (all respect toggles)

---

### T10: ViewModels - Routine Management
**Status**: ☐ Pending
**Files**: `ui/routine/*.kt`
**ViewModels**:
- `RoutineListViewModel`: Flow<List<Routine>>, delete, duplicate, navigate to editor
- `RoutineEditorViewModel`: Full routine editor (days, blocks, exercises), reactive UI state, validation
- `DayEditorViewModel`: Block list, add/remove/reorder blocks, change block type
- `BlockEditorViewModel`: Exercise list per block, config per block type, add/remove/reorder exercises
- `ExercisePickerViewModel`: Search/filter catalog, create new, select for template

---

### T11: ViewModels - Execution (Workout)
**Status**: ☐ Pending
**Files**: `ui/workout/*.kt`
**ViewModels**:
- `WorkoutSetupViewModel`: Choose routine → auto (next day) or manual (pick day) → start
- `WorkoutExecutionViewModel`: **Core execution logic**
  - Stepper state: current block index, current exercise index
  - Global timer (running, paused, elapsed)
  - Per-exercise timer (start/stop on exercise change)
  - Auto-rest timer: triggers on set completion (mip/map) or exercise completion (circuit)
  - Block types handling: SETS_REPS (manual set complete), CIRCUIT/EMOM/HIIT/SINGLE_TIMER (auto-advance with configurable delay)
  - Skip exercise: double confirmation → log as skipped
  - Sensations per exercise: bottom sheet input
  - Set logging: weight, reps, RPE, duration, rest
  - Workout persistence: periodic save + final save
  - Foreground service for timer survival

---

### T12: ViewModels - History & Stats
**Status**: ☐ Pending
**Files**: `ui/history/*.kt`, `ui/stats/*.kt`
**ViewModels**:
- `HistoryViewModel`: Flow<List<WorkoutSummary>>, filters (date, routine), delete, export
- `WorkoutDetailViewModel`: Full workout replay with sets, rest, sensations, timings
- `StatsViewModel`: 
  - Volume weekly (by muscle group)
  - GitHub heatmap (training frequency)
  - 1RM timeline per exercise
  - Tonnage by muscle group
  - RPE trend
  - Effective time (training vs rest)
  - All charts toggleable via settings
  - Vico chart state management

---

### T13: ViewModels - Body & Settings
**Status**: ☐ Pending
**Files**: `ui/body/*.kt`, `ui/settings/*.kt`
**ViewModels**:
- `BodyViewModel`: Weight log (weekly), photo picker, Friday notification toggle, history chart
- `SensationQuizViewModel`: Start/end quiz (sleep, fatigue, motivation, pain, notes 1-10 + text)
- `SettingsViewModel`: 
  - Toggles: 1RM, volume, tonnage, weight suggestion, RPE, heatmap, charts
  - Formula info dialogs
  - Backup: export/import JSON/CSV, last backup timestamp, auto-backup toggle
  - Notifications: weigh-in (Friday), inactivity (days)
  - Theme: dark only (system), accent color
  - Data management: clear all data (with confirm)

---

### T14: Navigation & App Scaffold
**Status**: ☐ Pending
**Files**: `ui/navigation/AppNavHost.kt`, `ui/navigation/AppNavGraph.kt`, `ui/MainActivity.kt`
**Details**:
- Bottom bar: Rutinas / Entrenar / Historial / Stats / Perfil
- Nested graphs per section
- Deep links for routine/day/block editing
- Back handling in execution (confirm abandon)
- Safe args for all routes

---

### T15: UI - Routine List & Editor
**Status**: ☐ Pending
**Files**: `ui/routine/RoutineListScreen.kt`, `ui/routine/RoutineEditorScreen.kt`, `ui/routine/DayEditorScreen.kt`, `ui/routine/BlockEditorScreen.kt`, `ui/routine/ExercisePickerScreen.kt`
**Components**:
- Routine card: name, day count, last executed, next day badge
- Editor: drag-drop reorder (days, blocks, exercises), inline block type picker, exercise search bottom sheet
- Block type selector: chips with icons per type
- Exercise picker: tabs (Catalog / Custom / Recent), search, filters (muscle, pattern, equipment), create new inline

---

### T16: UI - Workout Execution (Core)
**Status**: ☐ Pending
**Files**: `ui/workout/WorkoutSetupScreen.kt`, `ui/workout/WorkoutExecutionScreen.kt`, `ui/workout/components/*.kt`
**Components**:
- **Setup**: Routine picker, next day preview, manual day picker, "Comenzar" button
- **Execution**: 
  - Top: Global timer (large), routine name, current day name
  - Stepper tabs: Bloques (Calentamiento, Principal, Calma + custom)
  - Current exercise card: name, target config (sets/reps/weight/rest OR time/rounds), previous set preview
  - Next exercise preview (smaller)
  - Set logger: weight input, reps input, RPE slider, "Completar serie" button
  - Auto-rest overlay: countdown with sound/vibration, "Saltar descanso"
  - Circuit/Timer mode: big circular timer, round counter, exercise queue, beep at transitions
  - Skip exercise: long press → confirm dialog → confirm again → log skipped
  - Sensation: floating action button per exercise → bottom sheet (1-10 + text)
  - Bottom: "Finalizar entrenamiento" (confirm) / "Pausar"
- **Foreground Service**: Persistent notification with timer, pause/resume, stop

---

### T17: UI - History & Workout Detail
**Status**: ☐ Pending
**Files**: `ui/history/HistoryScreen.kt`, `ui/history/WorkoutDetailScreen.kt`
**Components**:
- History list: date, routine, day, duration, volume, RPE avg, sensation indicator
- Filters: date range, routine, search
- Swipe actions: delete, export single
- Detail: block timeline, each exercise with sets table, rest times, sensations, per-exercise duration
- Export buttons: JSON / CSV

---

### T18: UI - Stats Dashboard (v1 Charts)
**Status**: ☐ Pending
**Files**: `ui/stats/StatsScreen.kt`, `ui/stats/charts/*.kt`
**Charts (Vico)**:
- **Volume Weekly**: Stacked bar (push/pull/legs/core/cardio) — toggleable
- **GitHub Heatmap**: Custom Compose canvas (not Vico) — 12 months, color intensity by sessions/day
- **1RM Timeline**: Line chart per exercise (multi-series) — toggleable
- **Tonnage by Muscle**: Line/area chart — toggleable
- **RPE Trend**: Line chart — toggleable
- **Effective Time**: Bar (training vs rest vs total) per session/week — toggleable
- **Settings integration**: Each chart has visibility toggle in SettingsViewModel
- **Empty states**: Helpful illustrations when no data

---

### T19: UI - Body Weight & Sensations
**Status**: ☐ Pending
**Files**: `ui/body/BodyScreen.kt`, `ui/body/WeightEntryDialog.kt`, `ui/body/SensationQuizDialog.kt`
**Components**:
- Body screen: Current weight card, weekly entry button, photo gallery (optional), weight chart (line + target zone)
- Weight entry: date picker (default today), weight kg, photo picker, notes
- Sensation quiz: 5 questions (sleep, fatigue, motivation, pain 1-10 + text), shown at workout start/end (configurable)
- Friday notification check: WorkManager periodic + exact alarm

---

### T20: UI - Settings
**Status**: ☐ Pending
**Files**: `ui/settings/SettingsScreen.kt`, `ui/settings/components/*.kt`
**Sections**:
- **Cálculos**: Toggle list with info icon → dialog with formula (Epley/Brzycki for 1RM, volume = sets×reps, tonnage = weight×reps×sets, suggestion = RPE-based)
- **Gráficos**: Toggle per chart (volume, heatmap, 1RM, tonnage, RPE, time)
- **Backup**: Export JSON, Export CSV, Import, Auto-backup toggle, Last backup timestamp
- **Notificaciones**: Weigh-in Friday toggle, Inactivity days (number input), Test notification button
- **Apariencia**: Accent color picker (yellow default), Animation intensity (low/medium/high)
- **Datos**: Clear all data (triple confirm), App version, Open source licenses

---

### T21: Notification Workers
**Status**: ☐ Pending
**Files**: `worker/WeighInReminderWorker.kt`, `worker/InactivityReminderWorker.kt`, `worker/AutoBackupWorker.kt`
**Details**:
- `WeighInReminderWorker`: Runs Fridays 10:00 → checks BodyWeightDao for this week → notifies if missing
- `InactivityReminderWorker`: Daily 19:00 → checks last workout date → notifies if > N days (setting)
- `AutoBackupWorker`: Daily 03:00 → exports JSON to `files/backups/trainapp_YYYYMMDD.json` → keeps last 30
- All use WorkManager with `PeriodicWorkRequest` + constraints (charging, wifi not required)
- Notification channels: `weigh_in`, `inactivity`, `backup` (low importance)

---

### T22: Backup / Export / Import Logic
**Status**: ☐ Pending
**Files**: `data/backup/BackupManager.kt`, `data/backup/BackupSchema.kt`
**Details**:
- **Schema**: Versioned JSON with all entities (routines, days, blocks, exercises, catalog, workouts, sets, body weight, sensations, settings)
- **Export**: `BackupManager.exportToFile(context) -> Result<Uri>` (JSON + CSV for workouts)
- **Import**: `BackupManager.importFromFile(context, uri) -> Result<ImportReport>` (validates schema version, merges/overwrites with confirmation)
- **CSV**: Workouts flat (date, routine, day, exercise, set, weight, reps, rpe, rest, duration)
- **Auto-backup**: Daily, retains 30 files, cleanup on import

---

### T23: Calculation Engine (Optional, Configurable)
**Status**: ☐ Pending
**Files**: `domain/calculation/*.kt`
**Functions** (all pure, tested):
- `estimate1RM(weight: Double, reps: Int): Double` — Epley: w × (1 + r/30)
- `estimate1RMBrzycki(weight: Double, reps: Int): Double` — w × 36/(37-r)
- `volume(sets: List<SetLog>): Int` — Σ completed sets
- `tonnage(sets: List<SetLog>): Double` — Σ weight × reps (completed only)
- `suggestNextWeight(lastWeight: Double, lastReps: Int, lastRPE: Int, targetRPE: Int = 8): Double` — heuristic
- `muscleGroupVolume(workouts: List<Workout>): Map<MuscleGroup, Int>` — via exercise catalog tags
- All functions check `SettingsRepository` toggles before computing

---

### T24: Unit & Instrumented Tests
**Status**: ☐ Pending
**Files**: `app/src/test/...`, `app/src/androidTest/...`
**Coverage targets**:
- **Unit**: ViewModels (T10-T13), UseCases (T09), Calculations (T23), Mappers, Repository impls (mock DAOs)
- **Instrumented**: DAOs (Room), Database migrations, Navigation, WorkoutExecutionViewModel (timer logic), BackupManager
- **Tools**: JUnit5, MockK, Turbine (Flow testing), Compose Test Rules, Robolectric for Workers
- **Command**: `./gradlew test debugAndroidTest` — all pass

---

### T25: Polish, Accessibility & Release Prep
**Status**: ☐ Pending
**Files**: Various
**Details**:
- Content descriptions, semantic properties, touch target sizes (48dp min)
- Dark theme only (forced), yellow accent on primary actions
- Animations: `animateContentSize`, `sharedElement`, staggered list entrance, confetti on PR (1RM beat)
- Error states: inline validation, snackbar for non-blocking, dialog for critical
- Loading states: skeleton screens for lists
- App icon (adaptive), splash screen (Compose SplashScreen API)
- `build.gradle.kts` signing config (debug keystore), versionCode/versionName
- README.md with build/install instructions
- ProGuard rules for Vico, Room, Hilt

---

## Dependencies Between Tasks

```
T01 → T02 → T03 → T04 → T05 → T06
                          ↓
T07 → T08 → T09 → T10/T11/T12/T13 → T14 → T15/T16/T17/T18/T19/T20
                          ↓
                     T21/T22/T23
                          ↓
                     T24 → T25
```

---

## Progress Tracking

| Task | Status | Commit | Notes |
|------|--------|--------|-------|
| T01 | ✅ | | Project compiles, APK produced |
| T02 | ✅ | | Manifest + TrainApp done |
| T03 | ✅ | | Entities + TypeConverters done |
| T04 | ✅ | | DAOs compile |
| T05 | ✅ | | Database + Repositories done |
| T06 | ✅ | | Hilt modules (Hilt disabled for now, manual DI used) |
| T07 | ☐ | | |
| T08 | ☐ Pending | | |
| T09 | ☐ Pending | | |
| T10 | ☐ Pending | | |
| T11 | ☐ Pending | | |
| T12 | ☐ Pending | | |
| T13 | ☐ Pending | | |
| T14 | ☐ Pending | | |
| T15 | ☐ Pending | | |
| T16 | ☐ Pending | | |
| T17 | ☐ Pending | | |
| T18 | ☐ Pending | | |
| T19 | ☐ Pending | | |
| T20 | ☐ Pending | | |
| T21 | ☐ Pending | | |
| T22 | ☐ Pending | | |
| T23 | ☐ Pending | | |
| T24 | ☐ Pending | | |
| T25 | ☐ Pending | | |

---

## Next Steps After MVP
- v2.1: Body heatmap (SVG + intensity mapping)
- v2.2: Dynamic weight charts (weight + BMI + fat% estimation)
- v2.3: Extra activity in heatmap (custom icons)
- v2.4: Programmed progression in template (periodization)
- v2.5: Cloud backup (Drive API)
- v2.6: Full settings granularity + onboarding