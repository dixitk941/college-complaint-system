<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="${complaint.ticketNo}" />
<c:set var="nav" value="complaints" />
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<div class="split-head">
    <div>
        <p class="kicker">Ticket</p>
        <p class="ticket"><c:out value="${complaint.ticketNo}"/></p>
        <h1><c:out value="${complaint.title}"/></h1>
    </div>
    <p class="chips">
        <span class="chip status-${complaint.status}"><c:out value="${complaint.status.label}"/></span>
        <span class="chip priority-${complaint.priority}"><c:out value="${complaint.priority.label}"/></span>
    </p>
</div>

<div class="detail">
    <section class="card">
        <h2>What was reported</h2>
        <p class="prose"><c:out value="${complaint.description}"/></p>
        <h2>Remarks</h2>
        <c:choose>
            <c:when test="${empty complaint.remarks}">
                <p class="muted">No remarks yet. The office writes here when the status changes.</p>
            </c:when>
            <c:otherwise>
                <ol class="timeline">
                    <c:forEach var="remark" items="${complaint.remarks}">
                        <li>
                            <div class="timeline-top">
                                <strong><c:out value="${remark.author.fullName}"/></strong>
                                <span><c:out value="${remark.createdAtLabel}"/></span>
                            </div>
                            <c:if test="${remark.statusChange}">
                                <p class="chips">
                                    <span class="chip status-${remark.oldStatus}"><c:out value="${remark.oldStatus.label}"/></span>
                                    <span class="arrow" aria-hidden="true">to</span>
                                    <span class="chip status-${remark.newStatus}"><c:out value="${remark.newStatus.label}"/></span>
                                </p>
                            </c:if>
                            <p class="prose"><c:out value="${remark.message}"/></p>
                        </li>
                    </c:forEach>
                </ol>
            </c:otherwise>
        </c:choose>
    </section>

    <aside class="stack">
        <div class="card">
            <h2>Record</h2>
            <dl class="meta">
                <dt>Category</dt>
                <dd><c:out value="${complaint.category.label}"/></dd>
                <dt>Place</dt>
                <dd><c:out value="${empty complaint.location ? 'Not specified' : complaint.location}"/></dd>
                <dt>Filed</dt>
                <dd><c:out value="${complaint.createdAtLabel}"/></dd>
                <dt>Updated</dt>
                <dd><c:out value="${complaint.updatedAtLabel}"/></dd>
                <dt>Student</dt>
                <dd><c:out value="${complaint.student.fullName}"/></dd>
                <dt>Roll number</dt>
                <dd><c:out value="${complaint.student.rollNumber}"/></dd>
                <dt>Department</dt>
                <dd><c:out value="${complaint.student.department}"/></dd>
                <dt>Email</dt>
                <dd><c:out value="${complaint.student.email}"/></dd>
                <dt>Phone</dt>
                <dd><c:out value="${empty complaint.student.phone ? 'Not given' : complaint.student.phone}"/></dd>
            </dl>
        </div>

        <div class="card">
            <c:choose>
                <c:when test="${loggedUser.admin && !complaint.withdrawn}">
                    <h2>Office update</h2>
                    <form method="post" action="${ctx}/admin/complaint/update">
                        <input type="hidden" name="id" value="${complaint.id}">
                        <div class="field">
                            <label for="status">Status</label>
                            <select id="status" name="status" required>
                                <c:forEach var="st" items="${adminStatuses}">
                                    <option value="${st.name()}" <c:if test="${complaint.status == st}">selected</c:if>>
                                        <c:out value="${st.label}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="field">
                            <label for="priority">Priority</label>
                            <select id="priority" name="priority" required>
                                <c:forEach var="item" items="${priorities}">
                                    <option value="${item.name()}" <c:if test="${complaint.priority == item}">selected</c:if>>
                                        <c:out value="${item.label}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="field">
                            <label for="remark">Remark</label>
                            <textarea id="remark" name="remark" required minlength="5" maxlength="1000"></textarea>
                        </div>
                        <button class="btn btn-primary" type="submit">Save update</button>
                    </form>
                </c:when>
                <c:when test="${loggedUser.student && complaint.open}">
                    <h2>Your complaint is still open</h2>
                    <div class="actions">
                        <a class="btn btn-primary" href="${ctx}/student/complaint/edit?id=${complaint.id}">Edit</a>
                        <form method="post" action="${ctx}/student/complaint/withdraw"
                              onsubmit="return confirm('Withdraw this complaint? You will not be able to reopen it.');">
                            <input type="hidden" name="id" value="${complaint.id}">
                            <button class="btn btn-ghost" type="submit">Withdraw</button>
                        </form>
                    </div>
                </c:when>
                <c:when test="${complaint.withdrawn}">
                    <h2>Withdrawn</h2>
                    <p class="muted">The student closed this complaint. The record stays on file.</p>
                </c:when>
                <c:otherwise>
                    <h2>Locked</h2>
                    <p class="muted">This complaint is <c:out value="${complaint.status.label}"/>. The text can no longer be edited.</p>
                </c:otherwise>
            </c:choose>
            <p class="back-link">
                <a href="${ctx}${loggedUser.admin ? '/admin/complaints' : '/student/complaints'}">Back to the list</a>
            </p>
        </div>
    </aside>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
