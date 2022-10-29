<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- nhập trang header và gán tham số title cho header -->
<c:import url="header.jsp">
    <c:param name="title" value="Shopping Home"/>
</c:import>

<!-- nếu chưa có param phân trang thì quay lại trang controller -->
<c:if test="${requestScope.listPagination == null}">
    <c:redirect url="/ProductController"/>
</c:if>

<%-- session lưu trạng thái trang xem gần nhất, khi vào trang này nó sẽ lưu thông tin là trang này luôn để nhớ
 	, sang trang khác thì trang khác sẽ tự đối session này thành thông tin của trang đó
 	 thông tin phân tách nhau bởi dấu ",", phần tử đầu tiên là tên trang, các thông tin tiếp theo là
 	 các giá trị cần thiết để hiện thị lại chính xác trang này đang hiển thị thông tin gì --%>
<c:set var="saveState" scope="session" value="home,${requestScope.currentSearch},${requestScope.currentPage}"/>

<!-- vùng danh sách sản phẩm -->
<div class="mid">

    <!-- dùng for lấy ra các sản phẩm trong param chứa list phân trang và hiển thị
            gán sản phẩm cho biến image dùng lấy ra các thuộc tính sp -->
    <c:forEach var="image" items="${requestScope.listPagination}">

        <!-- khung chứa sản phẩm -->
        <!-- click vào sản phẩm quay lại controller và gửi theo param id sản phẩm cùng action=info -->
        <a class="gallery" href="<c:url value="/ProductController?action=info&info=${image.id}"/>">

            <!-- hình ảnh sp -->
            <img class="fakeimg" src="${image.src}" alt="iPhone">

            <!-- các thông tin sp -->
            <p class="type">${fn:toUpperCase(image.type)}</p>
            <p class="name">${image.name}</p>
            <p class="price">$${image.price}</p>
        </a>
    </c:forEach>

    <!-- phân trang -->
    <div class="center">
        <div class="pagination">

            <!-- nếu trang 1 thì không hiển thị <<,
                    còn không gửi tham số trang = trang hiện tại - 1 và quay lại controller khi click vào << -->
            <a href="<c:url value="/ProductController?action=pageBack&page=${requestScope.currentPage - 1}&search=${requestScope.currentSearch}"/>"
                    <c:if test="${requestScope.currentPage == 1}">
                        style="display:none"
                    </c:if>
            >
                &laquo;
            </a>


            <!-- dùng for hiển thị số trang cần thiết dựa theo chỉ số trang lớn nhất tìm được
                    nếu trang trang nào đang là trang hiển thị sản phẩm thì gán class="active" để đánh dấu màu khác -->
            <c:forEach var="i" begin="1" end="${requestScope.maxPage}">
                <a href="<c:url value="/ProductController?action=pageSet&page=${i}&search=${requestScope.currentSearch}"/>"
                   <c:if test="${i == requestScope.currentPage}">class="active"</c:if> >${i}</a>
            </c:forEach>

            <!-- nếu trang hiện tại + 1 > chỉ số trang cuối thì không hiển thị >>, do chỉ số trang cuối là số thập phân
                    nên cần cộng trang hiện tại với 1 và so sánh >
                   còn không gửi tham số trang = trang hiện tại + 1 và quay lại controller khi click vào >> -->
            <a href="<c:url value="/ProductController?action=pageNext&page=${requestScope.currentPage + 1}&search=${requestScope.currentSearch}"/>"
               <%-- dùng > vì nếu ko có sản phẩm trang hiện tại sẽ là 1 còn maxpage là 0
                    nếu dùng requestScope.currentPage = requestScope.maxPage  thì sẽ ko đúng và vẫn hiển thị dấu >>
                    trường hợp có sản phẩm mà dùng requestScope.currentPage > requestScope.maxPage thì luôn
                    không đúng và luôn hiển thị dấu >> --%>
               <c:if test="${requestScope.currentPage + 1 > requestScope.maxPage}">style="display:none"</c:if>
            >
                &raquo;
            </a>

        </div>
    </div>
</div>

<c:import url="footer.jsp"></c:import>
