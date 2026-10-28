<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Admin dashboard" />
<c:set var="nav" value="dashboard" />
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="split-head">
    <div>
        <p class="kicker">Administrator desk</p>
        <h1>Complaints on campus</h1>
        <p class="lede">Student accounts: <c:out value="${studentCount}"/></p>
    </div>
    <a class="btn btn-primary" href="${ctx}/admin/complaints">Open the full list</a>
</div>

<section class="stat-grid">
    <a class="stat" href="${ctx}/admin/complaints">
        <span class="stat-value"><c:out value="${totalComplaints}"/></span>
        <span class="stat-label">All</span>
    </a>
    <c:forEach var="row" items="${statusCounts}">
        <a class="stat" href="${ctx}/admin/complaints?status=${row.key}">
            <span class="stat-value"><c:out value="${row.count}"/></span>
            <span class="stat-label"><c:out value="${row.label}"/></span>
        </a>
    </c:forEach>
</section>

<section>
    <h2>Latest complaints</h2>
    <div class="table-wrap">
        <table>
            <thead>
            <tr>
                <th>Ticket</th>
                <th>Student</th>
                <th>Title</th>
                <th>Status</th>
                <th>Filed</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="complaint" items="${recentComplaints}">
                <tr>
                    <td class="ticket"><c:out value="${complaint.ticketNo}"/></td>
                    <td><c:out value="${complaint.student.fullName}"/></td>
                    <td><c:out value="${complaint.title}"/></td>
                    <td><span class="chip status-${complaint.status}"><c:out value="${complaint.status.label}"/></span></td>
                    <td><c:out value="${complaint.createdAtLabel}"/></td>
                    <td><a href="${ctx}/complaint?id=${complaint.id}">Open</a></td>
                </tr>
            </c:forEach>
            <c:if test="${empty recentComplaints}">
                <tr><td colspan="6">No complaints have been filed.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</section>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
