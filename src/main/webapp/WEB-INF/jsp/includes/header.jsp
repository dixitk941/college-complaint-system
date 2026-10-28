<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>
        <c:if test="${not empty pageTitle}"><c:out value="${pageTitle}"/> · </c:if>
        <c:out value="${collegeName}"/>
    </title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<header class="topbar">
    <a class="brand" href="${ctx}/">
        <span class="brand-mark">CC</span>
        <span>
            <strong><c:out value="${collegeName}"/></strong>
            <small>Complaint Management System</small>
        </span>
    </a>
    <nav>
        <a class="${nav == 'home' ? 'active' : ''}" href="${ctx}/">Home</a>
        <c:choose>
            <c:when test="${loggedUser.admin}">
                <a class="${nav == 'dashboard' ? 'active' : ''}" href="${ctx}/admin/dashboard">Dashboard</a>
                <a class="${nav == 'complaints' ? 'active' : ''}" href="${ctx}/admin/complaints">Complaints</a>
                <span class="who"><c:out value="${loggedUser.fullName}"/></span>
                <a href="${ctx}/logout">Sign out</a>
            </c:when>
            <c:when test="${loggedUser.student}">
                <a class="${nav == 'dashboard' ? 'active' : ''}" href="${ctx}/student/dashboard">Dashboard</a>
                <a class="${nav == 'complaints' ? 'active' : ''}" href="${ctx}/student/complaints">My complaints</a>
                <a class="${nav == 'new' ? 'active' : ''}" href="${ctx}/student/complaint/new">File complaint</a>
                <span class="who"><c:out value="${loggedUser.fullName}"/></span>
                <a href="${ctx}/logout">Sign out</a>
            </c:when>
            <c:otherwise>
                <a class="${nav == 'login' ? 'active' : ''}" href="${ctx}/login">Sign in</a>
                <a class="${nav == 'register' ? 'active' : ''}" href="${ctx}/register">Register</a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>
<main class="page">
    <c:if test="${not empty flash}">
        <div class="banner success" role="status"><c:out value="${flash}"/></div>
    </c:if>
    <c:if test="${not empty flashError}">
        <div class="banner error" role="alert"><c:out value="${flashError}"/></div>
    </c:if>
