<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title><sitemesh:write property="title"/></title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<sitemesh:write property="head"/></head>
<body class="bg-light">
<nav class="navbar navbar-dark bg-dark"><div class="container"><a class="navbar-brand" href="<c:url value='/admin'/>">UTE · Admin</a>
<form method="post" action="<c:url value='/logout'/>"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"><button class="btn btn-outline-light btn-sm">Đăng xuất</button></form></div></nav>
<div class="container py-4"><div class="row g-4"><aside class="col-md-3"><div class="list-group">
<a class="list-group-item list-group-item-action" href="<c:url value='/admin'/>">Tổng quan</a>
<a class="list-group-item list-group-item-action" href="<c:url value='/admin/categories'/>">Danh mục</a>
<a class="list-group-item list-group-item-action" href="<c:url value='/admin/users'/>">Người dùng</a>
</div></aside><main class="col-md-9"><div class="bg-white rounded border p-4">
<c:if test="${not empty success}"><div class="alert alert-success"><c:out value="${success}"/></div></c:if>
<c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>
<sitemesh:write property="body"/></div></main></div></div>
<footer class="container border-top py-3 text-secondary">Java Web · Spring Boot 4 · Category &amp; User</footer></body></html>

