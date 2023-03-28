DELETE FROM STATES;

INSERT INTO states (name)
VALUES ('PENDING'),
       ('CONFIRMED'),
       ('REJECTED'),
       ('PUBLISHED'),
       ('CANCELED');