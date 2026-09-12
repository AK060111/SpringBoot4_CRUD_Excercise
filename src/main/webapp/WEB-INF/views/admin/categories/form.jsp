<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html><head><title>Danh mục · Biểu mẫu</title></head><body><h1 class="h3">${empty id ? 'Thêm' : 'Sửa'} danh mục</h1>
<c:url var="saveUrl" value="/admin/categories/${empty id ? 'add' : 'edit/'}${empty id ? '' : id}"/>
<form:form method="post" action="${saveUrl}" modelAttribute="form">
<form:errors path="*" cssClass="alert alert-danger d-block" element="div"/>
<div class="mb-3"><label class="form-label" for="name">Tên danh mục</label><form:input path="name" id="name" cssClass="form-control" /><form:errors path="name" cssClass="text-danger"/></div>

<button class="btn btn-primary">Lưu</button> <a class="btn btn-secondary" href="<c:url value='/admin/categories'/>">Hủy</a>
</form:form></body></html>
