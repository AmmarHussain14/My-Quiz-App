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

## 📦 How to Run
### Requirements
- Android Studio (latest)!

- Kotlin
- Min SDK 21+

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/My-Quiz-App.git
![WhatsApp Image 2025-11-11 at 9 06 35 PM](https://github.com/user-attachments/assets/056afa72-112c-4fc3-922a-def47f77f5c0)
![WhatsApp Image 2025-11-11 at 9 06 35 PM-2](https://github.com/user-attachments/assets/1aac2a85-7743-4e04-8105-26da886b12a4)
![WhatsApp Image 2025-11-11 at 9 06 36 PM](https://github.com/user-attachments/assets/bacc9019-2fb6-4f55-ac87-d8929106ca4c)
![WhatsApp Image 2025-11-11 at 9 06 35 PM-3](https://github.com/user-attachments/assets/247064b5-7da8-4365-b344-8b5dc66cca9c)
![WhatsApp Image 2025-11-11 at 9 06 36 PM-3](https://github.com/user-attachments/assets/2dd358d1-8a96-4745-b078-75954f7ab495)
![WhatsApp Image 2025-11-11 at 9 06 36 PM-2](https://github.com/user-attachments/assets/8e1ed6c9-7659-4faa-b47d-33ee097c9cac)

