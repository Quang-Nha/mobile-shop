<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/cart.css">

<c:import url="header.jsp">
	<c:param name="title" value="Info Cart"/>
</c:import>

<%-- session lưu trạng thái trang xem gần nhất, khi vào trang này nó sẽ lưu thông tin là trang này luôn để nhớ
 	, sang trang khác thì trang khác sẽ tự đối session này thành thông tin của trang đó
 	 thông tin phân tách nhau bởi dấu ",", phần tử đầu tiên là tên trang, các thông tin tiếp theo là
 	 các giá trị cần thiết để hiện thị lại chính xác trang này đang hiển thị thông tin gì --%>
<c:set var="saveState" scope="session" value="cart"/>

<%-- form sẽ hiển thị khi muốn edit lại giỏ hàng và gửi lại trang CartEditControl.java --%>
<form action="CartEditControl" method="post">
	
	<%-- bảng chứa thông tin đơn hàng --%>
	<table>
	  <caption>Cart</caption>
	  
	  <%-- hàng đầu tiên là tiêu đề --%>
	  <tr>
	    <th>Products in cart: ${sessionScope.cart.getCart().size()}</th>
	    <th>Price</th>
	    <th style="width: 20%">Quantity</th>
	    <th>Amount</th>
	    <th style="width: 10%"></th>
	  </tr>
	  
	  <%-- các hàng tiếp theo in ra thông tin từng sản phẩm trong list của giỏi hàng
	  		lấy list của giỏ hàng từ session cart gọi hàm getCart() sẽ trả về list 
	  		mỗi vòng for tạo một hàng, mỗi hàng là 1 snar phẩm --%>
	  <c:forEach varStatus="theCount" var="product" items="${sessionScope.cart.getCart()}">
	  
		  <tr>
		    <td>${product.name} <br> ID: ${product.id}</td>
		    
		    <td>($)${product.price}</td>
		    
		    <td>

		    	<%-- nếu có tham số action = edit thì hiện input chỉnh sửa số lượng sản phẩm
		    		với tên của nó gắn thêm vào đuôi chỉ số của lần lặp
		    		và 1 input ẩn có chứa id sản phẩm với tên của nó cũng gắn thêm vào đuôi chỉ số của lần lặp
		    		để gửi cho servlet CartEditControl.java khi lấy tham số sẽ dựa theo phần đuôi có
		    		số giống nhau để ghép cặp gửi giá trị id, số lượng của chúng cho hàm chỉnh sửa số lượng
		    		rồi quay lại trang này
		    		còn ko có tham số action = edit thì chỉ hiện số lượng sản phẩm --%>
		    	<c:if test="${param.action eq 'edit'}">
	    			<input class="quantity"  type="text" name="quantity${theCount.index}" value="${product.number}">
	    			<input type="hidden" name="id${theCount.index}" value="${product.id}">
	    		</c:if>
		    	
		    	<c:if test="${param.action != 'edit'}">
	    			 ${product.number}
	    		</c:if>
	    		
		    </td>
		    
		    <%-- fomat về dạng 2 chữ số phần thập phân --%>
		    <td>($)<fmt:formatNumber value="${product.price * product.number}" maxFractionDigits="2"/></td>
		    
		    <td>
		    
		    	<%-- cột cuối cùng nếu có tham số action = edit thì hiện nút Delete có link chứa tham số
		    		action = deletefromcart và id = id của sản phẩm đang lặp rồi gửi cho trang AddToCartController.java
		    		để thực hiện xóa sản phẩm và quay lại trang này --%>
		     	<c:if test="${param.action eq 'edit'}">
	    			<a class="delete" href="<c:url value="/AddToCartController?action=deletefromcart&id=${product.id}"/>">Delete</a>
	    		</c:if>
		    </td>
		    
		  </tr>
	  
	  </c:forEach>
	  
	  <%-- hàng cuối cùng chứa nút xác nhận edit xóa hoặc sửa số lượng sản phẩm
	  		và giá trị tổng tiền của đơn hàng fomat về 2 số phần thập phân --%>
	  <tr>
	    <td colspan="2" class="edit1"></td>
	    <td class="edit">
		    <c:if test="${param.action eq 'edit'}">
		    	<input class="edit_product" type="submit" value="ok">
		    </c:if>

			<%-- gửi luôn về chính trang này thêm action=edit --%>
		    <c:if test="${param.action  != 'edit'}">
		    	<a  class="edit_product" href="<c:url value="/cart.jsp?action=edit"/>">Edit product</a>
		    </c:if>
	    </td>
	    <td class="edit">Total: $<fmt:formatNumber value="${cart.getAmount()}" maxFractionDigits="2"/></td>
	    <td class="edit2"></td>
	  
	</table>

