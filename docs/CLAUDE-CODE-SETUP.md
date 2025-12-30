# Claude Code GitHub Actions Setup

Guida per integrare Claude Code come reviewer automatico nelle PR.

## Prerequisiti

- Repository GitHub
- API key Anthropic ([console.anthropic.com](https://console.anthropic.com))

## Setup

### 1. Aggiungere il secret

Repository → Settings → Secrets and variables → Actions → New repository secret

- **Name:** `ANTHROPIC_API_KEY`
- **Value:** la tua API key

### 2. Creare il workflow

`.github/workflows/claude.yml`:

```yaml
name: Claude Code

on:
  pull_request:
    types: [opened, synchronize]
  issue_comment:
    types: [created]
  pull_request_review_comment:
    types: [created]

permissions:
  contents: read
  pull-requests: write
  issues: write
  id-token: write

jobs:
  claude-review:
    if: github.event_name == 'pull_request'
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 1

      - name: Check for custom guidelines
        id: guidelines
        run: |
          if [ -f ".claude/CODE_REVIEW_GUIDELINES.md" ]; then
            echo "has_custom=true" >> $GITHUB_OUTPUT
            GUIDELINES=$(cat .claude/CODE_REVIEW_GUIDELINES.md)
            echo "custom_content<<EOF" >> $GITHUB_OUTPUT
            echo "$GUIDELINES" >> $GITHUB_OUTPUT
            echo "EOF" >> $GITHUB_OUTPUT
          else
            echo "has_custom=false" >> $GITHUB_OUTPUT
          fi

      - name: Claude Review (Custom Guidelines)
        if: steps.guidelines.outputs.has_custom == 'true'
        uses: anthropics/claude-code-action@v1
        with:
          anthropic_api_key: ${{ secrets.ANTHROPIC_API_KEY }}
          claude_args: '--max-turns 15'
          prompt: |
            REPO: ${{ github.repository }}
            PR #${{ github.event.pull_request.number }}

            ## Team Guidelines
            ${{ steps.guidelines.outputs.custom_content }}

            ## Instructions
            Review following team guidelines. Create inline comments.
            Severity: 🔴 Critical, 🟠 Major, 🟡 Minor, 🟢 Suggestion

      - name: Claude Review (Default)
        if: steps.guidelines.outputs.has_custom == 'false'
        uses: anthropics/claude-code-action@v1
        with:
          anthropic_api_key: ${{ secrets.ANTHROPIC_API_KEY }}
          claude_args: '--max-turns 15'
          prompt: |
            Review this PR with Java/Spring Boot best practices.
            Create inline comments on issues found.

  claude-respond:
    if: github.event_name == 'issue_comment' || github.event_name == 'pull_request_review_comment'
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: anthropics/claude-code-action@v1
        with:
          anthropic_api_key: ${{ secrets.ANTHROPIC_API_KEY }}
```

## Custom Guidelines

Per personalizzare la review, crea `.claude/CODE_REVIEW_GUIDELINES.md`:

```markdown
# Code Review Guidelines

## Regole Obbligatorie
- Ogni endpoint DEVE avere validazione input
- Usare constructor injection, non @Autowired
- Test coverage minimo 80%

## Convenzioni
- Package: com.example.{service}.{layer}
- DTO come Java Record

## Red Flags 🚨
- Query SQL con concatenazione stringhe
- Password hardcoded
- Log di dati sensibili
```

**Comportamento:**
- Se il file esiste → review basata sulle tue guidelines
- Se non esiste → review con best practices Java/Spring Boot di default

## Jobs

| Job | Trigger | Descrizione |
|-----|---------|-------------|
| `claude-review` | PR aperta/aggiornata | Review automatica con commenti inline |
| `claude-respond` | `@claude` nei commenti | Interazione diretta con Claude |

## Note tecniche

- **Non specificare `--allowedTools`**: l'action usa i default MCP corretti
- **`id-token: write`**: necessario per autenticazione OIDC
- **`--max-turns`**: limita iterazioni Claude (controllo costi)

## Riferimenti

- [Documentazione ufficiale](https://code.claude.com/docs/en/github-actions)
- [Repository action](https://github.com/anthropics/claude-code-action)
