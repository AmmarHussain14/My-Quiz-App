# My-Quiz-App
# 📚 MCQ Quiz App

A simple and engaging **Multiple Choice Quiz App** built in Android using Kotlin.  
The app loads questions from a provided JSON API, displays one question at a time, tracks answer streaks, and shows a final results summary at the end.

---

## 🚀 Features

### 1. Launch & Load
- Loads questions from the provided JSON (local or API).
- Parses into `List<Question>`.
- Displays a **splash/loading screen** while preparing data.

### 2. Quiz Flow
- Each screen shows:
  - Question text
  - Four answer options
  - Skip button

- When user selects an option:
  - Correct & selected answers are highlighted.
  - After 2 seconds, automatically moves to the next question.

- If user selects **Skip**, moves immediately to the next question.

### Streak System
- Tracks **consecutive correct answers**.
- When streak reaches **3**, a special **streak badge** lights up ✨.
- Any wrong answer → streak resets to **0**.
- Longest streak is stored and shown at the end.

### 3. Result Screen
After all 10 questions are completed:
- Shows Score: **Correct / Total**
- Shows **Longest Streak**
- Option to **Restart Quiz**

---

## 🎨 UI & Experience
- Smooth transitions and micro-animations.
- Simple, clean layout with Material Design components.
- Consistent colors, typography, spacing.
- Accessible, clean tap targets & readable text.

---

## 🏛️ Architecture
- **MVVM Pattern**
- Clear separation of:
  - UI Layer (Activities / Fragments)
  - State / ViewModel
  - Data / Repository

### Key Classes
| Component | Responsibility |
|----------|----------------|
| `QuestionRepository` | Loads and parses questions JSON |
| `QuizViewModel` | Holds question index, score, streak logic & UI state |
| `QuestionScreen` | Displays one question, listens to UI events |
| `ResultScreen` | Shows score summary and restart option |

---
# 🧠 Streak Logic (Simplified)
if (userAnswer == correctAnswer) {
streak++
if (streak >= 3) showStreakBadge()
} else {
streak = 0
}

---

![WhatsApp Image 2025-11-11 at 9 06 36 PM](https://github.com/user-attachments/assets/04da00b0-07f8-4315-b2ee-3b51c91fc7f8)
![WhatsApp Image 2025-11-11 at 9 06 36 PM-3](https://github.com/user-attachments/assets/293f3d11-55b7-455e-8e13-5ddcf8a0ae0c)
![WhatsApp Image 2025-11-11 at 9 06 36 PM-2](https://github.com/user-attachments/assets/d6f3171e-a084-4e3f-8a94-894a561a932d)
![WhatsApp Image 2025-11-11 at 9 06 35 PM](https://github.com/user-attachments/assets/28d28e56-eee0-414f-990f-4a751980ed4e)
![WhatsApp Image 2025-11-11 at 9 06 35 PM-3](https://github.com/user-attachments/assets/f36863d5-e9c9-4e3c-b952-869c7e8bfb72)
![WhatsApp Image 2025-11-11 at 9 06 35 PM-2](https://github.com/user-attachments/assets/424cd316-40e3-48be-8050-4ec92cd530c8)

## 📦 How to Run
### Requirements
- Android Studio (latest)!

- Kotlin
- Min SDK 21+

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/My-Quiz-App.git
