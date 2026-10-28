<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Home" />
<c:set var="nav" value="home" />
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<section class="hero">
    <div>
        <p class="kicker">Campus office</p>
        <h1>Report a campus problem. Track it until it is closed.</h1>
        <p class="lede">Students file a complaint with the office that owns it. Administrators record every status change in the same ticket.</p>
        <div class="actions">
            <c:choose>
                <c:when test="${loggedUser.admin}">
                    <a class="btn btn-primary" href="${ctx}/admin/dashboard">Open the admin desk</a>
                </c:when>
                <c:when test="${loggedUser.student}">
                    <a class="btn btn-primary" href="${ctx}/student/dashboard">Open my complaints</a>
                    <a class="btn btn-ghost" href="${ctx}/student/complaint/new">File a complaint</a>
                </c:when>
                <c:otherwise>
                    <a class="btn btn-primary" href="${ctx}/login">Sign in</a>
                    <a class="btn btn-ghost" href="${ctx}/register">Create a student account</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <article class="sample-ticket">
        <p class="kicker">Sample record</p>
        <p class="ticket">CMS-2026-0014</p>
        <h2>Wi-Fi keeps dropping in Programming Lab 3</h2>
        <p class="muted">CSE Block, Programming Lab 3</p>
        <p class="chips">
            <span class="chip status-IN_PROGRESS">In progress</span>
            <span class="chip priority-HIGH">High</span>
        </p>
        <p class="muted">Last remark: Estate office assigned a technician.</p>
    </article>
</section>

<section class="steps">
    <article class="step">
        <span>01</span>
        <h2>File</h2>
        <p>Choose a category, name the place, and write what happened.</p>
    </article>
    <article class="step">
        <span>02</span>
        <h2>Track</h2>
        <p>Each complaint gets a ticket number and a status you can open later.</p>
    </article>
    <article class="step">
        <span>03</span>
        <h2>Resolve</h2>
        <p>The office adds a remark whenever the status changes. Nothing is deleted.</p>
    </article>
</section>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
