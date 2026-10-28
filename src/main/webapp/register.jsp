<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Register" />
<c:set var="nav" value="register" />
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<section class="card form-card">
    <p class="kicker">Student account</p>
    <h1>Register</h1>
    <p class="lede">Use your roll number. Administrator accounts are not created from this form.</p>
    <c:if test="${not empty error}">
        <div class="banner error" role="alert"><c:out value="${error}"/></div>
    </c:if>
    <form method="post" action="${ctx}/register" class="form-grid">
        <div class="field">
            <label for="fullName">Full name</label>
            <input id="fullName" name="fullName" required maxlength="100" value="<c:out value='${param.fullName}'/>">
        </div>
        <div class="field">
            <label for="username">Username</label>
            <input id="username" name="username" required minlength="4" maxlength="30" autocomplete="username"
                   value="<c:out value='${param.username}'/>">
        </div>
        <div class="field">
            <label for="email">Email</label>
            <input id="email" name="email" type="email" required maxlength="120" value="<c:out value='${param.email}'/>">
        </div>
        <div class="field">
            <label for="rollNumber">Roll number</label>
            <input id="rollNumber" name="rollNumber" required maxlength="20" value="<c:out value='${param.rollNumber}'/>">
        </div>
        <div class="field">
            <label for="department">Department</label>
            <select id="department" name="department" required>
                <option value="">Select department</option>
                <c:forEach var="dept" items="${departments}">
                    <option value="<c:out value='${dept}'/>" <c:if test="${param.department == dept}">selected</c:if>>
                        <c:out value="${dept}"/>
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="field">
            <label for="yearOfStudy">Year</label>
            <select id="yearOfStudy" name="yearOfStudy" required>
                <option value="">Select year</option>
                <c:forEach var="year" items="${years}">
                    <option value="<c:out value='${year}'/>" <c:if test="${param.yearOfStudy == year}">selected</c:if>>
                        <c:out value="${year}"/>
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="field">
            <label for="phone">Mobile number</label>
            <input id="phone" name="phone" inputmode="numeric" maxlength="10" value="<c:out value='${param.phone}'/>">
        </div>
        <div class="field">
            <label for="password">Password</label>
            <input id="password" name="password" type="password" required minlength="6" autocomplete="new-password">
        </div>
        <div class="field span-2">
            <label for="confirmPassword">Confirm password</label>
            <input id="confirmPassword" name="confirmPassword" type="password" required minlength="6" autocomplete="new-password">
        </div>
        <div class="span-2 actions">
            <button class="btn btn-primary" type="submit">Create account</button>
            <a class="btn btn-ghost" href="${ctx}/login">I already have an account</a>
        </div>
    </form>
</section>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
