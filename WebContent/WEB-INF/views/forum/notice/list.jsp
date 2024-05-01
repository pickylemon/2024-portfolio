<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">

    <!-- viewport meta -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="MartPlace - Complete Online Multipurpose Marketplace HTML Template">
    <meta name="keywords" content="marketplace, easy digital download, digital product, digital, html5">

    <title>포트폴리오</title>

    <!-- inject:css -->
    <link rel="stylesheet" href="<%=ctx%>/resources/template/css/animate.css">
    <link rel="stylesheet" href="<%=ctx%>/resources/template/css/font-awesome.min.css">
    <link rel="stylesheet" href="<%=ctx%>/resources/template/css/fontello.css">
    <link rel="stylesheet" href="<%=ctx%>/resources/template/css/jquery-ui.css">
    <link rel="stylesheet" href="<%=ctx%>/resources/template/css/lnr-icon.css">
    <link rel="stylesheet" href="<%=ctx%>/resources/template/css/owl.carousel.css">
    <link rel="stylesheet" href="<%=ctx%>/resources/template/css/slick.css">
    <link rel="stylesheet" href="<%=ctx%>/resources/template/css/trumbowyg.min.css">
    <link rel="stylesheet" href="<%=ctx%>/resources/template/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="<%=ctx%>/resources/template/css/style.css">
    <!-- endinject -->

    <!-- Favicon -->
    <link rel="icon" type="image/png" sizes="16x16" href="<%=ctx%>/resources/template/images/favicon.png">    
	<script type="text/javascript">
		var ctx = '<%= request.getContextPath() %>';
	</script>	
	<script src="<%=ctx%>/resources/js/page.js"></script>
</head>

<body class="preload home1 mutlti-vendor">
<%-- ${list } <!-- 서버에서 온 값 출력해서 체크하는 용도 --> --%>
    <section class="section--padding2">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="">
                        <div class="modules__content">
                            <div class="withdraw_module withdraw_history">
                                <div class="withdraw_table_header">
                                    <h3>공지사항</h3>
                                </div>
                                <div class="table-responsive">
                                    <table class="table withdraw__table">
                                        <thead>
                                            <tr>
                                            	<th>No</th>
                                                <th>제목</th>
                                                <th>Date</th>
                                                <th>작성자</th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="item" items="${list}">
                                        	<tr>
                                        		<td>${item.boardSeq }</td>
                                        		<td>
                                        		<a href="<c:url value='/board/readPage.do?boardSeq=${item.boardSeq }'/>">
                                        		${item.title }
                                        		</a></td>
                                        		<td>${item.regDtm }</td>
                                        		<td>${item.regMemberId }</td>
                                        	</tr>
                                        </c:forEach>
                                        
<!--                                             <tr> -->
<!--                                                 <td>1</td> -->
<!--                                                 <td> -->
<%--                                                 	<a href="<c:url value='/forum/notice/readPage.do'/>"> --%>
<!--                                                 		Payoneer -->
<!--                                                 	</a> -->
<!--                                                 </td> -->
<!--                                                 <td>2024.03.23 21:57:13</td> -->
<!--                                                 <td>홍길동</td> -->
<!--                                             </tr> -->
<!--                                             <tr> -->
<!--                                                 <td>1</td> -->
<!--                                                 <td>Payoneer</td> -->
<!--                                                 <td>2024.03.23 21:57:13</td> -->
<!--                                                 <td>홍길동</td> -->
<!--                                             </tr> -->
<!--                                             <tr> -->
<!--                                                 <td>1</td> -->
<!--                                                 <td>Payoneer</td> -->
<!--                                                 <td>2024.03.23 21:57:13</td> -->
<!--                                                 <td>홍길동</td> -->
<!--                                             </tr> -->
<!--                                             <tr> -->
<!--                                                 <td>1</td> -->
<!--                                                 <td>Payoneer</td> -->
<!--                                                 <td>2024.03.23 21:57:13</td> -->
<!--                                                 <td>홍길동</td> -->
<!--                                             </tr> -->
<!--                                             <tr> -->
<!--                                                 <td>1</td> -->
<!--                                                 <td>Payoneer</td> -->
<!--                                                 <td>2024.03.23 21:57:13</td> -->
<!--                                                 <td>홍길동</td> -->
<!--                                             </tr> -->
                                        </tbody>
                                    </table>
                                    <div style="display: inline-block; margin: 0 5px; float: right; padding-right:10px;">
		                                <a href="<c:url value='/forum/notice/writePage.do'/>">
		                                	<button class="btn btn--round btn--bordered btn-sm btn-secondary">작성</button>
		                                </a>
		                            </div>
                                    <div class="pagination-area" style="padding-top: 45px;">
				                        <nav class="navigation pagination" role="navigation">
				                            <div class="nav-links">
