<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error happened</title>
    </head>
    <body>
        <h1>Видимо что-то случилось</h1>
        <a href="${pageContext.servletContext.contextPath}">Вернуться</a>
        <p>${pageContext.exception.message}</p>
        <c:forEach items="${pageContext.exception.stackTrace}" var="traceElement">
            ${traceElement} <br/>
        </c:forEach>
    </body>
</html>
