# comments-service

Данный микросервис работает с комментариями

Реалиазация

## Реализация:

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
http://localhost:8082/api/comments/3
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-15T05:58:03.962",
  "text": "text",
  "username": "name",
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
http://localhost:8082/api/comments
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-15T05:58:03.962",
      "text": "text",
      "username": "name",
      "newsId": 1
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
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
    "sorted": false,
    "unsorted": true
  },
  "numberOfElements": 1,
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
http://localhost:8082/api/comments/news-id/1
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-15T10:08:25.382",
      "text": "text",
      "username": "name",
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

Если список пуст:

```json
{
  "errorMessage": "List is empty!",
  "errorCode": 404
}
```

#### GET запрос searchByText(Integer offset, Integer limit, String fragment)

Request:

```http request
http://localhost:8082/api/comments/search/text/n
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-15T10:08:25.382",
      "text": "text",
      "username": "name",
      "newsId": 1
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
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
    "sorted": false,
    "unsorted": true
  },
  "numberOfElements": 1,
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
http://localhost:8082/api/comments/search/username/n
```

Response:

```json
{
  "content": [
    {
      "id": 1,
      "time": "2024-02-15T10:08:25.382",
      "text": "text",
      "username": "name",
      "newsId": 1
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
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
    "sorted": false,
    "unsorted": true
  },
  "numberOfElements": 1,
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

#### POST запрос create(CommentCreateDto commentCreateDto)

Request:

```http request
http://localhost:8082/api/comments
```

Body:

```json
{
  "text": "text2",
  "username": "name",
  "newsId": 1
}
```

Response:

```json
{
  "id": 2,
  "time": "2024-02-15T09:01:36.871",
  "text": "text2",
  "username": "name",
  "newsId": 1
}
```

#### PUT запрос update(CommentUpdateDto commentUpdateDto)

Request:

```http request
http://localhost:8082/api/comments
```

Body:

```json
{
  "id": 1,
  "text": "text",
  "username": "name",
  "newsId": 1
}
```

Response:

```json
{
  "id": 1,
  "time": "2024-02-15T09:38:56.493",
  "text": "text",
  "username": "name",
  "newsId": 1
}
```

#### DELETE запрос delete(Long id)

Request:

```http request
http://localhost:8082/api/comments/1
```