<%-- 				                                <c:url value='/forum/notice/list.do?page=1&size=10'/> --%>
												<c:if test="${ph.startPage ne 1 }">
													<a class="prev page-numbers" href="<c:url value='/board/listPage.do?page=${ph.startPage-1 }&size=${ph.pageSize }'/>">
					                                    <span class="lnr lnr-arrow-left"></span>
					                                </a>
												</c:if>
												<c:forEach var="i" begin="${ph.startPage }" end="${ph.endPage }">
													<a class="page-numbers" href="<c:url value='/board/listPage.do?page=${i }&size=${ph.pageSize }'/>">${i }</a>
												</c:forEach>
<%-- 				                                <a class="page-numbers current" href="<c:url value='/forum/notice/listPage.do?page=1&size=10'/>">1</a> --%>
<%-- 				                                <a class="page-numbers" href="<c:url value='/forum/notice/listPage.do?page=2&size=10'/>">2</a> --%>
<%-- 				                                <a class="page-numbers" href="<c:url value='/forum/notice/listPage.do?page=3&size=10'/>">3</a> --%>
<%-- 				                                <a class="page-numbers" href="<c:url value='/forum/notice/listPage.do?page=4&size=10'/>">4</a> --%>
<%-- 				                                <a class="page-numbers" href="<c:url value='/forum/notice/listPage.do?page=5&size=10'/>">5</a> --%>
<%-- 				                                <a class="page-numbers" href="<c:url value='/forum/notice/listPage.do?page=6&size=10'/>">6</a> --%>
<%-- 				                                <a class="page-numbers" href="<c:url value='/forum/notice/listPage.do?page=7&size=10'/>">7</a> --%>
<%-- 				                                <a class="page-numbers" href="<c:url value='/forum/notice/listPage.do?page=8&size=10'/>">8</a> --%>
<%-- 				                                <a class="page-numbers" href="<c:url value='/forum/notice/listPage.do?page=9&size=10'/>">9</a> --%>
				                                <c:if test="${ph.endPage ne ph.totalPage }">
													<a class="next page-numbers" href="<c:url value='/board/listPage.do?page=${ph.endPage+1 }&size=${ph.pageSize }'/>">
					                                    <span class="lnr lnr-arrow-right"></span>
					                                </a>
												</c:if>
				                                
				                                
				                            </div>
				                        </nav>
				                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- end .col-md-6 -->
            </div>
            <!-- end .row -->
        </div>
        <!-- end .container -->
    </section>
    
   	<!--//////////////////// JS GOES HERE ////////////////-->
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA0C5etf1GVmL_ldVAichWwFFVcDfa1y_c"></script>
    <!-- inject:js -->
    <script src="<%=ctx%>/resources/template/js/vendor/jquery/jquery-1.12.3.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/jquery/popper.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/jquery/uikit.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/bootstrap.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/chart.bundle.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/grid.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/jquery-ui.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/jquery.barrating.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/jquery.countdown.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/jquery.counterup.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/jquery.easing1.3.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/owl.carousel.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/slick.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/tether.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/trumbowyg.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/waypoints.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/dashboard.js"></script>
    <script src="<%=ctx%>/resources/template/js/main.js"></script>
    <script src="<%=ctx%>/resources/template/js/map.js"></script>
    <!-- endinject -->
    
    <script>
    let msg = '${msg}'
    if(msg!='') {
    	alert(msg)
    }

    </script>
</body>

</html>
