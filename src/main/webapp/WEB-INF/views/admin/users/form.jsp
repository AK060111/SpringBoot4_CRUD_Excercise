<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html><head><title>Người dùng · Biểu mẫu</title></head><body><h1 class="h3">${empty id ? 'Thêm' : 'Sửa'} người dùng</h1>
<c:url var="saveUrl" value="/admin/users/${empty id ? 'add' : 'edit/'}${empty id ? '' : id}"/>
<form:form method="post" action="${saveUrl}" modelAttribute="form">
<form:errors path="*" cssClass="alert alert-danger d-block" element="div"/>
<div class="mb-3"><label class="form-label" for="username">Username</label><form:input path="username" id="username" cssClass="form-control" /><form:errors path="username" cssClass="text-danger"/></div>
<div class="mb-3"><label class="form-label" for="email">Email</label><form:input path="email" id="email" cssClass="form-control" type="email"/><form:errors path="email" cssClass="text-danger"/></div>
<div class="mb-3"><label class="form-label" for="fullname">Họ tên</label><form:input path="fullname" id="fullname" cssClass="form-control" /><form:errors path="fullname" cssClass="text-danger"/></div>
<div class="mb-3"><label class="form-label" for="role">Vai trò</label><form:select path="role" id="role" cssClass="form-select"><form:option value="" label="Chọn vai trò"/><form:options items="${roles}"/></form:select><form:errors path="role" cssClass="text-danger"/></div>
<div class="mb-3"><label class="form-label" for="password">Mật khẩu</label><form:password path="password" id="password" cssClass="form-control" showPassword="false" autocomplete="new-password"/><div class="form-text">Từ 8 ký tự, tối đa 72 byte UTF-8. Khi sửa, để trống để giữ mật khẩu cũ. Nếu lưu lỗi, hãy nhập lại mật khẩu mới.</div><form:errors path="password" cssClass="text-danger"/></div>
<button class="btn btn-primary">Lưu</button> <a class="btn btn-secondary" href="<c:url value='/admin/users'/>">Hủy</a>
</form:form></body></html>
