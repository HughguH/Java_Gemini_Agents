# Conversational Support AI Agents (Java + Gemini)

Zadanie rekrutacyjne: stworzenie dwóch współpracujących agentów AI do obsługi wsparcia klienta w jednej konwersacji.

## Opis rozwiązania

Projekt implementuje system konwersacyjnego wsparcia z dwoma specjalistycznymi agentami:

- **Technical Agent** – odpowiada na pytania techniczne, korzystając z lokalnych dokumentów (RAG).
- **Billing Agent** – obsługuje kwestie rozliczeniowe, w tym trzy narzędzia (tool calling):
  - `get_current_plan` – sprawdzenie aktualnego planu użytkownika
  - `explain_refund_policy` – wyjaśnienie polityki zwrotów
  - `create_refund_request` – wygenerowanie linku do wniosku o zwrot

### Kluczowe funkcjonalności
- Automatyczny routing pytań do odpowiedniego agenta na podstawie decyzji LLM (Gemini)
- Wyszukiwanie relevantnych fragmentów z lokalnych dokumentów technicznych (case-insensitive)
- Obsługa tool calling w agencie Billing z interaktywnym pytaniem o User ID
- Pełna obsługa multi-turn conversation (wielokrokowa konwersacja)
- Brak użycia zewnętrznych bibliotek agentowych (czysta Java + oficjalne SDK Gemini)
- Graceful handling pytań spoza zakresu obu agentów

### PROMPTY NALEŻY WYSYŁAĆ W JĘZYKU ANGIELSKIM

## Jak uruchomić

1. Sklonuj repozytorium
2. Ustaw zmienną środowiskową z kluczem API
3. Uruchom program : )
