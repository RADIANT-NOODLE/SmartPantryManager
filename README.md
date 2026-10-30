# Smart Pantry Manager

An Android app, written in **Java**, that helps reduce food waste. You tell it what ingredients you already have at home, and it suggests only the recipes you can cook **right now**, with no shopping trip required.

Built for the *Mobile App Development 700* practical assignment (Richfield).

## Core idea: strict matching

A recipe is suggested **only if every ingredient it needs is in your pantry, in at least the required quantity.**

- A recipe needing 5 ingredients where the pantry holds only 4 is **not** shown.
- A recipe where an ingredient is present but in too small a quantity is **not** shown.
- Matching still copes with everyday messiness:
  - **Names are normalised**, so `Tomatoes`, ` tomato ` and `TOMATO` are the same ingredient.
  - **Units are converted**, so `1 kg` of flour covers a recipe that needs `250 g`, and `500 ml` of milk covers `1 cup`.
  - **Duplicate rows are combined**, so two entries of `flour 100 g` count as `200 g`.

The logic lives in `util/RecipeMatcher.java`. It is plain Java with no Android classes, so it is covered by JVM unit tests.

## Features

- **Pantry management:** add, edit and delete ingredients (name, quantity, unit and an optional expiry date).
- **Pantry list:** a `RecyclerView` with a custom adapter, backed by the database.
- **Recipe collection:** 20 recipes seeded into the database on first run.
- **Suggested Recipes:** shows only recipes that pass the strict-matching rule, with a friendly message when none match.
- **Recipe detail:** full ingredient list and numbered method.
- **Settings:** highlight ingredients that are expiring soon, with a 2, 3 or 7 day window.
- **Input validation** on the add/edit form.
- **Bottom navigation** between Pantry, Recipes and Settings.

## Screens

| Screen | Type |
|---|---|
| Pantry List | Fragment (bottom navigation tab) |
| Add / Edit Ingredient | Activity, opened by Intent |
| Suggested Recipes | Fragment (bottom navigation tab) |
| Recipe Detail | Activity, opened by Intent |
| Settings | Fragment (bottom navigation tab) |

## Tech stack

- Java 11, Android Studio, minSdk 24
- **Room (SQLite)** for local persistence
- Material Components (Material 3 theme)
- RecyclerView, ConstraintLayout/LinearLayout layouts
- SharedPreferences for settings
- JUnit for unit tests

## Database

Room creates and manages the local SQLite database `pantry_db`. Data persists after the app is closed and reopened.

| Table | Purpose | Key columns |
|---|---|---|
| `pantry_items` | The user's ingredients | `id`, `name`, `quantity`, `unit`, `expiryDate` |
| `recipes` | Seeded recipe collection | `id`, `name`, `steps` |
| `recipe_ingredients` | Ingredients each recipe needs | `id`, `recipeId` (foreign key to `recipes`), `name`, `quantity`, `unit` |

All database work runs on a background thread (`AppDatabase.IO`), because Room does not allow queries on the UI thread. Recipes are seeded once by `RecipeSeeder` when the `recipes` table is empty.

## Project structure

```
app/src/main/java/com/example/smartpantrymanager/
├── MainActivity.java            Hosts the toolbar, bottom navigation and fragments
├── AddEditItemActivity.java     Add / edit form with validation
├── RecipeDetailActivity.java    Ingredients and method for one recipe
├── db/                          AppDatabase, PantryDao, RecipeDao, RecipeRepository, RecipeSeeder
├── model/                       PantryItem, Recipe, RecipeIngredient, RecipeWithIngredients
├── ui/                          PantryFragment, RecipesFragment, SettingsFragment and adapters
└── util/                        RecipeMatcher, UnitConverter, NameNormalizer, ExpiryUtil, Formatters, Prefs
```

## Getting started

1. Clone the repository.
2. In Android Studio choose **File > Open** and select the project folder (the one containing `settings.gradle`).
3. Wait for Gradle to sync (internet is needed the first time).
4. Create an emulator in **Tools > Device Manager**, or plug in a phone with USB debugging enabled.
5. Press **Run**.

## Using the app

1. Open the **Pantry** tab and tap **+** to add ingredients.
2. Open the **Recipes** tab to see what you can cook right now.
3. Tap a recipe to see its ingredients and method.
4. Use **Settings** to turn expiry highlighting on or off.

### Entering ingredients

Pick the unit the recipes use so quantities can be compared:

| Ingredient type | Use unit |
|---|---|
| Eggs, bread slices, garlic cloves, onions, tomatoes | `pcs` |
| Butter, cheese, flour, rice, pasta, meat | `g` or `kg` |
| Milk, stock | `ml` or `l` |
| Oil, sugar | `tbsp` (sugar can also be `tsp`) |
| Salt, spices | `tsp` |

Units are only compared within the same group (weight, volume or count). For example, salt entered in grams cannot be compared with a recipe that needs a teaspoon, so that recipe is not suggested. This strict behaviour can be relaxed with `RecipeMatcher.ASSUME_ENOUGH_ON_UNIT_MISMATCH`.

### Quick demo

1. Add `egg` 3 `pcs` and `butter` 50 `g`. **Scrambled Eggs** appears.
2. Add `cheese` 100 `g`. **Cheese Omelette** still does not appear, because it also needs salt.
3. Add `salt` 1 `tsp`. **Cheese Omelette** appears.
4. Edit the eggs down to 2. Both recipes disappear.

## Tests

Run the unit tests in Android Studio: right-click `app/src/test/java` and choose **Run Tests**.

- `RecipeMatcherTest` covers: 4 of 5 ingredients is not suggested, all ingredients is suggested, too little quantity, singular and plural names, unit conversion, combined pantry rows, empty pantry, and unit mismatch behaviour.
- `NameNormalizerTest` covers lowercasing, trimming, plural handling and words that only look plural (such as hummus).

## Out of scope

As required by the assignment brief, the app uses **no** Google Maps, mapping SDKs, location or GPS features, and no payments.

## Author

*Your name, student number and module details here.*
