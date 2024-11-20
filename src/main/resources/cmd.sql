CREATE DATABASE IF NOT EXISTS medicalcenter;

USE medicalcenter;

CREATE TABLE IF NOT EXISTS user (
    UserId INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS employee (
    EmployeeId INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Email VARCHAR(50) NOT NULL,
    ContactNumber VARCHAR(15) NOT NULL,
    Address VARCHAR(100) NOT NULL,
    Role VARCHAR(20) NOT NULL,
    UserId INT NOT NULL,
    FOREIGN KEY (UserId) REFERENCES user(UserId)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS doctor (
    DoctorId INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Email VARCHAR(50) NOT NULL,
    ContactNumber VARCHAR(15) NOT NULL,
    Address VARCHAR(100) NOT NULL,
    UserId INT NOT NULL,
    FOREIGN KEY (UserId) REFERENCES user(UserId)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS timetable (
    TimetableId INT AUTO_INCREMENT PRIMARY KEY,
    Date DATE NOT NULL,
    StartTime TIME NOT NULL,
    EndTime TIME NOT NULL,
    DoctorId INT NOT NULL,
    FOREIGN KEY (DoctorId) REFERENCES doctor(DoctorId)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS prescription (
    PrescriptionId INT AUTO_INCREMENT PRIMARY KEY,
    PrescriptionDate DATE NOT NULL,
    MedicineDetails TEXT NOT NULL,
    Dosage TEXT NOT NULL,
    UserId INT NOT NULL,
    DoctorId INT NOT NULL,
    FOREIGN KEY (UserId) REFERENCES user(UserId)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    FOREIGN KEY (DoctorId) REFERENCES doctor(DoctorId)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS patient (
    PatientId INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Email VARCHAR(50) NOT NULL,
    ContactNumber VARCHAR(15) NOT NULL,
    Address VARCHAR(100) NOT NULL,
    DOB DATE NOT NULL,
    Gender VARCHAR(10) NOT NULL,
    RegistrationDate DATE NOT NULL,
    UserId INT NOT NULL,
    PrescriptionId INT NOT NULL,
    FOREIGN KEY (UserId) REFERENCES user(UserId)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    FOREIGN KEY (PrescriptionId) REFERENCES prescription(PrescriptionId)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS appointment (
    AppointmentId INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Age VARCHAR(10) NOT NULL,
    Date DATE NOT NULL,
    Description TEXT NOT NULL,
    Status VARCHAR(20) NOT NULL,
    DoctorId INT NOT NULL,
    UserId INT NOT NULL,
    FOREIGN KEY (DoctorId) REFERENCES doctor(DoctorId)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    FOREIGN KEY (UserId) REFERENCES user(UserId)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
    OrderId INT AUTO_INCREMENT PRIMARY KEY,
    OrderDate DATE NOT NULL,
    PatientId INT NOT NULL,
    FOREIGN KEY (PatientId) REFERENCES patient(PatientId)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS payment (
    PaymentId INT AUTO_INCREMENT PRIMARY KEY,
    Date DATE NOT NULL,
    Amount DECIMAL(10, 2) NOT NULL,
    PaymentMethod VARCHAR(20) NOT NULL,
    OrderId INT NOT NULL,
    FOREIGN KEY (OrderId) REFERENCES orders(OrderId)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS item (
    ItemId INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Description TEXT NOT NULL,
    ExpireDate DATE NOT NULL,
    PackSize VARCHAR(50) NOT NULL,
    UnitPrice DECIMAL(10, 2) NOT NULL,
    StockQuantity INT NOT NULL
);

CREATE TABLE IF NOT EXISTS orderdetail (
    OrderDetailId INT AUTO_INCREMENT PRIMARY KEY,
    Quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    OrderId INT NOT NULL,
    ItemId INT NOT NULL,
    FOREIGN KEY (OrderId) REFERENCES orders(OrderId)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    FOREIGN KEY (ItemId) REFERENCES item(ItemId)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE Role (
    RoleId INT AUTO_INCREMENT PRIMARY KEY,
    RoleName VARCHAR(50) NOT NULL UNIQUE,
    Description TEXT
);

CREATE TABLE Gender (
                      GenderId INT AUTO_INCREMENT PRIMARY KEY,
                      Gender VARCHAR(50) NOT NULL UNIQUE,
                      Description TEXT
);

INSERT INTO doctor (Name, Email, ContactNumber, Address, UserId) VALUES
                                                                     ('Kamal Gunarathna', 'kamalgunathana@gmail.com', '0713498265', 'Galle', 1);


INSERT INTO timetable (Date, StartTime, EndTime, DoctorId) VALUES
                                                               ('2024-10-30', '08:00:00', '12:00:00', 1);


INSERT INTO prescription (PrescriptionDate, MedicineDetails, Dosage, UserId, DoctorId) VALUES
    ('2024-10-25', 'Paracetamol 500mg', 'Twice a day', 1, 1);


INSERT INTO patient (Name, Email, ContactNumber, Address, DOB, Gender, RegistrationDate, UserId, PrescriptionId) VALUES
    ('Sugath Kumara', 'sugathkumara@gmail.com', '0782751856', 'Gampaha', '1995-05-20', 'Male', '2024-10-25', 1, 1);


INSERT INTO appointment (Name, Age, Date, Description, Status, DoctorId, UserId)  VALUES
    ('Jagath perera', '35', '2024-11-21', 'Follow-up consultation for diabetes', 'Scheduled', 1, 1),
    ('Nimali sansala', '28', '2024-11-22', 'General health check-up', 'Scheduled', 2, 1);


INSERT INTO orders (OrderDate, PatientId) VALUES
    ('2024-10-26',1);


INSERT INTO payment (Date, Amount, PaymentMethod, OrderId) VALUES
    ('2024-11-21', 150.00, 'Card', 1),
    ('2024-11-22', 200.00, 'Cash', 2),
    ('2024-11-24', 300.00, 'Online', 4);


INSERT INTO item (Name, Description, ExpireDate, PackSize, UnitPrice, StockQuantity) VALUES
    ('Bandages', 'Sterile bandages', '2025-12-31', '10 pcs/pack', 5.00, 100),
    ('Antibiotic Cream', 'For wound care', '2025-06-30', '15g tube', 7.50, 50);


INSERT INTO orderdetail (Quantity, Price, OrderId, ItemId) VALUES
    (2, 10.00, 1, 1),
    (1, 7.50, 1, 2);

INSERT INTO Role (RoleName, Description) VALUES
    ('Admin', 'Handles administrative and other supportive roles'),
    ('Doctor', 'Handles patient treatments and prescriptions'),
    ('Cashier', 'Handles patient payments and billing');

INSERT INTO Gender (Gender, Description) VALUES
    ('Male', 'Male'),
    ('Female', 'Female'),
    ('Other', 'Other');
