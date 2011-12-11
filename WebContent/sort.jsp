<%@ page import="com.chuidiang.chutasks.*"%>
<%
   Dao dao = (Dao) request.getSession().getAttribute("dao");
   dao.ordena(request.getParameter("orden"));
%>