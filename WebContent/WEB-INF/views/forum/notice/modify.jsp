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
	<link rel="stylesheet" href="<%=ctx%>/resources/template/css/trumbowyg.min.css">
    <!-- endinject -->

    <!-- Favicon -->
    <link rel="icon" type="image/png" sizes="16x16" href="<%=ctx%>/resources/template/images/favicon.png">    
    <script src="<%=ctx%>/resources/template/js/vendor/jquery/jquery-1.12.3.js"></script>
	<script type="text/javascript">
		var ctx = '<%= request.getContextPath() %>';
		var memberSeq = '<%= session.getAttribute("memberSeq") %>';
	</script>	
	<script src="<%=ctx%>/resources/js/page.js"></script>
	<link rel="stylesheet" href="<%=ctx%>/resources/template/css/trumbowyg.min.css">
    <script src="<%=ctx%>/resources/template/js/vendor/trumbowyg.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/trumbowyg/ko.js"></script>
    <script type="text/javascript">
    
	    console.log('${page}')
		let page = '${page}'
    
    	$('#trumbowyg-demo').trumbowyg({
	        lang: 'kr'
	    });
	    
	    
	    $(document).ready(function(){
	    	//게시물 정보 뿌려주기
	    	$('#trumbowyg-demo').trumbowyg('html', '${boardDto.content}');
	    })
	    
	    //개별 파일 삭제 ajax 요청
	    function deleteFile(attachSeq){
	    	$.ajax({    
	    		type : 'delete',             
	    		url : '<%=ctx%>/forum/notice/'+attachSeq+'/deleteFile.do',
	    		async : true,
	    		headers : {
	    			"Content-Type" : "application/json",
	    			"accept" : "application/json"
	    		},
	    		dataType : 'text',
	    		success : function(result) {
	    			// 결과 성공 콜백함수 
	    			if(result == "FILE_DEL_OK"){
	    				alert("첨부파일을 성공적으로 삭제했습니다.")
	  					location.href='<%=ctx%>/forum/notice/${boardDto.boardTypeSeq }/${boardDto.boardSeq }/modifyPage.do'
	    			} else if(result == "FILE_DEL_FAIL"){
	    				alert("첨부파일 삭제에 실패했습니다.");
	    			}
    			},
	    		error : function(result) {
	    			// 결과 에러 콜백함수
	    			let response = result;
	    			console.log(response);
	    		}
	    	});
	    	
	    }
	    
	</script>
	<style>
	.question-form .form-group .attachments label, 
	.question-form .form-group .attachments p.label {
	    border: 1px solid #ececec;
	    line-height: 55px;
	    padding: 0 20px;
	    text-align: center;
	    cursor: pointer;
	}
	</style>
</head>

<body class="preload home1 mutlti-vendor">
    <!--================================
            START DASHBOARD AREA
    =================================-->
    <section class="support_threads_area section--padding2">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="question-form cardify p-4">
                        <form action="<%=ctx %>/forum/notice/${boardDto.boardTypeSeq }/${boardDto.boardSeq }/modify.do" method="post" enctype="multipart/form-data">
                            <div class="form-group">
                                <label>제목</label>
                                <input type="text" placeholder="Enter title here" required id="title" name="title" value="${boardDto.title}" >
                            </div>
                            <div class="form-group">
                                <label>Description</label>
                                <div id="trumbowyg-demo"></div>
                            </div>
                            <div class="form-group">
                                <div class="attachments">
                                <b style="font-size:20px;">Attachments</b>
                                <c:set var="listSize" value='${attFileList.size() }'/>
                                <c:forEach items="${attFileList }" var="attFile">
                                	<div> 
										첨부 파일 : ${attFile.orgFileNm } (${attFile.fileSize })
										<button type="button" onclick="javascript:deleteFile(${attFile.attachSeq})">삭제</button>
                                	</div>
                                </c:forEach>
                                
                                <!--  첨부파일은 총 3개까지 추가 가능 -->
                                <c:forEach begin='${listSize +1}' end="3" step="1">
                                    <label>
                                        <span class="lnr lnr-paperclip"></span> Add File
                                        <span>or Drop Files Here</span>
                                        <input type="file" name="attFiles" style="display:inline-block;font-size:15px;" >
                                    </label>
                                </c:forEach>                              
                                </div>
                            </div>
                            <div class="form-group">
                                <button type="submit" id="postMod" class="btn btn--md btn-primary">Modify Request</button>
                            	<a href="<c:url value='/forum/notice/listPage.do'/>" class="btn btn--md btn-light">Cancel</a>
                            </div>
                        </form>
                    </div><!-- ends: .question-form -->
                </div>
                <!-- end .col-md-12 -->
            </div>
            <!-- end .row -->
        </div>
        <!-- end .container -->
    </section>
    <!--================================
            END DASHBOARD AREA
    =================================-->
   	<!--//////////////////// JS GOES HERE ////////////////-->
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA0C5etf1GVmL_ldVAichWwFFVcDfa1y_c"></script>
    <!-- inject:js -->

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
</body>

</html>
	