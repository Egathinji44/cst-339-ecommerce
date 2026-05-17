-- NexusStore Seed Data – Milestone 4
-- INSERT IGNORE means re-running on startup will not create duplicate rows.

-- Default admin account  (username: admin / password: admin123)
INSERT IGNORE INTO users (first_name, last_name, email, username, password, role)
VALUES ('Admin', 'User', 'admin@nexusstore.com', 'admin', 'admin123', 'ADMIN');

-- Sample product data
INSERT IGNORE INTO products (product_id, name, description, price, stock_quantity, category, image_url, date_added) VALUES
(1, 'Samsung 65" QLED TV',
    '4K Ultra HD Smart TV with Quantum HDR and Alexa Built-in.',
    899.99, 15, 'Electronics',
    'https://placehold.co/300x200/1a1a2e/ffffff?text=Samsung+TV',
    CURDATE()),
(2, 'Apple iPhone 15',
    '6.1-inch Super Retina XDR display, 48MP camera, USB-C connector.',
    799.99, 30, 'Electronics',
    'https://placehold.co/300x200/1a1a2e/ffffff?text=iPhone+15',
    CURDATE()),
(3, 'Nike Air Max 270',
    'Lightweight running shoe with Max Air unit and breathable mesh upper.',
    150.00, 50, 'Apparel',
    'https://placehold.co/300x200/2d6a4f/ffffff?text=Nike+Air+Max',
    CURDATE()),
(4, 'Levi''s 501 Original Jeans',
    'Classic straight fit jeans in 100% cotton denim. Iconic since 1873.',
    69.99, 75, 'Apparel',
    'https://placehold.co/300x200/2d6a4f/ffffff?text=Levis+501',
    CURDATE()),
(5, 'Sony WH-1000XM5 Headphones',
    'Industry-leading noise canceling wireless headphones with 30-hour battery.',
    349.99, 20, 'Accessories',
    'https://placehold.co/300x200/6b4c93/ffffff?text=Sony+XM5',
    CURDATE()),
(6, 'Anker USB-C Charging Cable 6ft',
    'Braided nylon USB-C to USB-C cable, supports 100W fast charging.',
    19.99, 200, 'Accessories',
    'https://placehold.co/300x200/6b4c93/ffffff?text=Anker+Cable',
    CURDATE());
