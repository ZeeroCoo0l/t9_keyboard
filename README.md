# 📱 T9 Text Prediction App

This is a JavaFX-based text input app that mimics the classic **T9 predictive text system** — the kind you’d find on old-school mobile phones. It's built using Java 21, JavaFX, and FXML, with a custom word suggestion engine under the hood.

> 🚀 Made over a weekend to learn more about the T9-keyboard!

---

## ✨ Features

- Simulates T9 input using JavaFX buttons
- Predictive word suggestions based on numeric input
- Support for learning: Prioritizes frequently used ones
- Scroll through word suggestions
- Persistent word memory saved locally

---

## 🧠 How it works

- Words are loaded into a `TreeMap` keyed by their T9 sequence (e.g., `"4663"` → `"GOOD"`)
- A `PriorityQueue` ranks word suggestions based on usage frequency and term frequency
- The UI is built using JavaFX + FXML and updates suggestions in real-time as you "type"

---

## 🛠 How to Run

### Prerequisites

- Java 21+
- JavaFX SDK (you can configure it through your IDE or via Maven)
