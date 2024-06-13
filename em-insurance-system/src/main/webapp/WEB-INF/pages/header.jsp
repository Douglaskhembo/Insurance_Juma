<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<div class="top_nav">
	<div class="nav_menu">
		<nav>
			<div class="nav toggle">
				<a id="menu_toggle"><i class="fa fa-bars"></i></a>
			</div>

			<ul class="nav navbar-nav navbar-right">
				<%--<h1 class="count red"><c:out value="${sessionScope.timeoutMessage}"></c:out></h1>--%>
				<li class="">
					<a href="javascript:;"
					class="user-profile dropdown-toggle" data-toggle="dropdown"
					aria-expanded="false"> <security:authorize
							access="isAuthenticated()">
							<security:authentication property="principal.username" />
						</security:authorize> <span class=" fa fa-angle-down"></span>
				</a>
					<ul class="dropdown-menu dropdown-usermenu pull-right">
<%--						<li><a href="javascript:;"> Profile</a></li>--%>
<%--						<li><a href="javascript:;">Settings--%>
<%--						</a></li>--%>
<%--						<li><a href="javascript:;">Help</a></li>--%>
						<li><c:url var="logoutUrl" value="/logout" />
							<form action="${logoutUrl}" method="post" id="logoutForm">
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" />
							</form> <a href="#" onclick="javascript:logoutForm.submit();"><spring:message
									code="header.logout" /></a></li>
					</ul></li>

<%--				<li role="presentation" class="dropdown"><a href="javascript:;"--%>
<%--					class="dropdown-toggle info-number" data-toggle="dropdown"--%>
<%--					aria-expanded="false"> <i class="fa fa-envelope-o"></i> <span--%>
<%--						class="badge bg-green">0</span>--%>
<%--				</a>--%>
<%--					<ul id="menu1" class="dropdown-menu list-unstyled msg_list"--%>
<%--						role="menu">--%>

<%--							<div class="text-center">--%>
<%--								<a> <strong>See All Alerts</strong> <i--%>
<%--									class="fa fa-angle-right"></i>--%>
<%--								</a>--%>
<%--							</div>--%>
<%--						</li>--%>
					</ul></li>
			</ul>
		</nav>
	</div>
</div>

