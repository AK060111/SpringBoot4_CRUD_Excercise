# Java Web — Category & User Admin

Project Maven nằm ngay trong thư mục hiện tại. Spring Boot **4.0.3**, compile `--release 17`, đóng gói **WAR**. Không dùng Thymeleaf, SPA, DAO thủ công hay `web.xml`.

## Cấu trúc

```text
pom.xml
src/main/java/vn/edu/ute/admin/
  AdminWebApplication.java
  config/       # SiteMesh và khởi tạo ADMIN từ môi trường
  security/     # Spring Security, BCrypt, UserDetailsService
  controller/   # Home, Category, User
  dto/          # Form không có ID, validation
  entity/       # Category, User, Role
  repository/   # Spring Data JPA
  service/      # Transaction, CRUD, search, pagination
src/main/resources/application.properties
src/main/webapp/WEB-INF/
  views/        # Login, admin, list/form, pagination
  decorators/main.jsp
src/test/       # HTTP integration test với H2
```

## Dependencies

- Spring MVC, Data JPA, Security, Validation thuộc Spring Boot 4.
- Tomcat 11 (Servlet 6.1), Jasper; Tomcat/Jasper có scope `provided` để hỗ trợ WAR chạy độc lập hoặc deploy.
- Jakarta JSTL API + implementation GlassFish.
- SiteMesh **3.3.0-RC1** (bản release candidate hỗ trợ Servlet 6.1; artifact 3.3.0 final chưa có trên Maven Central khi build), cấu hình filter Java, chỉ dispatcher REQUEST cho admin. Decorator dùng thẻ HTML `sitemesh:write`, không dùng JSP taglib SiteMesh cũ. Các trang nguồn có head/body để SiteMesh trích xuất; response sau decorate chỉ có một bộ html/head/body.
- Microsoft SQL Server JDBC; H2 chỉ trong test.
- Bootstrap 5.3.3 từ CDN (cần Internet để tải CSS).

`config/JspConfig.java` đặt `alwaysInclude=true` và content type UTF-8: JSP được include để SiteMesh có thể xử lý response trước khi Tomcat 11 commit. `SiteMeshConfig` khai báo prefix `/WEB-INF/decorators/` rồi map tên `main.jsp`, tránh lặp prefix. Security kiểm tra request bên ngoài; không chạy lại filter trong include nội bộ.

## Chuẩn bị SQL Server và cấu hình

1. Cài/chạy SQL Server sẵn có, bật TCP/IP và chuẩn bị database trống, ví dụ `JavaWebAdmin`.
2. Tạo login/user có quyền trên database đó. Không đặt thông tin đăng nhập thật vào source.
3. Đặt biến môi trường của tiến trình chạy Java trong IDE hoặc terminal:

| Biến | Giá trị cần điền |
|---|---|
| `DB_URL` | `jdbc:sqlserver://localhost:1433;databaseName=JavaWebAdmin;encrypt=true;trustServerCertificate=true` (chỉ ví dụ máy học tập; với server thật dùng chứng chỉ tin cậy) |
| `DB_USERNAME` | Username SQL Server của bạn |
| `DB_PASSWORD` | Password SQL Server của bạn |
| `DB_DDL_AUTO` | `update` cho lần tạo bảng đầu tiên; sau đó đổi `validate` (mặc định) |
| `ADMIN_USERNAME` | Username ADMIN ban đầu bạn tự chọn |
| `ADMIN_PASSWORD` | Mật khẩu ADMIN bạn tự chọn, ít nhất 8 ký tự và tối đa 72 byte UTF-8 |
| `ADMIN_EMAIL` | Email ADMIN ban đầu |

Hibernate chỉ tạo hai bảng `categories` và `app_users` trong database đã có; không tự tạo database. Tên danh mục và họ tên dùng NVARCHAR để hỗ trợ tiếng Việt. Username/email có unique constraint. Không thêm quan hệ Category–User ngoài yêu cầu.

ADMIN chỉ được tạo nếu cả ba biến ADMIN có giá trị và username chưa tồn tại. Không tự ghi đè mật khẩu/vai trò tài khoản cũ. Sau khi tạo thành công có thể bỏ ba biến ADMIN. Mật khẩu dùng BCrypt trực tiếp, không có migration/OTP/email.

## Build và chạy

Cần Maven và JDK 17 trở lên; khuyến nghị dùng JDK 17 để đúng môi trường bài tập. POM luôn biên dịch target Java 17, kể cả Maven đang chạy bằng JDK mới hơn.

