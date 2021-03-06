<%--
  Created by IntelliJ IDEA.
  User: USER
  Date: 2022-03-07
  Time: 오후 5:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>CRUD게시판 : 회원가입</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">

</head>
<body style="margin-right: 25%; margin-left:25%; margin-top: 10%;">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <jsp:useBean id="alert" scope="request" class="java.lang.String" />

    <h2 style="font-weight: bold;">JOIN US</h2>
    <p style="color: red;">${requestScope.alert}</p>
    <form class="row g-3" action="join.do" method="post">
        <div class="col-12">
            <label for="inputId" class="form-label">아이디</label>
            <input type="text" name="id" class="form-control" id="inputId">
        </div>
        <div class="col-md-6">
            <label for="inputPassword" class="form-label">비밀번호</label>
            <input type="password" name="password" class="form-control" id="inputPassword">
        </div>
        <div class="col-md-6">
            <label for="inputPasswordCheck" class="form-label">비밀번호 확인</label>
            <input type="password" name="passwordCheck" class="form-control" id="inputPasswordCheck">
        </div>
        <div class="col-12">
            <button type="submit" class="btn btn-primary">다음</button>
            <a href="start.do" class="btn btn-danger">취소</a>
        </div>
    </form>
</body>
</html>
