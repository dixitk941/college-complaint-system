<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Something went wrong" />
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<section class="card">
    <p class="kicker">Notice</p>
    <h1>That page is not available</h1>
    <p>
        <c:choose>
            <c:when test="${not empty requestScope['javax.servlet.error.message']}">
                <c:out value="${requestScope['javax.servlet.error.message']}"/>
            </c:when>
            <c:otherwise>The request could not be completed.</c:otherwise>
        </c:choose>
    </p>
    <p class="muted">
        <c:if test="${not empty requestScope['javax.servlet.error.status_code']}">
            Status <c:out value="${requestScope['javax.servlet.error.status_code']}"/>
        </c:if>
    </p>
    <a class="btn btn-primary" href="${ctx}/">Back to the start</a>
</section>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
