# Основная часть
GoidaCards  - мобильная игра в жанре карточный rogue-like. Является курсовой работой 2-го курса по дисциплине "технологии и методы программирования". Так же продвигается эта игра на курсе Samsung innovation campus java мобильная разработка.
## Основный игровой процесс
Главный геймплей игрока является прохождение подземелья, сражение против врагов с помощью карт, собирание уникальной колоды. Чем лучше игрок этим занимается, тем больше очков он получает в конце и в итоге может попасть лидерборд как человек с самым большим количеством очков.

# Создатели
Клотченко Валерий Вадимович - backend разработчик.
Чаплыгин Виктор Андреевич - frontend разработчик.

# Особая благодарность
Алина Лоскутова - художник
Алина Мезенцева - художник
Богдан Полянский - музыкант

# GoidaGame

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and a main class extending `Game` that sets the first screen.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `android`: Android mobile platform. Needs Android SDK.
- `html`: Web platform using GWT and WebGL. Supports only Java projects.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `android:lint`: performs Android project validation.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `html:dist`: compiles GWT sources. The compiled application can be found at `html/build/dist`: you can use any HTTP server to deploy it.
- `html:superDev`: compiles GWT sources and runs the application in SuperDev mode. It will be available at [localhost:8080/html](http://localhost:8080/html). Use only during development.
- `idea`: generates IntelliJ project data.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
