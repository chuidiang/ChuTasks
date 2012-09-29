<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.chuidiang.chutasks.*"%>
<%
   if (null != request.getParameter("salvar")) {
				Dao dao = (Dao) request.getSession().getAttribute("dao");
				dao.salvaTarea(request.getParameter("inputId"),
						request.getParameter("inputProyecto"),
						request.getParameter("inputPersona"),
						request.getParameter("inputDescripcion"),
						request.getParameter("inputEstado"));
			}
			if (null != request.getParameter("borrar")) {
				Dao dao = (Dao) request.getSession().getAttribute("dao");
				dao.borraTarea(request.getParameter("borrar"));
			}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ChuTasks</title>
<script type="text/javascript" src="js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.16.custom.min.js"></script>
<link type="text/css" href="css/main.css" rel="stylesheet" />
<link type="text/css"
   href="css/custom-theme/jquery-ui-1.8.16.custom.css" rel="stylesheet" />
<script>
	$(document).ready(function() {
		$("#tablaTareas").sortable({
			items : 'tr',
			update : function(event, ui) {
				var array = $("#tablaTareas").sortable("toArray");
				$.ajax('sort.jsp', {
					data : {
						orden : $("#tablaTareas").sortable("toArray").toString()
					}
				});
			}
		});
		$("#dialogoTarea").dialog({autoOpen : false});
		$("#botonCrear").click(function() {
	        $("#inputId").attr("value","");
	        $("#inputProyecto").attr("value","");
	        $("#inputPersona").attr("value","");
	        $("#inputDescripcion").attr("value","");
	        $("#inputEstado").attr("value","");
			$("#dialogoTarea").dialog("open");
		});
	});
	
	function editar (id){
		var tds = $("#"+id+" td");
        $("#inputId").attr("value",id);
		$("#inputProyecto").attr("value",tds[0].innerHTML);
        $("#inputPersona").attr("value",tds[1].innerHTML);
        $("#inputDescripcion").attr("value",tds[2].innerHTML);
        $("#inputEstado").attr("value",tds[3].innerHTML);
        $("#dialogoTarea").dialog("open");
    }
	
	function borrar (id){
		$("#idBorrar").attr("value",id);
		$("#formBorrar").submit();
	}
</script>
</head>
<body>
   <jsp:useBean id="dao" scope="session"
      class="com.chuidiang.chutasks.Dao" />
   <table>
      <thead>
         <tr>
            <td>Proyecto</td>
            <td>Persona</td>
            <td class="descripcion">Descripcion</td>
            <td>Porcentaje</td>
            <td>Editar</td>
            <td>Borrar</td>
         </tr>
      </thead>
      <tbody id="tablaTareas">
         <c:forEach items="${dao.tareas}" var="tarea">
            <tr id="${tarea.id}">
               <td><c:out value="${tarea.proyecto}"></c:out></td>
               <td><c:out value="${tarea.persona}"></c:out></td>
               <td><c:out value="${tarea.descripcion}"></c:out></td>
               <td><c:out value="${tarea.estado}"></c:out></td>
               <td><button
                     onclick="editar(<c:out value='${tarea.id}'/>)">editar</button>
               </td>
               <td><button
                     onclick="borrar(<c:out value='${tarea.id}'/>)">Borrar</button>
               </td>
            </tr>
         </c:forEach>
         <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td><button id="botonCrear">Crear</button>
            </td>
            <td></td>
         </tr>
      </tbody>
   </table>
   <div id="dialogoTarea" title="Crear/Modficar tarea">
      <form action="index.jsp" method="post">
         <input type="hidden" name="inputId" id="inputId" value="" />
         <table>
            <tr>
               <td>Proyecto:</td>
               <td><input type="text" id="inputProyecto"
                  name="inputProyecto" value=""></input>
               </td>
            </tr>
            <tr>
               <td>Persona:</td>
               <td><input type="text" id="inputPersona"
                  name="inputPersona" value=""></input></td>
            </tr>
            <tr>
               <td>Descripcion:</td>
               <td><textArea rows="10" cols="80" id="inputDescripcion"
                     name="inputDescripcion"></textArea></td>
            </tr>
            <tr>
               <td>Estado:</td>
               <td><input type="text" id="inputEstado"
                  name="inputEstado" value=""></input></td>
            </tr>
            <tr>
               <td></td>
               <td><input type="submit" name="salvar"
                  value="Salvar" />
               </td>
            </tr>
         </table>
      </form>
   </div>
   <form id="formBorrar" style="visibility: hidden; display: none;"
      action="index.jsp" method="post">
      <input type="hidden" id="idBorrar" name="borrar" value="" /> <input
         type="submit" name="botonBborrar" value="Borrar" />
   </form>
</body>
</html>

