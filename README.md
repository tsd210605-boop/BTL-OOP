# Work-Management-System-TTCS-N39-15
Hệ thống quản lý và báo cáo bài tập (Work Management & Reporting System)

## Mô tả
Ứng dụng là hệ thống quản lý bài tập, nhóm, giảng viên, sinh viên và lịch học, kèm theo chức năng báo cáo và thống kê. Dự án gồm backend Spring Boot (có hỗ trợ chạy Java Desktop Swing) và tài nguyên SQL để khởi tạo cơ sở dữ liệu.

## Mục lục
- Mô tả
- Tính năng chính
- Công nghệ
- Yêu cầu
- Cài đặt nhanh
- Chạy ứng dụng
- Cấu trúc thư mục chính
- Tài nguyên hữu ích
- Góp phần & Liên hệ

## Tính năng chính
- Xác thực và phân quyền (Auth)
- Quản lý bài tập (`BaiTap`): tạo, cập nhật, phân công, deadline
- Quản lý sinh viên (`SinhVien`) và giảng viên (`GiangVien`)
- Quản lý nhóm (`Nhom`) và môn học (`MonHoc`)
- Lịch học (`LichHoc`) và báo cáo lớp/sinh viên
- Thống kê báo cáo (`ThongKe`)
- Giao diện desktop Swing (một phần UI trong `ui`)

## Công nghệ
- Java 17 (được khuyến nghị theo script)
- Spring Boot 3.2.5
- Spring Data JPA, Spring Security
- MySQL (connector: `mysql-connector-j`)
- Apache POI (xuất/nhập Excel)
- Lombok
- Maven

## Yêu cầu
- JDK 17 (hoặc JDK tương thích) — script `run-backend.bat` dùng JDK 17.
- Maven
- MySQL (tạo database và cấu hình kết nối)

## Cài đặt nhanh
1. Tạo database MySQL tên `ql_cv` (hoặc tùy ý, rồi cập nhật `spring.datasource.url`). Trong thư mục dự án có sẵn các script:

SQL_scripts/create.sql
SQL_scripts/insert.sql

2. Cập nhật thông tin kết nối cơ sở dữ liệu trong [backend/src/main/resources/application.properties](backend/src/main/resources/application.properties#L1). Mặc định file chứa:


spring.datasource.url=jdbc:mysql://localhost:3306/ql_cv
spring.datasource.username=root
spring.datasource.password=123456
server.port=8081

3. (Tuỳ chọn) Thiết lập `JAVA_HOME` tới JDK 17 nếu gặp lỗi tương thích Lombok.

## Chạy ứng dụng
Từ thư mục `backend` bạn có thể chạy bằng Maven hoặc bằng script Windows có sẵn.

- Chạy với Maven (chạy backend API):

```bash
mvn spring-boot:run


- Sử dụng script Windows (đã cấu hình `JAVA_HOME` trong file):

powershell
backend\run-backend.bat


- Chạy phiên bản desktop (Spring Boot + Swing):

powershell
backend\run-desktop.bat


Ghi chú: `run-desktop.bat` chạy Spring Boot với tham số `-Djava.awt.headless=false` để bật giao diện Swing.

## Build (đóng gói)
Để đóng gói ứng dụng thành jar:

bash
mvn clean package


Sau khi build, file jar sẽ nằm trong `backend/target`.

## Cấu trúc thư mục chính
- [backend/pom.xml](backend/pom.xml#L1) — cấu hình Maven và dependencies
- [backend/src/main/java/com/example/workreport/WorkreportApplication.java](backend/src/main/java/com/example/workreport/WorkreportApplication.java#L1) — entrypoint Spring Boot
- [backend/src/main/resources/application.properties](backend/src/main/resources/application.properties#L1) — cấu hình ứng dụng
- [backend/src/main/java/com/example/workreport/controller] — các controller REST chính: `AuthController`, `BaiTapController`, `GiangVienController`, `LichHocController`, `MonHocController`, `NhomController`, `SinhVienController`, `ThongKeController`, `AdminController`, `MetaController`
- [SQL_scripts] — script khởi tạo CSDL

## API & Endpoints (tổng quan)
Ứng dụng tổ chức controller theo chức năng (xem trong thư mục `controller`). Một số route chính:
- Xác thực: `AuthController`
- Quản lý bài tập: `BaiTapController`
- Quản lý sinh viên/giảng viên: `SinhVienController`, `GiangVienController`
- Lịch học: `LichHocController`
- Thống kê: `ThongKeController`

Mở các file controller trong [backend/src/main/java/com/example/workreport/controller](backend/src/main/java/com/example/workreport/controller) để xem chi tiết endpoint và payload.

## Cấu hình & Lưu ý vận hành
- Port mặc định: `8081` (cấu hình trong `application.properties`).
- Upload file giới hạn 50MB (cấu hình `spring.servlet.multipart.*`).
- Nếu gặp lỗi Lombok/annotation processor khi build, đảm bảo JDK và IDE đã bật annotation processing và Lombok được cài trong IDE.

## Góp phần
- Mô tả bug hoặc feature request bằng cách mở issue.
- Muốn chạy module desktop: chắc chắn máy đang có môi trường đồ họa (không headless) và `JAVA_HOME` trỏ tới JDK 17.

## License
Vui lòng thêm file `LICENSE` nếu muốn cấp phép cụ thể. Hiện không có file license kèm theo.

## Tác giả & Liên hệ
- Dự án: Bài thực tập cơ sở — Nhóm lớp 39, nhóm 15
- Nếu cần hỗ trợ tiếp, cho tôi biết phần bạn muốn tôi mở rộng (API docs, Postman collection, hoặc steps deploy).

