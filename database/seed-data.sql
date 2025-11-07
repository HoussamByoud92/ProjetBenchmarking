-- Seed 2000 categories
INSERT INTO category (code, name, updated_at)
SELECT 
    'CAT' || LPAD(num::text, 4, '0'),
    'Category ' || num,
    NOW()
FROM generate_series(1, 2000) AS t(num)
ON CONFLICT DO NOTHING;

-- Seed 100,000 items (~50 per category)
INSERT INTO item (sku, name, price, stock, category_id, updated_at, description)
SELECT
    'SKU' || LPAD(num::text, 6, '0'),
    'Item ' || num,
    (random() * 1000)::numeric(10,2),
    (random() * 1000)::int,
    ((num - 1) / 50) + 1,
    NOW(),
    'Description for item ' || num
FROM generate_series(1, 100000) AS t(num)
ON CONFLICT DO NOTHING;
