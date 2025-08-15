-- ----------------------------------------------------
-- USERS
-- ----------------------------------------------------
DO 
$$
    BEGIN
        IF NOT EXISTS (
            SELECT
            FROM
                pg_roles
            WHERE
                rolname = 'SystemAdmin') THEN
        CREATE ROLE "SystemAdmin" WITH LOGIN PASSWORD 'inventhorPokemon';
    END IF;
    END
$$;

GRANT ALL PRIVILEGES ON DATABASE inventhor TO "SystemAdmin";

-- ----------------------------------------------------
-- SCHEMA
-- ----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS inventhor AUTHORIZATION "SystemAdmin";

SET search_path TO inventhor; -- tells the database to use the inventhor schema by default

-- -----------------------------------------------------
-- Table inventhor.audit_log
-- This table stores audit logs for changes made to sensitive tables.
-- It records the table name, operation type (INSERT, UPDATE, DELETE), row data, timestamp, and user who made the change.
-- JSONB is used for row_data to allow flexible storage of different table structures. It can store the entire row data as JSON.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.audit_log (
    auditnr SERIAL PRIMARY KEY,
    tblname VARCHAR(50) NOT NULL,
    operation VARCHAR(10) NOT NULL, -- 'INSERT', 'UPDATE', 'DELETE'
    rowdata JSONB,
    changedat TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changedby VARCHAR(50)
);

-- -----------------------------------------------------
-- Table inventhor.address
-- This table stores addresses for employees, customers, suppliers, and warehouses.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.address (
    addressnr SERIAL NOT NULL PRIMARY KEY,
    country VARCHAR(56) NOT NULL,
    city VARCHAR(100) NOT NULL,
    street VARCHAR(150) NOT NULL,
    postcode VARCHAR(20)
);

