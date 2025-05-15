# pioneerPixelTest

Swagger page http://localhost:8080/swagger-ui/index.html#/

Для получения токена необходимо отправить запрос POST на http://localhost:8080/api/auth/login с параметрами username и password

Пример запроса:
```json
{
  "username": "test1",
  "password": "test"
}
```
В ответе приходит токен:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmZWE4MWYzZS1jMjYwLTQ2ZTMtOGVjYi1hY2RjMzY2NGIxYzciLCJpYXQiOjE3NDcyOTc0NzcsImV4cCI6MTc0NzM4Mzg3N30.nYKIGKH3O1_V57xXBpu6aaisJ-5uAclpRACbv7Zrs0s"
}
```

Эндпоинты /api/auth/login и GET /api/users/** публичные.

Для вызова PUT/POST эндпоинтов в хидерах запроса в Authorization нужно передавать данный токен.

