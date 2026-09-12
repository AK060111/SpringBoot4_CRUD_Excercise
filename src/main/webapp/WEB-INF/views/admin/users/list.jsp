<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><head><title>Người dùng</title></head><body>
<div class="d-flex justify-content-between align-items-center mb-3"><h1 class="h3">Người dùng</h1><a class="btn btn-primary" href="<c:url value='/admin/users/add'/>">Thêm mới</a></div>
<form method="get" action="<c:url value='/admin/users'/>" class="d-flex gap-2 mb-3"><input class="form-control" aria-label="Từ khóa tìm kiếm" name="keyword" value="<c:out value='${keyword}'/>" placeholder="Username, email hoặc họ tên"><button class="btn btn-outline-primary">Tìm</button></form>
<div class="table-responsive"><table class="table align-middle"><thead><tr><th>ID</th><th>Username</th><th>Email</th><th>Họ tên</th><th>Vai trò</th><th>Thao tác</th></tr></thead><tbody>
<c:forEach items="${page.content}" var="item"><tr><td>${item.id}</td><td><c:out value="${item.username}"/></td><td><c:out value="${item.email}"/></td><td><c:out value="${item.fullname}"/></td><td><c:out value="${item.role}"/></td>
<td><div class="d-flex gap-2"><a class="btn btn-sm btn-outline-primary" href="<c:url value='/admin/users/edit/${item.id}'/>">Sửa</a>
<form method="post" action="<c:url value='/admin/users/delete/${item.id}'/>" onsubmit="return confirm('Xóa bản ghi này?')"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"><button class="btn btn-sm btn-outline-danger">Xóa</button></form></div></td></tr></c:forEach>
<c:if test="${empty page.content}"><tr><td colspan="6">Không tìm thấy dữ liệu.</td></tr></c:if></tbody></table></div>
<%@ include file="../pagination.jsp" %></body></html>
