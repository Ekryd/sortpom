# SortPom — Coding Guidelines for AI Agents

## Project Overview

SortPom is a Maven plugin that sorts `pom.xml` files. It is a two-module Maven project:

- **`sorter/`** — Core sorting library (pure business logic, no Maven dependencies)
- **`maven-plugin/`** — Maven plugin wrapper around the sorter

## Build & Tooling

```bash
mvn verify              # Full build: compile, test, integration tests
mvn test                # Unit tests only
mvn fmt:format          # Auto-format all Java source (Spotify fmt-maven-plugin)
mvn fmt:check           # Verify formatting without modifying files
```

Always run `mvn fmt:format` before committing, or let it run as part of the build. Code that fails the format check will break CI.

Integration tests live in `maven-plugin/src/it/` and are executed via `maven-invoker-plugin` during `verify`.

## Java Version

- Current target: **Java 17** (`maven.compiler.release=17`)
- Use `var` for local variable type inference where it improves readability (it is widely used in this codebase)
- Java 17 features are available: sealed classes, pattern matching for `instanceof`, records, text blocks
- Do not use language features beyond Java 17

## Code Style

Formatting is enforced automatically by `fmt-maven-plugin` (Google Java Format). Do not manually adjust whitespace or brace placement — just run `mvn fmt:format`.

**Naming:**
- Classes: `PascalCase`
- Methods and fields: `camelCase`
- Constants: `UPPER_SNAKE_CASE`
- Packages: lowercase, e.g. `sortpom.wrapper.content`

**Javadoc:** Add Javadoc to public classes and non-trivial public methods. Include `@author` on classes only where already present in the file.

**Annotations:** Use `@SuppressWarnings` sparingly and only with justification.

## Architecture

The codebase is layered; keep concerns separated:

| Layer | Location | Responsibility |
|---|---|---|
| Plugin (Mojo) | `maven-plugin/.../SortMojo`, `VerifyMojo`, `AbstractParentMojo` | Maven lifecycle, parameter wiring |
| Application | `sorter/.../SortPomImpl`, `SortPomService` | Orchestration |
| Domain | `sorter/.../wrapper/`, `sorter/.../verify/` | Sorting logic, XML structure |
| Infrastructure | `sorter/.../util/`, `sorter/.../output/` | File I/O, XML serialisation |

**Key patterns in use:**
- **Builder** — `PluginParameters.Builder` for all configuration; use this pattern for new config objects
- **Strategy / Wrapper hierarchy** — `Wrapper<T>` implementations define different sort behaviours; add new sorting strategies here
- **Factory** — `WrapperFactory` / `WrapperFactoryImpl` creates wrappers; register new wrapper types here
- **Template Method** — `HierarchyWrapperOperation` defines the tree traversal hooks

Never mix Maven plugin concerns (Mojo annotations, `MavenProject`) into the `sorter` module.

## Error Handling

- Throw `FailureException` (extends `RuntimeException`) for unrecoverable errors in the sorter module
- The plugin layer converts `FailureException` to `MojoFailureException` via `ExceptionConverter`
- Do not propagate checked exceptions beyond module boundaries

## Testing

**Coverage requirement: 100%.** Every new production code path must have a corresponding test.

### Test frameworks
- JUnit 6 (Jupiter) — `@Test`, `@ParameterizedTest`, etc.
- Mockito 5 — for mocking collaborators
- Hamcrest 3 — use for all assertions: `assertThat(actual, matcher)`
- JUnit `Assertions` — only for `assertThrows` and `fail` (no Hamcrest equivalent)

### Test structure

Test classes mirror the production package structure and are named `{Subject}Test`.

For XML sorting tests, use the fluent test utilities rather than constructing objects manually:

```java
// Sorting / round-trip XML tests
XmlProcessorTestUtil.create()
    .predefinedSortOrder("recommended_2008_06")
    .testInputAndExpected("src/test/resources/MyFeature_input.xml",
                          "src/test/resources/MyFeature_expected.xml");

// Full integration through SortPomImpl
SortPomImplUtil.create()
    .sortDependencies("scope,groupId,artifactId")
    .testFiles("src/test/resources/MyFeature_input.xml",
               "src/test/resources/MyFeature_expected.xml");
```

XML fixture files live in `sorter/src/test/resources/`. Name them `<Feature>_input.xml` and `<Feature>_expected.xml`.

### What to test

- Normal (happy-path) cases with fixture XML files
- Edge cases: empty elements, comments, processing instructions, special characters
- Error cases: verify `FailureException` is thrown with the correct message
- Parameter validation: invalid combinations should fail with a clear error

### What not to do

- Do not mock the file system or XML parser in integration-style tests; use real fixture files
- Do not use `@BeforeEach` to share mutable state across unrelated tests
- Do not test private methods directly; test through the public API

## Adding New Features

1. Add or update the parameter in `PluginParameters` (and its `Builder`)
2. Wire the parameter through `AbstractParentMojo` if it is user-facing
3. Implement the logic in the sorter module (typically a new `Wrapper` implementation or a new phase in an existing wrapper)
4. Register the wrapper in `WrapperFactoryImpl` if needed
5. Add fixture XML files and tests covering all branches
6. Run `mvn verify` to confirm 100% coverage and passing integration tests
