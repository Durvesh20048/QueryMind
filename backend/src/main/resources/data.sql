INSERT INTO customers (id, name, city, signup_date) VALUES
  (1, 'Aarav Sharma', 'Pune', '2025-01-10'),
  (2, 'Priya Desai', 'Mumbai', '2025-02-14'),
  (3, 'Rohan Patil', 'Kolhapur', '2025-03-02'),
  (4, 'Sneha Kulkarni', 'Pune', '2025-03-20'),
  (5, 'Vikram Joshi', 'Sangli', '2025-04-05');

INSERT INTO products (id, name, category, price) VALUES
  (1, 'Wireless Mouse', 'Electronics', 599.00),
  (2, 'Mechanical Keyboard', 'Electronics', 2499.00),
  (3, 'Yoga Mat', 'Fitness', 799.00),
  (4, 'Water Bottle', 'Fitness', 299.00),
  (5, 'Laptop Stand', 'Electronics', 1199.00),
  (6, 'Running Shoes', 'Fitness', 3499.00);

INSERT INTO orders (id, customer_id, order_date, status) VALUES
  (1, 1, '2025-05-01', 'DELIVERED'),
  (2, 2, '2025-05-03', 'DELIVERED'),
  (3, 1, '2025-05-10', 'DELIVERED'),
  (4, 3, '2025-05-12', 'CANCELLED'),
  (5, 4, '2025-05-15', 'DELIVERED'),
  (6, 5, '2025-05-18', 'PENDING'),
  (7, 2, '2025-05-20', 'DELIVERED');

INSERT INTO order_items (id, order_id, product_id, quantity) VALUES
  (1, 1, 1, 2),
  (2, 1, 3, 1),
  (3, 2, 2, 1),
  (4, 3, 5, 1),
  (5, 4, 6, 1),
  (6, 5, 4, 3),
  (7, 6, 2, 1),
  (8, 7, 1, 1),
  (9, 7, 6, 1);
