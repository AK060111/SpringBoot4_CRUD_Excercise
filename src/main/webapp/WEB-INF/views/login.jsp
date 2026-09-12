<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Đăng nhập</title><link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"></head>
<body class="bg-light"><main class="container py-5" style="max-width:480px"><div class="card card-body"><h1 class="h3">Đăng nhập</h1>
<c:if test="${param.error != null}"><p class="alert alert-danger">Tên đăng nhập hoặc mật khẩu không đúng.</p></c:if>
<c:if test="${param.logout != null}"><p class="alert alert-success">Đã đăng xuất.</p></c:if>
<form method="post" action="<c:url value='/login'/>"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
<label for="username" class="form-label">Username</label><input id="username" name="username" class="form-control mb-3" required autocomplete="username">
<label for="password" class="form-label">Mật khẩu</label><input id="password" type="password" name="password" class="form-control mb-3" required autocomplete="current-password">
<button class="btn btn-primary">Đăng nhập</button></form></div></main></body></html>
