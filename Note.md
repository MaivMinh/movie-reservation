============== SPRING SECURITY FILTERS  ======================
1. Trong toàn bộ Web/App Server nói chung sẽ sử dụng các Filter để đóng vai trò intercept các Request được gửi từ Client tới. Với mục tiêu tiền xử lý và chuyển thành các đối tượng Request mà Web server(Servlet container) có thể hiểu được.
2. Spring Security cũng sử dụng khái niệm này đối với inbuilt filters của nó. Các Filter này được thiết lập bên trong SecurityFilterChain như CSRF Filter, CORS Filter, Channel Processing Filter.... Các Filter này được cấu hình trong **_ProjectSecurityConfig_**
3. Các Filters phía trước sau khi thực hiện xong sẽ Forward HTTP object cho các Filters phía sau thực hiện logic riêng của nó.


=================== QUAN TRỌNG =========================
1. Ngay sau khi các Filters làm việc xong(Spring app và Spring Security) thì Request sẽ được chuyển sang cho DispatcherServlet, nó sẽ có nhiệm vụ Map các Endpoint với Các RestController/Controller tương ứng.

===================== CSRF Attack Protection in Stateless Server. ======================

Reference Documentation: _https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa_ 

1. Nếu làm việc với các hệ thống Stateless thông qua JWT Tokens thì việc bảo vệ hệ thống khởi CSRF attacks là điều cần thiết. Bởi vì, JWT tokens chỉ chịu trách nhiệm cho việc triển khai Stateless và Authen/author. Ngoài những vấn đề trên thì JWT tokens sẽ khá giống với SessionID vì nó cũng được lưu ở Cookies. Và vì việc lưu ở Cookies nó cũng sẽ có tiềm ẩn khả năng khiến hệ thống bị tấn công CSRF.
2. Để triển khai việc bảo vệ khỏi CSRF, trước hết chúng ta phải tạo ra các CSRF Tokens đi kèm với JWT Tokens. Các bước xử lý của CSRF Tokens + JWT Tokens khác giống với CSRF Tokens + JSessionID Token. Chúng ta sẽ phải lưu CSRF Tokens ở Cookies. Khi Client thực hiện một POST/PUT/PATCH/DELETE... method thì phải đính kèm CSRF Tokens cùng với Request.
3. Sử dụng CookieCsrfTokenRepository để thực hiện việc **_write cookie named XSRF-TOKEN_** và **_read it from HTTP request header named X-XSRF-TOKEN_** **_or the request parameter _csrf by default_**
4. **_Using CsrfTokenRequestAttributeHandler_**: 
   1. CsrfTokenRequestAttributeHandler giúp cho CsrfToken có sẵn dưới dạng một thuộc tính của HttpServletRequest là "_csrf".
   2. CsrfToken cũng có sẵn dưới dạng thuộc tính của HttpServletRequest CsrfToken.class.getName() => CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());