-- -----------------------------------------------------
-- Table inventhor.employeerole
-- This table stores the roles of employees, such as Admin or Staff.
CREATE TABLE IF NOT EXISTS inventhor.employeerole (
    rolenr SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

-- -----------------------------------------------------
-- Table inventhor.paymentmethod
-- This table stores the different payment methods available for customer payments.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.paymentmethod (
    paymentmethodnr SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

-- -----------------------------------------------------
-- Table inventhor.employee
-- This table stores employee information, including their role and associated address.
-- Based on the role, employees can have different permissions in the system.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.employee (
    employeenr SERIAL NOT NULL PRIMARY KEY,
    email VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    passwordhash VARCHAR(100), -- Store hashed password for security
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    position VARCHAR(50),
    rolenr INTEGER NOT NULL,
    employeddate DATE,
    isActive BOOLEAN NOT NULL DEFAULT TRUE,
    image VARCHAR(255),
    addressnr INTEGER,
    CONSTRAINT fk_employee_address 
    FOREIGN KEY (addressnr) 
    REFERENCES inventhor.address (addressnr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION,
    CONSTRAINT fk_employee_role
    FOREIGN KEY (rolenr)
    REFERENCES inventhor.employeerole (rolenr)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table inventhor.customer
-- This table stores customer information, including their email, name, and associated address.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.customer (
    customernr SERIAL NOT NULL PRIMARY KEY,
    email VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    addressnr INTEGER,
    CONSTRAINT fk_customer_address 
    FOREIGN KEY (addressnr) 
    REFERENCES inventhor.address (addressnr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table inventhor.supplier
-- This table stores supplier information, including their contact details and associated address.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.supplier (
    suppliernr SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    contactperson VARCHAR(100),
    email VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    notes VARCHAR(500),
    website VARCHAR(255),
    addressnr INTEGER NOT NULL,
    CONSTRAINT fk_supplier_address 
    FOREIGN KEY (addressnr) 
    REFERENCES inventhor.address (addressnr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table inventhor.category
-- This table stores product categories.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.category (
    categorynr SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- -----------------------------------------------------
-- Table inventhor.product
-- This table stores product information, including dimensions, weight, price, and category.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.product (
    productnr SERIAL NOT NULL PRIMARY KEY,
    image VARCHAR(255),
    name VARCHAR(50) NOT NULL,
    description VARCHAR(60) NOT NULL,
    categorynr INTEGER NOT NULL,
    width DECIMAL(6, 2) NOT NULL CHECK (width >= 0),
    height DECIMAL(6, 2) NOT NULL CHECK (height >= 0),
    depth DECIMAL(6, 2) NOT NULL CHECK (depth >= 0),
    weight DECIMAL(8, 2) NOT NULL CHECK (weight >= 0),
    sellprice DECIMAL(8, 2) NOT NULL CHECK (sellprice >= 0),
    unit VARCHAR(20) NOT NULL DEFAULT 'pcs', -- Default unit is pieces
    CONSTRAINT fk_product_category
    FOREIGN KEY (categorynr)
    REFERENCES inventhor.category (categorynr)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table inventhor.pricehistory
-- This table stores the price history of products.
-- When customer order will be created, the sell prise will be stored in this table by trigger.
-- When warehouse order will be created, the buy price will be stored in this table by trigger.
-- When sell price registred, the buy price will be NULL. And when buy price registred, the sell price will be NULL.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.pricehistory (
    pricehistorynr SERIAL NOT NULL PRIMARY KEY,
    productnr INTEGER NOT NULL,
    sellprice DECIMAL(8, 2),
    buyprice DECIMAL(8, 2),
    setdate TIMESTAMP NOT NULL,
    CONSTRAINT fk_pricehistory_product 
    FOREIGN KEY (productnr) 
    REFERENCES inventhor.product (productnr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table inventhor.sellinghistory
-- This table stores the selling history of products.
-- It records the product number, quantity sold, and the date of sale.
-- This table is used to track the sales performance of products over time.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.sellinghistory (
    sellinghistorynr SERIAL NOT NULL PRIMARY KEY,
    productnr INTEGER NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    saledate TIMESTAMP NOT NULL,
    CONSTRAINT fk_sellinghistory_product 
    FOREIGN KEY (productnr) 
    REFERENCES inventhor.product (productnr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table inventhor.productsupplier
-- This table establishes a many-to-many relationship between products and suppliers.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.productsupplier (
    productnr INTEGER NOT NULL,
    suppliernr INTEGER NOT NULL,
    PRIMARY KEY (productnr, suppliernr),
    CONSTRAINT fk_productsupplier_product 
    FOREIGN KEY (productnr) 
    REFERENCES inventhor.product (productnr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION,
    CONSTRAINT fk_productsupplier_supplier 
    FOREIGN KEY (suppliernr) 
    REFERENCES inventhor.supplier (suppliernr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table inventhor.warehouse
-- This table stores warehouse information, including the address.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.warehouse (
    warehousenr SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    addressnr INTEGER NOT NULL,
    CONSTRAINT fk_warehouse_address 
    FOREIGN KEY (addressnr) 
    REFERENCES inventhor.address (addressnr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table inventhor.location
-- This table stores the location of products within a warehouse, including warehouse, rack number, and place number.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.location (
    warehousenr INTEGER NOT NULL,
    racknr INTEGER NOT NULL,
    placenr INTEGER NOT NULL,
    PRIMARY KEY (warehousenr, racknr, placenr),
    CONSTRAINT fk_location_warehouse 
    FOREIGN KEY (warehousenr) 
    REFERENCES inventhor.warehouse (warehousenr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table inventhor.inventorywarehouse
-- This table stores the inventory levels of products in each warehouse, including maximum and minimum stock levels.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.inventorywarehouse (
    warehousenr INTEGER NOT NULL,
    productnr INTEGER NOT NULL,
    maxstocklvl DECIMAL(8, 2),
    minstocklvl DECIMAL(8, 2),
    PRIMARY KEY (warehousenr, productnr),
    CONSTRAINT fk_inventorywarehouse_warehouse 
    FOREIGN KEY (warehousenr) 
    REFERENCES inventhor.warehouse (warehousenr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION,
    CONSTRAINT fk_inventorywarehouse_product 
    FOREIGN KEY (productnr) 
    REFERENCES inventhor.product (productnr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table inventhor.locationproduct
-- This table stores the products located in each location, including quantity.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.locationproduct (
    warehousenr INTEGER NOT NULL,
    racknr INTEGER NOT NULL,
    placenr INTEGER NOT NULL,
    productnr INTEGER NOT NULL,
    quantity DECIMAL(8, 2) NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (warehousenr, racknr, placenr, productnr),
    CONSTRAINT fk_locationproduct_product 
    FOREIGN KEY (productnr) 
    REFERENCES inventhor.product (productnr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION,
    CONSTRAINT fk_locationproduct_location
    FOREIGN KEY (warehousenr, racknr, placenr)
    REFERENCES inventhor.location (warehousenr, racknr, placenr)
);

-- -----------------------------------------------------
-- Table inventhor.orderstatus
-- This table stores the different statuses an order can have.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.orderstatus (
    statusnr SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

-- -----------------------------------------------------
-- Table inventhor.customerorder
-- This table stores customer orders, including order date, status, delivery date, and associated customer.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.customerorder (
    ordernr SERIAL NOT NULL PRIMARY KEY,
    customernr INTEGER NOT NULL,
    orderdate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    statusnr INTEGER NOT NULL CHECK (statusnr > 0 AND statusnr <= 5), -- Assuming 5 statuses
    deliverydate TIMESTAMP,
    CONSTRAINT fk_customerorder_order
    FOREIGN KEY (customernr)
    REFERENCES inventhor.customer (customernr)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_customerorder_orderstatus
    FOREIGN KEY (statusnr)
    REFERENCES inventhor.orderstatus (statusnr)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table inventhor.customerorderproduct
-- This table stores the products associated with each customer order, including quantity
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.customerorderproduct (
    ordernr INTEGER NOT NULL,
    productnr INTEGER NOT NULL,
    warehousenr INTEGER NOT NULL, -- The warehouse from which the product is shipped
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (ordernr, productnr, warehousenr),
    CONSTRAINT fk_customerorderproduct_customerorder
    FOREIGN KEY (ordernr)
    REFERENCES inventhor.customerorder (ordernr)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_customerorderproduct_product
    FOREIGN KEY (productnr)
    REFERENCES inventhor.product (productnr)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_customerorderproduct_warehouse
    FOREIGN KEY (warehousenr)
    REFERENCES inventhor.warehouse (warehousenr)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

-- ----------------------------------------------------
-- Table inventhor.customerpayment
-- This table stores payments made by customers for their orders, including payment date, method, and amount.
-- ----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.customerpayment (
    paymentnr SERIAL NOT NULL PRIMARY KEY,
    ordernr INTEGER NOT NULL,
    paymentdate TIMESTAMP,
    paymentmethod INTEGER, 
    amount DECIMAL(10, 2) NOT NULL CHECK (amount >= 0),
    CONSTRAINT fk_customerpayment_customerorder 
    FOREIGN KEY (ordernr) 
    REFERENCES inventhor.customerorder (ordernr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table inventhor.warehouseorder
-- This table stores warehouse orders, including order date, supplier, and warehouse.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.warehouseorder ( 
    ordernr SERIAL NOT NULL PRIMARY KEY,
    warehousenr INTEGER NOT NULL,
    suppliernr INTEGER NOT NULL,
    orderdate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    statusnr INTEGER NOT NULL,
    deliverydate TIMESTAMP,
    CONSTRAINT fk_warehouseorder_supplier 
    FOREIGN KEY (suppliernr) 
    REFERENCES inventhor.supplier (suppliernr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION,
    CONSTRAINT fk_warehouseorder_warehouse
    FOREIGN KEY (warehousenr)
    REFERENCES inventhor.warehouse (warehousenr)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_warehouseorder_orderstatus
    FOREIGN KEY (statusnr)
    REFERENCES inventhor.orderstatus (statusnr)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table inventhor.warehouseorderproduct
-- This table stores the products associated with each warehouse order, including quantity and price.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.warehouseorderproduct (
    ordernr INTEGER NOT NULL,
    productnr INTEGER NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    buyprice DECIMAL(8, 2) NOT NULL CHECK (buyprice >= 0), -- buying price
    PRIMARY KEY (ordernr, productnr),
    CONSTRAINT fk_warehouseorderproduct_warehouseorder
    FOREIGN KEY (ordernr) 
    REFERENCES inventhor.warehouseorder (ordernr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION,
    CONSTRAINT fk_warehouseorderproduct_product 
    FOREIGN KEY (productnr) 
    REFERENCES inventhor.product (productnr) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION
);

-- ---------------------------------------------------------
-- Table inventhor.notificationtype
-- This table stores the types of notifications that can be sent to employees.
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.notificationtype (
    notificationtypenr SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

-- ----------------------------------------------------
-- Table inventhor.notification
-- This table stores notifications for employees, including title, message, date, read status, and type.
-- ----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventhor.notification (
    notificationnr SERIAL NOT NULL PRIMARY KEY,
    notificationtypenr INTEGER NOT NULL,
    title VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    date TIMESTAMP NOT NULL,
    isread BOOLEAN NOT NULL,
    employeenr INTEGER NOT NULL,
    CONSTRAINT fk_notification_employee
    FOREIGN KEY (employeenr)
    REFERENCES inventhor.employee (employeenr)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_notification_notificationtype
    FOREIGN KEY (notificationtypenr)
    REFERENCES inventhor.notificationtype (notificationtypenr)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

-- ---------------------------------------------------------
-- Give access to user
-- ---------------------------------------------------------

-- Grant access to schema
GRANT USAGE ON SCHEMA inventhor TO "SystemAdmin";

-- Grant all table privileges
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA inventhor TO "SystemAdmin";

-- Grant sequence privileges
GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA inventhor TO "SystemAdmin";

-- -----------------------------------------------------
-- Audit Function
-- -----------------------------------------------------
CREATE OR REPLACE FUNCTION inventhor.fn_audit_trigger() RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO inventhor.audit_log(tblname, operation, rowdata, changedby)
        VALUES (TG_TABLE_NAME, TG_OP, row_to_json(OLD), current_user);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO inventhor.audit_log(tblname, operation, rowdata, changedby)
        VALUES (TG_TABLE_NAME, TG_OP, row_to_json(NEW), current_user);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO inventhor.audit_log(tblname, operation, rowdata, changedby)
        VALUES (TG_TABLE_NAME, TG_OP, row_to_json(NEW), current_user);
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- -----------------------------------------------------
-- Attach Triggers to Sensitive Tables
-- -----------------------------------------------------
CREATE TRIGGER trg_audit_employee
AFTER INSERT OR UPDATE OR DELETE ON inventhor.employee
FOR EACH ROW EXECUTE FUNCTION inventhor.fn_audit_trigger();

CREATE TRIGGER trg_audit_product
AFTER INSERT OR UPDATE OR DELETE ON inventhor.product
FOR EACH ROW EXECUTE FUNCTION inventhor.fn_audit_trigger();

CREATE TRIGGER trg_audit_warehouseorder
AFTER INSERT OR UPDATE OR DELETE ON inventhor.warehouseorder
FOR EACH ROW EXECUTE FUNCTION inventhor.fn_audit_trigger();

CREATE TRIGGER trg_audit_customerorder
AFTER INSERT OR UPDATE OR DELETE ON inventhor.customerorder
FOR EACH ROW EXECUTE FUNCTION inventhor.fn_audit_trigger();

-- -----------------------------------------------------
-- Trigger function for Price History recording
-- This function records the sell price when a customer order is created
-- and the buy price when a warehouse order is created.
-- It inserts a new record into the pricehistory table with the appropriate price and product.
-- -----------------------------------------------------
CREATE OR REPLACE FUNCTION inventhor.fn_record_price_history() RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        -- For customer orders, record the sell price
        IF TG_TABLE_NAME = 'customerorderproduct' THEN
            INSERT INTO inventhor.pricehistory (productnr, sellprice, setdate)
            VALUES (
                NEW.productnr,
                (SELECT sellprice FROM inventhor.product WHERE productnr = NEW.productnr),
                (SELECT orderdate FROM inventhor.customerorder WHERE ordernr = NEW.ordernr)
            );
        END IF;

        -- For warehouse orders, record the buy price
        IF TG_TABLE_NAME = 'warehouseorderproduct' THEN
            INSERT INTO inventhor.pricehistory (productnr, buyprice, setdate)
            VALUES (NEW.productnr, NEW.buyprice, (SELECT orderdate FROM inventhor.warehouseorder WHERE ordernr = NEW.ordernr));
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- -----------------------------------------------------
-- Attach Trigger to Customer Order Product
-- This trigger will fire after an insert on the customerorderproduct table
-- to record the sell price in the pricehistory table.
CREATE TRIGGER trg_record_sell_price
AFTER INSERT ON inventhor.customerorderproduct
FOR EACH ROW EXECUTE FUNCTION inventhor.fn_record_price_history();

-- Attach Trigger to Warehouse Order Product
-- This trigger will fire after an insert on the warehouseorderproduct table
-- to record the buy price in the pricehistory table.
CREATE TRIGGER trg_record_buy_price
AFTER INSERT ON inventhor.warehouseorderproduct
FOR EACH ROW EXECUTE FUNCTION inventhor.fn_record_price_history();

-- -----------------------------------------------------
-- Trigger function for Selling History recording
-- This function records the quantity sold of a product when a customer order is created.
-- It inserts a new record into the sellinghistory table with the product number and quantity sold.
-- -----------------------------------------------------
CREATE OR REPLACE FUNCTION inventhor.fn_record_selling_history() RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        -- Record the quantity sold in the sellinghistory table
        INSERT INTO inventhor.sellinghistory (productnr, quantity, saledate)
        VALUES (NEW.productnr, NEW.quantity, (SELECT orderdate FROM inventhor.customerorder WHERE ordernr = NEW.ordernr));
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- -----------------------------------------------------
-- Attach Trigger to Customer Order Product
-- This trigger will fire after an insert on the customerorderproduct table 
-- to record the quantity sold in the sellinghistory table.
CREATE TRIGGER trg_record_selling_history
AFTER INSERT ON inventhor.customerorderproduct
FOR EACH ROW EXECUTE FUNCTION inventhor.fn_record_selling_history();

-- ---------------------------------------------------------------------------------------------
-- Trigger function to check stock levels after a customer order is inserted
-- ----------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION inventhor.fn_check_stock_after_order() RETURNS TRIGGER AS $$
DECLARE
    rec_product RECORD;
    rec_warehouse RECORD;
    current_qty DECIMAL(8,2);
    max_lvl DECIMAL(8,2);
    min_lvl DECIMAL(8,2);
    warehouse_name VARCHAR(50);
    product_name VARCHAR(50);
    emp RECORD;
    info_type INT;
    danger_type INT;
    warning_type INT;
BEGIN
    -- Get notification type ids
    SELECT notificationtypenr INTO info_type FROM inventhor.notificationtype WHERE name = 'Info';
    SELECT notificationtypenr INTO danger_type FROM inventhor.notificationtype WHERE name = 'Danger';
    SELECT notificationtypenr INTO warning_type FROM inventhor.notificationtype WHERE name = 'Warning';

    -- Loop through each product in the new order
    FOR rec_product IN
        SELECT productnr FROM inventhor.customerorderproduct WHERE ordernr = NEW.ordernr
    LOOP
        -- Loop through each warehouse that stocks this product
        FOR rec_warehouse IN
            SELECT iw.warehousenr, iw.maxstocklvl, iw.minstocklvl, w.name AS warehouse_name, p.name AS product_name
            FROM inventhor.inventorywarehouse iw
            JOIN inventhor.warehouse w ON iw.warehousenr = w.warehousenr
            JOIN inventhor.product p ON iw.productnr = p.productnr
            WHERE iw.productnr = rec_product.productnr
        LOOP
            -- Calculate current stock in this warehouse
            SELECT COALESCE(SUM(quantity), 0) INTO current_qty
            FROM inventhor.locationproduct
            WHERE warehousenr = rec_warehouse.warehousenr AND productnr = rec_product.productnr;

            max_lvl := rec_warehouse.maxstocklvl;
            min_lvl := rec_warehouse.minstocklvl;
            warehouse_name := rec_warehouse.warehouse_name;
            product_name := rec_warehouse.product_name;

            -- Send notifications to all employees based on stock level
            FOR emp IN SELECT employeenr FROM inventhor.employee WHERE isActive = TRUE
            LOOP
                -- If stock >= maxstocklvl
                IF current_qty >= max_lvl THEN
                    INSERT INTO inventhor.notification (notificationtypenr, title, message, date, isread, employeenr)
                    VALUES (info_type, 'Stock Info', warehouse_name || ' have a lot of ' || product_name || ' on stock!', NOW(), FALSE, emp.employeenr);
                -- If stock <= minstocklvl
                ELSIF current_qty <= min_lvl THEN
                    INSERT INTO inventhor.notification (notificationtypenr, title, message, date, isread, employeenr)
                    VALUES (danger_type, 'Stock Danger', 'You need to order more ' || product_name || ' for ' || warehouse_name || '!', NOW(), FALSE, emp.employeenr);
                -- If stock <= 20% of maxstocklvl
                ELSIF current_qty <= (max_lvl * 0.2) THEN
                    INSERT INTO inventhor.notification (notificationtypenr, title, message, date, isread, employeenr)
                    VALUES (warning_type, 'Stock Warning', 'It is few left of ' || product_name || ' in ' || warehouse_name || ' !', NOW(), FALSE, emp.employeenr);
                END IF;
            END LOOP;
        END LOOP;
    END LOOP;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Attach the trigger to customerorder table
CREATE TRIGGER trg_check_stock_after_order
AFTER INSERT ON inventhor.customerorder
FOR EACH ROW EXECUTE FUNCTION inventhor.fn_check_stock_after_order();

-- -------------------------------------------------------------------------------------------------------------------
-- INSERT DEFAULT DATA
-- --------------------------------------------------------------------------------------------------

-- Insert default 50 addresses
INSERT INTO inventhor.address (country, city, street, postcode)
VALUES 
('Norway', 'Oslo', 'Karl Johans gate 1', '0154'),
('Norway', 'Bergen', 'Bryggen 2', '5003'),
('Norway', 'Stavanger', 'Øvre Holmegate 3', '4006'),
('Norway', 'Trondheim', 'Bakklandet 4', '7014'),
('Norway', 'Drammen', 'Bragernes Torg 5', '3017'),
('Norway', 'Tromsø', 'Storgata 6', '9008'),
('Norway', 'Kristiansand', 'Markens gate 7', '4611'),
('Norway', 'Ålesund', 'Apotekergata 8', '6004'),
('Norway', 'Molde', 'Storgata 9', '6413'),
('Norway', 'Haugesund', 'Haraldsgata 10', '5528'),
('Norway', 'Lillestrøm', 'Storgata 11', '2000'),
('Norway', 'Sandnes', 'Langgata 12', '4306'),
('Norway', 'Bodø', 'Storgata 13', '8006'),
('Norway', 'Hamar', 'Torggata 14', '2317'),
('Norway', 'Kongsberg', 'Stortorvet 15', '3611'),
('Norway', 'Gjøvik', 'Jernbanegata 16', '2815'),
('Norway', 'Larvik', 'Torget 17', '3256'),
('Norway', 'Moss', 'Storgata 18', '1530'),
('Norway', 'Porsgrunn', 'Storgata 19', '3915'),
('Norway', 'Skien', 'Kirkegata 20', '3701'),
('Norway', 'Fredrikstad', 'Storgata 21', '1607'),
('Norway', 'Horten', 'Torggata 22', '3186'),
('Norway', 'Halden', 'Storgata 23', '1771'),
('Norway', 'Arendal', 'Torvet 24', '4836'),
('Norway', 'Lillesand', 'Storgata 25', '4790'),
('Norway', 'Flekkefjord', 'Torget 26', '4400'),
('Norway', 'Mandal', 'Storgata 27', '4514'),
('Norway', 'Sarpsborg', 'Storgata 28', '1706'),
('Norway', 'Kongsvinger', 'Storgata 29', '2211'),
('Norway', 'Elverum', 'Storgata 30', '2408'),
('Norway', 'Hønefoss', 'Storgata 31', '3510'),
('Norway', 'Jessheim', 'Storgata 32', '2050'),
('Norway', 'Sandefjord', 'Storgata 33', '3211'),
('Norway', 'Bærum', 'Storgata 34', '1338'),
('Norway', 'Asker', 'Storgata 35', '1383'),
('Norway', 'Lørenskog', 'Storgata 36', '1473'),
('Norway', 'Moss', 'Storgata 37', '1530'),
('Norway', 'Horten', 'Storgata 38', '3186'),
('Norway', 'Drammen', 'Storgata 39', '3015'),
('Norway', 'Bergen', 'Storgata 40', '5003'),
('Norway', 'Oslo', 'Storgata 41', '0154'),
('Norway', 'Stavanger', 'Storgata 42', '4006'),
('Norway', 'Trondheim', 'Storgata 43', '7014'),
('Norway', 'Tromsø', 'Storgata 44', '9008'),
('Norway', 'Kristiansand', 'Storgata 45', '4611'),
('Norway', 'Ålesund', 'Storgata 46', '6004'),
('Norway', 'Molde', 'Storgata 47', '6413'),
('Norway', 'Haugesund', 'Storgata 48', '5528'),
('Norway', 'Lillestrøm', 'Storgata 49', '2000'),
('Norway', 'Sandnes', 'Storgata 50', '4306');

-- --------------------------------------------------------------------------------------------------------------------------------------------
-- Insert employee roles
INSERT INTO inventhor.employeerole (name)
VALUES
('admin'),
('staff');

-- --------------------------------------------------------------------------------------------------------------------------------------------
-- Insert payment methods
INSERT INTO inventhor.paymentmethod (name)
VALUES
('Credit Card'),
('PayPal'),
('Bank Transfer'),
('Vipps');

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert to notification types
INSERT INTO inventhor.notificationtype (name)
VALUES
('Danger'),
('Warning'),
('Info');

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert default 5 categories
INSERT INTO inventhor.category (name)
VALUES
('Food'),
('Toys'),
('Lingerie'),
('Electronics'),
('Home Decor');

-- -------------------------------------------------------------------------------------------------------------------------------------------
-- Insert default 10 employees
INSERT INTO inventhor.employee (email, phone, passwordhash, firstname, lastname, position, rolenr, employeddate, isActive, image, addressnr)
VALUES
('annaandersen@inventhor.com', '90000001', 'hash1_Abc123XyZ789', 'Anna', 'Andersen', 'Manager', 1, '2023-01-10', TRUE, 'https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 1),
('bjornberg@inventhor.com', '90000002', 'hash2_QwErTyUiOp12', 'Bjørn', 'Berg', 'Warehouse Staff', 2, '2023-02-15', TRUE, 'https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 2),
('clarachristensen@inventhor.com', '90000003', 'hash3_ZxCvBnM12345', 'Clara', 'Christensen', 'Sales', 2, '2023-03-20', TRUE, 'https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 3),
('daviddahl@inventhor.com', '90000004', 'hash4_LmNoPqRsTu67', 'David', 'Dahl', 'Admin', 1, '2023-04-05', TRUE, 'https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 4),
('evaeriksen@inventhor.com', '90000005', 'hash5_AsDfGhJkL890', 'Eva', 'Eriksen', 'Support', 2, '2023-05-12', TRUE, 'https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 5),
('frankfoss@inventhor.com', '90000006', 'hash6_PoIuYtReWq12', 'Frank', 'Foss', 'Warehouse Staff', 2, '2023-06-18', TRUE, 'https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 6),
('gretegundersen@inventhor.com', '90000007', 'hash7_MnBvCxZaQw34', 'Grete', 'Gundersen', 'Finance', 1, '2023-07-22', TRUE, 'https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 7),
('hakonhansen@inventhor.com', '90000008', 'hash8_TyUiOpAsDf56', 'Håkon', 'Hansen', 'Sales', 2, '2023-08-30', TRUE, 'https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 8),
('idaiversen@inventhor.com', '90000009', 'hash9_GhJkLmNoPq78', 'Ida', 'Iversen', 'Support', 2, '2023-09-14', TRUE, 'https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 9),
('jonasjohansen@inventhor.com', '90000010', 'hash10_ZaQwSxEdCv90', 'Jonas', 'Johansen', 'Manager', 1, '2023-10-01', TRUE, 'https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 10);

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert default 20 customers 
INSERT INTO inventhor.customer (email, phone, firstname, lastname, addressnr)
VALUES
('karinordmann@inventhor.com', '90100001', 'Kari', 'Nordmann', 11),
('olahansen@inventhor.com', '90100002', 'Ola', 'Hansen', 12),
('perjohansen@inventhor.com', '90100003', 'Per', 'Johansen', 13),
('liselarsen@inventhor.com', '90100004', 'Lise', 'Larsen', 14),
('monaberg@inventhor.com', '90100005', 'Mona', 'Berg', 15),
('eriksolberg@inventhor.com', '90100006', 'Erik', 'Solberg', 16),
('sirinilsen@inventhor.com', '90100007', 'Siri', 'Nilsen', 17),
('tomkristiansen@inventhor.com', '90100008', 'Tom', 'Kristiansen', 18),
('ninaolsen@inventhor.com', '90100009', 'Nina', 'Olsen', 19),
('arneandreassen@inventhor.com', '90100010', 'Arne', 'Andreassen', 20),
('verahalvorsen@inventhor.com', '90100011', 'Vera', 'Halvorsen', 21),
('runepaulsen@inventhor.com', '90100012', 'Rune', 'Paulsen', 22),
('sofiekarlsen@inventhor.com', '90100013', 'Sofie', 'Karlsen', 23),
('mariusmoen@inventhor.com', '90100014', 'Marius', 'Moen', 24),
('lindalie@inventhor.com', '90100015', 'Linda', 'Lie', 25),
('henrikbakke@inventhor.com', '90100016', 'Henrik', 'Bakke', 26),
('camillaaas@inventhor.com', '90100017', 'Camilla', 'Aas', 27),
('fredrikmyhre@inventhor.com', '90100018', 'Fredrik', 'Myhre', 28),
('juliedahl@inventhor.com', '90100019', 'Julie', 'Dahl', 29),
('simenrasmussen@inventhor.com', '90100020', 'Simen', 'Rasmussen', 30);

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert default 10 suppliers 
INSERT INTO inventhor.supplier (name, contactperson, email, phone, notes, website, addressnr)
VALUES
('Nordic Foods', 'Ole Olsen', 'nordicfoods@nordicfoods.com', '90200001', 'Supplier of Scandinavian food products', 'https://nordicfoods.com', 31),
('Toy Universe', 'Kari Karlsen', 'toyuniverse@toyuniverse.com', '90200002', 'Toys and games for all ages', 'https://toyuniverse.com', 32),
('Sweet Treats', 'Per Pedersen', 'sweettreats@sweettreats.com', '90200003', 'Candy and chocolate distributor', 'https://sweettreats.com', 33),
('Luxe Lingerie', 'Lise Larsen', 'luxelingerie@luxelingerie.com', '90200004', 'Premium lingerie and sleepwear', 'https://luxelingerie.com', 34),
('Pet Paradise', 'Mona Monsen', 'petparadise@petparadise.com', '90200005', 'Pet food and accessories', 'https://petparadise.com', 35),
('Fresh Produce Co', 'Erik Eriksen', 'freshproduce@freshproduce.com', '90200006', 'Fresh fruits and vegetables', 'https://freshproduce.com', 36),
('Game Galaxy', 'Siri Simonsen', 'gamegalaxy@gamegalaxy.com', '90200007', 'Board games and puzzles', 'https://gamegalaxy.com', 37),
('Fashion Kids', 'Tom Thomassen', 'fashionkids@fashionkids.com', '90200008', 'Children''s clothing and toys', 'https://fashionkids.com', 38),
('Bakery Supplies', 'Nina Nilsen', 'bakerysupplies@bakerysupplies.com', '90200009', 'Bakery ingredients and tools', 'https://bakerysupplies.com', 39),
('Chic Home', 'Arne Arnesen', 'chichome@chichome.com', '90200010', 'Home decor and kitchenware', 'https://chichome.com', 40);

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert default 30 products with dimensions, weight, sellprice, and category
INSERT INTO inventhor.product (image, name, description, width, height, depth, weight, sellprice, unit, categorynr)
VALUES
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Organic Apples', 'Fresh organic apples from local farms', 0.1, 0.1, 0.1, 0.2, 20.00, 'kg', 1),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Chocolate Bar', 'Delicious dark chocolate bar', 0.05, 0.2, 0.01, 0.1, 15.00, 'pcs', 1),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Teddy Bear', 'Soft and cuddly teddy bear for children', 0.3, 0.5, 0.2, 0.5, 100.00, 'pcs', 2),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Lingerie Set', 'Elegant lace lingerie set', 0.2, 0.3, 0.1, 0.05, 300.00, 'pcs', 3),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Dog Food', 'Premium dog food with natural ingredients', 0.4, 0.6, 0.3, 1.5, 250.00, 'kg', 5),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Fresh Carrots', 'Organic fresh carrots', 0.05, 0.2, 0.05, 0.1, 10.00, 'kg', 1),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Board Game', 'Fun family board game for all ages', 0.4, 0.4, 0.05, 1.2, 200.00, 'pcs', 2),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Kids T-Shirt', 'Colorful t-shirt for kids', 0.3, 0.4, 0.02, 0.15, 50.00, 'pcs', 4),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Baking Flour', 'High-quality baking flour for all your baking needs', 0.2, 0.3, 0.2, 1.0, 30.00, 'kg', 1),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Ceramic Vase', 'Beautiful ceramic vase for home decor', 0.15, 0.3, 0.15, 0.8, 150.00, 'pcs', 5),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Smartphone', 'Latest model smartphone with advanced features', 0.07, 0.15, 0.01, 0.2, 800.00, 'pcs', 4),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Bluetooth Speaker', 'Portable Bluetooth speaker with great sound', 0.1, 0.1, 0.1, 0.5, 300.00, 'pcs', 4),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Coffee Maker', 'Automatic coffee maker with programmable settings', 0.3, 0.4, 0.3, 2.5, 1200.00, 'pcs', 4),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Winter Jacket', 'Warm winter jacket for cold weather', 0.6, 1.2, 0.4, 1.5, 1500.00, 'pcs', 3),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Running Shoes', 'Comfortable running shoes for all terrains', 0.3, 0.15, 0.1, 1.2, 800.00, 'pcs', 3),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Yoga Mat', 'Non-slip yoga mat for all types of workouts', 0.6, 0.2, 0.01, 1.5, 200.00, 'pcs', 3),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Backpack', 'Durable backpack for school or travel', 0.4, 0.5, 0.2, 1.8, 600.00, 'pcs', 3),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Water Bottle', 'Stainless steel water bottle for hydration on the go', 0.1, 0.3, 0.1, 0.3, 100.00, 'l', 3),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Camping Tent', 'Spacious camping tent for outdoor adventures', 2.0, 1.5, 1.5, 5.0, 3000.00, 'pcs', 3),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Fishing Rod', 'High-quality fishing rod for fishing enthusiasts', 1.8, 0.1, 0.1, 2.0, 1500.00, 'm', 3),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Guitar', 'Acoustic guitar with rich sound quality', 1.0, 0.4, 0.1, 2.5, 2500.00, 'pcs', 3),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Digital Camera', 'High-resolution digital camera for photography lovers', 0.15, 0.1, 0.05, 1.2, 5000.00, 'pcs', 4),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Laptop', 'Powerful laptop for work and entertainment', 0.35, 0.02, 0.25, 2.5, 12000.00, 'pcs', 4),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Smartwatch', 'Smartwatch with fitness tracking features', 0.05, 0.05, 0.01, 0.2, 2000.00, 'pcs', 4),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Headphones', 'Noise-cancelling headphones for immersive sound', 0.2, 0.2, 0.05, 0.5, 1500.00, 'pcs', 4),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Electric Kettle', 'Fast boiling electric kettle with auto shut-off', 0.2, 0.3, 0.2, 1.5, 800.00, 'pcs', 4),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Blender', 'High-speed blender for smoothies and soups', 0.3, 0.4, 0.3, 2.0, 1000.00, 'pcs', 4),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Vacuum Cleaner', 'Powerful vacuum cleaner for home cleaning', 0.4, 1.0, 0.3, 5.0, 2500.00, 'pcs', 4),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Air Purifier', 'Air purifier with HEPA filter for clean air', 0.3, 0.5, 0.3, 3.0, 2000.00, 'pcs', 4),
('https://img.icons8.com/?size=100&id=104948&format=png&color=000000', 'Electric Toothbrush', 'Rechargeable electric toothbrush with timer', 0.05, 0.2, 0.05, 0.3, 500.00, 'pcs', 4);

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Connect products to suppliers
INSERT INTO inventhor.productsupplier (productnr, suppliernr)
VALUES
(1, 1), -- Organic Apples from Nordic Foods
(2, 3), -- Chocolate Bar from Sweet Treats
(3, 2), -- Teddy Bear from Toy Universe
(4, 4), -- Lingerie Set from Luxe Lingerie
(5, 5), -- Dog Food from Pet Paradise
(6, 1), -- Fresh Carrots from Nordic Foods
(7, 2), -- Board Game from Toy Universe
(8, 6), -- Kids T-Shirt from Fashion Kids
(9, 7), -- Baking Flour from Bakery Supplies
(10, 8), -- Ceramic Vase from Chic Home
(11, 9), -- Smartphone from Electronics Supplier
(12, 9), -- Bluetooth Speaker from Electronics Supplier
(13, 9), -- Coffee Maker from Electronics Supplier
(14, 10), -- Winter Jacket from Fashion Supplier
(15, 10), -- Running Shoes from Fashion Supplier
(16, 10), -- Yoga Mat from Fashion Supplier,
(17, 10), -- Backpack from Fashion Supplier,
(18, 10), -- Water Bottle from Fashion Supplier,
(19, 10), -- Camping Tent from Fashion Supplier,
(20, 10), -- Fishing Rod from Fashion Supplier,
(21, 9), -- Guitar from Electronics Supplier,
(22, 9), -- Digital Camera from Electronics Supplier,
(23, 9), -- Laptop from Electronics Supplier,
(24, 9), -- Smartwatch from Electronics Supplier,
(25, 9), -- Headphones from Electronics Supplier,
(26, 9), -- Electric Kettle from Electronics Supplier,
(27, 9), -- Blender from Electronics Supplier,
(28, 9), -- Vacuum Cleaner from Electronics Supplier,
(29, 9), -- Air Purifier from Electronics Supplier,
(30, 9); -- Electric Toothbrush from Electronics Supplier

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert default 10 warehouses
INSERT INTO inventhor.warehouse (name, addressnr)
VALUES
('Main Warehouse', 41),
('North Warehouse', 42),
('South Warehouse', 43),
('East Warehouse', 44),
('West Warehouse', 45),
('Central Warehouse', 46),
('Regional Warehouse A', 47),
('Regional Warehouse B', 48),
('Distribution Center', 49),
('Backup Warehouse', 50);

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert locations
INSERT INTO inventhor.location (warehousenr, racknr, placenr)
VALUES
-- Main Warehouse (warehousenr = 1): 5 racks, 5 shelves per rack, 2 places per shelf
-- Rack 1
(1, 1, 101), (1, 1, 102), (1, 1, 201), (1, 1, 202), (1, 1, 301), (1, 1, 302), (1, 1, 401), (1, 1, 402), (1, 1, 501), (1, 1, 502),
-- Rack 2
(1, 2, 101), (1, 2, 102), (1, 2, 201), (1, 2, 202), (1, 2, 301), (1, 2, 302), (1, 2, 401), (1, 2, 402), (1, 2, 501), (1, 2, 502),
-- Rack 3
(1, 3, 101), (1, 3, 102), (1, 3, 201), (1, 3, 202), (1, 3, 301), (1, 3, 302), (1, 3, 401), (1, 3, 402), (1, 3, 501), (1, 3, 502),
-- Rack 4
(1, 4, 101), (1, 4, 102), (1, 4, 201), (1, 4, 202), (1, 4, 301), (1, 4, 302), (1, 4, 401), (1, 4, 402), (1, 4, 501), (1, 4, 502),
-- Rack 5
(1, 5, 101), (1, 5, 102), (1, 5, 201), (1, 5, 202), (1, 5, 301), (1, 5, 302), (1, 5, 401), (1, 5, 402), (1, 5, 501), (1, 5, 502),

-- North Warehouse (warehousenr = 2): 10 racks
-- Each rack: 5 shelves, 2 places per shelf
(2, 1, 101), (2, 1, 102), (2, 1, 201), (2, 1, 202), (2, 1, 301), (2, 1, 302), (2, 1, 401), (2, 1, 402), (2, 1, 501), (2, 1, 502),
(2, 2, 101), (2, 2, 102), (2, 2, 201), (2, 2, 202), (2, 2, 301), (2, 2, 302), (2, 2, 401), (2, 2, 402), (2, 2, 501), (2, 2, 502),
(2, 3, 101), (2, 3, 102), (2, 3, 201), (2, 3, 202), (2, 3, 301), (2, 3, 302), (2, 3, 401), (2, 3, 402), (2, 3, 501), (2, 3, 502),
(2, 4, 101), (2, 4, 102), (2, 4, 201), (2, 4, 202), (2, 4, 301), (2, 4, 302), (2, 4, 401), (2, 4, 402), (2, 4, 501), (2, 4, 502),
(2, 5, 101), (2, 5, 102), (2, 5, 201), (2, 5, 202), (2, 5, 301), (2, 5, 302), (2, 5, 401), (2, 5, 402), (2, 5, 501), (2, 5, 502),
(2, 6, 101), (2, 6, 102), (2, 6, 201), (2, 6, 202), (2, 6, 301), (2, 6, 302), (2, 6, 401), (2, 6, 402), (2, 6, 501), (2, 6, 502),
(2, 7, 101), (2, 7, 102), (2, 7, 201), (2, 7, 202), (2, 7, 301), (2, 7, 302), (2, 7, 401), (2, 7, 402), (2, 7, 501), (2, 7, 502),
(2, 8, 101), (2, 8, 102), (2, 8, 201), (2, 8, 202), (2, 8, 301), (2, 8, 302), (2, 8, 401), (2, 8, 402), (2, 8, 501), (2, 8, 502),
(2, 9, 101), (2, 9, 102), (2, 9, 201), (2, 9, 202), (2, 9, 301), (2, 9, 302), (2, 9, 401), (2, 9, 402), (2, 9, 501), (2, 9, 502),
(2, 10, 101), (2, 10, 102), (2, 10, 201), (2, 10, 202), (2, 10, 301), (2, 10, 302), (2, 10, 401), (2, 10, 402), (2, 10, 501), (2, 10, 502),

-- South Warehouse (warehousenr = 3): 5 racks
(3, 1, 101), (3, 1, 102), (3, 1, 201), (3, 1, 202), (3, 1, 301), (3, 1, 302), (3, 1, 401), (3, 1, 402), (3, 1, 501), (3, 1, 502),
(3, 2, 101), (3, 2, 102), (3, 2, 201), (3, 2, 202), (3, 2, 301), (3, 2, 302), (3, 2, 401), (3, 2, 402), (3, 2, 501), (3, 2, 502),
(3, 3, 101), (3, 3, 102), (3, 3, 201), (3, 3, 202), (3, 3, 301), (3, 3, 302), (3, 3, 401), (3, 3, 402), (3, 3, 501), (3, 3, 502),
(3, 4, 101), (3, 4, 102), (3, 4, 201), (3, 4, 202), (3, 4, 301), (3, 4, 302), (3, 4, 401), (3, 4, 402), (3, 4, 501), (3, 4, 502),
(3, 5, 101), (3, 5, 102), (3, 5, 201), (3, 5, 202), (3, 5, 301), (3, 5, 302), (3, 5, 401), (3, 5, 402), (3, 5, 501), (3, 5, 502),

-- East Warehouse (warehousenr = 4): 10 racks
(4, 1, 101), (4, 1, 102), (4, 1, 201), (4, 1, 202), (4, 1, 301), (4, 1, 302), (4, 1, 401), (4, 1, 402), (4, 1, 501), (4, 1, 502),
(4, 2, 101), (4, 2, 102), (4, 2, 201), (4, 2, 202), (4, 2, 301), (4, 2, 302), (4, 2, 401), (4, 2, 402), (4, 2, 501), (4, 2, 502),
(4, 3, 101), (4, 3, 102), (4, 3, 201), (4, 3, 202), (4, 3, 301), (4, 3, 302), (4, 3, 401), (4, 3, 402), (4, 3, 501), (4, 3, 502),
(4, 4, 101), (4, 4, 102), (4, 4, 201), (4, 4, 202), (4, 4, 301), (4, 4, 302), (4, 4, 401), (4, 4, 402), (4, 4, 501), (4, 4, 502),
(4, 5, 101), (4, 5, 102), (4, 5, 201), (4, 5, 202), (4, 5, 301), (4, 5, 302), (4, 5, 401), (4, 5, 402), (4, 5, 501), (4, 5, 502),
(4, 6, 101), (4, 6, 102), (4, 6, 201), (4, 6, 202), (4, 6, 301), (4, 6, 302), (4, 6, 401), (4, 6, 402), (4, 6, 501), (4, 6, 502),
(4, 7, 101), (4, 7, 102), (4, 7, 201), (4, 7, 202), (4, 7, 301), (4, 7, 302), (4, 7, 401), (4, 7, 402), (4, 7, 501), (4, 7, 502),
(4, 8, 101), (4, 8, 102), (4, 8, 201), (4, 8, 202), (4, 8, 301), (4, 8, 302), (4, 8, 401), (4, 8, 402), (4, 8, 501), (4, 8, 502),
(4, 9, 101), (4, 9, 102), (4, 9, 201), (4, 9, 202), (4, 9, 301), (4, 9, 302), (4, 9, 401), (4, 9, 402), (4, 9, 501), (4, 9, 502),
(4, 10, 101), (4, 10, 102), (4, 10, 201), (4, 10, 202), (4, 10, 301), (4, 10, 302), (4, 10, 401), (4, 10, 402), (4, 10, 501), (4, 10, 502),

-- West Warehouse (warehousenr = 5): 5 racks
(5, 1, 101), (5, 1, 102), (5, 1, 201), (5, 1, 202), (5, 1, 301), (5, 1, 302), (5, 1, 401), (5, 1, 402), (5, 1, 501), (5, 1, 502),
(5, 2, 101), (5, 2, 102), (5, 2, 201), (5, 2, 202), (5, 2, 301), (5, 2, 302), (5, 2, 401), (5, 2, 402), (5, 2, 501), (5, 2, 502),
(5, 3, 101), (5, 3, 102), (5, 3, 201), (5, 3, 202), (5, 3, 301), (5, 3, 302), (5, 3, 401), (5, 3, 402), (5, 3, 501), (5, 3, 502),
(5, 4, 101), (5, 4, 102), (5, 4, 201), (5, 4, 202), (5, 4, 301), (5, 4, 302), (5, 4, 401), (5, 4, 402), (5, 4, 501), (5, 4, 502),
(5, 5, 101), (5, 5, 102), (5, 5, 201), (5, 5, 202), (5, 5, 301), (5, 5, 302), (5, 5, 401), (5, 5, 402), (5, 5, 501), (5, 5, 502),

-- Central Warehouse (warehousenr = 6): 10 racks
(6, 1, 101), (6, 1, 102), (6, 1, 201), (6, 1, 202), (6, 1, 301), (6, 1, 302), (6, 1, 401), (6, 1, 402), (6, 1, 501), (6, 1, 502),
(6, 2, 101), (6, 2, 102), (6, 2, 201), (6, 2, 202), (6, 2, 301), (6, 2, 302), (6, 2, 401), (6, 2, 402), (6, 2, 501), (6, 2, 502),
(6, 3, 101), (6, 3, 102), (6, 3, 201), (6, 3, 202), (6, 3, 301), (6, 3, 302), (6, 3, 401), (6, 3, 402), (6, 3, 501), (6, 3, 502),
(6, 4, 101), (6, 4, 102), (6, 4, 201), (6, 4, 202), (6, 4, 301), (6, 4, 302), (6, 4, 401), (6, 4, 402), (6, 4, 501), (6, 4, 502),
(6, 5, 101), (6, 5, 102), (6, 5, 201), (6, 5, 202), (6, 5, 301), (6, 5, 302), (6, 5, 401), (6, 5, 402), (6, 5, 501), (6, 5, 502),
(6, 6, 101), (6, 6, 102), (6, 6, 201), (6, 6, 202), (6, 6, 301), (6, 6, 302), (6, 6, 401), (6, 6, 402), (6, 6, 501), (6, 6, 502),
(6, 7, 101), (6, 7, 102), (6, 7, 201), (6, 7, 202), (6, 7, 301), (6, 7, 302), (6, 7, 401), (6, 7, 402), (6, 7, 501), (6, 7, 502),
(6, 8, 101), (6, 8, 102), (6, 8, 201), (6, 8, 202), (6, 8, 301), (6, 8, 302), (6, 8, 401), (6, 8, 402), (6, 8, 501), (6, 8, 502),
(6, 9, 101), (6, 9, 102), (6, 9, 201), (6, 9, 202), (6, 9, 301), (6, 9, 302), (6, 9, 401), (6, 9, 402), (6, 9, 501), (6, 9, 502),
(6, 10, 101), (6, 10, 102), (6, 10, 201), (6, 10, 202), (6, 10, 301), (6, 10, 302), (6, 10, 401), (6, 10, 402), (6, 10, 501), (6, 10, 502),

-- Regional Warehouse A (warehousenr = 7): 10 racks
(7, 1, 101), (7, 1, 102), (7, 1, 201), (7, 1, 202), (7, 1, 301), (7, 1, 302), (7, 1, 401), (7, 1, 402), (7, 1, 501), (7, 1, 502),
(7, 2, 101), (7, 2, 102), (7, 2, 201), (7, 2, 202), (7, 2, 301), (7, 2, 302), (7, 2, 401), (7, 2, 402), (7, 2, 501), (7, 2, 502),
(7, 3, 101), (7, 3, 102), (7, 3, 201), (7, 3, 202), (7, 3, 301), (7, 3, 302), (7, 3, 401), (7, 3, 402), (7, 3, 501), (7, 3, 502),
(7, 4, 101), (7, 4, 102), (7, 4, 201), (7, 4, 202), (7, 4, 301), (7, 4, 302), (7, 4, 401), (7, 4, 402), (7, 4, 501), (7, 4, 502),
(7, 5, 101), (7, 5, 102), (7, 5, 201), (7, 5, 202), (7, 5, 301), (7, 5, 302), (7, 5, 401), (7, 5, 402), (7, 5, 501), (7, 5, 502),
(7, 6, 101), (7, 6, 102), (7, 6, 201), (7, 6, 202), (7, 6, 301), (7, 6, 302), (7, 6, 401), (7, 6, 402), (7, 6, 501), (7, 6, 502),
(7, 7, 101), (7, 7, 102), (7, 7, 201), (7, 7, 202), (7, 7, 301), (7, 7, 302), (7, 7, 401), (7, 7, 402), (7, 7, 501), (7, 7, 502),
(7, 8, 101), (7, 8, 102), (7, 8, 201), (7, 8, 202), (7, 8, 301), (7, 8, 302), (7, 8, 401), (7, 8, 402), (7, 8, 501), (7, 8, 502),
(7, 9, 101), (7, 9, 102), (7, 9, 201), (7, 9, 202), (7, 9, 301), (7, 9, 302), (7, 9, 401), (7, 9, 402), (7, 9, 501), (7, 9, 502),
(7, 10, 101), (7, 10, 102), (7, 10, 201), (7, 10, 202), (7, 10, 301), (7, 10, 302), (7, 10, 401), (7, 10, 402), (7, 10, 501), (7, 10, 502),

-- Regional Warehouse B (warehousenr = 8): 10 racks
(8, 1, 101), (8, 1, 102), (8, 1, 201), (8, 1, 202), (8, 1, 301), (8, 1, 302), (8, 1, 401), (8, 1, 402), (8, 1, 501), (8, 1, 502),
(8, 2, 101), (8, 2, 102), (8, 2, 201), (8, 2, 202), (8, 2, 301), (8, 2, 302), (8, 2, 401), (8, 2, 402), (8, 2, 501), (8, 2, 502),
(8, 3, 101), (8, 3, 102), (8, 3, 201), (8, 3, 202), (8, 3, 301), (8, 3, 302), (8, 3, 401), (8, 3, 402), (8, 3, 501), (8, 3, 502),
(8, 4, 101), (8, 4, 102), (8, 4, 201), (8, 4, 202), (8, 4, 301), (8, 4, 302), (8, 4, 401), (8, 4, 402), (8, 4, 501), (8, 4, 502),
(8, 5, 101), (8, 5, 102), (8, 5, 201), (8, 5, 202), (8, 5, 301), (8, 5, 302), (8, 5, 401), (8, 5, 402), (8, 5, 501), (8, 5, 502),
(8, 6, 101), (8, 6, 102), (8, 6, 201), (8, 6, 202), (8, 6, 301), (8, 6, 302), (8, 6, 401), (8, 6, 402), (8, 6, 501), (8, 6, 502),
(8, 7, 101), (8, 7, 102), (8, 7, 201), (8, 7, 202), (8, 7, 301), (8, 7, 302), (8, 7, 401), (8, 7, 402), (8, 7, 501), (8, 7, 502),
(8, 8, 101), (8, 8, 102), (8, 8, 201), (8, 8, 202), (8, 8, 301), (8, 8, 302), (8, 8, 401), (8, 8, 402), (8, 8, 501), (8, 8, 502),
(8, 9, 101), (8, 9, 102), (8, 9, 201), (8, 9, 202), (8, 9, 301), (8, 9, 302), (8, 9, 401), (8, 9, 402), (8, 9, 501), (8, 9, 502),
(8, 10, 101), (8, 10, 102), (8, 10, 201), (8, 10, 202), (8, 10, 301), (8, 10, 302), (8, 10, 401), (8, 10, 402), (8, 10, 501), (8, 10, 502),

-- Distribution Center (warehousenr = 9): 10 racks
(9, 1, 101), (9, 1, 102), (9, 1, 201), (9, 1, 202), (9, 1, 301), (9, 1, 302), (9, 1, 401), (9, 1, 402), (9, 1, 501), (9, 1, 502),
(9, 2, 101), (9, 2, 102), (9, 2, 201), (9, 2, 202), (9, 2, 301), (9, 2, 302), (9, 2, 401), (9, 2, 402), (9, 2, 501), (9, 2, 502),
(9, 3, 101), (9, 3, 102), (9, 3, 201), (9, 3, 202), (9, 3, 301), (9, 3, 302), (9, 3, 401), (9, 3, 402), (9, 3, 501), (9, 3, 502),
(9, 4, 101), (9, 4, 102), (9, 4, 201), (9, 4, 202), (9, 4, 301), (9, 4, 302), (9, 4, 401), (9, 4, 402), (9, 4, 501), (9, 4, 502),
(9, 5, 101), (9, 5, 102), (9, 5, 201), (9, 5, 202), (9, 5, 301), (9, 5, 302), (9, 5, 401), (9, 5, 402), (9, 5, 501), (9, 5, 502),
(9, 6, 101), (9, 6, 102), (9, 6, 201), (9, 6, 202), (9, 6, 301), (9, 6, 302), (9, 6, 401), (9, 6, 402), (9, 6, 501), (9, 6, 502),
(9, 7, 101), (9, 7, 102), (9, 7, 201), (9, 7, 202), (9, 7, 301), (9, 7, 302), (9, 7, 401), (9, 7, 402), (9, 7, 501), (9, 7, 502),
(9, 8, 101), (9, 8, 102), (9, 8, 201), (9, 8, 202), (9, 8, 301), (9, 8, 302), (9, 8, 401), (9, 8, 402), (9, 8, 501), (9, 8, 502),
(9, 9, 101), (9, 9, 102), (9, 9, 201), (9, 9, 202), (9, 9, 301), (9, 9, 302), (9, 9, 401), (9, 9, 402), (9, 9, 501), (9, 9, 502),
(9, 10, 101), (9, 10, 102), (9, 10, 201), (9, 10, 202), (9, 10, 301), (9, 10, 302), (9, 10, 401), (9, 10, 402), (9, 10, 501), (9, 10, 502),

-- Backup Warehouse (warehousenr = 10): 5 racks
(10, 1, 101), (10, 1, 102), (10, 1, 201), (10, 1, 202), (10, 1, 301), (10, 1, 302), (10, 1, 401), (10, 1, 402), (10, 1, 501), (10, 1, 502),
(10, 2, 101), (10, 2, 102), (10, 2, 201), (10, 2, 202), (10, 2, 301), (10, 2, 302), (10, 2, 401), (10, 2, 402), (10, 2, 501), (10, 2, 502),
(10, 3, 101), (10, 3, 102), (10, 3, 201), (10, 3, 202), (10, 3, 301), (10, 3, 302), (10, 3, 401), (10, 3, 402), (10, 3, 501), (10, 3, 502),
(10, 4, 101), (10, 4, 102), (10, 4, 201), (10, 4, 202), (10, 4, 301), (10, 4, 302), (10, 4, 401), (10, 4, 402), (10, 4, 501), (10, 4, 502),
(10, 5, 101), (10, 5, 102), (10, 5, 201), (10, 5, 202), (10, 5, 301), (10, 5, 302), (10, 5, 401), (10, 5, 402), (10, 5, 501), (10, 5, 502);

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert inventory warehouse
INSERT INTO inventhor.inventorywarehouse (warehousenr, productnr, maxstocklvl, minstocklvl)
VALUES
-- North Warehouse (warehousenr = 1): 10 racks
(1, 1, 100, 20), (1, 2, 80, 15), (1, 3, 60, 10), (1, 4, 50, 5), (1, 5, 120, 30),
(1, 6, 90, 20), (1, 7, 70, 15), (1, 8, 110, 25), (1, 9, 130, 35), (1, 10, 40, 10),
-- South Warehouse (warehousenr = 2): 10 racks
(2, 1, 100, 20), (2, 2, 80, 15), (2, 3, 60, 10), (2, 4, 50, 5), (2, 5, 120, 30),
(2, 6, 90, 20), (2, 7, 70, 15), (2, 8, 110, 25), (2, 9, 130, 35), (2, 10, 40, 10),
-- East Warehouse (warehousenr = 3): 5 racks
(3, 1, 100, 20), (3, 2, 80, 15), (3, 3, 60, 10), (3, 4, 50, 5), (3, 5, 120, 30),
-- West Warehouse (warehousenr = 4): 5 racks
(4, 1, 100, 20), (4, 2, 80, 15), (4, 3, 60, 10), (4, 4, 50, 5), (4, 5, 120, 30),
-- Central Warehouse (warehousenr = 5): 10 racks
(5, 1, 100, 20), (5, 2, 80, 15), (5, 3, 60, 10), (5, 4, 50, 5), (5, 5, 120, 30),
(5, 6, 90, 20), (5, 7, 70, 15), (5, 8, 110, 25), (5, 9, 130, 35), (5, 10, 40, 10),
-- Regional Warehouse A (warehousenr = 6): 10 racks
(6, 1, 100, 20), (6, 2, 80, 15), (6, 3, 60, 10), (6, 4, 50, 5), (6, 5, 120, 30),
(6, 6, 90, 20), (6, 7, 70, 15), (6, 8, 110, 25), (6, 9, 130, 35), (6, 10, 40, 10),
-- Regional Warehouse B (warehousenr = 7): 10 racks
(7, 1, 100, 20), (7, 2, 80, 15), (7, 3, 60, 10), (7, 4, 50, 5), (7, 5, 120, 30),
(7, 6, 90, 20), (7, 7, 70, 15), (7, 8, 110, 25), (7, 9, 130, 35), (7, 10, 40, 10),
-- Distribution Center (warehousenr = 8): 10 racks
(8, 1, 100, 20), (8, 2, 80, 15), (8, 3, 60, 10), (8, 4, 50, 5), (8, 5, 120, 30),
(8, 6, 90, 20), (8, 7, 70, 15), (8, 8, 110, 25), (8, 9, 130, 35), (8, 10, 40, 10),
-- Backup Warehouse (warehousenr = 9): 5 racks
(9, 1, 100, 20), (9, 2, 80, 15), (9, 3, 60, 10), (9, 4, 50, 5), (9, 5, 120, 30),
-- Additional warehouses
(10, 1, 100, 20), (10, 2, 80, 15), (10, 3, 60, 10), (10, 4, 50, 5), (10, 5, 120, 30);

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert products to locations
INSERT INTO inventhor.locationproduct (warehousenr, racknr, placenr, productnr, quantity)
VALUES
-- Product 1: Organic Apples in Main Warehouse, Rack 1, Place 101
(1, 1, 101, 1, 50),
-- Product 1: Organic Apples in Main Warehouse, Rack 2, Place 101
(1, 2, 101, 1, 30),
-- Product 1: Organic Apples in Main Warehouse, Rack 3, Place 101
(1, 3, 101, 1, 20),

-- Product 2: Chocolate Bar in Main Warehouse, Rack 1, Place 102
(1, 1, 102, 2, 40),
-- Product 2: Chocolate Bar in Main Warehouse, Rack 2, Place 102
(1, 2, 102, 2, 25),
-- Product 2: Chocolate Bar in Main Warehouse, Rack 3, Place 102
(1, 3, 102, 2, 15),

-- Product 3: Teddy Bear in Main Warehouse, Rack 1, Place 201
(1, 1, 201, 3, 35),
-- Product 3: Teddy Bear in Main Warehouse, Rack 2, Place 201
(1, 2, 201, 3, 20),
-- Product 3: Teddy Bear in Main Warehouse, Rack 3, Place 201
(1, 3, 201, 3, 10),

-- Product 4: Lingerie Set in Main Warehouse, Rack 1, Place 202
(1, 1, 202, 4, 20),
-- Product 4: Lingerie Set in Main Warehouse, Rack 2, Place 202
(1, 2, 202, 4, 10),
-- Product 4: Lingerie Set in Main Warehouse, Rack 3, Place 202
(1, 3, 202, 4, 5),

-- Product 5: Dog Food in Main Warehouse, Rack 1, Place 301
(1, 1, 301, 5, 60),
-- Product 5: Dog Food in Main Warehouse, Rack 2, Place 301
(1, 2, 301, 5, 30),
-- Product 5: Dog Food in Main Warehouse, Rack 3, Place 301
(1, 3, 301, 5, 15),

-- Product 6: Fresh Carrots in Main Warehouse, Rack 1, Place 302
(1, 1, 302, 6, 45),
-- Product 6: Fresh Carrots in Main Warehouse, Rack 2, Place 302
(1, 2, 302, 6, 20),
-- Product 6: Fresh Carrots in Main Warehouse, Rack 3, Place 302
(1, 3, 302, 6, 10),

-- Product 7: Board Game in Main Warehouse, Rack 1, Place 401
(1, 1, 401, 7, 25),
-- Product 7: Board Game in Main Warehouse, Rack 2, Place 401
(1, 2, 401, 7, 15),
-- Product 7: Board Game in Main Warehouse, Rack 3, Place 401
(1, 3, 401, 7, 8),

-- Product 8: Kids T-Shirt in Main Warehouse, Rack 1, Place 402
(1, 1, 402, 8, 30),
-- Product 8: Kids T-Shirt in Main Warehouse, Rack 2, Place 402
(1, 2, 402, 8, 15),
-- Product 8: Kids T-Shirt in Main Warehouse, Rack 3, Place 402
(1, 3, 402, 8, 7),

-- Product 9: Baking Flour in Main Warehouse, Rack 1, Place 501
(1, 1, 501, 9, 55),
-- Product 9: Baking Flour in Main Warehouse, Rack 2, Place 501
(1, 2, 501, 9, 25),
-- Product 9: Baking Flour in Main Warehouse, Rack 3, Place 501
(1, 3, 501, 9, 12),

-- Product 10: Ceramic Vase in Main Warehouse, Rack 1, Place 502
(1, 1, 502, 10, 12),
-- Product 10: Ceramic Vase in Main Warehouse, Rack 2, Place 502
(1, 2, 502, 10, 8),
-- Product 10: Ceramic Vase in Main Warehouse, Rack 3, Place 502
(1, 3, 502, 10, 4),

-- Product 11: Smartphone in Main Warehouse, Rack 4, Place 101
(1, 4, 101, 11, 10),
-- Product 11: Smartphone in Main Warehouse, Rack 5, Place 101
(1, 5, 101, 11, 5),

-- Product 12: Bluetooth Speaker in Main Warehouse, Rack 4, Place 102
(1, 4, 102, 12, 15),
-- Product 12: Bluetooth Speaker in Main Warehouse, Rack 5, Place 102
(1, 5, 102, 12, 7),

-- Product 13: Coffee Maker in Main Warehouse, Rack 4, Place 201
(1, 4, 201, 13, 8),
-- Product 13: Coffee Maker in Main Warehouse, Rack 5, Place 201
(1, 5, 201, 13, 4),

-- Product 14: Winter Jacket in Main Warehouse, Rack 4, Place 202
(1, 4, 202, 14, 20),
-- Product 14: Winter Jacket in Main Warehouse, Rack 5, Place 202
(1, 5, 202, 14, 10),

-- Product 15: Running Shoes in Main Warehouse, Rack 4, Place 301
(1, 4, 301, 15, 18),
-- Product 15: Running Shoes in Main Warehouse, Rack 5, Place 301
(1, 5, 301, 15, 8),

-- Product 16: Yoga Mat in Main Warehouse, Rack 4, Place 302
(1, 4, 302, 16, 22),
-- Product 16: Yoga Mat in Main Warehouse, Rack 5, Place 302
(1, 5, 302, 16, 10),

-- Product 17: Backpack in Main Warehouse, Rack 4, Place 401
(1, 4, 401, 17, 14),
-- Product 17: Backpack in Main Warehouse, Rack 5, Place 401
(1, 5, 401, 17, 7),

-- Product 18: Water Bottle in Main Warehouse, Rack 4, Place 402
(1, 4, 402, 18, 30),
-- Product 18: Water Bottle in Main Warehouse, Rack 5, Place 402
(1, 5, 402, 18, 15),

-- Product 19: Camping Tent in Main Warehouse, Rack 4, Place 501
(1, 4, 501, 19, 6),
-- Product 19: Camping Tent in Main Warehouse, Rack 5, Place 501
(1, 5, 501, 19, 3),

-- Product 20: Fishing Rod in Main Warehouse, Rack 4, Place 502
(1, 4, 502, 20, 8),
-- Product 20: Fishing Rod in Main Warehouse, Rack 5, Place 502
(1, 5, 502, 20, 4),

-- Product 21: Guitar in North Warehouse, Rack 1, Place 101
(2, 1, 101, 21, 5),
-- Product 21: Guitar in North Warehouse, Rack 2, Place 101
(2, 2, 101, 21, 2),

-- Product 22: Digital Camera in North Warehouse, Rack 1, Place 102
(2, 1, 102, 22, 6),
-- Product 22: Digital Camera in North Warehouse, Rack 2, Place 102
(2, 2, 102, 22, 3),

-- Product 23: Laptop in North Warehouse, Rack 1, Place 201
(2, 1, 201, 23, 4),
-- Product 23: Laptop in North Warehouse, Rack 2, Place 201
(2, 2, 201, 23, 2),

-- Product 24: Smartwatch in North Warehouse, Rack 1, Place 202
(2, 1, 202, 24, 12),
-- Product 24: Smartwatch in North Warehouse, Rack 2, Place 202
(2, 2, 202, 24, 6),

-- Product 25: Headphones in North Warehouse, Rack 1, Place 301
(2, 1, 301, 25, 10),
-- Product 25: Headphones in North Warehouse, Rack 2, Place 301
(2, 2, 301, 25, 5),

-- Product 26: Electric Kettle in North Warehouse, Rack 1, Place 302
(2, 1, 302, 26, 8),
-- Product 26: Electric Kettle in North Warehouse, Rack 2, Place 302
(2, 2, 302, 26, 4),

-- Product 27: Blender in North Warehouse, Rack 1, Place 401
(2, 1, 401, 27, 7),
-- Product 27: Blender in North Warehouse, Rack 2, Place 401
(2, 2, 401, 27, 3),

-- Product 28: Vacuum Cleaner in North Warehouse, Rack 1, Place 402
(2, 1, 402, 28, 5),
-- Product 28: Vacuum Cleaner in North Warehouse, Rack 2, Place 402
(2, 2, 402, 28, 2),

-- Product 29: Air Purifier in North Warehouse, Rack 1, Place 501
(2, 1, 501, 29, 6),
-- Product 29: Air Purifier in North Warehouse, Rack 2, Place 501
(2, 2, 501, 29, 3),

-- Product 30: Electric Toothbrush in North Warehouse, Rack 1, Place 502
(2, 1, 502, 30, 20),
-- Product 30: Electric Toothbrush in North Warehouse, Rack 2, Place 502
(2, 2, 502, 30, 10),

-- Warehouse 3 (South Warehouse)
(3, 1, 101, 1, 15),
(3, 1, 102, 2, 10),
(3, 2, 201, 3, 8),
(3, 2, 202, 4, 5),
(3, 3, 301, 5, 12),

-- Warehouse 4 (East Warehouse)
(4, 1, 101, 11, 7),
(4, 1, 102, 12, 5),
(4, 2, 201, 13, 4),
(4, 2, 202, 14, 6),
(4, 3, 301, 15, 5),
(4, 3, 302, 16, 3),
(4, 4, 401, 17, 2),
(4, 4, 402, 18, 8),
(4, 5, 501, 19, 2),
(4, 5, 502, 20, 1),

-- Warehouse 5 (West Warehouse)
(5, 1, 101, 21, 4),
(5, 1, 102, 22, 2),
(5, 2, 201, 23, 3),
(5, 2, 202, 24, 2),
(5, 3, 301, 25, 5),
(5, 3, 302, 26, 3),

-- Warehouse 6 (Central Warehouse)
(6, 1, 101, 1, 10),
(6, 1, 102, 2, 8),
(6, 2, 201, 3, 6),
(6, 2, 202, 4, 4),
(6, 3, 301, 5, 12),

-- Warehouse 7 (Regional Warehouse A)
(7, 1, 101, 11, 3),
(7, 1, 102, 12, 2),
(7, 2, 201, 13, 2),
(7, 2, 202, 14, 1),
(7, 3, 301, 15, 2),
(7, 3, 302, 16, 1),
(7, 4, 401, 17, 2),
(7, 4, 402, 18, 2),
(7, 5, 501, 19, 1),
(7, 5, 502, 20, 1),

-- Warehouse 8 (Regional Warehouse B)
(8, 1, 101, 21, 2),
(8, 1, 102, 22, 1),
(8, 2, 201, 23, 2),
(8, 2, 202, 24, 1),
(8, 3, 301, 25, 2),
(8, 3, 302, 26, 1),

-- Warehouse 9 (Distribution Center)
(9, 1, 101, 1, 5),
(9, 1, 102, 2, 4),
(9, 2, 201, 3, 3),
(9, 2, 202, 4, 2),
(9, 3, 301, 5, 2),
(9, 3, 302, 6, 1),
(9, 4, 401, 7, 1),
(9, 4, 402, 8, 1),
(9, 5, 501, 9, 1),
(9, 5, 502, 10, 1),

-- Warehouse 10 (Backup Warehouse)
(10, 1, 101, 11, 1),
(10, 1, 102, 12, 1),
(10, 2, 201, 13, 1),
(10, 2, 202, 14, 1),
(10, 3, 301, 15, 1),
(10, 3, 302, 16, 1),
(10, 4, 401, 17, 1),
(10, 4, 402, 18, 1),
(10, 5, 501, 19, 1),
(10, 5, 502, 20, 1);

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert order status
INSERT INTO inventhor.orderstatus (name)
VALUES
('Ordered'),
('Picked'),
('Delivered'),
('Shipped'),
('Cancelled');

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert customer orders
INSERT INTO inventhor.customerorder (customernr, orderdate, statusnr, deliverydate)
VALUES
-- Existing orders for May 2025
(1, '2025-05-01', 2, NULL),
(2, '2025-05-02', 2, NULL),
(3, '2025-05-03', 3, '2025-05-05'),
(4, '2025-05-04', 4, NULL),
(5, '2025-05-05', 5, NULL),
(6, '2025-05-06', 2, NULL),
(7, '2025-05-07', 2, NULL),
(8, '2025-05-08', 3, '2025-05-10'),
(9, '2025-05-09', 4, NULL),
(10, '2025-05-10', 5, NULL),
(11, '2025-05-11', 2, NULL),
(12, '2025-05-12', 2, NULL),
(13, '2025-05-13', 3, '2025-05-15'),
(14, '2025-05-14', 4, NULL),
(15, '2025-05-15', 5, NULL),
(16, '2025-05-16', 2, NULL),
(17, '2025-05-17', 2, NULL),
(18, '2025-05-18', 3, '2025-05-20'),
(19, '2025-05-19', 4, NULL),
(20, '2025-05-20', 5, NULL),

-- Add 10+ orders per month from Jan 2024 to Jun 2025
-- January 2024
(1, '2024-01-02', 2, '2024-01-23'),
(2, '2024-01-04', 3, '2024-01-25'),
(3, '2024-01-07', 4, '2024-01-28'),
(4, '2024-01-09', 5, '2024-01-30'),
(5, '2024-01-12', 2, '2024-02-02'),
(6, '2024-01-14', 3, '2024-02-04'),
(7, '2024-01-17', 4, '2024-02-07'),
(8, '2024-01-19', 5, '2024-02-09'),
(9, '2024-01-22', 2, '2024-02-12'),
(10, '2024-01-24', 3, '2024-02-14'),

-- February 2024
(11, '2024-02-02', 4, '2024-02-23'),
(12, '2024-02-04', 5, '2024-02-25'),
(13, '2024-02-07', 2, '2024-02-28'),
(14, '2024-02-09', 3, '2024-03-01'),
(15, '2024-02-12', 4, '2024-03-04'),
(16, '2024-02-14', 5, '2024-03-06'),
(17, '2024-02-17', 2, '2024-03-09'),
(18, '2024-02-19', 3, '2024-03-11'),
(19, '2024-02-22', 4, '2024-03-14'),
(20, '2024-02-24', 5, '2024-03-16'),

-- March 2024
(1, '2024-03-02', 2, '2024-03-23'),
(2, '2024-03-04', 3, '2024-03-25'),
(3, '2024-03-07', 4, '2024-03-28'),
(4, '2024-03-09', 5, '2024-03-30'),
(5, '2024-03-12', 2, '2024-04-02'),
(6, '2024-03-14', 3, '2024-04-04'),
(7, '2024-03-17', 4, '2024-04-07'),
(8, '2024-03-19', 5, '2024-04-09'),
(9, '2024-03-22', 2, '2024-04-12'),
(10, '2024-03-24', 3, '2024-04-14'),

-- April 2024
(11, '2024-04-02', 4, '2024-04-23'),
(12, '2024-04-04', 5, '2024-04-25'),
(13, '2024-04-07', 2, '2024-04-28'),
(14, '2024-04-09', 3, '2024-04-30'),
(15, '2024-04-12', 4, '2024-05-03'),
(16, '2024-04-14', 5, '2024-05-05'),
(17, '2024-04-17', 2, '2024-05-08'),
(18, '2024-04-19', 3, '2024-05-10'),
(19, '2024-04-22', 4, '2024-05-13'),
(20, '2024-04-24', 5, '2024-05-15'),

-- May 2024
(1, '2024-05-02', 2, '2024-05-23'),
(2, '2024-05-04', 3, '2024-05-25'),
(3, '2024-05-07', 4, '2024-05-28'),
(4, '2024-05-09', 5, '2024-05-30'),
(5, '2024-05-12', 2, '2024-06-02'),
(6, '2024-05-14', 3, '2024-06-04'),
(7, '2024-05-17', 4, '2024-06-07'),
(8, '2024-05-19', 5, '2024-06-09'),
(9, '2024-05-22', 2, '2024-06-12'),
(10, '2024-05-24', 3, '2024-06-14'),

-- June 2024
(11, '2024-06-02', 4, '2024-06-23'),
(12, '2024-06-04', 5, '2024-06-25'),
(13, '2024-06-07', 2, '2024-06-28'),
(14, '2024-06-09', 3, '2024-06-30'),
(15, '2024-06-12', 4, '2024-07-03'),
(16, '2024-06-14', 5, '2024-07-05'),
(17, '2024-06-17', 2, '2024-07-08'),
(18, '2024-06-19', 3, '2024-07-10'),
(19, '2024-06-22', 4, '2024-07-13'),
(20, '2024-06-24', 5, '2024-07-15'),

-- July 2024
(1, '2024-07-02', 2, '2024-07-23'),
(2, '2024-07-04', 3, '2024-07-25'),
(3, '2024-07-07', 4, '2024-07-28'),
(4, '2024-07-09', 5, '2024-07-30'),
(5, '2024-07-12', 2, '2024-08-02'),
(6, '2024-07-14', 3, '2024-08-04'),
(7, '2024-07-17', 4, '2024-08-07'),
(8, '2024-07-19', 5, '2024-08-09'),
(9, '2024-07-22', 2, '2024-08-12'),
(10, '2024-07-24', 3, '2024-08-14'),

-- August 2024
(11, '2024-08-02', 4, '2024-08-23'),
(12, '2024-08-04', 5, '2024-08-25'),
(13, '2024-08-07', 2, '2024-08-28'),
(14, '2024-08-09', 3, '2024-08-30'),
(15, '2024-08-12', 4, '2024-09-02'),
(16, '2024-08-14', 5, '2024-09-04'),
(17, '2024-08-17', 2, '2024-09-07'),
(18, '2024-08-19', 3, '2024-09-09'),
(19, '2024-08-22', 4, '2024-09-12'),
(20, '2024-08-24', 5, '2024-09-14'),

-- September 2024
(1, '2024-09-02', 2, '2024-09-23'),
(2, '2024-09-04', 3, '2024-09-25'),
(3, '2024-09-07', 4, '2024-09-28'),
(4, '2024-09-09', 5, '2024-09-30'),
(5, '2024-09-12', 2, '2024-10-03'),
(6, '2024-09-14', 3, '2024-10-05'),
(7, '2024-09-17', 4, '2024-10-08'),
(8, '2024-09-19', 5, '2024-10-10'),
(9, '2024-09-22', 2, '2024-10-13'),
(10, '2024-09-24', 3, '2024-10-15'),

-- October 2024
(11, '2024-10-02', 4, '2024-10-23'),
(12, '2024-10-04', 5, '2024-10-25'),
(13, '2024-10-07', 2, '2024-10-28'),
(14, '2024-10-09', 3, '2024-10-30'),
(15, '2024-10-12', 4, '2024-11-02'),
(16, '2024-10-14', 5, '2024-11-04'),
(17, '2024-10-17', 2, '2024-11-07'),
(18, '2024-10-19', 3, '2024-11-09'),
(19, '2024-10-22', 4, '2024-11-12'),
(20, '2024-10-24', 5, '2024-11-14'),

-- November 2024
(1, '2024-11-02', 2, '2024-11-23'),
(2, '2024-11-04', 3, '2024-11-25'),
(3, '2024-11-07', 4, '2024-11-28'),
(4, '2024-11-09', 5, '2024-11-30'),
(5, '2024-11-12', 2, '2024-12-03'),
(6, '2024-11-14', 3, '2024-12-05'),
(7, '2024-11-17', 4, '2024-12-08'),
(8, '2024-11-19', 5, '2024-12-10'),
(9, '2024-11-22', 2, '2024-12-13'),
(10, '2024-11-24', 3, '2024-12-14'),

-- December 2024
(11, '2024-12-02', 4, '2024-12-23'),
(12, '2024-12-04', 5, '2024-12-25'),
(13, '2024-12-07', 2, '2024-12-28'),
(14, '2024-12-09', 3, '2024-12-30'),
(15, '2024-12-12', 4, '2025-01-02'),
(16, '2024-12-14', 5, '2025-01-04'),
(17, '2024-12-17', 2, '2025-01-07'),
(18, '2024-12-19', 3, '2025-01-09'),
(19, '2024-12-22', 4, '2025-01-12'),
(20, '2024-12-24', 5, '2025-01-14'),

-- January 2025
(1, '2025-01-02', 2, '2025-01-23'),
(2, '2025-01-04', 3, '2025-01-25'),
(3, '2025-01-07', 4, '2025-01-28'),
(4, '2025-01-09', 5, '2025-01-30'),
(5, '2025-01-12', 2, '2025-02-02'),
(6, '2025-01-14', 3, '2025-02-04'),
(7, '2025-01-17', 4, '2025-02-07'),
(8, '2025-01-19', 5, '2025-02-09'),
(9, '2025-01-22', 2, '2025-02-12'),
(10, '2025-01-24', 3, '2025-02-14'),

-- February 2025
(11, '2025-02-02', 4, '2025-02-23'),
(12, '2025-02-04', 5, '2025-02-25'),
(13, '2025-02-07', 2, '2025-02-28'),
(14, '2025-02-09', 3, '2025-03-02'),
(15, '2025-02-12', 4, '2025-03-05'),
(16, '2025-02-14', 5, '2025-03-07'),
(17, '2025-02-17', 2, '2025-03-10'),
(18, '2025-02-19', 3, '2025-03-12'),
(19, '2025-02-22', 4, '2025-03-15'),
(20, '2025-02-24', 5, '2025-03-17'),

-- March 2025
(1, '2025-03-02', 2, '2025-03-23'),
(2, '2025-03-04', 3, '2025-03-25'),
(3, '2025-03-07', 4, '2025-03-28'),
(4, '2025-03-09', 5, '2025-03-30'),
(5, '2025-03-12', 2, '2025-04-02'),
(6, '2025-03-14', 3, '2025-04-04'),
(7, '2025-03-17', 4, '2025-04-07'),
(8, '2025-03-19', 5, '2025-04-09'),
(9, '2025-03-22', 2, '2025-04-12'),
(10, '2025-03-24', 3, '2025-04-14'),

-- April 2025
(11, '2025-04-02', 4, NULL),
(12, '2025-04-04', 5, NULL),
(13, '2025-04-07', 2, NULL),
(14, '2025-04-09', 3, '2025-04-11'),
(15, '2025-04-12', 4, NULL),
(16, '2025-04-14', 5, NULL),
(17, '2025-04-17', 2, NULL),
(18, '2025-04-19', 3, '2025-04-21'),
(19, '2025-04-22', 4, NULL),
(20, '2025-04-24', 5, NULL),

-- June 2025
(1, '2025-06-02', 2, NULL),
(2, '2025-06-04', 3, '2025-06-06'),
(3, '2025-06-07', 4, NULL),
(4, '2025-06-09', 5, NULL),
(5, '2025-06-12', 2, NULL),
(6, '2025-06-14', 3, '2025-06-16'),
(7, '2025-06-17', 4, NULL),
(8, '2025-06-19', 5, NULL),
(9, '2025-06-22', 2, NULL),
(10, '2025-06-24', 3, '2025-06-26');

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert product to customer order
INSERT INTO inventhor.customerorderproduct (ordernr, productnr, warehousenr, quantity)
VALUES
-- Orders 1-100 (existing, unchanged)
(1, 1, 1, 5),(2, 2, 2, 3),(3, 3, 1, 2),(4, 4, 3, 1),(5, 5, 5, 4),
(6, 6, 6, 6),(7, 7, 2, 2),(8, 8, 1, 3),(9, 9, 9, 5),(10, 10, 10, 1),
(11, 11, 4, 2),(12, 12, 1, 3),(13, 13, 6, 1),(14, 14, 7, 4),(15, 15, 8, 2),
(16, 16, 1, 3),(17, 17, 7, 1),(18, 18, 1, 5),(19, 19, 1, 2),(1, 2, 2, 2),
(2, 3, 1, 1),(3, 4, 3, 2),(4, 5, 5, 1),(5, 6, 6, 2),(6, 7, 2, 1),
(7, 8, 1, 2),(8, 9, 9, 1),(9, 10, 10, 2),(10, 1, 1, 1),(11, 12, 1, 2),
(12, 13, 6, 1),(13, 14, 7, 2),(14, 15, 8, 1),(15, 16, 1, 2),(16, 17, 7, 1),
(17, 18, 1, 2),(18, 19, 1, 1),(19, 20, 1, 2),(20, 11, 4, 1),
(1, 3, 3, 1),(1, 4, 4, 2),
(2, 5, 6, 2),(2, 6, 7, 1),
(3, 7, 8, 1),(3, 8, 9, 2),
(4, 9, 10, 1),(4, 10, 1, 1),
(5, 11, 2, 1),(5, 12, 3, 1),
(6, 13, 4, 1),(6, 14, 5, 1),
(7, 15, 6, 1),(7, 16, 7, 1),
(8, 17, 8, 1),(8, 18, 9, 1),
(9, 19, 10, 1),(9, 20, 1, 1),
(10, 3, 2, 1),(10, 4, 3, 1),
(11, 5, 4, 1),(11, 6, 5, 1),
(12, 7, 6, 1),(12, 8, 7, 1),
(13, 9, 8, 1),(13, 10, 9, 1),
(14, 11, 10, 1),(14, 12, 1, 1),
(15, 13, 2, 1),(15, 14, 3, 1),
(16, 15, 4, 1),
(17, 17, 5, 1),(17, 18, 6, 1),
(18, 19, 7, 1),(18, 20, 8, 1),
(19, 3, 9, 1),(19, 4, 10, 1),
(20, 5, 1, 1),(20, 6, 2, 1),
(21, 3, 3, 2),(22, 4, 4, 1),(23, 5, 5, 3),(24, 6, 6, 2),(25, 7, 7, 1),
(26, 8, 8, 2),(27, 9, 9, 1),(28, 10, 10, 2),(29, 1, 1, 1),(30, 2, 2, 2),
(31, 3, 3, 1),(32, 4, 4, 2),(33, 5, 5, 1),(34, 6, 6, 2),(35, 7, 7, 1),
(36, 8, 8, 2),(37, 9, 9, 1),(38, 10, 10, 2),(39, 1, 1, 1),(40, 2, 2, 2),
(41, 3, 3, 2),(42, 4, 4, 1),(43, 5, 5, 3),(44, 6, 6, 2),(45, 7, 7, 1),
(46, 8, 8, 2),(47, 9, 9, 1),(48, 10, 10, 2),(49, 1, 1, 1),(50, 2, 2, 2),
(51, 3, 3, 1),(52, 4, 4, 2),(53, 5, 5, 1),(54, 6, 6, 2),(55, 7, 7, 1),
(56, 8, 8, 2),(57, 9, 9, 1),(58, 10, 10, 2),(59, 1, 1, 1),(60, 2, 2, 2),
(61, 3, 3, 2),(62, 4, 4, 1),(63, 5, 5, 3),(64, 6, 6, 2),(65, 7, 7, 1),
(66, 8, 8, 2),(67, 9, 9, 1),(68, 10, 10, 2),(69, 1, 1, 1),(70, 2, 2, 2),
(71, 3, 3, 1),(72, 4, 4, 2),(73, 5, 5, 1),(74, 6, 6, 2),(75, 7, 7, 1),
(76, 8, 8, 2),(77, 9, 9, 1),(78, 10, 10, 2),(79, 1, 1, 1),(80, 2, 2, 2),
(81, 3, 3, 2),(82, 4, 4, 1),(83, 5, 5, 3),(84, 6, 6, 2),(85, 7, 7, 1),
(86, 8, 8, 2),(87, 9, 9, 1),(88, 10, 10, 2),(89, 1, 1, 1),(90, 2, 2, 2),
(91, 3, 3, 1),(92, 4, 4, 2),(93, 5, 5, 1),(94, 6, 6, 2),(95, 7, 7, 1),
(96, 8, 8, 2),(97, 9, 9, 1),(98, 10, 10, 2),(99, 1, 1, 1),(100, 2, 2, 2),

(101, 3, 3, 2),(102, 4, 4, 1),(103, 5, 5, 3),(104, 6, 6, 2),(105, 7, 7, 1),
(106, 8, 8, 2),(107, 9, 9, 1),(108, 10, 10, 2),(109, 1, 1, 1),(110, 2, 2, 2),
(111, 3, 3, 1),(112, 4, 4, 2),(113, 5, 5, 1),(114, 6, 6, 2),(115, 7, 7, 1),
(116, 8, 8, 2),(117, 9, 9, 1),(118, 10, 10, 2),(119, 1, 1, 1),(120, 2, 2, 2),
(121, 3, 3, 2),(122, 4, 4, 1),(123, 5, 5, 3),(124, 6, 6, 2),(125, 7, 7, 1),
(126, 8, 8, 2),(127, 9, 9, 1),(128, 10, 10, 2),(129, 1, 1, 1),(130, 2, 2, 2),
(131, 3, 3, 1),(132, 4, 4, 2),(133, 5, 5, 1),(134, 6, 6, 2),(135, 7, 7, 1),
(136, 8, 8, 2),(137, 9, 9, 1),(138, 10, 10, 2),(139, 1, 1, 1),(140, 2, 2, 2),
(141, 3, 3, 2),(142, 4, 4, 1),(143, 5, 5, 3),(144, 6, 6, 2),(145, 7, 7, 1),
(146, 8, 8, 2),(147, 9, 9, 1),(148, 10, 10, 2),(149, 1, 1, 1),(150, 2, 2, 2),
(151, 3, 3, 1),(152, 4, 4, 2),(153, 5, 5, 1),(154, 6, 6, 2),(155, 7, 7, 1),
(156, 8, 8, 2),(157, 9, 9, 1),(158, 10, 10, 2),(159, 1, 1, 1),(160, 2, 2, 2),
(161, 3, 3, 2),(162, 4, 4, 1),(163, 5, 5, 3),(164, 6, 6, 2),(165, 7, 7, 1),
(166, 8, 8, 2),(167, 9, 9, 1),(168, 10, 10, 2),(169, 1, 1, 1),(170, 2, 2, 2),
(171, 3, 3, 1),(172, 4, 4, 2),(173, 5, 5, 1),(174, 6, 6, 2),(175, 7, 7, 1),
(176, 8, 8, 2),(177, 9, 9, 1),(178, 10, 10, 2),(179, 1, 1, 1),(180, 2, 2, 2),
(181, 3, 3, 2),(182, 4, 4, 1),(183, 5, 5, 3),(184, 6, 6, 2),(185, 7, 7, 1),
(186, 8, 8, 2),(187, 9, 9, 1),(188, 10, 10, 2),(189, 1, 1, 1),(190, 2, 2, 2),

(180, 3, 4, 2),(180, 4, 5, 1),
(181, 5, 6, 3),(181, 6, 7, 2),
(182, 7, 8, 1),(182, 8, 9, 2),
(183, 9, 10, 1),(183, 10, 1, 2),
(184, 1, 2, 1),(184, 2, 3, 2),
(185, 3, 4, 1),(185, 4, 5, 2),
(186, 5, 6, 1),(186, 6, 7, 2),
(187, 7, 8, 1),(187, 8, 9, 2),
(188, 9, 10, 1),(188, 10, 1, 2),

(190, 3, 4, 2),(190, 4, 5, 1),

-- Add productnr 1 for orders 21 to 190
(21, 1, 1, 12),
(22, 1, 2, 25),
(23, 1, 3, 37),
(24, 1, 4, 44),
(25, 1, 5, 53),
(26, 1, 6, 67),
(27, 1, 7, 19),
(28, 1, 8, 88),
(29, 1, 9, 7),
(30, 1, 10, 61),
(31, 1, 1, 29),
(32, 1, 2, 73),
(33, 1, 3, 56),
(34, 1, 4, 8),
(35, 1, 5, 92),
(36, 1, 6, 21),
(37, 1, 7, 34),
(38, 1, 8, 77),
(39, 1, 9, 49),
(40, 1, 10, 65),
(41, 1, 1, 14),
(42, 1, 2, 97),
(43, 1, 3, 3),
(44, 1, 4, 84),
(45, 1, 5, 58),
(46, 1, 6, 27),
(47, 1, 7, 100),
(48, 1, 8, 41),
(49, 1, 9, 16),
(50, 1, 10, 70),
(51, 1, 1, 36),
(52, 1, 2, 63),
(53, 1, 3, 11),
(54, 1, 4, 80),
(55, 1, 5, 23),
(56, 1, 6, 90),
(57, 1, 7, 5),
(58, 1, 8, 54),
(59, 1, 9, 32),
(60, 1, 10, 76),
(61, 1, 1, 18),
(62, 1, 2, 99),
(63, 1, 3, 2),
(64, 1, 4, 85),
(65, 1, 5, 60),
(66, 1, 6, 28),
(67, 1, 7, 95),
(68, 1, 8, 47),
(69, 1, 9, 13),
(70, 1, 10, 68),
(71, 1, 1, 39),
(72, 1, 2, 74),
(73, 1, 3, 57),
(74, 1, 4, 9),
(75, 1, 5, 93),
(76, 1, 6, 22),
(77, 1, 7, 35),
(78, 1, 8, 78),
(79, 1, 9, 50),
(80, 1, 10, 66),
(81, 1, 1, 15),
(82, 1, 2, 98),
(83, 1, 3, 4),
(84, 1, 4, 83),
(85, 1, 5, 59),
(86, 1, 6, 26),
(87, 1, 7, 96),
(88, 1, 8, 40),
(89, 1, 9, 17),
(90, 1, 10, 71),
(91, 1, 1, 38),
(92, 1, 2, 75),
(93, 1, 3, 55),
(94, 1, 4, 10),
(95, 1, 5, 91),
(96, 1, 6, 20),
(97, 1, 7, 33),
(98, 1, 8, 79),
(99, 1, 9, 51),
(100, 1, 10, 64),
(101, 1, 1, 24),
(102, 1, 2, 89),
(103, 1, 3, 6),
(104, 1, 4, 82),
(105, 1, 5, 61),
(106, 1, 6, 30),
(107, 1, 7, 94),
(108, 1, 8, 43),
(109, 1, 9, 12),
(110, 1, 10, 69),
(111, 1, 1, 37),
(112, 1, 2, 72),
(113, 1, 3, 53),
(114, 1, 4, 7),
(115, 1, 5, 87),
(116, 1, 6, 25),
(117, 1, 7, 48),
(118, 1, 8, 81),
(119, 1, 9, 52),
(120, 1, 10, 67),
(121, 1, 1, 19),
(122, 1, 2, 86),
(123, 1, 3, 31),
(124, 1, 4, 73),
(125, 1, 5, 62),
(126, 1, 6, 8),
(127, 1, 7, 91),
(128, 1, 8, 21),
(129, 1, 9, 34),
(130, 1, 10, 77),
(131, 1, 1, 49),
(132, 1, 2, 65),
(133, 1, 3, 14),
(134, 1, 4, 97),
(135, 1, 5, 3),
(136, 1, 6, 84),
(137, 1, 7, 58),
(138, 1, 8, 27),
(139, 1, 9, 100),
(140, 1, 10, 41),
(141, 1, 1, 16),
(142, 1, 2, 70),
(143, 1, 3, 36),
(144, 1, 4, 63),
(145, 1, 5, 11),
(146, 1, 6, 80),
(147, 1, 7, 23),
(148, 1, 8, 90),
(149, 1, 9, 5),
(150, 1, 10, 54),
(151, 1, 1, 32),
(152, 1, 2, 76),
(153, 1, 3, 18),
(154, 1, 4, 99),
(155, 1, 5, 2),
(156, 1, 6, 85),
(157, 1, 7, 60),
(158, 1, 8, 28),
(159, 1, 9, 95),
(160, 1, 10, 47),
(161, 1, 1, 13),
(162, 1, 2, 68),
(163, 1, 3, 39),
(164, 1, 4, 74),
(165, 1, 5, 57),
(166, 1, 6, 9),
(167, 1, 7, 93),
(168, 1, 8, 22),
(169, 1, 9, 35),
(170, 1, 10, 78),
(171, 1, 1, 50),
(172, 1, 2, 66),
(173, 1, 3, 15),
(174, 1, 4, 98),
(175, 1, 5, 4),
(176, 1, 6, 83),
(177, 1, 7, 59),
(178, 1, 8, 26),
(179, 1, 9, 96),
(180, 1, 10, 40),
(181, 1, 1, 17),
(182, 1, 2, 71),
(183, 1, 3, 38),
(184, 1, 4, 75),
(185, 1, 5, 55),
(186, 1, 6, 10),
(187, 1, 7, 91),
(188, 1, 8, 20),
(189, 1, 9, 33),
(190, 1, 10, 79);
-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert customer payment
INSERT INTO inventhor.customerpayment (ordernr, paymentdate, paymentmethod, amount)
VALUES
-- Payment for Order 1
(1, '2025-05-01', 1, 830.00),
-- Payment for Order 2
(2, '2025-05-02', 2, 205.00),
-- Payment for Order 3
(3, '2025-05-03', 3, 535.00),
-- Payment for Order 4
(4, '2025-05-04', 1, 355.00),
-- Payment for Order 5
(5, NULL, 1, 1035.00),
-- Payment for Order 6
(6, '2025-05-06', 1, 175.00),
-- Payment for Order 7
(7, '2025-05-07', 2, 435.00),
-- Payment for Order 8
(8, '2025-05-08', 3, 185.00),
-- Payment for Order 9
(9, '2025-05-09', 1, 185.00),
-- Payment for Order 10
(10, NULL, 1, 185.00),
-- Payment for Order 11
(11, '2025-05-11', 1, 1655.00),
-- Payment for Order 12
(12, '2025-05-12', 2, 935.00),
-- Payment for Order 13
(13, '2025-05-13', 3, 1235.00),
-- Payment for Order 14
(14, '2025-05-14', 1, 6035.00),
-- Payment for Order 15
(15, NULL, 1, 1635.00),
-- Payment for Order 16
(16, '2025-05-16', 1, 635.00),
-- Payment for Order 17
(17, '2025-05-17', 2, 635.00),
-- Payment for Order 18
(18, '2025-05-18', 3, 535.00),
-- Payment for Order 19
(19, '2025-05-19', 1, 95.00),
-- Payment for Order 20
(20, NULL, 1, 135.00),

-- Orders 21-50:
(21, NULL, 1, 440.00),
(22, NULL, 1, 800.00),
(23, NULL, 1, 1040.00),
(24, NULL, 1, 1480.00),
(25, NULL, 1, 1310.00),
(26, NULL, 1, 1360.00),
(27, NULL, 1, 580.00),
(28, NULL, 1, 1860.00),
(29, NULL, 1, 170.00),
(30, NULL, 1, 1520.00),
(31, NULL, 1, 1380.00),
(32, NULL, 1, 2060.00),
(33, NULL, 1, 2320.00),
(34, NULL, 1, 3160.00),
(35, NULL, 1, 2640.00),
(36, NULL, 1, 820.00),
(37, NULL, 1, 1280.00),
(38, NULL, 1, 1740.00),
(39, NULL, 1, 3980.00),
(40, NULL, 1, 4300.00),
(41, NULL, 1, 2780.00),
(42, NULL, 1, 11940.00),
(43, NULL, 1, 12060.00),
(44, NULL, 1, 5680.00),
(45, NULL, 1, 2660.00),
(46, NULL, 1, 2140.00),
(47, NULL, 1, 3000.00),
(48, NULL, 1, 5820.00),
(49, NULL, 1, 2320.00),
(50, NULL, 1, 2400.00),

-- Orders 51-100
(51, NULL, 1, 320.00),
(52, NULL, 1, 1240.00),
(53, NULL, 1, 1370.00),
(54, NULL, 1, 80.00),
(55, NULL, 1, 1460.00),
(56, NULL, 1, 460.00),
(57, NULL, 1, 290.00),
(58, NULL, 1, 320.00),
(59, NULL, 1, 20.00),
(60, NULL, 1, 70.00),
(61, NULL, 1, 220.00),
(62, NULL, 1, 340.00),
(63, NULL, 1, 790.00),
(64, NULL, 1, 140.00),
(65, NULL, 1, 1420.00),
(66, NULL, 1, 220.00),
(67, NULL, 1, 210.00),
(68, NULL, 1, 320.00),
(69, NULL, 1, 20.00),
(70, NULL, 1, 70.00),
(71, NULL, 1, 320.00),
(72, NULL, 1, 640.00),
(73, NULL, 1, 830.00),
(74, NULL, 1, 80.00),
(75, NULL, 1, 1460.00),
(76, NULL, 1, 460.00),
(77, NULL, 1, 290.00),
(78, NULL, 1, 320.00),
(79, NULL, 1, 20.00),
(80, NULL, 1, 70.00),
(81, NULL, 1, 220.00),
(82, NULL, 1, 340.00),
(83, NULL, 1, 790.00),
(84, NULL, 1, 140.00),
(85, NULL, 1, 1420.00),
(86, NULL, 1, 220.00),
(87, NULL, 1, 210.00),
(88, NULL, 1, 320.00),
(89, NULL, 1, 20.00),
(90, NULL, 1, 70.00),
(91, NULL, 1, 320.00),
(92, NULL, 1, 640.00),
(93, NULL, 1, 830.00),
(94, NULL, 1, 80.00),
(95, NULL, 1, 1460.00),
(96, NULL, 1, 460.00),
(97, NULL, 1, 290.00),
(98, NULL, 1, 320.00),
(99, NULL, 1, 20.00),
(100, NULL, 1, 70.00),

-- Orders 101-190: Only Organic Apples (productnr=1)
(101, NULL, 1, 480.00),
(102, NULL, 1, 1780.00),
(103, NULL, 1, 120.00),
(104, NULL, 1, 1640.00),
(105, NULL, 1, 1220.00),
(106, NULL, 1, 600.00),
(107, NULL, 1, 1880.00),
(108, NULL, 1, 860.00),
(109, NULL, 1, 240.00),
(110, NULL, 1, 1380.00),
(111, NULL, 1, 740.00),
(112, NULL, 1, 1440.00),
(113, NULL, 1, 1060.00),
(114, NULL, 1, 140.00),
(115, NULL, 1, 1740.00),
(116, NULL, 1, 500.00),
(117, NULL, 1, 960.00),
(118, NULL, 1, 1620.00),
(119, NULL, 1, 1040.00),
(120, NULL, 1, 1340.00),
(121, NULL, 1, 380.00),
(122, NULL, 1, 1720.00),
(123, NULL, 1, 620.00),
(124, NULL, 1, 1460.00),
(125, NULL, 1, 1240.00),
(126, NULL, 1, 160.00),
(127, NULL, 1, 1820.00),
(128, NULL, 1, 420.00),
(129, NULL, 1, 680.00),
(130, NULL, 1, 1540.00),
(131, NULL, 1, 980.00),
(132, NULL, 1, 1300.00),
(133, NULL, 1, 280.00),
(134, NULL, 1, 1940.00),
(135, NULL, 1, 60.00),
(136, NULL, 1, 1680.00),
(137, NULL, 1, 1160.00),
(138, NULL, 1, 540.00),
(139, NULL, 1, 2000.00),
(140, NULL, 1, 820.00),
(141, NULL, 1, 320.00),
(142, NULL, 1, 1400.00),
(143, NULL, 1, 720.00),
(144, NULL, 1, 1260.00),
(145, NULL, 1, 220.00),
(146, NULL, 1, 1600.00),
(147, NULL, 1, 460.00),
(148, NULL, 1, 1800.00),
(149, NULL, 1, 100.00),
(150, NULL, 1, 1080.00),
(151, NULL, 1, 640.00),
(152, NULL, 1, 1520.00),
(153, NULL, 1, 360.00),
(154, NULL, 1, 1980.00),
(155, NULL, 1, 40.00),
(156, NULL, 1, 1700.00),
(157, NULL, 1, 1200.00),
(158, NULL, 1, 560.00),
(159, NULL, 1, 1900.00),
(160, NULL, 1, 940.00),
(161, NULL, 1, 260.00),
(162, NULL, 1, 1360.00),
(163, NULL, 1, 780.00),
(164, NULL, 1, 1480.00),
(165, NULL, 1, 1140.00),
(166, NULL, 1, 180.00),
(167, NULL, 1, 1860.00),
(168, NULL, 1, 440.00),
(169, NULL, 1, 700.00),
(170, NULL, 1, 1560.00),
(171, NULL, 1, 1000.00),
(172, NULL, 1, 1320.00),
(173, NULL, 1, 300.00),
(174, NULL, 1, 1960.00),
(175, NULL, 1, 80.00),
(176, NULL, 1, 1660.00),
(177, NULL, 1, 1180.00),
(178, NULL, 1, 520.00),
(179, NULL, 1, 1920.00),
(180, NULL, 1, 800.00),
(181, NULL, 1, 340.00),
(182, NULL, 1, 1420.00),
(183, NULL, 1, 760.00),
(184, NULL, 1, 1500.00),
(185, NULL, 1, 1100.00),
(186, NULL, 1, 200.00),
(187, NULL, 1, 1820.00),
(188, NULL, 1, 400.00),
(189, NULL, 1, 660.00),
(190, NULL, 1, 1580.00);

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert to warehouse order
INSERT INTO inventhor.warehouseorder (warehousenr, suppliernr, orderdate, statusnr, deliverydate)
VALUES
-- Warehouse Order 1: North Warehouse, Supplier 1, Ordered on 2025-05-01, Status: Ordered
(1, 1, '2025-05-01', 1, NULL),
-- Warehouse Order 2: South Warehouse, Supplier 2, Ordered on 2025-05-02, Status: Picked
(2, 2, '2025-05-02', 2, NULL),
-- Warehouse Order 3: East Warehouse, Supplier 3, Ordered on 2025-05-03, Status: Delivered
(3, 3, '2025-05-03', 3, '2025-05-05'),
-- Warehouse Order 4: West Warehouse, Supplier 4, Ordered on 2025-05-04, Status: Shipped
(4, 4, '2025-05-04', 4, '2025-05-06'),
-- Warehouse Order 5: Central Warehouse, Supplier 5, Ordered on 2025-05-05, Status: Cancelled
(5, 5, '2025-05-05', 5, NULL),
-- Warehouse Order 6: Regional Warehouse A, Supplier 6, Ordered on 2025-05-06, Status: Ordered
(6, 6, '2025-05-06', 1, NULL),
-- Warehouse Order 7: Regional Warehouse B, Supplier 7, Ordered on 2025-05-07, Status: Picked
(7, 7, '2025-05-07', 2, NULL),
-- Warehouse Order 8: Distribution Center, Supplier 8, Ordered on 2025-05-08, Status: Delivered
(8, 8, '2025-05-08', 3, '2025-05-10'),
-- Warehouse Order 9: Backup Warehouse, Supplier 9, Ordered on 2025-05-09, Status: Shipped
(9, 9, '2025-05-09', 4, '2025-05-11'),
-- Warehouse Order 10: Additional Warehouse, Supplier 10, Ordered on 2025-05-10, Status: Cancelled
(10, 10, '2025-05-10', 5, NULL),
-- Warehouse Order 11: North Warehouse, Supplier 1, Ordered on 2025-05-11, Status: Ordered
(1, 1, '2025-05-11', 1, NULL),
-- Warehouse Order 12: South Warehouse, Supplier 2, Ordered on 2025-05-12, Status: Picked
(2, 2, '2025-05-12', 2, NULL),
-- Warehouse Order 13: East Warehouse, Supplier 3, Ordered on 2025-05-13, Status: Delivered
(3, 3, '2025-05-13', 3, '2025-05-15'),
-- Warehouse Order 14: West Warehouse, Supplier 4, Ordered on 2025-05-14, Status: Shipped
(4, 4, '2025-05-14', 4, '2025-05-16'),
-- Warehouse Order 15: Central Warehouse, Supplier 5, Ordered on 2025-05-15, Status: Cancelled
(5, 5, '2025-05-15', 5, NULL),
-- Warehouse Order 16: Regional Warehouse A, Supplier 6, Ordered on 2025-05-16, Status: Ordered
(6, 6, '2025-05-16', 1, NULL),
-- Warehouse Order 17: Regional Warehouse B, Supplier 7, Ordered on 2025-05-17, Status: Picked
(7, 7, '2025-05-17', 2, NULL),
-- Warehouse Order 18: Distribution Center, Supplier 8, Ordered on 2025-05-18, Status: Delivered
(8, 8, '2025-05-18', 3, '2025-05-20'),
-- Warehouse Order 19: Backup Warehouse, Supplier 9, Ordered on 2025-05-19, Status: Shipped
(9, 9, '2025-05-19', 4, '2025-05-21'),
-- Warehouse Order 20: Additional Warehouse, Supplier 10, Ordered on 2025-05-20, Status: Cancelled
(10, 10, '2025-05-20', 5, NULL),

-- 2024 Warehouse Orders (Jan-Dec, 2 per month, 24 total)
-- Jan 2024
(1, 1, '2024-01-05', 1, '2024-01-26'),
(2, 2, '2024-01-15', 2, '2024-02-05'),
-- Feb 2024
(3, 3, '2024-02-05', 3, '2024-02-26'),
(4, 4, '2024-02-15', 4, '2024-03-07'),
-- Mar 2024
(5, 5, '2024-03-05', 5, '2024-03-26'),
(6, 6, '2024-03-15', 1, '2024-04-05'),
-- Apr 2024
(7, 7, '2024-04-05', 2, '2024-04-26'),
(8, 8, '2024-04-15', 3, '2024-05-06'),
-- May 2024
(9, 9, '2024-05-05', 4, '2024-05-26'),
(10, 10, '2024-05-15', 5, '2024-06-05'),
-- Jun 2024
(1, 1, '2024-06-05', 1, '2024-06-26'),
(2, 2, '2024-06-15', 2, '2024-07-06'),
-- Jul 2024
(3, 3, '2024-07-05', 3, '2024-07-26'),
(4, 4, '2024-07-15', 4, '2024-08-05'),
-- Aug 2024
(5, 5, '2024-08-05', 5, '2024-08-26'),
(6, 6, '2024-08-15', 1, '2024-09-05'),
-- Sep 2024
(7, 7, '2024-09-05', 2, '2024-09-26'),
(8, 8, '2024-09-15', 3, '2024-10-06'),
-- Oct 2024
(9, 9, '2024-10-05', 4, '2024-10-26'),
(10, 10, '2024-10-15', 5, '2024-11-05'),
-- Nov 2024
(1, 1, '2024-11-05', 1, '2024-11-26'),
(2, 2, '2024-11-15', 2, '2024-12-06'),
-- Dec 2024
(3, 3, '2024-12-05', 3, '2024-12-26'),
(4, 4, '2024-12-15', 4, '2025-01-05'),

-- 2025 Warehouse Orders (Jan, Feb, Mar, Apr, Jun, 2 per month, 12 total)
-- Jan 2025
(5, 5, '2025-01-05', 5, '2025-01-26'),
(6, 6, '2025-01-15', 1, '2025-02-05'),
-- Feb 2025
(7, 7, '2025-02-05', 2, '2025-02-26'),
(8, 8, '2025-02-15', 3, '2025-03-08'),
-- Mar 2025
(9, 9, '2025-03-05', 4, '2025-03-26'),
(10, 10, '2025-03-15', 5, '2025-04-05'),
-- Apr 2025
(1, 1, '2025-04-05', 1, '2025-04-26'),
(2, 2, '2025-04-15', 2, '2025-05-06'),
-- Jun 2025
(3, 3, '2025-06-05', 3, NULL),
(4, 4, '2025-06-15', 4, NULL),
(5, 5, '2025-06-25', 5, NULL),
(6, 6, '2025-06-28', 1, NULL);

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert product to warehouse order
INSERT INTO inventhor.warehouseorderproduct (ordernr, productnr, quantity,buyprice)
VALUES
-- Warehouse Order 1: North Warehouse, Product 1, Quantity 100, Buy Price 23.47
(1, 1, 100, 23.47),
-- Warehouse Order 2: South Warehouse, Product 2, Quantity 200, Buy Price 10.50
(2, 2, 200, 10.50),
-- Warehouse Order 3: East Warehouse, Product 3, Quantity 150, Buy Price 72.00
(3, 3, 150, 72.00),
-- Warehouse Order 4: West Warehouse, Product 4, Quantity 80, Buy Price 185.00
(4, 4, 80, 185.00),
-- Warehouse Order 5: Central Warehouse, Product 5, Quantity 120, Buy Price 175.00
(5, 5, 120, 175.00),
-- Warehouse Order 6: Regional Warehouse A, Product 6, Quantity 90, Buy Price 7.50
(6, 6, 90, 7.50),
-- Warehouse Order 7: Regional Warehouse B, Product 7, Quantity 70, Buy Price 118.00
(7, 7, 70, 118.00),
-- Warehouse Order 8: Distribution Center, Product 8, Quantity 110, Buy Price 32.00
(8, 8, 110, 32.00),
-- Warehouse Order 9: Backup Warehouse, Product 9, Quantity 130, Buy Price 19.50
(9, 9, 130, 19.50),
-- Warehouse Order 10: Additional Warehouse, Product 10, Quantity 40, Buy Price 92.00
(10, 10, 40, 92.00),
-- Warehouse Order 11: North Warehouse, Product 11, Quantity 60, Buy Price 610.00
(11, 11, 60, 610.00),
-- Warehouse Order 12: South Warehouse, Product 12, Quantity 80, Buy Price 175.00
(12, 12, 80, 175.00),
-- Warehouse Order 13: East Warehouse, Product 13, Quantity 50, Buy Price 920.00
(13, 13, 50, 920.00),
-- Warehouse Order 14: West Warehouse, Product 14, Quantity 90, Buy Price 890.00
(14, 14, 90, 890.00),
-- Warehouse Order 15: Central Warehouse, Product 15, Quantity 70, Buy Price 510.00
(15, 15, 70, 510.00),
-- Warehouse Order 16: Regional Warehouse A, Product 16, Quantity 100, Buy Price 125.00
(16, 16, 100, 125.00),
-- Warehouse Order 17: Regional Warehouse B, Product 17, Quantity 120, Buy Price 340.00
(17, 17, 120, 340.00),
-- Warehouse Order 18: Distribution Center, Product 18, Quantity 150, Buy Price 62.00
(18, 18, 150, 62.00),
-- Warehouse Order 19: Backup Warehouse, Product 19, Quantity 200, Buy Price 1820.00
(19, 19, 200, 1820.00),
-- Warehouse Order 20: Additional Warehouse, Product 20, Quantity 80, Buy Price 910.00
(20, 20, 80, 910.00),
-- Add more products (from 1 to 5) for orders from 21 to 51, with varying buyprice for the same product
(21, 1, 50, 17.82),
(21, 2, 30, 10.20),
(21, 3, 20, 71.00),
(21, 4, 10, 182.00),
(21, 5, 5, 179.00),

(22, 1, 40, 28.13),
(22, 2, 25, 10.80),
(22, 3, 15, 69.00),
(22, 4, 8, 181.00),
(22, 5, 4, 181.00),

(23, 1, 60, 11.67),
(23, 2, 35, 10.00),
(23, 3, 25, 73.00),
(23, 4, 12, 179.00),
(23, 5, 6, 183.00),

(24, 1, 30, 13.94),
(24, 2, 20, 10.60),
(24, 3, 10, 70.00),
(24, 4, 5, 180.00),
(24, 5, 2, 178.00),

(25, 1, 70, 19.21),
(25, 2, 45, 11.00),
(25, 3, 35, 72.00),
(25, 4, 18, 184.00),
(25, 5, 9, 180.00),

(26, 1, 55, 21.08),
(26, 2, 28, 10.30),
(26, 3, 18, 71.50),
(26, 4, 9, 181.00),
(26, 5, 4, 182.00),

(27, 1, 65, 12.55),
(27, 2, 32, 10.90),
(27, 3, 22, 70.50),
(27, 4, 11, 183.00),
(27, 5, 5, 179.00),

(28, 1, 48, 25.77),
(28, 2, 24, 10.40),
(28, 3, 16, 72.00),
(28, 4, 8, 180.00),
(28, 5, 3, 181.00),

(29, 1, 38, 29.41),
(29, 2, 19, 10.10),
(29, 3, 13, 69.50),
(29, 4, 7, 182.00),
(29, 5, 2, 180.00),

(30, 1, 42, 14.62),
(30, 2, 21, 10.70),
(30, 3, 14, 71.00),
(30, 4, 7, 181.00),
(30, 5, 3, 182.00),

(31, 1, 53, 27.34),
(31, 2, 27, 10.20),
(31, 3, 17, 70.00),
(31, 4, 9, 180.00),
(31, 5, 4, 183.00),

(32, 1, 47, 16.89),
(32, 2, 23, 10.90),
(32, 3, 15, 72.00),
(32, 4, 8, 182.00),
(32, 5, 3, 179.00),

(33, 1, 58, 20.12),
(33, 2, 29, 10.00),
(33, 3, 19, 73.00),
(33, 4, 10, 181.00),
(33, 5, 5, 180.00),

(34, 1, 36, 11.23),
(34, 2, 18, 10.60),
(34, 3, 12, 70.00),
(34, 4, 6, 180.00),
(34, 5, 2, 180.00),

(35, 1, 62, 24.56),
(35, 2, 31, 10.30),
(35, 3, 21, 71.20),
(35, 4, 11, 182.00),
(35, 5, 6, 181.00),

(36, 1, 41, 13.11),
(36, 2, 20, 10.70),
(36, 3, 13, 70.80),
(36, 4, 7, 180.00),
(36, 5, 3, 180.00),

(37, 1, 57, 26.80),
(37, 2, 28, 10.40),
(37, 3, 18, 71.10),
(37, 4, 9, 181.00),
(37, 5, 4, 182.00),

(38, 1, 44, 18.34),
(38, 2, 22, 10.00),
(38, 3, 14, 70.00),
(38, 4, 7, 180.00),
(38, 5, 3, 180.00),

(39, 1, 39, 22.67),
(39, 2, 19, 10.90),
(39, 3, 12, 72.00),
(39, 4, 6, 182.00),
(39, 5, 2, 179.00),

(40, 1, 51, 15.99),
(40, 2, 25, 10.80),
(40, 3, 16, 71.00),
(40, 4, 8, 181.00),
(40, 5, 4, 180.00),

(41, 1, 46, 19.88),
(41, 2, 23, 10.20),
(41, 3, 15, 70.00),
(41, 4, 7, 180.00),
(41, 5, 3, 183.00),

(42, 1, 59, 12.44),
(42, 2, 29, 10.60),
(42, 3, 19, 70.00),
(42, 4, 10, 180.00),
(42, 5, 5, 180.00),

(43, 1, 37, 27.76),
(43, 2, 18, 10.40),
(43, 3, 12, 71.00),
(43, 4, 6, 181.00),
(43, 5, 2, 182.00),

(44, 1, 63, 29.02),
(44, 2, 31, 11.00),
(44, 3, 21, 72.00),
(44, 4, 11, 184.00),
(44, 5, 6, 180.00),

(45, 1, 43, 14.15),
(45, 2, 21, 10.30),
(45, 3, 14, 71.50),
(45, 4, 7, 181.00),
(45, 5, 3, 182.00),

(46, 1, 56, 21.55),
(46, 2, 28, 10.90),
(46, 3, 18, 70.50),
(46, 4, 9, 183.00),
(46, 5, 4, 179.00),

(47, 1, 45, 17.01),
(47, 2, 22, 10.60),
(47, 3, 14, 70.00),
(47, 4, 7, 180.00),
(47, 5, 3, 180.00),

(48, 1, 40, 23.92),
(48, 2, 20, 10.10),
(48, 3, 13, 69.50),
(48, 4, 7, 182.00),
(48, 5, 3, 180.00),

(49, 1, 52, 28.77),
(49, 2, 26, 10.70),
(49, 3, 17, 71.00),
(49, 4, 9, 181.00),
(49, 5, 4, 182.00),

(50, 1, 49, 11.89),
(50, 2, 24, 10.20),
(50, 3, 16, 70.00),
(50, 4, 8, 180.00),
(50, 5, 4, 183.00),

(51, 1, 54, 20.66),
(51, 2, 27, 10.90),
(51, 3, 18, 72.00),
(51, 4, 9, 182.00),
(51, 5, 4, 179.00)
;

-- ----------------------------------------------------------------------------------------------------------------------------------------------
-- Insert notification
INSERT INTO inventhor.notification (notificationtypenr, title, message, date, isread, employeenr)
VALUES
-- Notificationtype Warning (2) about low stock for Apples 
(2, 'Low Stock Warning', 'The stock for Organic Apples is below the minimum level.', '2025-05-01 09:15:00', FALSE, 1),
(2, 'Low Stock Warning', 'The stock for Chocolate Bar is below the minimum level.', '2025-05-05 14:30:00', FALSE, 1),
(2, 'Low Stock Warning', 'The stock for Teddy Bear is below the minimum level.', '2025-05-10 11:45:00', FALSE, 1),
(2, 'Low Stock Warning', 'The stock for Lingerie Set is below the minimum level.', '2025-05-18 16:20:00', FALSE, 1),
(2, 'Low Stock Warning', 'The stock for Dog Food is below the minimum level.', '2025-05-27 08:05:00', FALSE, 1);
