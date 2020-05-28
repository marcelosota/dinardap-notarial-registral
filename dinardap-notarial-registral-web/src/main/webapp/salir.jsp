<%
	session.invalidate();
	if(request.getServletContext().getInitParameter("javax.faces.PROJECT_STAGE").toUpperCase().equals("DEVELOPMENT"))
		response.sendRedirect("https://sauqa.dinardap.gob.ec/oidc/logout");
	else
		response.sendRedirect("https://sau.dinardap.gob.ec/oidc/logout");
	//response.sendRedirect(request.getContextPath()+"/index.jsp");
%>