</form>

<%-- hiển thị thông tin khách hàng lấy từ session đăng nhập userSession --%>
<div><span class="cleft">Customer name</span>  <input class="cright" type="text" disabled="disabled" value="${sessionScope.userSession.username}"></div> 
<div><span class="cleft">Customer Address</span> <input class="cright" type="text" disabled="disabled" value="${sessionScope.userSession.address}"></div> 

<%-- form để nhập thông tin mã giảm giá discount và nút gửi xác nhận thanh toán
	nó sẽ gửi tham số mã giảm giá sang servlet PayController và dùng các session giỏ hàng, tài khoản
	người dùng đang có sẵn để tính toán thêm dữ liệu vào database rồi xóa session đơn hàng
	sau đó quay lại trang này với giỏ hàng rỗng và 1 bản update lịch sử đơn hàng được thêm vào database --%>
<form action="PayController" method="post">

<div><span class="cleft">	Discount code</span>  <input class="cright" name="discount" type="text"></div> 

<div style="width:80%"><input class="submit" type="submit" value="Submit"></div>

<%-- nếu nhấn nút gửi và servlet PayController xác nhận đơn hàng thành công
	hoặc không bằng tham số action lai trang này, dựa vào nó để hiển thị thông báo --%>
<c:if test="${param.action eq 'notcomplete'}">
  <h3 style="color:#ff0000;text-align:center;">Need to login or create cart</h3>
</c:if>

<c:if test="${param.action eq 'complete'}">
  <h3 style="color:#ff0000;text-align:center;">Order Success</h3>
</c:if>

</form>

<%-- tạo kết nối database để query thông tin lịch sử các đơn hàng theo email khách hàng --%>
<sql:transaction dataSource="jdbc/shoppingdb">

	<%-- lấy thông tin các đơn hàng của khách hàng có mail chỉ định xắ xếp giảm dần theo order_id
		join 2 bảng orders và orders_detail theo order_id, nhóm theo order_id và lấy sum các giá
		theo từng order_id --%>
	<sql:query sql="select o.order_id, order_status, order_date, order_discount_code, order_address, sum(price_product) as price
					from orders as o
					inner join orders_detail as od
				    on o.order_id = od.order_id
					where user_mail = ?
					group by user_mail, o.order_id, order_status, order_date, order_discount_code, order_address 
					order by o.order_id desc"
					var="order">
		<sql:param>${sessionScope.userSession.email}</sql:param>
	</sql:query>

	<%-- bảng hiển thị theo từng đơn hàng với các cột lấy từ các cột đã tạo trong truy vấn sql
		mỗi hàng là 1 đơn hàng --%>
	<table>
		<caption>Order history</caption>
		<tr>
		   <th>Order ID: </th>
		   <th>Order status</th>
		   <th>Order date</th>
		   <th>Discount code</th>
		   <th>Order address</th>
		   <th>Total price</th>
		 </tr>
		 <c:forEach var="order" items="${order.rows}">
		 	<tr>
		 		<td>${order.order_id}</td>
		 		<td>${order.order_status}</td>
		 		<td>${order.order_date}</td>
		 		<td>${order.order_discount_code}</td>
		 		<td>${order.order_address}</td>
		 		<td>${order.price}</td>
		 	</tr>
		 </c:forEach>
		  
	</table>

</sql:transaction>

<c:import url="footer.jsp"></c:import>