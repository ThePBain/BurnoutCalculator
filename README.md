# Burnout Risk Tracker

**Patrick Bain - SE 2070 - April 23, 2026**

## Project Description

This program tracks a student's risk of academic burnout. Instead of treating
burnout as a yes-or-no condition, it models it as a gradual risk score from 0 to 100
that gets sorted into one of three tiers: **Low**, **Moderate**, or **High**. The
program looks at how many assignments a student has, how heavy each one is, how
close the deadlines are, whether the deadlines are clustered together, how much
sleep the student has been getting, and whether they have had time to recover.
Based on those factors it calculates a risk score, identifies the top driver of
that score, and gives a tailored recommendation.

## Features

- Tracks a `Student` along with their `Assignment`s and `SleepLog`s
- Risk score is split into four normalized components so no single factor dominates:
  - **Workload** (0-40) - assignments weighted by urgency and difficulty
  - **Sleep** (0-25) - penalty based on average hours slept
  - **Density** (0-20) - penalty for deadlines clustered within 3 days
  - **Recovery** (0-15) - penalty for streaks of low-sleep nights
- Reports the **top driver** of the score so the advice is actually specific
- Final report includes a visual progress bar for each component
- `RiskTier` enum replaces magic strings for safer, typo-proof tier logic
- `RecommendationEngine` keeps advice logic separate from score math
- `InputHelper` validates every prompt and re-prompts on bad input instead of crashing
- `--demo` flag loads sample data for easy presentations
- `BurnoutCalculatorTest` provides self-contained tests (no JUnit required)

## Design

| File | Responsibility |
| --- | --- |
| `Student.java` | Holds a name, assignments, and sleep logs |
| `Assignment.java` | A single assignment (title, days until due, weight 1-5) |
| `SleepLog.java` | One night of sleep (day number, hours) |
| `RiskTier.java` | Enum for `LOW`, `MODERATE`, `HIGH` |
| `BurnoutCalculator.java` | Scores the four components and produces the report |
| `RecommendationEngine.java` | Turns a calculated risk into a targeted recommendation |
| `InputHelper.java` | Validated console input helpers |
| `BurnoutTrackerMain.java` | Runs the interactive prompt and the `--demo` flow |
| `BurnoutCalculatorTest.java` | Lightweight test runner |

## How to Run

1. Make sure Java JDK is installed on your computer.
2. Save all files in the same folder.
3. Open a terminal and `cd` into that folder.
4. Compile the program: `javac *.java`
5. Run the interactive program: `java BurnoutTrackerMain`
6. Or run the built-in demo: `java BurnoutTrackerMain --demo`
7. Run the tests: `java BurnoutCalculatorTest`

## Example Run

```
=== Burnout Risk Tracker (demo mode) ===

--- Burnout Risk Report for Demo Student ---
Assignments tracked: 5
Sleep logs tracked:  7

Component breakdown:
  Workload  28.4 / 40.0  [##############------]
  Sleep     17.0 / 25.0  [##############------]
  Density    8.0 / 20.0  [########------------]
  Recovery  10.0 / 15.0  [#############-------]

Risk Score: 63.4 / 100  [#############-------]
Risk Tier:  High
Top driver: Workload

Recommendation: High risk of burnout. Your overall workload is heavy.
Rank assignments by weight and tackle the biggest first. Consider talking
to a teacher or pushing a deadline if possible.
```