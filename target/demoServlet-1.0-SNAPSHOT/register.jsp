<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Register</title>
        <link rel="stylesheet" href="css/login.css">
    </head>

    <body style="padding:0 30% ;">

        <%-- nếu đã đăng nhập thì không hiển thị trang này mà chuyển luôn về trang home --%>
        <c:if test="${sessionScope.userSession != null}">
            <c:redirect url="/ProductController"/>
        </c:if>

        <div class="top">

            <h1 style="color:#33cc33;text-align:center;padding:3%;">Register an account</h1>

            <form action="AccountControl" method="post">

                <!-- input ẩn gửi tham số xác định đã gửi form -->
                <input type="hidden" name="action" value="register"/>

                <!-- gán giá trị của session registerRemember cho các input, nếu nó null thì cho giá trị là rỗng
                    bằng cách dụng toán tử tam phân/Toán tử ba ngôi-->
                <label for="email">Email</label>
                <input type="text" id="email" name="email" placeholder="Email address"
                       value="${sessionScope.registerRemember == null ? "" : sessionScope.registerRemember.email}" required>

                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Password"
                       value="${sessionScope.registerRemember == null ? "" : sessionScope.registerRemember.password}" required>

                <label for="repassword">Repeat password</label>
                <input type="password" id="repassword" name="repeatpassword" placeholder="Repeat password" required>

                <label for="username">User name</label>
                <input type="text" id="username" name="username" placeholder="User name"
                       value="${sessionScope.registerRemember == null ? "" : sessionScope.registerRemember.username}" required>

                <label for="address">Address</label>
                <input type="text" id="address" name="address" placeholder="Address"
                       value="${sessionScope.registerRemember == null ? "" : sessionScope.registerRemember.address}" required>

                <label for="phone">Phone number</label>
                <input type="text" id="phone" name="phone" placeholder="Phone number"
                       value="${sessionScope.registerRemember == null ? "" : sessionScope.registerRemember.phone}" required>

                <input type="submit" value="Register">

                <input type="checkbox" id="remember" name="registerRememberChecked" value="1" ${sessionScope.registerRememberChecked}>
                <label for="remember">Remember me</label>

                <a href="<c:url value="/ProductController"/>">
                    <div style="float:right;background-color:#f44336;color:white;padding:10px 20px;margin:0 0 0 1%;">
                        Cancel
                    </div>
                </a>

                <%-- chuyển sang trang đăng nhập --%>
                <a href="<c:url value="/AccountControl?action=login"/>">
                    <div style="float:right;background-color:#f44336;color:white;padding:10px 20px;margin:0;">Login
                    </div>
                </a>

                <!-- hiển thị thông báo lỗi đăng nhập nhờ request message -->
                <div style="color:red;text-align:center;clear:right;">${requestScope.message}</div>
            </form>

        </div>

    </body>
</html>