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
	<script src="https://kit.fontawesome.com/a26f9e7c74.js" crossorigin="anonymous"></script>
    
    <style>
    .reply_content {
    	width: 100%;
    }
    .name_vote {
    	width: 100%;
    }
    .pull-left{
    	width: 100%;
    }
    .inner {
    	display: flex;
    	justify-content: space-between; 
    	align-items: center;
    	width: 100%;
    }
    .delModBtnGrp button {
    	border: 0px;
   	    color: #fff;
	    background: #6fa9e9;
	    font-weight: 400;
	    font-size: 14px;
	    line-height: 24px;
	    display: inline-block;
	    padding: 0 10px;
 	    -webkit-border-radius: 100px; 
	    border-radius: 100px; 
    }
    div.contentBtn.hiddenComment {
    	display : none;
    }
    
    .comment-form-area.edit .comment-reply-form button.edit, 
    .comment-form-area.edit .comment-reply-form button.cancel {
    	color: #fff;
	    background: #6fa9e9;
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
                    <div class="forum_detail_area ">
                        <div class="cardify forum--issue">
                            <div class="title_vote clearfix">
                                <h3>${boardDto.title }</h3>

                                <div class="vote">
                                    <a href="#" id="cThumbUpAnchor" data-isLike='Y' data-thumb=true class="${isLike eq 'Y'? 'active':'' }" onclick="javascript:thumbClick(${boardDto.boardSeq }, ${boardDto.boardTypeSeq }, this);">
                                        <span class="lnr lnr-thumbs-up"></span>
                                    </a>
                                    <a href="#" id="cThumbDownAnchor" data-isLike='N' data-thumb=false class="${isLike eq 'N'? 'active':'' }" onclick="javascript:thumbClick(${boardDto.boardSeq }, ${boardDto.boardTypeSeq }, this);">
                                        <span class="lnr lnr-thumbs-down"></span>
                                    </a>
                                </div>
                                <!-- end .vote -->
                            </div>
                            <!-- end .title_vote -->
                            <div class="suppot_query_tag">
                                <img class="poster_avatar" src="<%=ctx%>/resources/template/images/support_avat1.png" alt="Support Avatar"> ${boardDto.regMemberId }
                                <span>${boardDto.regDtm }</span>
                            </div>
                            <p style="    margin-bottom: 0; margin-top: 19px;">
                            	${boardDto.content }</p>
                            <br/><br/><br/>
                            <c:if test="${attFileList.size() != 0}">
	                            <c:forEach items="${attFileList }" var="attFile">
	                            	<a href="<%=ctx%>/forum/download.do?attachSeq=${attFile.attachSeq}">다운로드 : ${attFile.orgFileNm } (${attFile.fileSize })</a>
	                            	<br>
	                            </c:forEach>
	                            <br>
	                            <br>
                            </c:if>
                            
                            <!-- 첨부된 파일이 2개 이상일때만 전체 압축해서 다운로드 받을 수 있음 -->
                            <c:if test="${attFileList.size() > 1}">
                            	<a href="<%=ctx%>/forum/${boardDto.boardTypeSeq }/${boardDto.boardSeq }/download.do">파일 전체 다운로드</a>
                            	<br>
                            </c:if>
                            
                            <!-- 수정하기, 삭제하기 버튼은 본인일때만 보여야 하는 버튼 -->
                            <c:if test='${sessionScope.memberSeq eq boardDto.regMemberSeq }'>
                            	<a href="<c:url value='/forum/notice/${boardDto.boardTypeSeq }/${boardDto.boardSeq }/modifyPage.do'/>" id="modBtn" >글 수정하기</a><br>
                            	<a href="#" id="delBtn" onclick="javascript:deletePage()">글 삭제하기</a><br>
                            </c:if>
                        </div>
                        <!-- end .forum_issue -->


                        <div class="forum--replays cardify">
                            <div class="area_title">
                                <h4>${comments.size()} Replies</h4>
                            </div>
                            <!-- end .area_title -->
                            <c:forEach var="comment" items="${comments }">
                            	<div class="forum_single_reply" data-commentSeq="${comment.commentSeq }">
                            	<!-- commentDto의 lvl을 활용해 패딩값을 조절해서 댓글 계층구조를 시각적으로 드러내기 -->
	                                <div class="reply_content" style="padding-left: ${18 + 30*comment.lvl}px">
	                                    <div class="name_vote">
	                                        <div class="pull-left">
	                                        	<div class="inner">
		                                        	<h4>${comment.memberNm }
		                                                <span>staff</span>
		                                            </h4>
		                                            <div class="vote">
			                                            <a href="#" class="active">
			                                                <span class="lnr lnr-thumbs-up"></span>
			                                            </a>
			                                            <a href="#" class="">
			                                                <span class="lnr lnr-thumbs-down"></span>
			                                            </a>
		                                        	</div>
	                                        	</div>
	                                            <p>${comment.regDtm }</p>
	                                        </div>
	                                        <!-- end .pull-left -->
	
	                                    </div>
	                                    <!-- end .vote -->  
	                                    <!-- 대댓글의 경우(lvl이 0이 아님) 대댓글 마크 표현 -->
	                                    <div class="contentBtn">
	                                    	<p> 
	                                    		<c:if test="${comment.lvl ne 0}"> <i class="far fa-regular fa-comments"></i> </c:if>
	                                    		<span class="commentContent">${comment.content }</span>
	                                    	</p>
	                                    	<div class="delModBtnGrp">
	                                    	 <!-- 댓글 수정 삭제 버튼은 작성자에게만 보인다. -->
	                                    	<c:if test="${sessionScope.memberSeq eq comment.memberSeq }">
	                                    		<button type="button" class="modBtn" onclick="javascript:openModifyWindow(this)">댓글 수정</button>
	                                    		<button type="button" class="delBtn" onclick="javascript:deleteComment(${boardDto.boardTypeSeq}, ${boardDto.boardSeq }, this)">댓글 삭제</button>
                                    		</c:if>
                                    		<button type="button" class="replyBtn" 
                                    			data-commentSeq="${comment.commentSeq }" data-commentLvl="${comment.lvl}"
                                    			onclick="javascript:addReply(this)">답글</button>
	                                    	</div>
	                                    </div>
	                                </div>
	                                <!-- end .reply_content -->
                            	</div>
                            <!-- end .forum_single_reply -->
                            </c:forEach>

							<!-- 댓글 및 대댓글 등록 창 -->
                            <div class="comment-form-area">
                                <h4>Leave a comment</h4>
                                <!-- comment reply -->
                                <div class="media comment-form support__comment">
                                    <div class="media-left">
                                        <a href="#">
                                            <img class="media-object" src="<%=ctx%>/resources/template/images/m7.png" alt="Commentator Avatar">
                                        </a>
                                    </div>
	                                   <div class="media-body">
	                                       <form class="comment-reply-form">
	                                           <div id="trumbowyg-demo"></div>
	                                           <button type="button" onclick="javascript:addComment(${boardDto.boardTypeSeq}, ${boardDto.boardSeq})" class="btn btn--sm btn--round">Post Comment</button>
	                                           <button type="button" onclick="location.href='<%=ctx %>/forum/notice/readPage.do?boardSeq=${boardDto.boardSeq }&boardTypeSeq=${boardDto.boardTypeSeq }'"  class="btn btn--sm btn--round">Cancel</button>
	                                       </form>
	                                   </div>
                                </div>
                                <!-- comment reply -->
                            </div>
                            
                            <!-- 댓글 수정 창 -->
                            <div class="comment-form-area edit" style="display:none">
                                <h4>Edit your comment</h4>
                                <!-- comment reply -->
                                <div class="media comment-form support__comment">
                                    <div class="media-left">
                                        <a href="#">
                                            <img class="media-object" src="<%=ctx%>/resources/template/images/m7.png" alt="Commentator Avatar">
                                        </a>
                                    </div>
	                                   <div class="media-body">
	                                       <div class="comment-reply-form">
	                                           <div id="comment-edit"></div>
	                                           <button type="button" onclick="javascript:modifyComment(${boardDto.boardTypeSeq}, ${boardDto.boardSeq }, this)" class="btn btn--sm btn--round edit">Edit Comment</button>
	                                           <button type="button" 
	                                           		onclick="location.href='<%=ctx %>/forum/notice/readPage.do?boardSeq=${boardDto.boardSeq }&boardTypeSeq=${boardDto.boardTypeSeq }'" 
	                                           		class="btn btn--sm btn--round cancel">Cancel</button>
	                                       </div>
	                                   </div>
                                </div>
                                <!-- comment reply -->
                            </div>
                            
                        </div>
                        <!-- end .forum_replays -->
                    </div>
                    <!-- end .forum_detail_area -->
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
    
    <script type="text/javascript">
		var ctx = '<%= request.getContextPath() %>';
	</script>	
	<script src="<%=ctx%>/resources/js/page.js"></script>

	
    <script src="<%=ctx%>/resources/template/js/vendor/trumbowyg.min.js"></script>
    <script src="<%=ctx%>/resources/template/js/vendor/trumbowyg/ko.js"></script>
    <script type="text/javascript">
     	let boardSeq = ${boardDto.boardSeq}
     	let memberSeq = <%= session.getAttribute("memberSeq")%>
     	
     	console.log("sessionMemberSeq=" + memberSeq)
     	console.log("dtoMemberSeq="+${boardDto.regMemberSeq})
     	
	    $('#trumbowyg-demo').trumbowyg({
	        lang: 'kr'
	    });
     	
     	$('#comment-edit').trumbowyg({
     		lang: 'kr'
     	})

	    
	    function thumbClick(boardSeq, boardTypeSeq, elem) {
	    	console.log('clicked');
	    	alert('clicked!');
	    	
	    	console.log(boardSeq);
	    	console.log(boardTypeSeq);
	    	
	    	let url = '<%=ctx%>/forum/notice/'
	    	url += boardTypeSeq + '/'
	    	url += boardSeq + '/'
	    	url += 'vote.do?isLike='+ elem.getAttribute("data-isLike")
	    	url += '&thumb=' + elem.getAttribute("data-thumb")
	    		
	    			
	    	$.ajax({    
	    		type : 'get',           
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
	    		dataType : 'text',
	    		success : function(result) {
	    			// 결과 성공 콜백함수 
	    			console.log("result = " + result);
	    			const response = JSON.parse(result);
	    			
	    			if(response.code == 0) { //이전 투표 결과가 없는 경우.
	    				$("a[data-thumb="+response.thumb+"]").addClass('active')
	    			} else if (response.code == 1) { //이전 투표결과를 취소
	    				$("a[data-thumb="+response.thumb+"]").removeClass('active')
	    			} else { //이전 투표결과를 반대로 
	    				$("a[data-thumb="+!response.thumb+"]").removeClass('active')
	    				$("a[data-thumb="+response.thumb+"]").addClass('active')
	    			}

	    		},
	    		error : function(request, status, error) {
	    			// 결과 에러 콜백함수
	    			alert('failed');
	    			console.log(error)
	    		}
	    	});
	    }
	    
	    
	    function deletePage(){
	    	if(!confirm('게시글을 정말 삭제하시겠습니까?')){
	    		return;
	    	}
	    	
	    	let url = '<%=ctx%>/forum/notice/'+${boardDto.boardTypeSeq}
	    	url += '/'+ ${boardDto.boardSeq} +'/deletePage.do'
	    	
	    	$.ajax({    
	    		type : 'delete',           
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
// 	    		dataType : 'text',
	    		success : function(result) {
	    			// 결과 성공 콜백함수 
	    			console.log(result);
	    			alert(result.msg)
	    			if(result.code == 1){
	    				location.href='<%=ctx%>/forum/notice/listPage.do'
	    			}
	    			
	    		},
	    		error : function(request, status, error) {
	    			// 결과 에러 콜백함수
	    			alert('failed');
	    			console.log(error)
	    		}
	    	});
	    }
	    
	    function addComment(boardTypeSeq, boardSeq){
	    	let commentDto = {
	    			boardTypeSeq: boardTypeSeq,
	    			boardSeq: boardSeq,
	    			content: $('#trumbowyg-demo').trumbowyg('html')
	    	};
	    	
	    	$.ajax({    
	    		type : 'post',           
	    		url : '<%=ctx%>/forum/notice/addComment.do',
	    		async : true,
	    		// 비동기화 여부 (default : true)
	    		headers : {
	    			// Http header
	    			"Content-Type" : "application/json",
	    			"accept" : "application/json"
	    		},
	    		dataType : 'json',
				data: JSON.stringify(commentDto),
	    		success : function(result) {
	    			// 결과 성공 콜백함수 
	    			console.log(result);
	    			if(result.code == 1) {
	    				//댓글이 성공적으로 등록되면 get요청
	    				alert(result.msg);
	    				location.href='<%=ctx%>/forum/notice/readPage.do?boardSeq='+boardSeq+'&boardTypeSeq='+boardTypeSeq
	    				return;
	    			} else {
	    				alert(result.msg);
	    			}
	    		
	    			
	    		},
	    		error : function(request, status, error) {
	    			// 결과 에러 콜백함수
	    			alert('댓글 등록에 실패했습니다.');
	    			console.log(error)
	    		}
	    	});
	    }
	    
	    function deleteComment(boardTypeSeq, boardSeq, elem){
	    	if(!confirm('댓글을 정말 삭제하시겠습니까?')){
	    		return;
	    	}
	    	
	    	let comment = elem.closest('div.forum_single_reply');
	    
	    	console.log("commentSeq = " + comment.getAttribute("data-commentSeq"))
	    	url = '<%=ctx%>/forum/notice/'
	    	url += comment.getAttribute('data-commentSeq')+'/deleteComment.do'
	    	
	    	$.ajax({    
	    		type : 'delete',           
	    		url : url,
	    		async : true,
	    		// 비동기화 여부 (default : true)
	    		headers : {
	    			// Http header
// 	    			"Content-Type" : "application/json",
	    			"accept" : "application/json"
	    		},
	    		dataType : 'json',
	    		success : function(result) {
	    			// 결과 성공 콜백함수 
	    			console.log(result);
	    			if(result.code == 1) {
	    				//댓글이 성공적으로 등록되면 get요청
	    				alert(result.msg);
	    				location.href='<%=ctx%>/forum/notice/readPage.do?boardSeq='+boardSeq+'&boardTypeSeq='+boardTypeSeq
	    				return;
	    			} else {
	    				alert(result.msg);
	    			}
	    		},
	    		
	    		//jQuery .ajax는 js fetchAPI와 달리
	    		//상태코드가 200이 아니면 error 콜백에서 처리한다.
	    		error : function(result) {
	    			// 결과 에러 콜백함수
	    			/* 확인용.
	    			console.log('댓글 등록 실패')
	    			console.log(result);
	    			console.log(result.responseJSON);
	    			*/
	    			alert(result.responseJSON.msg);
	    			console.log(error);
	    		}
	    	});
	    }
	    
	    // 댓글 수정창을 보여주는 메서드
	    function openModifyWindow(elem){
	    	//1. 현재 댓글 내용을 댓글 수정 창에 뿌리기.
	    	let contentBox = elem.closest('div.contentBtn');
	    	let commentContent = contentBox.querySelector('span.commentContent').innerHTML;
	    	$('#comment-edit').trumbowyg('html', commentContent);
	    	
	    	//2.댓글 수정 창을 현재 댓글 위치로 가져오고(&display를 block) 현재 댓글 내용 요소는 삭제.
	    	let commentArea = elem.closest('div.forum_single_reply');
	    	let editForm = document.querySelector('div.comment-form-area.edit');
	    	commentArea.append(editForm);
	    	editForm.style.display = "block";
	    	
	    	//2-2. 이전의 다른 댓글의 수정버튼을 누른 상태라면, 그 댓글의 수정창은 닫혀야 한다.
	    	let hiddenElem = document.querySelector('div.contentBtn.hiddenComment');
	    	console.log(hiddenElem);
	    	if(hiddenElem != null) {
	    		hiddenElem.classList.remove("hiddenComment");
	    	}
	    	contentBox.classList.add("hiddenComment");
	    	
	    	//3.댓글 수정 창의 수정 버튼에 현재 댓글의 commentSeq를 심어둔다.
	    	let editBtn = editForm.querySelector('button.edit');
	    	editBtn.setAttribute('data-commentSeq', commentArea.getAttribute('data-commentSeq'));
	    }
	    
	    // 댓글 수정을 비동기로 요청하는 메서드
	    function modifyComment(boardTypeSeq, boardSeq, elem) {
	    	let commentDto = {
	    			commentSeq: elem.getAttribute("data-commentSeq"),
	    			content: $('#comment-edit').trumbowyg('html')
	    	};
	      	
	      		$.ajax({    
		    		type : 'patch',           
		    		url : '<%=ctx%>/forum/notice/modifyComment.do',
		    		async : true,
		    		// 비동기화 여부 (default : true)
		    		headers : {
		    			// Http header
	 	    			"Content-Type" : "application/json",
		    			"accept" : "application/json"
		    		},
		    		data: JSON.stringify(commentDto),
		    		dataType : 'json',
		    		success : function(result) {
		    			// 결과 성공 콜백함수 
		    			console.log(result);
		    			if(result.code == 1) {
		    				//댓글이 성공적으로 등록되면 get요청
		    				alert(result.msg);
		    				location.href='<%=ctx%>/forum/notice/readPage.do?boardSeq='+boardSeq+'&boardTypeSeq='+boardTypeSeq
		    				return;
		    			} else {
		    				alert(result.msg);
		    			}
		    		},
		    
		    		error : function(result) {
		    			// 결과 에러 콜백함수
		    			/* 확인용.
		    			console.log('댓글 수정 실패')
		    			console.log(result);
		    			console.log(result.responseJSON);
		    			*/
		    			alert(result.responseJSON.msg);
		    			console.log(error);
		    		}
		    	});
	    	
	    	
	    }

	</script>
</body>

</html>
	