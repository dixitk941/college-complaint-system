<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Dashboard" />
<c:set var="nav" value="dashboard" />
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="split-head">
    <div>
        <p class="kicker">Student desk</p>
        <h1><c:out value="${loggedUser.fullName}"/></h1>
        <p class="lede">
            <c:out value="${loggedUser.rollNumber}"/> ·
            <c:out value="${loggedUser.department}"/> ·
            <c:out value="${loggedUser.yearOfStudy}"/>
        </p>
    </div>
    <a class="btn btn-primary" href="${ctx}/student/complaint/new">File a complaint</a>
</div>

<section class="stat-grid">
    <a class="stat" href="${ctx}/student/complaints">
        <span class="stat-value"><c:out value="${totalComplaints}"/></span>
        <span class="stat-label">All</span>
    </a>
    <c:forEach var="row" items="${statusCounts}">
        <a class="stat" href="${ctx}/student/complaints?status=${row.key}">
            <span class="stat-value"><c:out value="${row.count}"/></span>
            <span class="stat-label"><c:out value="${row.label}"/></span>
        </a>
    </c:forEach>
</section>

<section>
    <div class="split-head">
        <h2>Recent complaints</h2>
        <a href="${ctx}/student/complaints">View all</a>
    </div>
    <div class="table-wrap">
        <table>
            <thead>
            <tr>
                <th>Ticket</th>
                <th>Title</th>
                <th>Status</th>
                <th>Updated</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="complaint" items="${recentComplaints}">
                <tr>
                    <td class="ticket"><c:out value="${complaint.ticketNo}"/></td>
                    <td><c:out value="${complaint.title}"/></td>
                    <td><span class="chip status-${complaint.status}"><c:out value="${complaint.status.label}"/></span></td>
                    <td><c:out value="${complaint.updatedAtLabel}"/></td>
                    <td><a href="${ctx}/complaint?id=${complaint.id}">Open</a></td>
                </tr>
            </c:forEach>
            <c:if test="${empty recentComplaints}">
                <tr>
                    <td colspan="5">You have not filed a complaint yet.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</section>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
