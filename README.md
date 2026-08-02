# BrushAndroid

A personal hands-on practice repo for Android + Kotlin + Jetpack Compose interview prep. Each topic pairs a concept with a small, runnable Compose demo screen — tap a button, watch the behavior, connect it back to the "why."

## Structure

```
app/src/main/java/com/prajwalhs/brushandroid/
├── MainActivity.kt          # tap-through list of every demo screen
└── coroutinesflow/           # Category A — Coroutines & Flow
    ├── suspendfun/
    ├── launchbuilder/
    ├── asyncawait/
    ├── runblockingdemo/
    ├── withcontextdemo/
    ├── viewmodelscope/
    ├── scopevariants/
    ├── supervisorjobdemo/
    ├── structuredconcurrency/
    ├── exceptionhandling/
    ├── parallelapicalls/
    ├── returnlabels/
    └── flowon/
```

Each topic folder holds one `.kt` file: a small `@Composable` screen with buttons that trigger the concept and show the result live.

More categories (Flow operators, Compose state, Navigation, DI, Architecture, etc.) get added the same way as I work through them.

## Running it

Open in Android Studio, run on an emulator or device, and pick a topic from the list on the home screen.
