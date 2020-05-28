<%
	String url = request.getServletContext().getInitParameter("url.sau")+"scope=openid&response_type=token";
	String redirect_uri = "&redirect_uri="+request.getServletContext().getInitParameter("redirect.uri");
	String client_id = "&client_id="+request.getServletContext().getInitParameter("client.id");
	//System.out.println(url+redirect_uri+client_id);
	response.sendRedirect(url+redirect_uri+client_id);
%>
<!-- 
<html>
<body>
	<form id="forma" action="main" method="get">
		Usuario: <input type="text" name="access_token"><br />
		Password: <input type="text" name="session_state"><br />
		<input type="hidden" name="perfil" value="2"/>
		<input type="submit" value="Iniciar" />
	</form>
</body>
</html> -->