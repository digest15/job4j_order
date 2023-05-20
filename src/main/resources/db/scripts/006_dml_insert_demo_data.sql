INSERT INTO customers (id, name) VALUES (1, 'Petr Arsentev');

INSERT INTO dishes (id, name, description) VALUES (1, 'Hot Dog', 'Dog and Dough');

INSERT INTO orders (id, customer_id) VALUES (1, 1);

INSERT INTO orders_dishes (id, order_id, dish_id) VALUES (1, 1, 1);

INSERT INTO orders_status (id, order_id, creation_date) VALUES (1, 1, '2022-08-24T15:59:57');





