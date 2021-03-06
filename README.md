# Trace Doc REST API Testing

### Введение

Тестирование REST API системы основано на отправке запросов через **RestAssured** библиотеку, которая обвёрнута в **Serenity** и тем самым в коде для отправки запроса пишем **SerenityRest** и далее через точечную нотацию параметры запроса.
Использование **SerenityRest** позволяет пользоваться всем возможностями библиотеки **RestAssured** и получать при этом подробный и наглядный отчёт с деталями каждого вызова.

### Структура проекта

Все существующие в TD эндпоинты реализованы в API библиотеке **TDU_lib**, которая находится в **/test/java/tracedoc/api**. 
В этой же папке находится файл **TDUEndpoints**, перечисляющий все имеющиеся эндпоинты. 
В url эндпоинтов используется форматированная строка, чтобы при отправке запроса можно было вставлять данные полученные из предыдущего запроса.

В папке **JsonObjects** находятся классы для сериализации параметров в тело запроса в формате JSON.

В папке **settings** находится файл **settings** в котором хранятся данные о базовом url для отправки запроса, а так же данные авторизации.

Тесты написаны с использованием BDD методологии фреймворка Cucumber на русском языке для удобства чтения заказчиком.
Файлы с описанием сценария расположены в **/src/test/resource/features**. Шаги из сценариев реализованы в соответствующих классах в папке **stepdefinitions**.
В шагах используется библиотека **TDU_lib** для отправки запросов, и выполняется проверка результата.

Дополнительно в шагах тестов используется генерация текста для наполнения файла для загрузки в систему. 
Для это реализован простой генератор, который находится в папке **text_generator**.
Метод **generateString** принимат в качестве параметра цифровое значение, означающее количество сгенерированных фраз.

### Запуск тестов

Запуск теста осуществляется с помощью команды **mvn clean verify** из консоли.
И после выполнения тестов формируется html версия отчёта, которая сохраняется в **../td-tests/target/site/serenity/index.html**

Для того чтобы команда **mvn clean verify** работала, необходимо установить **Maven**.
Иначе вы увидите в консоли ответ `"mvn" не является внутренней или внешней исполняемой командой`.

Чтобы установить Maven, необходимо скачать его с официального сайта (https://maven.apache.org/download.cgi) и распаковать определённое место, лучше всего в C:\Program Files\maven...
Далее прописать в глобальную переменную **PATH** среды Windows путь к **bin** директории в той папке куда вы разархивировали Maven.

Также для работы Maven-а потребуется установка JDK, который тоже можно скачать с официального сайта (https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)

Для написания и редактирования тестов необходимо использовать IDE поддерживающую язык разработки Java. 
Мой личный выбор IntelliJ IDEA (https://www.jetbrains.com/idea/download/)