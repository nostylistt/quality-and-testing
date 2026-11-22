# Javers Audit

## Зауваження

1. Немає жодної інформації про те, хто і коли створив або змінив item.
2. Поля аудиту Spring Data зберігають тільки останню зміну, історії змін немає.
3. Після видалення item неможливо дізнатись, яким він був.
4. Немає запитів, через які можна подивитись історію змін.

## Що зроблено

1. Додана залежність javers-spring-boot-starter-mongo, історія зберігається в MongoDB.
2. Додано клас AuditMetadata з полями createdDate, createdBy, lastModifiedDate, lastModifiedBy, Item його наслідує.
3. Додані AuditionConfiguration з @EnableMongoAuditing і AuditorAwareImpl, який повертає ім'я користувача системи.
4. Час аудиту обрізається до мілісекунд, щоб Javers не показував зайві зміни createdDate.
5. Додано JaversConfiguration з AuthorProvider, автор змін ADMIN.
6. ItemRepository позначений @JaversSpringDataAuditable, тому кожне створення, оновлення і видалення item потрапляє в історію.
7. Видалення виконується через об'єкт, а не через id, тому Javers фіксує і видалення.
8. Логіка аудиту винесена в AuditService.
9. Додано AuditController з п'ятьма запитами:
   GET /api/v1/audit/items/changes, усі зміни всіх item
   GET /api/v1/audit/items/shadows, усі знімки всіх item
   GET /api/v1/audit/items/{id}/shadows, історія одного item
   GET /api/v1/audit/items/{id}/changes, зміни одного item у JSON
   GET /api/v1/audit/items/{id}/changes/pretty, зміни одного item у вигляді тексту
10. Equals в Item більше не падає, якщо id ще не заданий.
