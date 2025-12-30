# Claude Code Instructions

This is a Spring Boot 3 demo project.

## Code Review Guidelines

### Dependency Injection
- SEMPRE usare constructor injection
- MAI usare `new` per creare service/repository
- MAI usare `@Autowired` sui campi

### Database
- SEMPRE usare JPA/Spring Data, MAI JDBC raw
- SEMPRE usare `@Transactional` per operazioni di scrittura
- MAI costruire query SQL con concatenazione stringhe

### Sicurezza
- MAI hardcodare password o secrets nel codice
- SEMPRE validare input con `@Valid`
- MAI restituire direttamente input utente (XSS)
- SEMPRE verificare autorizzazioni sugli endpoint sensibili

### Naming
- Variabili: nomi descrittivi, no abbreviazioni (`user` non `u`)
- Metodi: verbo + sostantivo (`getUsers`, `calculateDiscount`)

## Red Flags
Segnala SEMPRE come Critical:
- Password/secret hardcoded
- SQL injection
- XSS vulnerability
- Endpoint senza autorizzazione
