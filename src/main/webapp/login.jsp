<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Sign in" />
<c:set var="nav" value="login" />
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<section class="auth-layout">
    <div class="card">
        <p class="kicker">Account</p>
        <h1>Sign in</h1>
        <c:if test="${not empty error}">
            <div class="banner error" role="alert"><c:out value="${error}"/></div>
        </c:if>
        <form method="post" action="${ctx}/login">
            <div class="field">
                <label for="username">Username</label>
                <input id="username" name="username" autocomplete="username" required maxlength="30"
                       value="<c:out value='${param.username}'/>">
            </div>
            <div class="field">
                <label for="password">Password</label>
                <input id="password" name="password" type="password" autocomplete="current-password" required>
            </div>
            <button class="btn btn-primary" type="submit">Sign in</button>
        </form>
        <p class="muted">New student? <a href="${ctx}/register">Create an account</a></p>
    </div>
    <aside class="lab-accounts">
        <h2>Lab accounts</h2>
        <p>These accounts are created the first time the application connects to MySQL.</p>
        <p><strong>Administrator</strong><br><code>admin</code> / <code>admin123</code></p>
        <p><strong>Student</strong><br><code>student</code> / <code>student123</code></p>
        <p class="muted">Passwords are stored as a SHA-256 hash, not as plain text.</p>
    </aside>
</section>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
