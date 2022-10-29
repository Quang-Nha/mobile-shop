<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/home.css">

<!-- title dựa theo param ở trang chèn -->
<title>${param.title}</title>
</head>

<body>

<!-- header -->
<div class="header">

  <div class="left_header">
    <h2>PRJ321x</h2>
    <p>Welcome to my Wedsite</p>
  </div>
  
  <!-- form nhập sản phẩm cần tìm theo tên -->
  <form action="<c:url value="/ProductController?action=search"/>" method="post" class="right_header">
    <a href="#">Categories &#9660;</a>
    
    <!-- giá trị của value là chính nó được gửi lại từ trang controller -->
    <input type="text" name="search" value="${requestScope.currentSearch}" placeholder="What are you looking for?">
    
    <!-- cần nút gửi để nhấn enter sẽ gửi đi nhưng cho hiển thị là none -->
    <input type="submit" style="width:20%;background:#ffad33;display:none;" value="Search "/>
  </form>
  
</div>

<!-- thanh điều hướng -->
<ul class="topnav">

  <!-- nút home quay lại trang controller với không param -->
  <li><a href="<c:url value="/ProductController?action=home"/>">Home</a></li>
  
  <!-- trình đơn thả xuống -->
  <li class="dropdown">
    <a class="dropbtn">Products</a>
    <div class="dropdown-content">
      <a href="#">android</a>
      <a href="#">iphone</a>
      <a href="#">other</a>
    </div>
  </li>
  
  <li><a href="#">About us</a></li>

  <!-- xem xét trạng thái login nhờ userSession
  		nếu đã login thì hiển thị email vào logout
  		nếu chưa thì hiển thị login và register -->
  <c:choose>
	<c:when test="${sessionScope.userSession != null}">
	
		<li class="last"><a href="<c:url value="/LogoutControl"/>">Logout</a></li>
		
		<!-- hiển thị email của người dùng bằng session -->
 		<li class="last"><a href="#">${sessionScope.userSession.email}</a></li>
	</c:when>
	<c:otherwise>
		
		<li class="last"><a href="<c:url value="/AccountControl?action=register"/>">Register</a></li>
 		<li class="last"><a href="<c:url value="/AccountControl?action=login"/>">Login</a></li>
	</c:otherwise>
   </c:choose>
   
   <li><a href="#">About us</a></li>

</ul>

<!-- giỏ hàng -->
<div class="rightcolumn">
   <a style="display:block" href="<c:url value="/cart.jsp"/>">
   	<img src="<c:url value="/media/cart.PNG"/>" alt="cart">
   	<span class="desc">Cart</span>	
   </a>
</div> 