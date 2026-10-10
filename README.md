# Final Project: AccountProcessor Testing

![CI](https://github.com/scott-jwu23/final-project/actions/workflows/ci.yml/badge.svg)

A JUnit 5 test suite for `AccountProcessor.processBalance`, with JaCoCo coverage analysis and a GitHub Actions pipeline.

## Project structure
- `src/main/java/AccountProcessor.java`: code under test
- `src/test/java/AccountProcessorTest.java`: black-box (equivalence partitioning, boundary values) and white-box (CFG paths, def-use pairs) tests
- `docs/`: CFG diagram, JaCoCo screenshots, CI screenshot
- `.github/workflows/ci.yml`: CI workflow

## Run the tests locally
Requires JDK 17 and Maven.

```bash
mvn clean test
open target/site/jacoco/index.html
```

The second command opens the JaCoCo coverage report in your browser (macOS).

## Continuous integration
The workflow in `.github/workflows/ci.yml` runs on every push to `main` and on every pull request targeting `main`.

Steps:
1. Check out the repository
2. Set up JDK 17 (Temurin) with Maven dependency caching
3. Run `mvn clean verify`, which compiles the code, runs all JUnit tests, and generates the JaCoCo report
4. Upload the JaCoCo report as a downloadable artifact named `jacoco-report`

To see results, open the **Actions** tab on GitHub. A failing test fails the build. To view coverage from a CI run, open the run and download the `jacoco-report` artifact.

## Coverage
Branch coverage is 100%. The only uncovered line is the implicit default constructor, which contains no logic. See `docs/jacoco-summary.png` and `docs/jacoco-detail.png`.