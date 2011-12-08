package com.chuidiang.chutasks;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Tarea {
   private int id;
   private int estado;
   private String proyecto;
   private String persona;
   private String descripcion;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getEstado() {
      return estado;
   }

   public void setEstado(int estado) {
      this.estado = estado;
   }

   public String getProyecto() {
      return proyecto;
   }

   public void setProyecto(String proyecto) {
      this.proyecto = proyecto;
   }

   public String getPersona() {
      return persona;
   }

   public void setPersona(String persona) {
      this.persona = persona;
   }

   public String getDescripcion() {
      return descripcion;
   }

   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
   }
}
