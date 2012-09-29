package com.chuidiang.chutasks;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class Dao {
   private static DataSource dataSource = null;
   private boolean tablasCreadas = false;
   private Tarea tareaEnEdicion = null;
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

   public void ordena(String orden) {
      String[] indicesOrdenados = orden.split(",");
      int contador = 1;
      Connection conexion = null;
      PreparedStatement ps = null;

      try {
         conexion = dataSource.getConnection();
         ps = conexion.prepareStatement("update tarea set orden=? where id=?");
         for (String unIndice : indicesOrdenados) {
            int indice = Integer.parseInt(unIndice);
            for (Tarea tarea : lista) {
               if (tarea.getId() == indice) {
                  ps.setInt(1, contador);
                  ps.setInt(2, indice);
                  ps.executeUpdate();
                  break;
               }
            }
            contador++;
         }
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

   public void creaTablas() {
      if (existeTabla()) {
         return;
      }
      Connection conexion = null;
      Statement st = null;

      try {
         conexion = dataSource.getConnection();
         st = conexion.createStatement();
         st.executeUpdate("create table tarea (id integer not null auto_increment, "
               + "primary key(id), proyecto varchar(64), persona varchar(64), "
               + "descripcion varchar(1024), estado tinyint default 0, "
               + "orden int default 10000)");
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
      LinkedList<Tarea> listaTareas = new LinkedList<Tarea>();
      lista = listaTareas;
      try {
         creaTablas();

         conexion = dataSource.getConnection();
         ps = conexion
               .prepareStatement("select * from tarea order by orden asc");
         rs = ps.executeQuery();
         while (rs.next()) {
            Tarea unaTarea = new Tarea();
            unaTarea.setId(rs.getInt("id"));
            unaTarea.setDescripcion(rs.getString("descripcion"));
            unaTarea.setEstado(rs.getInt("estado"));
            unaTarea.setPersona(rs.getString("persona"));
            unaTarea.setProyecto(rs.getString("proyecto"));
            if (null != tareaEnEdicion) {
               if (tareaEnEdicion.getId() == unaTarea.getId()) {
                  unaTarea.setEditable(true);
               }
            }
            listaTareas.add(unaTarea);
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            rs.close();
         } catch (Exception e) {
            e.printStackTrace();
         }
         try {
            ps.close();
         } catch (Exception e) {
            e.printStackTrace();
         }
         try {
            conexion.close();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      lista = listaTareas;
      return lista;
   }

   public void addTarea(Tarea unaTarea) {
      Connection conexion = null;
      PreparedStatement ps = null;
      try {
         conexion = dataSource.getConnection();
         ps = conexion
               .prepareStatement("insert into tarea (proyecto, persona, descripcion, estado, orden) "
                     + "values (?,?,?,?,?)");
         ps.setString(1, unaTarea.getProyecto());
         ps.setString(2, unaTarea.getPersona());
         ps.setString(3, unaTarea.getDescripcion());
         ps.setInt(4, unaTarea.getEstado());
         ps.setInt(5, 10000);
         ps.executeUpdate();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            ps.close();
         } catch (Exception e) {
            e.printStackTrace();
         }
         try {
            conexion.close();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   public static void main(String[] args) {
      Pattern patron = Pattern.compile("<.*?>(.*?)</.*?>");

      String cadena = "<div>Inicio</div><a href=articulos>Articulo</a><b>hola</b>";

      Matcher encaja = patron.matcher(cadena);

      while (encaja.find()) {
         System.out.println(encaja.group(1));
      }

   }

   public void salvaTarea(String id, String proyecto, String persona,
         String descripcion, String estado) {
      try {
         Tarea tarea = new Tarea();
         if ((null != id) && (!"".equals(id))) {
            tarea.setId(Integer.parseInt(id));
         }
         tarea.setProyecto(proyecto);
         try {
            tarea.setEstado(Integer.parseInt(estado.trim()));
         } catch (Exception e2) {
            tarea.setEstado(0);
            e2.printStackTrace();
         }
         tarea.setPersona(persona);
         tarea.setDescripcion(descripcion);

         if ((null == id) || "".equals(id)) {
            addTarea(tarea);
         } else {
            saveAction(tarea);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void borraTarea(String stringIdTarea) {
      Connection conexion = null;
      PreparedStatement ps = null;
      try {
         int idTarea = Integer.parseInt(stringIdTarea);
         conexion = dataSource.getConnection();
         ps = conexion.prepareStatement("delete from tarea where id = ?");
         ps.setInt(1, idTarea);
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
      tareaEnEdicion = tarea;
   }

   public void saveAction(Tarea tarea) {
      tarea.setEditable(false);
      tareaEnEdicion = null;
      Connection conexion = null;
      PreparedStatement ps = null;
      try {
         conexion = dataSource.getConnection();
         ps = conexion
               .prepareStatement("update tarea set proyecto=?, persona=?, descripcion=?, "
                     + "estado=? where id = ?");
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
}
