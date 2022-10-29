<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/infoProduct.css">

<c:import url="header.jsp">
	<c:param name="title" value="Info Product"/>
</c:import>

<%-- session lưu trạng thái trang xem gần nhất, khi vào trang này nó sẽ lưu thông tin là trang này luôn để nhớ
 	, sang trang khác thì trang khác sẽ tự đối session này thành thông tin của trang đó --%>
<c:set var="saveState" scope="session" value="info,${param.info}"/>

<!-- tạo 1 sql:transaction -->
<sql:transaction dataSource="jdbc/shoppingdb">

	<!-- lấy thông tin sản phẩm theo tham số info là ID của nó, param info này từ trang home
	     chuyển cho ProductController và lại forward về trang này
		thông tin phân tách nhau bởi dấu ",", phần tử đầu tiên là tên trang, các thông tin tiếp theo là
		các giá trị cần thiết để hiện thị lại chính xác trang này đang hiển thị thông tin gì -->
	<sql:query sql="select * from products where product_id=?" var="results">
		<sql:param>${param.info}</sql:param>
	</sql:query>

	<!-- tạo biến nhận giá trị là hàng đầu tiên trong query chính là hàng thông tin sản phẩm -->
	<c:set var="product" value="${results.rows[0]}"/>

	<%-- nếu truy vấn không trả lại kết quả nào có thể người dùng tự gõ url đến trang này nên ko có tham số id
	 	quay lại trang điều khiển với 0 tham số để nó forward đến trang home --%>
	<c:if test="${empty product}">
		<c:redirect url="/ProductController"/>
	</c:if>

	<!-- gán thông tin sản phẩm để hiển thị các hình ảnh, miêu tả cần thiết -->
	<h2 class="name">${product.product_name}</h2>
	<hr/>
	<div class="infomid">
	
		<img src="${product.product_img_source}" alt="iPhone">
		
		<div class="info">
			<div class="price">${product.product_price}0.000 VND</div>
			<pre class="des">${product.product_des}</pre>
			
			<!-- form lấy thông về số lượng của sản phẩm người dùng muốn thêm vào giỏ hàng
				gửi tham số action = add và id = id sản phẩm hiện tại cho
				servlet AddToCartController để thực hiện -->
			<form action="AddToCartController?action=add&id=${param.info}" method="post">
			
				<!-- nhập số lượng muốn thêm vào giỏ hàng -->
				<label for="quantity"><b>Add quantity</b></label>
		    	<input type="text" id="quantity" name="quantity" placeholder="Add quantity" value="1">
		    	
		    	<!-- hiển thị số lượng hiện tại đang có trong giỏ hàng của sản phẩm này -->
		    	<h3>total in cart:
					<c:choose>
						<%-- nếu chưa có session giỏ hàng thì số lượng đã đặt hàng là 0 --%>
						<c:when test="${empty sessionScope.cart}">
							<c:out value="0"/>
						</c:when>
						<%-- nếu có session giỏ hàng thì lấy giỏ hàng ra
						 	lấy Product trong giỏ hàng bằng hàm getProduct truyền vào id sản phẩm
						 	là tham số của param.info
						 	lấy được Product rồi thì lấy số lượng đang có của nó trong giỏ hàng
						 	bằng hàm getNumber --%>
						<c:otherwise>
							${sessionScope.cart.getProduct(param.info).getNumber()}
						</c:otherwise>
					</c:choose>
				</h3>
		    
		    	<input style="background-color: #ffad33;
		    				  font-size: 15px;
							  color: white;
							  padding: 4% 14%;
							  border-radius: 5px;
							  cursor: pointer;
							  font-weight: bold;
							  margin: 0;
							  " type="submit" value="Add to cart">
		    
	    	</form>
	    	
	    	<!-- nút yêu cầu xóa sản phẩm khỏi giỏ hàng với action = delete và id = id sản phẩm
	    		 nếu số lượng đang trong giỏ hàng > 0 cho servlet AddToCartController -->
	    	<c:if test="${sessionScope.cart.getProduct(param.info).getNumber() > 0}">
	    		<div class="add_cart"> <a href="<c:url value="/AddToCartController?action=delete&id=${param.info}"/>">remove</a></div>
	    	</c:if>
			
		</div>
		
	</div>
</sql:transaction>

<c:import url="footer.jsp"></c:import>

