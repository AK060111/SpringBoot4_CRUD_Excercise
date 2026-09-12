# SpringBoot4 CRUD Exercise
Bài tập lập trình Web sử dụng Spring Boot 4 để xây dựng trang quản trị và thực hiện các chức năng CRUD.
## Chức năng
- Đăng nhập trang Admin
- Quản lý Category
  - Thêm
  - Sửa
  - Xóa
  - Tìm kiếm
  - Phân trang
- Quản lý User
  - Thêm
  - Sửa
  - Xóa
  - Tìm kiếm
  - Phân trang
- Phân quyền truy cập trang Admin
## Công nghệ sử dụng
- Java 17
- Spring Boot 4
- Spring MVC
- Spring Data JPA
- Spring Security
- JSP / JSTL
- SiteMesh 3
- Bootstrap
- SQL Server
- Maven
## Cơ sở dữ liệu
Database sử dụng:
JavaWebAdmin

Các bảng chính:
categories
app_users


## Chạy chương trình

Build project:

```bash
mvn clean package
```

Sau đó chạy:

```bash
java -jar target/admin-web.war
```

Truy cập:

```text
http://localhost:8080/login
```

## Thông tin

- Họ tên: Trần Anh Khoa
- MSSV: 24133032