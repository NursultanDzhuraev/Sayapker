INSERT INTO users (id, first_name, last_name, email, password, phone_number, image_url, role)
VALUES
    (1, 'Азамат', 'Кубанычбеков', 'azamat.@gmail.com',
     '$2a$12$xqTWUHKcL/Ag4D8cDnsx9.rGGztEy93z3zT604gekjXb6K3MhJpHG', '+996555123456',
     'http://example.com/images/azamat.jpg', 'USER'),
    (2, 'Нурсултан', 'Алимбеков', 'nursultan.@gmail.com',
     '$2a$12$xqTWUHKcL/Ag4D8cDnsx9.rGGztEy93z3zT604gekjXb6K3MhJpHG', '+996700987654',
     'http://example.com/images/nursultan.jpg', 'ADMIN'),
    (3, 'Эльмира', 'Садыкова', 'elmira.@gmail.com',
     '$2a$12$xqTWUHKcL/Ag4D8cDnsx9.rGGztEy93z3zT604gekjXb6K3MhJpHG', '+996777456789',
     'http://example.com/images/elmira.jpg', 'USER'),
    (4, 'Бакыт', 'Тойгонбаев', 'bakyt.@gmail.com',
     '$2a$12$xqTWUHKcL/Ag4D8cDnsx9.rGGztEy93z3zT604gekjXb6K3MhJpHG', '+996550111222',
     'http://example.com/images/bakyt.jpg', 'USER'),
    (5, 'Айгуль', 'Жумаева', 'aigul.@gmail.com',
     '$2a$12$xqTWUHKcL/Ag4D8cDnsx9.rGGztEy93z3zT604gekjXb6K3MhJpHG', '+996700333444',
     'http://example.com/images/aigul.jpg', 'USER');

INSERT INTO horses (id, name, birth_date, gender, breed, homeland, information, status, reason_of_rejection, registration_date, user_id)
VALUES
    (1, 'Ак Шумкар', '2018-05-10', 'MALE', 'Кыргыз Аты','Кыргызстан',
     'Чистокровный кыргызский жорго, быстрый и выносливый.', 'ACCEPTED', NULL,
     '2025-01-15', 1),
    (2, 'Кара Жорго', '2019-03-22', 'MALE', 'Ахалтекинская', 'Туркменистан',
      'Элегантный и грациозный конь.',  'PENDING', NULL, '2025-02-01',
     2),
    (3, 'Сырга', '2020-07-12', 'FEMALE', 'Кыргыз Аты', 'Кыргызстан',
      'Спокойная и дружелюбная кобыла.',  'REJECTED', 'Недостаточно документов',
     '2025-03-10', 3),
    (4, 'Буран', '2017-11-05', 'MALE', 'Орловский рысак', 'Россия',
     'Сильный и выносливый конь для скачек.', 'ACCEPTED', NULL,
     '2025-04-01', 4),
    (5, 'Жылдыз', '2021-01-30', 'FEMALE', 'Арабская', 'ОАЭ',
     'Идеальна для длительных прогулок.', 'ACCEPTED', NULL, '2025-04-20', 5);

INSERT INTO likes (id, user_id, horse_id)
VALUES
    (1, 1, 2),
    (2, 2, 1),
    (3, 3, 4),
    (4, 4, 1),
    (5, 5, 3);
INSERT INTO horse_ancestors (horse_id, ancestors_key, ancestors)
VALUES
    (1, 'father', 'Боз Аю'),
    (1, 'mother', 'Кара Кыз'),
    (2, 'father', 'Тулпар'),
    (2, 'mother', 'Жылдыз'),
    (3, 'father', 'Ак Булак'),
    (3, 'mother', 'Айсулуу'),
    (4, 'father', 'Гром'),
    (4, 'mother', 'Молния'),
    (5, 'father', 'Шейх'),
    (5, 'mother', 'Звезда');


INSERT INTO horse_images ( horse_id,images)
VALUES
(1,'https://static.vecteezy.com/system/resources/previews/026/762/517/non_2x/white-horse-running-on-the-sand-created-with-generative-ai-technology-photo.jpg'),
(1,'https://static.vecteezy.com/system/resources/previews/026/762/517/non_2x/white-horse-running-on-the-sand-created-with-generative-ai-technology-photo.jpg'),
(1,'https://static.vecteezy.com/system/resources/previews/026/762/517/non_2x/white-horse-running-on-the-sand-created-with-generative-ai-technology-photo.jpg'),
(2,'https://static.vecteezy.com/system/resources/previews/026/762/517/non_2x/white-horse-running-on-the-sand-created-with-generative-ai-technology-photo.jpg'),
(2,'https://static.vecteezy.com/system/resources/previews/026/762/517/non_2x/white-horse-running-on-the-sand-created-with-generative-ai-technology-photo.jpg'),
(3,'https://static.vecteezy.com/system/resources/previews/026/762/517/non_2x/white-horse-running-on-the-sand-created-with-generative-ai-technology-photo.jpg'),
(3,'https://static.vecteezy.com/system/resources/previews/026/762/517/non_2x/white-horse-running-on-the-sand-created-with-generative-ai-technology-photo.jpg'),
(3,'https://static.vecteezy.com/system/resources/previews/026/762/517/non_2x/white-horse-running-on-the-sand-created-with-generative-ai-technology-photo.jpg'),
(4,'https://static.vecteezy.com/system/resources/previews/026/762/517/non_2x/white-horse-running-on-the-sand-created-with-generative-ai-technology-photo.jpg'),
(4,'https://static.vecteezy.com/system/resources/previews/026/762/517/non_2x/white-horse-running-on-the-sand-created-with-generative-ai-technology-photo.jpg'),
(4,'https://static.vecteezy.com/system/resources/previews/026/762/517/non_2x/white-horse-running-on-the-sand-created-with-generative-ai-technology-photo.jpg'),
(5,'https://static.vecteezy.com/system/resources/previews/026/762/517/non_2x/white-horse-running-on-the-sand-created-with-generative-ai-technology-photo.jpg'),
(5,'https://static.vecteezy.com/system/resources/previews/026/762/517/non_2x/white-horse-running-on-the-sand-created-with-generative-ai-technology-photo.jpg'),
(5,'https://static.vecteezy.com/system/resources/previews/026/762/517/non_2x/white-horse-running-on-the-sand-created-with-generative-ai-technology-photo.jpg');