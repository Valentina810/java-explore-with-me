# Бекэнд проекта для организации мероприятий
Организатор может разместить в системе событие. 
Пользователи могут просматривать список событий и подавать 
заявки на участие в событиях. После того как заявка подана, 
её можно утвердить или отклонить. Также реализована возможность 
добавления комментариев к событиям (фича).
Также реализован функционал по сбору статистики просмотра мероприятий пользователями.

Проект состоит из трех отдельных микросервисов:
1. Основной сервис, который реализует бизнес-логику (включает в себя
контроллеры для работы с разными ролями: авторизованный пользователь, админ, неавторизованный пользователь),
имеет свою БД.
2. Сервис статистики, в который основной модуль присылает данные в случае, если пользователь просмотрел событие,
а также возвращает основному сервису данные о количестве просмотров события/событий по запросу. Имеет свою БД. 
3. Клиент сервиса статистики, который принимает и обрабатывает запросы к сервису статистики.

## ТЗ
- [Техническое задание для API основного сервиса](./ewm-main-service-spec.json)
- [Техническое задание для API сервиса статистики](./ewm-stats-service-spec.json)
- Для функционала комментариев поставновка задачи в п. Фича комментарии постановка задачи.

# Стек
- Java 11
- Spring Boot
- Hibernate
- PostgreSQL
- Maven
- Lombok
- Junit5, Mockito
- Docker

## Инструкция по локальному развертыванию
1. Должен быть установлен Docker, Java 11 и maven
2. Склонировать проект
3. Для всех модулей выполнить команду mvn package
4. Выполнить команду из папки с проектом docker-compose up
5. Проверить приложение можно с помощью  [postman-тестов](postman)


## Статус и планы по доработке проекта
Проект дорабатывать не планируется

# Фича комментарии постановка задачи

Необходимо реализовать возможность добавлять комментарии к событиям и модерировать их.

## Методы контроллера PrivateCommentController

### POST /users/{userId}/comment/events/{eventId} - добавление комментария

Максимальная длина комментария - 5000 символов. 

Если на входе длина комментария больше 5000 - то добавлять комментарий,
обрезав его до 5000 символов.

Комментарий не может быть пустым,минимальная длина - 1 символ.

При создании комментарий должен иметь статус MODERATION.

Код успешного ответа 201.

Не найден пользователь/событие - код ответа 404.

json на входе:

```
{
    "text":"djfbfdbkdb"
}
```

json на выходе:

```
{
    "id":6,
    "user":1,
    "event":2,
    "text":"djfbfdbkdb",
    "created":"2023-04-05T19:19:49.966914",
    "state":"MODERATION"
}
```

### PATCH /users/{userId}/comment/{commentId} - редактирование комментария

Пользователь может редактировать только свой комментарий, 
при попытке редактирования чужого комментария возвращать код ошибки 403.

Аналогично добавлению - если комментарий больше 5000 символов, 
при обновлении текст комментария обрезвть до 5000 символов.

После редактирования комментарий должен изменить статус на MODERATION.

Код успешного ответа 200.

Не найден пользователь/комментарий - код ответа 404.

json на входе:

```
{
    "text":"djfbfdbkdb"
}
```

json на выходе:

```
{
    "id":6,
    "user":1,
    "event":2,
    "text":"djfbfdbkdb",
    "created":"2023-04-05T19:19:49.966914",
    "state":"MODERATION"
}
```

### DELETE /users/{userId}/comment/{commentId} - удаление комментария

Пользователь может удалить только свой комментарий.

При попытке удаления чужого комментария возвращать код ошибки 403.

Без тела запроса и без тела ответа, код успешного ответа 204.

При выполнении запроса комментарий не удаляется, а меняет статус на REMOVED.

Не найден пользователь/комментарий - код ответа 404.

Если запрошенный для удаления комментарий находится в статусе REMOVED,
то вернуть код ответа 404.

### GET /users/{userId}/comment?from=0&size=10 - вывод комментариев пользователя ко всем событиям постранично

from - количество событий, которые нужно пропустить для формирования текущего набора (по умолчанию 0)

size - количество событий в наборе (по умолчанию 10)

Если комментариев нет - вернуть пустой список.

Отбираются комментарии в статусах PUBLISHED,MODERATION,REJECTED.

Первыми должны отображаться самые новые комментарии.

Код успешного ответа 200.

json на выходе:

```
[
    {
        "id":2,
        "user":1,
        "event":2,
        "text":"djfbfdbkdb",
        "created":"2023-04-05T19:19:49.966914",
        "state":"PUBLISHED"
    },
    {
        "id":6,
        "user":1,
        "event":2,
        "text":"djfbfdbkdb",
        "created":"2023-04-05T19:19:49.966914",
        "state":"PUBLISHED"
    }
]
```

## Методы контроллера AdminCommentController

### PATCH /admin/comment - измение статуса комментария

Можно изменить статус комментария на PUBLISHED

только если текущий статус комментария MODERATION.

Если комментарий в статусе REMOVED то его статус изменять нельзя.

В статус REMOVED можно перевести комментарий из статусов MODERATION,PUBLISHED,REJECTED.

В статус REJECTED можно перевести комментарий из статуса MODERATION.

При неподходящем для обновления статусе комментария возвращать код ответа 409.

Код успешного ответа 200.

json на входе:

```
{
  "commentIds": [
    1,
    2,
    3
  ],
  "state": "PUBLISHED"
}
```

json на выходе:

```
[
    {
        "id":2,
        "user":1,
        "event":2,
        "text":"djfbfdbkdb",
        "created":"2023-04-05T19:19:49.966914",
        "state":"PUBLISHED"
    },
    {
        "id":6,
        "user":1,
        "event":2,
        "text":"djfbfdbkdb",
        "created":"2023-04-05T19:19:49.966914",
        "state":"PUBLISHED"
    }
]
```

## Методы контроллера PublicCommentController
### GET /comment/events/{eventId}?from=0&size=10 - получение комментариев к событию постранично

from - количество событий, которые нужно пропустить для формирования текущего набора (по умолчанию 0)

size - количество событий в наборе (по умолчанию 10)

Если комментариев нет - вернуть пустой список.

Отбираются только комментарии в статусе PUBLISHED.

Первыми должны отображаться самые новые комментарии.

Код успешного ответа 200.

json на выходе:

```
[
    {
        "id":2,
        "user":  {
                    "id": 1,
                    "email": "petrov.i@practicummail.ru",
                    "name": "Петров Иван"
                   },
        "text":"djfbfdbkdb",
        "created":"2023-04-06T19:19:49.966914"
    },
    {
        "id":6,
        "user":  {
                    "id": 1,
                    "email": "petrov.i@practicummail.ru",
                    "name": "Петров Иван"
                   },
        "text":"djfbfdbkdb",
        "created":"2023-04-05T19:19:49.966914"
    }
]
```

### GET /comment/{commentId} - получение комментария по id

Возвращается только комментарий в статусе PUBLISHED.

Код успешного ответа 200.

Если комментарий не найден или имеет статус, отличный от PUBLISHED, вернуть 404.

json на выходе:

```
{
    "id":2,
    "user":  {
                "id": 1,
                "email": "petrov.i@practicummail.ru",
                "name": "Петров Иван"
             },
    "event":2,
    "text":"djfbfdbkdb",
    "created":"2023-04-06T19:19:49.966914"
}
```