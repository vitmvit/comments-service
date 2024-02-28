INSERT INTO "comment" ("id", "news_id", "time", "text", "username")
VALUES (1, 1, '2024-02-15 10:08:25.382904', 'text', 'nameOne'),
       (2, 1, '2024-02-15 12:29:50.081166', 'text', 'nameTwo'),
       (3, 1, '2024-02-15 12:30:01.129431', 'text', 'nameThree'),
       (4, 1, '2024-02-15 12:30:02.091136', 'text', 'nameFour'),
       (5, 1, '2024-02-15 12:31:38.069381', 'text', 'nameFive'),
       (6, 1, '2024-02-15 12:32:29.827384', 'text', 'nameSix'),
       (7, 1, '2024-02-15 12:33:50.833255', 'text', 'nameSeven');

ALTER TABLE "comment" DROP CONSTRAINT comment_pkey;