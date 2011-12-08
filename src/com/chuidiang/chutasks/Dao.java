package com.chuidiang.chutasks;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

@ManagedBean
@ApplicationScoped
public class Dao {
   private DataSource dataSource = null;
   private boolean tablasCreadas = false;

   public Dao() {
      BasicDataSource basicDataSource = new BasicDataSource();
      basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
      basicDataSource.setUrl("jdbc:mysql://localhost:3306/chutasks");
      basicDataSource.setPassword("");
      basicDataSource.setUsername("root");

      dataSource = basicDataSource;
   }

   public void creaTablas() {
      if (existeTabla()) {
         return;
      }
      Connection conexion = null;
      Statement st = null;

      try {
         conexion = dataSource.getConnection();
         st = conexion.createStatement();
         st.executeUpdate("create table tarea (id integer not null auto_increment, primary key(id), proyecto varchar(64), persona varchar(64), descripcion varchar(1024), estado tinyint default 0)");
         tablasCreadas = true;
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            st.close();
         } catch (SQLException e1) {
         }
         try {
            conexion.close();
         } catch (Exception e) {
         }
      }
   }

   public boolean existeTabla() {
      if (tablasCreadas) {
         return true;
      }
      Connection conexion = null;
      try {
         conexion = dataSource.getConnection();
         DatabaseMetaData dbMetaData = conexion.getMetaData();
         ResultSet rs = dbMetaData.getTables("chutasks", null, "tarea",
               new String[] { "TABLE" });
         while (rs.next()) {
            tablasCreadas = true;
            return true;
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            conexion.close();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return false;
   }

   public List<Tarea> getTareas() {
      Connection conexion = null;
      ResultSet rs = null;
      PreparedStatement ps = null;
      LinkedList<Tarea> listaTaras = new LinkedList<Tarea>();
      try {
         creaTablas();

         conexion = dataSource.getConnection();
         ps = conexion.prepareStatement("select * from tarea");
         rs = ps.executeQuery();
         while (rs.next()) {
            Tarea unaTarea = new Tarea();
            unaTarea.setId(rs.getInt("id"));
            unaTarea.setDescripcion(rs.getString("descripcion"));
            unaTarea.setEstado(rs.getInt("estado"));
            unaTarea.setPersona(rs.getString("persona"));
            unaTarea.setProyecto(rs.getString("proyecto"));
            listaTaras.add(unaTarea);
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            rs.close();
         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         try {
            ps.close();
         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         try {
            conexion.close();
         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      return listaTaras;
   }

   public void addTarea(Tarea unaTarea) {
      Connection conexion = null;
      PreparedStatement ps = null;
      try {
         conexion = dataSource.getConnection();
         ps = conexion
               .prepareStatement("insert into tarea (proyecto, persona, descripcion, estado) values (?,?,?,?)");
         ps.setString(1, unaTarea.getProyecto());
         ps.setString(2, unaTarea.getPersona());
         ps.setString(3, unaTarea.getDescripcion());
         ps.setInt(4, unaTarea.getEstado());
         ps.executeUpdate();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            ps.close();
         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         try {
            conexion.close();
         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   public static void main(String[] args) {
      new Dao().creaTablas();
   }
}
