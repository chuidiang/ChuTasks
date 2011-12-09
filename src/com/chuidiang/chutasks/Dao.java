package com.chuidiang.chutasks;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

@ManagedBean
@SessionScoped
public class Dao {
   private static DataSource dataSource = null;
   private boolean tablasCreadas = false;
   private LinkedList<Tarea> lista = null;

   public Dao() {
      if (null == dataSource) {
         BasicDataSource basicDataSource = new BasicDataSource();
         basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
         basicDataSource.setUrl("jdbc:mysql://localhost:3306/chutasks");
         basicDataSource.setPassword("");
         basicDataSource.setUsername("root");

         dataSource = basicDataSource;
      }
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
      if (null != lista) {
         return lista;
      }
      Connection conexion = null;
      ResultSet rs = null;
      PreparedStatement ps = null;
      LinkedList<Tarea> listaTareas = new LinkedList<Tarea>();
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
            listaTareas.add(unaTarea);
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
      lista = listaTareas;
      return listaTareas;
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

   public void removeTarea(Tarea tarea) {
      Connection conexion = null;
      PreparedStatement ps = null;
      try {
         conexion = dataSource.getConnection();
         ps = conexion.prepareStatement("delete from tarea where id = ?");
         ps.setInt(1, tarea.getId());
         ps.executeUpdate();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            ps.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
         try {
            conexion.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }

   public void editAction(Tarea tarea) {
      tarea.setEditable(true);
   }

   public void saveAction(Tarea tarea) {
      tarea.setEditable(false);
      Connection conexion = null;
      PreparedStatement ps = null;
      try {
         conexion = dataSource.getConnection();
         ps = conexion
               .prepareStatement("update tarea set proyecto=?, persona=?, descripcion=?, estado=? where id = ?");
         ps.setString(1, tarea.getProyecto());
         ps.setString(2, tarea.getPersona());
         ps.setString(3, tarea.getDescripcion());
         ps.setInt(4, tarea.getEstado());
         ps.setInt(5, tarea.getId());
         ps.executeUpdate();

      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            ps.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
         try {
            conexion.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }

   public static void main(String[] args) {
      new Dao().creaTablas();
   }
}
