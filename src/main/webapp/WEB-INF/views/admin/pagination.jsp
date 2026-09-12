<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${page.totalPages > 1}"><nav aria-label="Phân trang"><ul class="pagination flex-wrap">
<c:if test="${not page.first}"><c:url var="prevUrl" value=""><c:param name="keyword" value="${keyword}"/><c:param name="page" value="${page.number-1}"/></c:url><li class="page-item"><a class="page-link" href="<c:out value='${prevUrl}'/>">Previous</a></li></c:if>
<c:forEach begin="${page.number > 2 ? page.number - 2 : 0}" end="${page.number + 2 < page.totalPages ? page.number + 2 : page.totalPages - 1}" var="i">
<c:url var="pageUrl" value=""><c:param name="keyword" value="${keyword}"/><c:param name="page" value="${i}"/></c:url>
<li class="page-item ${i == page.number ? 'active' : ''}"><a class="page-link" href="<c:out value='${pageUrl}'/>">${i+1}</a></li></c:forEach>
<c:if test="${not page.last}"><c:url var="nextUrl" value=""><c:param name="keyword" value="${keyword}"/><c:param name="page" value="${page.number+1}"/></c:url><li class="page-item"><a class="page-link" href="<c:out value='${nextUrl}'/>">Next</a></li></c:if>
</ul></nav></c:if><p class="text-secondary">Tổng: ${page.totalElements} bản ghi · Trang ${page.totalPages == 0 ? 0 : page.number+1}/${page.totalPages}</p>
