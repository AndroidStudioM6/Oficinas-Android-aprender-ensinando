# Oficinas-Android-aprender-ensinando



| Grupo | Tema (Ao final da oficina, o app comum…) | Integrantes                                     |
|:-----:|------------------------------------------|-------------------------------------------------|
| 1 | **Kotlin essencial** (existe e roda, e as funções de validação foram testadas no Logcat) | Lucas, Lucas C. e Antonio                       |
| 2 | **Objetos (POO em Kotlin)** (tem o modelo de dados (Tarefa, Prioridade) e o repositório) | William, A. Vargas e G. Brandão                 |
| 3 | **Coleções** (guarda, filtra e ordena tarefas na memória) | Vitor D., Leonardo, Bernardo, Aldrey, G. Farias |
| 4 | **Componentes de interface** (tem uma tela que cadastra tarefas e as mostra numa lista) | Gabriel S., Gabriel P., Pedro, Joaquim          |
| 5 | **Temas e estilos** (tem identidade visual própria e modo escuro) | A. Biehl, A. Brito e Flavia                     |
| 6 | **Fragments** (está dividido em telas: lista, cadastro e sobre) | Mateus , Jorge, Isaac e Rafaela                 |
| 7 | **Barra de navegação e menus** (navega pela barra inferior e tem menus) | Vitor, Bruno e Otavio                           |
| 8 | **Banco de dados no Android** (salva as tarefas no banco: elas não somem ao fechar o app) | klint, Nicoly, Rafaela C., Mayara, Gustavo      |


## Organização por branches

Para manter o repositório organizado, **cada grupo deve trabalhar exclusivamente na sua própria branch**.

| Grupo | Branch |
|:-----:|--------|
| 1 | `grupo-1-kotlin-essencial` |
| 2 | `grupo-2-objetos-poo` |
| 3 | `grupo-3-colecoes` |
| 4 | `grupo-4-componentes-ui` |
| 5 | `grupo-5-temas-estilos` |
| 6 | `grupo-6-fragments` |
| 7 | `grupo-7-navegacao-menus` |
| 8 | `grupo-8-banco-de-dados` |

### Regras importantes

1. **Cada grupo deve commitar somente na sua branch.** Não faça commits na branch de outro grupo ou na `main`.
2. **A branch principal (`main`) é reservada ao projeto central da disciplina.** Qualquer alteração na `main` que não seja do projeto central, ou que venha de um grupo sem passar pelo processo de integração, é considerada indevida.
3. **Commits fora do lugar serão notificados e revertidos.** Se um commit for feito na `main` ou na branch de outro grupo sem autorização, o responsável será avisado e o commit será revertido (`git revert`) para evitar desorganização do projeto.
4. **A integração com a `main` é feita apenas via Pull Request**, com revisão antes do merge.
5. Antes de abrir o Pull Request, atualize sua branch com a `main`:
```bash
   git checkout grupo-X-nome-da-branch
   git merge main
```

### Como começar

```bash
# clonar o repositório
git clone https://github.com/<usuario>/Oficinas-Android-aprender-ensinando.git
cd Oficinas-Android-aprender-ensinando

# entrar na branch do seu grupo
git checkout grupo-X-nome-da-branch

# trabalhar, commitar e enviar
git add .
git commit -m "feat: descrição do que foi feito"
git push origin grupo-X-nome-da-branch
```

>  Em caso de dúvida sobre em qual branch commitar, pergunte antes de enviar. É mais fácil prevenir do que reverter.
