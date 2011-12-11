<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
   xmlns:ui="http://java.sun.com/jsf/facelets"
   xmlns:h="http://java.sun.com/jsf/html"
   xmlns:f="http://java.sun.com/jsf/core">

<h:head>
   <title>Hola mundo</title>
</h:head>
<h:body>
   <h:form>
      <h:dataTable value="#{dao.tareas}" var="tarea">
         <h:column>
            <f:facet name="header">
               <h:outputText value="proyecto"></h:outputText>
            </f:facet>
            <h:outputText value="#{tarea.proyecto}"
               rendered="#{!tarea.editable}"></h:outputText>
            <h:inputText value="#{tarea.proyecto}"
               rendered="#{tarea.editable}"></h:inputText>
         </h:column>
         <h:column>
            <f:facet name="header">
               <h:outputText value="persona"></h:outputText>
            </f:facet>
            <h:outputText value="#{tarea.persona}"
               rendered="#{!tarea.editable}"></h:outputText>
            <h:inputText value="#{tarea.persona}" rendered="#{tarea.editable}"></h:inputText>
         </h:column>
         <h:column>
            <f:facet name="header">
               <h:outputText value="Descripcion"></h:outputText>
            </f:facet>
            <h:outputText value="#{tarea.descripcion}"
               rendered="#{!tarea.editable}"></h:outputText>
            <h:inputText value="#{tarea.descripcion}"
               rendered="#{tarea.editable}"></h:inputText>
         </h:column>
         <h:column>
            <f:facet name="header">
               <h:outputText value="estado"></h:outputText>
            </f:facet>
            <h:outputText value="#{tarea.estado}"
               rendered="#{!tarea.editable}"></h:outputText>
            <h:inputText value="#{tarea.estado}"
               rendered="#{tarea.editable}"></h:inputText>
         </h:column>
         <h:column>
            <f:facet name="header">Editar</f:facet>
            <h:commandButton value="Editar"
               action="#{dao.editAction(tarea)}"
               rendered="#{!tarea.editable}" />
            <h:commandButton value="Salvar"
               action="#{dao.saveAction(tarea)}"
               rendered="#{tarea.editable}"/>
         </h:column>
         <h:column>
            <h:commandButton action="#{dao.removeTarea(tarea)}"
               value="Borrar"></h:commandButton>

         </h:column>
      </h:dataTable>
   </h:form>
   <h:form>
      <h:panelGrid columns="2">
         <h:outputText value="Proyecto:"></h:outputText>
         <h:inputText id="proyecto" value="#{tarea.proyecto}"></h:inputText>

         <h:outputText value="Persona:"></h:outputText>
         <h:inputText id="persona" value="#{tarea.persona}"></h:inputText>

         <h:outputText value="Descripcion:"></h:outputText>
         <h:inputText id="descripcion" value="#{tarea.descripcion}"></h:inputText>

         <h:outputText value="Estado:"></h:outputText>
         <h:inputText id="estado" value="#{tarea.estado}"></h:inputText>

         <h:outputLabel></h:outputLabel>
         <h:commandButton action="#{dao.addTarea(tarea)}" value="Añadir"></h:commandButton>
      </h:panelGrid>
   </h:form>
</h:body>
</html>