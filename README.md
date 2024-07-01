# comments-service

Данный микросервис работает с комментариями.

Является частью [этого проекта](https://github.com/vitmvit/core-service/tree/dev)

## Swagger

http://localhost:8083/api/doc/swagger-ui/index.html#/

## Реализация:

В каждом запросе необходимо передавать token в заголовке

### CommentController

Контроллер поддерживает следующие операции:

- получение комментария по id
- получение всех комментариев
- получение комментария по id новости
- поиск по фрагменту текста
- поиск по фрагменту никнейма пользователя
- создание комментария
- редактирование комментария
- удаление комментария

#### GET запрос getById(Long id)

Request:

```http request
http://localhost:8083/api/comments/1
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T19:06:01.405",
  "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
  "username": "username123",
  "newsId": 1
}
```

Если комментарий не найден:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

#### GET запрос getAll()

Request:

```http request
http://localhost:8083/api/comments
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-17T19:06:01.405",
      "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
      "username": "username123",
      "newsId": 1
    },
    {
      "id": 2,
      "time": "2024-02-17T19:06:01.405",
      "text": "I cant believe how much effort and dedication must have gone into creating something this beautiful. Absolutely stunning!",
      "username": "coolcat85",
      "newsId": 2
    },
    {
      "id": 3,
      "time": "2024-02-17T19:06:01.405",
      "text": "These findings are groundbreaking and have the potential to change the way we think about the world. Impressive work!",
      "username": "fearlessrunner",
      "newsId": 7
    },
    {
      "id": 4,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im so inspired by this. Its a great reminder that with hard work and determination, anything is possible!",
      "username": "fearlessrunner",
      "newsId": 7
    },
    {
      "id": 5,
      "time": "2024-02-17T19:06:01.405",
      "text": "This is an important topic that needs to be discussed more. Thank you for shedding light on it.",
      "username": "musiclover27",
      "newsId": 7
    },
    {
      "id": 6,
      "time": "2024-02-17T19:06:01.405",
      "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
      "username": "adventureseeker",
      "newsId": 3
    },
    {
      "id": 7,
      "time": "2024-02-17T19:06:01.405",
      "text": "Youve captured the essence of the subject matter perfectly. It evokes so many emotions and thoughts.",
      "username": "techguru99",
      "newsId": 4
    },
    {
      "id": 8,
      "time": "2024-02-17T19:06:01.405",
      "text": "Bravo! This is exactly what we need right now. Your message is powerful and impactful.",
      "username": "techguru99",
      "newsId": 5
    },
    {
      "id": 9,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im in awe of the level of detail and craftsmanship displayed here. Pure artistry at its finest!",
      "username": "adventureseeker",
      "newsId": 10
    },
    {
      "id": 10,
      "time": "2024-02-17T19:06:01.405",
      "text": "Thank you for sharing this information. Its eye-opening and thought-provoking. Keep up the great work!",
      "username": "smarthacker76",
      "newsId": 10
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 10,
  "last": true,
  "size": 15,
  "number": 0,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "numberOfElements": 10,
  "first": true,
  "empty": false
}
```

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### GET запрос getByNewsId(Integer offset, Integer limit, Long id)

Request:

```http request
http://localhost:8083/api/comments/news-id/1?offset=0&limit=15
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-17T19:06:01.405",
      "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
      "username": "username123",
      "newsId": 1
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 1,
  "last": true,
  "size": 15,
  "number": 0,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "numberOfElements": 1,
  "first": true,
  "empty": false
}
```

#### GET запрос searchByText(Integer offset, Integer limit, String fragment)

Request:

```http request
http://localhost:8083/api/comments/search/text/t?offset=0&limit=15
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-17T19:06:01.405",
      "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
      "username": "username123",
      "newsId": 1
    },
    {
      "id": 2,
      "time": "2024-02-17T19:06:01.405",
      "text": "I cant believe how much effort and dedication must have gone into creating something this beautiful. Absolutely stunning!",
      "username": "coolcat85",
      "newsId": 2
    },
    {
      "id": 3,
      "time": "2024-02-17T19:06:01.405",
      "text": "These findings are groundbreaking and have the potential to change the way we think about the world. Impressive work!",
      "username": "fearlessrunner",
      "newsId": 7
    },
    {
      "id": 4,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im so inspired by this. Its a great reminder that with hard work and determination, anything is possible!",
      "username": "fearlessrunner",
      "newsId": 7
    },
    {
      "id": 5,
      "time": "2024-02-17T19:06:01.405",
      "text": "This is an important topic that needs to be discussed more. Thank you for shedding light on it.",
      "username": "musiclover27",
      "newsId": 7
    },
    {
      "id": 6,
      "time": "2024-02-17T19:06:01.405",
      "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
      "username": "adventureseeker",
      "newsId": 3
    },
    {
      "id": 7,
      "time": "2024-02-17T19:06:01.405",
      "text": "Youve captured the essence of the subject matter perfectly. It evokes so many emotions and thoughts.",
      "username": "techguru99",
      "newsId": 4
    },
    {
      "id": 8,
      "time": "2024-02-17T19:06:01.405",
      "text": "Bravo! This is exactly what we need right now. Your message is powerful and impactful.",
      "username": "techguru99",
      "newsId": 5
    },
    {
      "id": 9,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im in awe of the level of detail and craftsmanship displayed here. Pure artistry at its finest!",
      "username": "adventureseeker",
      "newsId": 10
    },
    {
      "id": 10,
      "time": "2024-02-17T19:06:01.405",
      "text": "Thank you for sharing this information. Its eye-opening and thought-provoking. Keep up the great work!",
      "username": "smarthacker76",
      "newsId": 10
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 10,
  "last": true,
  "size": 15,
  "number": 0,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "numberOfElements": 10,
  "first": true,
  "empty": false
}
```

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### GET запрос searchByUsername(Integer offset, Integer limit, String fragment)

Request:

```http request
http://localhost:8083/api/comments/search/username/u?offset=0&limit=15
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-17T19:06:01.405",
      "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
      "username": "username123",
      "newsId": 1
    },
    {
      "id": 3,
      "time": "2024-02-17T19:06:01.405",
      "text": "These findings are groundbreaking and have the potential to change the way we think about the world. Impressive work!",
      "username": "fearlessrunner",
      "newsId": 7
    },
    {
      "id": 4,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im so inspired by this. Its a great reminder that with hard work and determination, anything is possible!",
      "username": "fearlessrunner",
      "newsId": 7
    },
    {
      "id": 5,
      "time": "2024-02-17T19:06:01.405",
      "text": "This is an important topic that needs to be discussed more. Thank you for shedding light on it.",
      "username": "musiclover27",
      "newsId": 7
    },
    {
      "id": 6,
      "time": "2024-02-17T19:06:01.405",
      "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
      "username": "adventureseeker",
      "newsId": 3
    },
    {
      "id": 7,
      "time": "2024-02-17T19:06:01.405",
      "text": "Youve captured the essence of the subject matter perfectly. It evokes so many emotions and thoughts.",
      "username": "techguru99",
      "newsId": 4
    },
    {
      "id": 8,
      "time": "2024-02-17T19:06:01.405",
      "text": "Bravo! This is exactly what we need right now. Your message is powerful and impactful.",
      "username": "techguru99",
      "newsId": 5
    },
    {
      "id": 9,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im in awe of the level of detail and craftsmanship displayed here. Pure artistry at its finest!",
      "username": "adventureseeker",
      "newsId": 10
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 8,
  "last": true,
  "size": 15,
  "number": 0,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "numberOfElements": 8,
  "first": true,
  "empty": false
}
```

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### POST запрос create(CommentCreateDto commentCreateDto, String auth)

Request:

```http request
http://localhost:8083/api/comments
```

Body:

```json
{
  "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
  "username": "username123",
  "newsId": 1
}
```

Response:

```json
{
  "id": 12,
  "time": "2024-02-20T21:25:13.167",
  "text": "Wow, this is truly extraordinary! Im blown away by the creativity and talent showcased here.",
  "username": "username123",
  "newsId": 1
}
```

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

#### PUT запрос update(CommentUpdateDto commentUpdateDto, String auth)

Request:

```http request
http://localhost:8083/api/comments
```

Body:

```json
{
  "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
  "username": "username123",
  "newsId": 1,
  "id": 1
}
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-17T19:06:01.405",
  "text": "I never cease to be amazed by the level of innovation in this field. Truly mind-boggling!",
  "username": "username123",
  "newsId": 1
}
```

Если текущий пользователь имеет доступ к этой функции, но обновляемый комментарий не найден:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

#### DELETE запрос delete(Long id, Long userId, String auth)

Request:

```http request
http://localhost:8083/api/comments/1/4
```

Если текущий пользователь не имеет доступа к этой функции:

```json
{
  "errorMessage": "No access",
  "errorCode": 403
}
```

