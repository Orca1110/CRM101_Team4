<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="/mysite/assets/css/board.css" rel="stylesheet" type="text/css">
<title>Mysite</title>

<script type="text/javascript">
	function list() {
		document.listFrm.action = "list.jsp";
		document.listFrm.submit();
	}
	
	function pageing(page) {
	    document.listFrm.nowPage.value = page;
	    document.listFrm.submit();
	}
	
	function block(value) {
	    document.listFrm.nowPage.value = 10 * (value - 1) + 1;
	    document.listFrm.submit();
	}
	
	function read(num){
		document.readFrm.num.value=num;
		document.readFrm.action="read.jsp";
		document.readFrm.submit();
	}
	
</script>
</head>
<body>
	<!-- 페이징 :  정다윤 -->
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		<div id="content">
			<div id="board">
				
				<form id="search_form" action="/mysite/board" method="get">
					<label for="keyField">검색 옵션:</label>
					<select id="keyField" name="keyField">
						<option value="name" >글쓴이</option>
						<option value="reg_date" >작성일</option>
						<option value="title" >제 목</option>
						<option value="content" >내 용</option>
			      	</select>				
					<input type="hidden" name="a" value="list">
    				<input type="hidden" name="keyField" value="${param.keyField}">
					<input type="text" id="keyWord" name="keyWord" value="${param.keyWord}">
					<input type="hidden" name="nowPage" value="1">
					<input type="submit" value="찾기" >
				</form>
				
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>				
					<c:forEach items="${list }" var="vo">
						<tr>
							<td>${vo.no }</td>
							<td><a href="/mysite/board?a=read&no=${vo.no }"> ${vo.title } </a></td>
							<td>${vo.userName }</td>
							<td>${vo.hit }</td>
							<td>${vo.regDate }</td>
							<td>
								<c:if test="${authUser.no == vo.userNo }">
									<a href="/mysite/board?a=delete&no=${vo.no }" class="del">삭제</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
				
			<!-- 페이징 및 블럭 처리 시작 -->
				<div class="pager">
					<!-- 전송된 값 확인
					totalRecord= ${totalRecord }
					nowPage= ${nowPage }
					totalPage= ${totalPage }
					nowBlock= ${nowBlock}
					totalBlock= ${totalBlock }
					pageStart= ${pageStart }
					pageEnd= ${pageEnd }
					start= ${start }
					-->
				<c:if test="${totalPage != 0}">
					<ul>
						<c:if test="${nowBlock > 1}">
							<li><a href="javascript:block('${nowBlock -1 }')">◀</a></li>
						</c:if>
									
						<c:forEach begin="${pageStart}" end="${pageEnd}" var="currentPage">
						    <c:set var="pageLink" value="/mysite/board?keyField=${keyField}&keyWord=${keyWord}&a=list&nowPage=${currentPage}" />						    <c:choose>
						        <c:when test="${currentPage eq nowPage}">
						            <li class="selected">[${currentPage}]</li>
						        </c:when>
						        <c:otherwise>
						            <li><a href="${pageLink}">[${currentPage}]</a></li>
						        </c:otherwise>
						    </c:choose>
						</c:forEach>
			
						<c:if test="${totalBlock > nowBlock}">
							<li><a href="javascript:block('${nowBlock +1 }')">▶</a></li>
						</c:if>
					</ul>
				</c:if>
			<!-- 페이징 및 블럭 처리 끝 -->
				</div>						
				<c:if test="${authUser != null }">
					<div class="bottom">
						<a href="/mysite/board?a=writeform" id="new-book">글쓰기</a>
					</div>
				</c:if>				
			</div>
		</div>
		<form name="listFrm" method="post" action= "/mysite/board?a=list">
			<input type="hidden" name="reload" value="true"> 
			<input type="hidden" name="nowPage" value="1">
		</form>
		<form name="readFrm" method="get">
			<input type="hidden" name="num"> 
			<input type="hidden" name="nowPage" value="${nowPage }"> 
			<input type="hidden" name="keyField" value="${keyField }"> 
			<input type="hidden" name="keyWord" value="${keyWord }">
		</form>
			<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
	</div><!-- /container -->
</body>
</html>	