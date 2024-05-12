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
// 	    	같은 뷰를 사용하지만
// 	    	게시글 작성일 때와 게시글 수정일 때 보이는 버튼이 다르다
	    	if(page == 'modify') {
	    		$('#postSave').css('display', 'none')
	    		$('#postMod').css('display', 'block')
	    	} else if(page == 'write') {
	    		$('#postSave').css('display', 'block')
	    		$('#postMod').css('display', 'none')
	    	}
	    	
	    	//게시글 수정페이지 요청으로 이 뷰에 왔을 경우, 
	    	//제목과 내용에 해당 데이터를 뿌려준다.
	    	if(${boardDto.content})
	    	$('#trumbowyg-demo').trumbowyg('html', '${boardDto.content}');
	    	$('#title').val('${boardDto.title}')
	    })

	    function boardSave() {
	    	let formData = new FormData()
	    	let dto = {};
	    	dto.title = $('#title').val()
	    	dto.content = $('#trumbowyg-demo').trumbowyg('html')
	    	let boardSaveDto = JSON.stringify(dto);
	    	formData.append('boardSaveDto', new Blob([dto], { type: 'application/json' }));
	    	formData.append("files", $('input[type=file	]')[0].files[0]);
	    	
	    	console.dir($('input[type=file	]')[0].files);
// 	    	boardSaveDto.title = $('#title').val()
// 	    	boardSaveDto.content = $('#trumbowyg-demo').trumbowyg('html')
	    	
	    	console.dir(boardSaveDto)
	    	
	    	for (var key of formData.keys()) {
			    console.log(key);
			  }
			  for (var value of formData.values()) {
			    console.log(value);
			  }
	    	
	    	$.ajax({    
	    		type : 'post',           
	    		// 타입 (get, post, put 등등)    
	    		url : '<%=ctx%>/forum/notice/writePage.do',
	    		// 요청할 서버url
	    		async : true,
	    		// 비동기화 여부 (default : true)
	    		headers : {
	    			// Http header
// 	    			"Content-Type" : "application/json",
	    			"accept" : "application/json"
	    		},
	    		cache: false,
	            contentType: false,
	            processData: false,
	    		data : formData,
	    		dataType : 'json',
	    		success : function(result) {
	    			// 결과 성공 콜백함수 
	    			console.log(result);
	    		
	    			if(result.code == 1){
	    				alert(result.msg);
	    				location.href='<%=ctx%>/forum/notice/listPage.do'
	    				//성공시, 게시글 정상 등록되었다는 alert와 함께, 게시글 목록으로 redirect
	    			} else {
	    				alert(result.msg);
	    			}

	    		},
	    		error : function(result) {
	    			// 결과 에러 콜백함수
	    			let response = result;
	    			console.log(response);
// 	    			const response = JSON.parse(result);
					let message = response.msg;
					console.log(typeof message);
	    			alert(message);
	    		}
	    	});
	    }
	    
	    function boardModify() {
	    	let boardModifyDto = {}
	    	boardModifyDto.boardSeq = ${boardDto.boardSeq}
	    	boardModifyDto.boardTypeSeq = ${boardDto.boardTypeSeq}
	    	boardModifyDto.title = $('#title').val()
	    	boardModifyDto.content = $('#trumbowyg-demo').trumbowyg('html')
	    	boardModifyDto.updateMemberSeq = memberSeq
	    	
	    	console.log(boardModifyDto)
	    	
	    	let url = '<%=ctx%>/forum/notice/'
	    	url += ${boardDto.boardTypeSeq}+'/'
	    	url += ${boardDto.boardSeq}+'/modifyPage.do'
	    	
	    	$.ajax({    
	    		type : 'patch',           
	    		// 타입 (get, post, put 등등)    
	    		url : url,
	    		// 요청할 서버url
	    		async : true,
	    		// 비동기화 여부 (default : true)
	    		headers : {
	    			// Http header
	    			"Content-Type" : "application/json",
	    			"accept" : "application/json"
	    		},
	    		data : JSON.stringify(boardModifyDto),
	    		dataType : 'json',
	    		success : function(result) {
	    			// 결과 성공 콜백함수 
	    			console.log(result);
	    		
	    			if(result.code == 1){
	    				alert(result.msg);
	    				location.href='<%=ctx%>/forum/notice/listPage.do'
	    				//성공시, 게시글 정상 등록되었다는 alert와 함께, 게시글 목록으로 redirect
	    			} else {
	    				alert(result.msg);
	    			}

	    		},
	    		error : function(result) {
	    			// 결과 에러 콜백함수
	    			let response = result;
	    			console.log(response);
// 	    			const response = JSON.parse(result);
					let message = response.msg;
					console.log(typeof message);
	    			alert(message);
	    		}
	    	});
	    }
	    
	</script>
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
                        <form action="<%=ctx %>/forum/notice/write.do" method="post" enctype="multipart/form-data">
                            <div class="form-group">
                                <label>제목</label>
                                <input type="text" placeholder="Enter title here" required id="title" >
                            </div>
                            <div class="form-group">
                                <label>Description</label>
                                <div id="trumbowyg-demo"></div>
                            </div>
                            <div class="form-group">
                                <div class="attachments">
                                    <label>Attachments</label>
                                    <label>
                                        <span class="lnr lnr-paperclip"></span> Add File
                                        <span>or Drop Files Here</span>
                                        <input type="file" name="attchedFiles" style="display:inline-block;" multiple>
                                    </label>
                                    <label>Attachments</label>
                                    <label>
                                        <span class="lnr lnr-paperclip"></span> Add File
                                        <span>or Drop Files Here</span>
                                        <input type="file" name="attchedFiles" style="display:inline-block;" multiple>
                                    </label>
                                    <label>Attachments</label>
                                    <label>
                                        <span class="lnr lnr-paperclip"></span> Add File
                                        <span>or Drop Files Here</span>
                                        <input type="file" name="attchedFiles" style="display:inline-block;" multiple>
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <button type="button" id="postSave" class="btn btn--md btn-primary" onclick="javascript:boardSave();">Submit Request</button>
                                <button type="button" id="postMod" class="btn btn--md btn-primary" onclick="javascript:boardModify();">Modify Request</button>
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
	