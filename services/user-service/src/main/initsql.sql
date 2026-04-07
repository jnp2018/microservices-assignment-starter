-- Create tbluser table
CREATE TABLE IF NOT EXISTS tbluser (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fullname VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create tblrole table
CREATE TABLE IF NOT EXISTS tblrole (
    name VARCHAR(255) PRIMARY KEY,
    description VARCHAR(500)
);

-- Create tblpermission table
CREATE TABLE IF NOT EXISTS tblpermission (
    name VARCHAR(255) PRIMARY KEY,
    description VARCHAR(500)
);

-- Create users_role join table
CREATE TABLE IF NOT EXISTS tbl_users_role (
    user_id BIGINT NOT NULL,
    role_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role_name),
    FOREIGN KEY (user_id) REFERENCES tbluser(id) ON DELETE CASCADE,
    FOREIGN KEY (role_name) REFERENCES tblrole(name) ON DELETE CASCADE
);

-- Create tblrole_permission join table
CREATE TABLE IF NOT EXISTS tbl_role_permission (
    role_name VARCHAR(255) NOT NULL,
    permission_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (role_name, permission_name),
    FOREIGN KEY (role_name) REFERENCES tblrole(name) ON DELETE CASCADE,
    FOREIGN KEY (permission_name) REFERENCES tblpermission(name) ON DELETE CASCADE
);

-- Insert sample users
INSERT INTO tbluser (username, password, fullname, phoneNumber, email) VALUES 
('khanh', '$2a$10$HPFnhuE4m8oKU0ri5FpSeOU5ARA21KI/8OHF60P.RHTaqp/Us0XoC', 'Khanh DQ1', '1234545320', 'khanh111@gmail.com'),
('admin', '$2a$10$1eJ3Xrc9pff5P0Dafg6ewn5JRMYUZKhNM6zMdUx1DtTfGlxdlX', 'Admin User', '0111111111', 'admin@example.com'),
('admin1', '$2a$10$bTl3SHzYK51Pesa WBWJleXmjC9vLu79CC0tXCtc2RFXAef6uoRS', 'Admin User', '01111111111111', 'admin1@example.com')
AS new_user
ON DUPLICATE KEY UPDATE email = new_user.email;

-- Insert roles
INSERT INTO tblrole (name, description) VALUES 
('ADMIN', 'Administrator'),
('USER', 'Regular user')
AS new_role
ON DUPLICATE KEY UPDATE description = new_role.description;

-- Insert permissions
INSERT INTO tblpermission (name, description) VALUES 
('CREATE', 'Create new records'),
('READ', 'Read records'),
('UPDATE', 'Update records'),
('DELETE', 'Delete records'),
('READ_ALL_USERS', 'Read all users')
AS new_perm
ON DUPLICATE KEY UPDATE description = new_perm.description;

-- Assign permissions to ADMIN role
INSERT INTO tbl_role_permission (role_name, permission_name) VALUES 
('ADMIN', 'CREATE'),
('ADMIN', 'READ'),
('ADMIN', 'UPDATE'),
('ADMIN', 'DELETE'),
('ADMIN', 'READ_ALL_USERS')
AS new_admin_perm
ON DUPLICATE KEY UPDATE role_name = new_admin_perm.role_name;

-- Assign limited permissions to USER role
INSERT INTO tbl_role_permission (role_name, permission_name) VALUES 
('USER', 'READ')
AS new_user_perm
ON DUPLICATE KEY UPDATE role_name = new_user_perm.role_name;

-- Assign roles to users
INSERT INTO tbl_users_role (user_id, role_name) VALUES 
(1, 'USER'),
(2, 'ADMIN'),
(3, 'ADMIN')
AS new_user_role
ON DUPLICATE KEY UPDATE role_name = new_user_role.role_name;

INSERT INTO `demo`.`tbl_role_permission` (`role_name`, `permission_name`) VALUES ('ADMIN', 'READ_ALL_USERS');
INSERT INTO `demo`.`tblpermission` (`name`, `description`) VALUES ('READ_ALL_USERS', 'Get all users');