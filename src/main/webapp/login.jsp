<%@ page import="com.example.asm3.bean.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Login</title>
        <link rel="stylesheet" href="css/login.css">
    </head>

    <body>

        <%-- nếu đã đăng nhập thì không hiển thị trang này mà chuyển luôn về trang home --%>
        <c:if test="${sessionScope.userSession != null}">
            <c:redirect url="/ProductController"/>
        </c:if>

        <!-- nếu chưa có session loginRemember thì cho các biến email và password rỗng
             không thì cho nó giá trị lấy từ session gửi qua
             value các ô của form sẽ dùng 2 biến này -->
        <c:choose>
            <c:when test="${sessionScope.loginRemember == null}">
                <c:set var="email" value=""/>
                <c:set var="password" value=""/>
            </c:when>
            <c:otherwise>
                <c:set var="email" value="${sessionScope.loginRemember.email}"/>
                <c:set var="password" value="${sessionScope.loginRemember.password}"/>
            </c:otherwise>
        </c:choose>

        <div class="top">
            <img alt="Avata" src="media/avata1.jpg">

            <form action="<c:url value="/AccountControl"/>" method="post">

                <!-- input ẩn gửi tham số xác định đã gửi form -->
                <input type="hidden" name="action" value="login"/>

                <!-- gán giá trị của 2 biến lưu thông tin gửi form lần trước cho các input -->
                <label for="email">Email</label>
                <input type="text" id="email" name="email" placeholder="Enter email"
                       value="${email}" required>

                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter password"
                       value="${password}" required>

                <input type="submit" value="Login">

                <%-- lấy giá trị session lưu thông tin check lần trước thêm vào ô
                     để check lại nếu lần gửi form trước đã check và ngược lại --%>
                <input type="checkbox" id="remember" name="loginRememberChecked" value="1" ${sessionScope.loginRememberChecked}>
                <label for="remember">Remember me</label>
            </form>

            <!-- hiển thị thông báo lỗi đăng nhập nhờ request message -->
            <div style="color:red;text-align:center;">${requestScope.message}</div>
        </div>

        <div class="bot">

            <a href="<c:url value="/ProductController"/>"><button>Cancel</button></a>

            <%-- chuyển sang trang đăng kí --%>
            <a href="<c:url value="/AccountControl?action=register"/>"><button>Register</button></a>

            <p style="float:right; margin:30px 15px 0 0;">Forgot <a href="#">password?</a></p>
        </div>

    </body>
</html>