<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<% pageContext.setAttribute( "newLine", "\n" ); %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <link href="/mysite/assets/css/board.css" rel="stylesheet" type="text/css">
    <title>Mysite</title>
</head>
<body>
    <div id="container">
        
        <c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
        <c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
        
        <div id="content">
            <div id="board" class="board-form">
                <table class="tbl-ex">
                    <tr>
                        <th colspan="2">글보기</th>
                    </tr>
                    <tr>
                        <td class="label">제목</td>
                        <td>${boardVo.title }</td>
                    </tr>
                    <tr>
                        <td class="label">내용</td>
                        <td>
                            <div class="view-content">
                                ${fn:replace(boardVo.content, newLine, "<br>")}
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">첨부파일</td>
                        <td>
                            <c:if test="${not empty boardVo.fileName}">
                                <c:forTokens var="fileName" items="${boardVo.fileName}" delims="," varStatus="st">
                                    <a download href="/mysite/upload/${fileName}">${fileName}</a>
                                    <c:if test="${!st.last}">
                                        <br>
                                    </c:if>
                                </c:forTokens>
                            </c:if>
                            <c:if test="${empty boardVo.fileName}">
                                <span>첨부파일이 없습니다.</span>
                            </c:if>
                        </td>
                    </tr>
                </table>
                <div class="bottom">
                    <a href="/mysite/board">글목록</a>
                    <c:if test="${authUser.no == boardVo.userNo}">
                        <a href="/mysite/board?a=modifyform&no=${boardVo.no}">글수정</a>
                    </c:if>
                </div>
            </div>
        </div>

        <c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
        
    </div><!-- /container -->
</body>
</html>
