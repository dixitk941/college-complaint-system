<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="editId" value="${complaint != null ? complaint.id : param.id}" />
<c:set var="pageTitle" value="${empty editId ? 'File a complaint' : 'Edit complaint'}" />
<c:set var="nav" value="new" />
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<c:set var="titleValue" value="${not empty param.title ? param.title : complaint.title}" />
<c:set var="descriptionValue" value="${not empty param.description ? param.description : complaint.description}" />
<c:set var="locationValue" value="${not empty param.location ? param.location : complaint.location}" />
<c:choose>
    <c:when test="${not empty param.category}">
        <c:set var="categoryValue" value="${param.category}" />
    </c:when>
    <c:when test="${complaint != null}">
        <c:set var="categoryValue" value="${complaint.category.name()}" />
    </c:when>
    <c:otherwise>
        <c:set var="categoryValue" value="" />
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${not empty param.priority}">
        <c:set var="priorityValue" value="${param.priority}" />
    </c:when>
    <c:when test="${complaint != null}">
        <c:set var="priorityValue" value="${complaint.priority.name()}" />
    </c:when>
    <c:otherwise>
        <c:set var="priorityValue" value="" />
    </c:otherwise>
</c:choose>

<section class="card form-card">
    <p class="kicker">Student desk</p>
    <h1><c:out value="${pageTitle}"/></h1>
    <p class="lede">An open complaint can be edited. After the office starts work, the text is locked and only remarks are added.</p>
    <c:if test="${not empty error}">
        <div class="banner error" role="alert"><c:out value="${error}"/></div>
    </c:if>
    <form method="post" action="${ctx}${empty editId ? '/student/complaint/new' : '/student/complaint/edit'}">
        <c:if test="${not empty editId}">
            <input type="hidden" name="id" value="<c:out value='${editId}'/>">
        </c:if>
        <div class="field">
            <label for="title">Title</label>
            <input id="title" name="title" required minlength="5" maxlength="120" value="<c:out value='${titleValue}'/>">
        </div>
        <div class="form-grid">
            <div class="field">
                <label for="category">Category</label>
                <select id="category" name="category" required>
                    <option value="">Select a category</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.name()}" <c:if test="${categoryValue == cat.name()}">selected</c:if>>
                            <c:out value="${cat.label}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="field">
                <label for="priority">Priority</label>
                <select id="priority" name="priority" required>
                    <option value="">Select a priority</option>
                    <c:forEach var="item" items="${priorities}">
                        <option value="${item.name()}" <c:if test="${priorityValue == item.name()}">selected</c:if>>
                            <c:out value="${item.label}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="field">
            <label for="location">Place on campus</label>
            <input id="location" name="location" maxlength="120" value="<c:out value='${locationValue}'/>">
        </div>
        <div class="field">
            <label for="description">What happened</label>
            <textarea id="description" name="description" required minlength="20" maxlength="2000"><c:out value="${descriptionValue}"/></textarea>
        </div>
        <div class="actions">
            <button class="btn btn-primary" type="submit">
                <c:out value="${empty editId ? 'File complaint' : 'Save changes'}"/>
            </button>
            <c:choose>
                <c:when test="${empty editId}">
                    <a class="btn btn-ghost" href="${ctx}/student/complaints">Cancel</a>
                </c:when>
                <c:otherwise>
                    <a class="btn btn-ghost" href="${ctx}/complaint?id=${editId}">Cancel</a>
                </c:otherwise>
            </c:choose>
        </div>
    </form>
</section>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
