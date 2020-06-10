<%@page import="java.util.Map"%>
<%@page import="javax.faces.context.FacesContext"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%
    session.invalidate();
    response.sendRedirect("tramiteNotarial.jsf");
%>