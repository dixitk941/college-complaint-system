<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="All complaints" />
<c:set var="nav" value="complaints" />
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="split-head">
    <div>
        <p class="kicker">Administrator desk</p>
        <h1>All complaints</h1>
        <p class="lede">${fn:length(complaints)} on this list</p>
    </div>
</div>

<form class="filters" method="get" action="${ctx}/admin/complaints">
    <div class="field grow">
        <label for="q">Search</label>
        <input id="q" name="q" type="search" maxlength="80" placeholder="Ticket, title, name, or roll number"
               value="<c:out value='${query}'/>">
    </div>
    <div class="field">
        <label for="status">Status</label>
        <select id="status" name="status">
            <option value="">All statuses</option>
            <c:forEach var="st" items="${statuses}">
                <option value="${st.name()}" <c:if test="${selectedStatus == st.name()}">selected</c:if>>
                    <c:out value="${st.label}"/>
                </option>
            </c:forEach>
        </select>
    </div>
    <div class="field">
        <label for="category">Category</label>
        <select id="category" name="category">
            <option value="">All categories</option>
            <c:forEach var="cat" items="${categories}">
                <option value="${cat.name()}" <c:if test="${selectedCategory == cat.name()}">selected</c:if>>
                    <c:out value="${cat.label}"/>
                </option>
            </c:forEach>
        </select>
    </div>
    <button class="btn btn-primary" type="submit">Filter</button>
    <a class="btn btn-ghost" href="${ctx}/admin/complaints">Clear</a>
</form>

<div class="table-wrap">
    <table>
        <thead>
        <tr>
            <th>Ticket</th>
            <th>Student</th>
            <th>Roll</th>
            <th>Title</th>
            <th>Category</th>
            <th>Priority</th>
            <th>Status</th>
            <th>Filed</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="complaint" items="${complaints}">
            <tr>
                <td class="ticket"><c:out value="${complaint.ticketNo}"/></td>
                <td><c:out value="${complaint.student.fullName}"/></td>
                <td><c:out value="${complaint.student.rollNumber}"/></td>
                <td><c:out value="${complaint.title}"/></td>
                <td><c:out value="${complaint.category.label}"/></td>
                <td><span class="chip priority-${complaint.priority}"><c:out value="${complaint.priority.label}"/></span></td>
                <td><span class="chip status-${complaint.status}"><c:out value="${complaint.status.label}"/></span></td>
                <td><c:out value="${complaint.createdAtLabel}"/></td>
                <td><a href="${ctx}/complaint?id=${complaint.id}">Open</a></td>
            </tr>
        </c:forEach>
        <c:if test="${empty complaints}">
            <tr><td colspan="9">No complaints match this filter.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
