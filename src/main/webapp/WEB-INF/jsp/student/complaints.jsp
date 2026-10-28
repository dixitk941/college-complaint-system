<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="My complaints" />
<c:set var="nav" value="complaints" />
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="split-head">
    <div>
        <p class="kicker">Student desk</p>
        <h1>My complaints</h1>
        <p class="lede">${fn:length(complaints)} on this list</p>
    </div>
    <a class="btn btn-primary" href="${ctx}/student/complaint/new">File a complaint</a>
</div>

<form class="filters" method="get" action="${ctx}/student/complaints">
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
    <button class="btn btn-primary" type="submit">Filter</button>
    <a class="btn btn-ghost" href="${ctx}/student/complaints">Clear</a>
</form>

<div class="table-wrap">
    <table>
        <thead>
        <tr>
            <th>Ticket</th>
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
                <td><c:out value="${complaint.title}"/></td>
                <td><c:out value="${complaint.category.label}"/></td>
                <td><span class="chip priority-${complaint.priority}"><c:out value="${complaint.priority.label}"/></span></td>
                <td><span class="chip status-${complaint.status}"><c:out value="${complaint.status.label}"/></span></td>
                <td><c:out value="${complaint.createdAtLabel}"/></td>
                <td><a href="${ctx}/complaint?id=${complaint.id}">Open</a></td>
            </tr>
        </c:forEach>
        <c:if test="${empty complaints}">
            <tr><td colspan="7">No complaints match this filter.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