```powershell
mvn clean package
java -jar target/admin-web.war
```

Lệnh `java -jar` ở trên chạy **WAR**, không phải executable JAR. Cũng có thể deploy `target/admin-web.war` vào Tomcat 11, khi đó context mặc định là `/admin-web` và server phải nhận được các biến môi trường.

Mở `http://localhost:8080/login` nếu chạy WAR độc lập, hoặc `http://localhost:8080/admin-web/login` khi deploy với tên WAR này. Đăng nhập bằng ADMIN đã tự cấu hình.

## Chức năng và quy ước

| URL | GET | POST |
|---|---|---|
| `/login` | Form đăng nhập | Xác thực |
| `/admin` | Tổng quan | — |
| `/admin/categories` | Danh sách, `keyword`, `page` | — |
| `/admin/categories/add` | Form thêm | Thêm |
| `/admin/categories/edit/{id}` | Form sửa | Sửa |
| `/admin/categories/delete/{id}` | Không hỗ trợ | Xóa |
| `/admin/users` | Danh sách, `keyword`, `page` | — |
| `/admin/users/add` | Form thêm | Thêm |
| `/admin/users/edit/{id}` | Form sửa | Sửa |
| `/admin/users/delete/{id}` | Không hỗ trợ | Xóa |
| `/logout` | — | Đăng xuất |

- Category tìm theo tên; User tìm theo username/email/fullname. Search không phân biệt hoa thường, escape wildcard theo cơ chế derived query Spring Data.
- Mỗi trang 10 dòng, thứ tự ID giảm dần. `page` bắt đầu từ 0; số trang hiển thị bắt đầu từ 1. Giữ keyword khi chuyển trang, có Previous/Next và tối đa 5 số trang quanh trang hiện tại. Trang âm về 0; trang quá lớn về trang cuối; dữ liệu rỗng an toàn.
- `/admin` và `/admin/**` chỉ ADMIN. USER nhận 403. CSRF bật mặc định; mọi thao tác thay đổi dùng POST.
- DTO không chứa ID/password hash. ID sửa lấy từ path. Khi validation lỗi, giữ các trường đã nhập; không phản chiếu mật khẩu ra HTML. Ô mật khẩu trống khi sửa giữ hash cũ.
- Category: trim, bắt buộc, tối đa 100 ký tự. User: trim username/email/fullname, kiểm tra độ dài, email, enum role, trùng username/email và mật khẩu. Fullname hỗ trợ Unicode.
- ID không tồn tại trả 404. Lỗi ghi/xóa database hiển thị thông báo chung trên JSP, không lộ chi tiết SQL. Output dữ liệu người dùng được escape.
- Vai trò lưu trong phiên đăng nhập; đổi role có hiệu lực với lần đăng nhập tiếp theo.

## Kiểm thử

`mvn clean package` chạy `AdminIntegrationTest`: khởi động Tomcat thật trên cổng ngẫu nhiên, render JSP/JSTL/SiteMesh và gọi HTTP với cookie/CSRF. Test dùng H2 chế độ MSSQLServer, tài khoản test sinh mật khẩu ngẫu nhiên, không cần SQL Server để build.

H2 không thay thế kiểm thử kết nối SQL Server thật. Sau khi cấu hình DB, kiểm tra thêm login, thêm/sửa/xóa, dữ liệu tiếng Việt, tìm kiếm và chuyển trang trên SQL Server của bạn.

Kết quả ngày 11/09/2026: **`mvn clean package` BUILD SUCCESS — 3 tests, 0 failures, 0 errors, 0 skipped**. Đã tạo `target/admin-web.war`, manifest dùng `WarLauncher`, Spring Boot 4.0.3; bytecode ứng dụng major version 61 (Java 17). WAR chứa Jakarta JSTL API 3.0.2 + implementation 3.0.1 và đúng một SiteMesh 3.3.0-RC1; không đóng gói H2. Maven dùng JDK 26.0.2.1 có sẵn trên máy, biên dịch main/test bằng `--release 17`. Không cài thêm JDK. Chưa kiểm thử trên SQL Server thật vì chưa có cấu hình kết nối.

Không chạy `git init`, commit, push hay tạo GitHub repository. `application.properties` chỉ có placeholder nên có thể quản lý bằng Git; file local chứa secret cần nằm trong `.gitignore`.
