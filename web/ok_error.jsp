<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Без паники</title>
    </head>
    <body>
        <h1>Всё в порядке просто ${pageContext.exception.message}</h1>
        <a href="${pageContext.servletContext.contextPath}">Вернуться</a>
    </body>
</html>
