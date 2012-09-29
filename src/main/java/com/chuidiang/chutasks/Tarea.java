package com.chuidiang.chutasks;

public class Tarea {
   private int id;
   private int estado;
   private String proyecto;
   private String persona;
   private String descripcion;
   private boolean editable = false;

   public void save() {
      System.out.println(estado);
   }

   public boolean isEditable() {
      return editable;
   }

   public void setEditable(boolean editable) {
      this.editable = editable;
   }

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
