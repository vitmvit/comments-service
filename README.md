# comments-service

Данный микросервис работает с комментариями.

Является частью [этого проекта](https://github.com/vitmvit/core-service/tree/dev)

## Swagger

http://localhost:8083/api/doc/swagger-ui/index.html#/

## Реализация

### CommentController

Контроллер поддерживает следующие операции:

- получение комментария по id
- получение всех комментариев (с фильтром по фрагменту текста и имени пользователя)
- получение комментария по id новости
- создание комментария
- редактирование комментария
- удаление комментария

#### GET CommentDto getById(@PathVariable("id") Long id):

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

#### GET PageContentDto<CommentDto> getAll(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize, @RequestParam(value = "username", required = false) String username, @RequestParam(value = "text", required = false) String text):

Request:

```http request
http://localhost:8083/api/comments?text=inspired&username=ssrunner
```

Response:

```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 1,
    "totalElements": 1
  },
  "content": [
    {
      "id": 4,
      "time": "2024-02-17T19:06:01.405",
      "text": "Im so inspired by this. Its a great reminder that with hard work and determination, anything is possible!",
      "username": "fearlessrunner",
      "newsId": 7
    }
  ]
}
```

Если список пуст:

```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 15,
    "totalPages": 0,
    "totalElements": 0
  },
  "content": []
}
```

#### GET PageContentDto<CommentDto> getByNewsId(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize, @PathVariable("id") Long id):

Request:

```http request
http://localhost:8083/api/comments/newsId/7?pageNumber=1&pageSize=2
```

Response:

```json
{
  "page": {
    "pageNumber": 1,
    "pageSize": 2,
    "totalPages": 2,
    "totalElements": 3
  },
  "content": [
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
    }
  ]
}
```

#### POST CommentDto create(@RequestBody CommentCreateDto commentCreateDto):

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

#### PUT CommentDto update(@RequestBody CommentUpdateDto commentUpdateDto):

Request:

```http request
http://localhost:8083/api/comments
```

Body:

```json
{
  "id": 11,
  "time": "2024-10-29T12:01:26.249",
  "text": "This is truly extraordinary! Im blown away by the creativity and talent showcased here.",
  "username": "username123",
  "newsId": 1
}
```

Response:

```json
{
  "id": 11,
  "time": "2024-10-29T12:01:26.249",
  "text": "This is truly extraordinary! Im blown away by the creativity and talent showcased here.",
  "username": "username123",
  "newsId": 1
}
```

Если обновляемый комментарий не найден:

```json
{
  "errorMessage": "Entity not found!",
  "errorCode": 404
}
```

#### DELETE void delete(@PathVariable("id") Long id):

Request:

```http request
http://localhost:8083/api/comments/11
```