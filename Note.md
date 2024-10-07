============== SPRING SECURITY FILTERS  ======================
1. Trong toàn bộ Web/App Server nói chung sẽ sử dụng các Filter để đóng vai trò intercept các Request được gửi từ Client tới. Với mục tiêu tiền xử lý và chuyển thành các đối tượng Request mà Web server(Servlet container) có thể hiểu được.
2. Spring Security cũng sử dụng khái niệm này đối với inbuilt filters của nó. Các Filter này được thiết lập bên trong SecurityFilterChain như CSRF Filter, CORS Filter, Channel Processing Filter.... Các Filter này được cấu hình trong **_ProjectSecurityConfig_**
3. Các Filters phía trước sau khi thực hiện xong sẽ Forward HTTP object cho các Filters phía sau thực hiện logic riêng của nó.


=================== QUAN TRỌNG =========================
1. Ngay sau khi các Filters làm việc xong(Spring app và Spring Security) thì Request sẽ được chuyển sang cho DispatcherServlet, nó sẽ có nhiệm vụ Map các Endpoint với Các RestController/Controller tương ứng.