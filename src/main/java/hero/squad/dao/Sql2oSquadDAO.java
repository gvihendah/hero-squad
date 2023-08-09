package hero.squad.dao;
import hero.squad.models.Squad;

import org.eclipse.jetty.server.SymlinkAllowedResourceAliasChecker;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oSquadDAO implements SquadDAO{
    private final Sql2o sql2o;

    public Sql2oSquadDAO(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public List<Squad> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM squads")
                    .executeAndFetch(Squad.class);
        }catch (Sql2oException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void add(Squad squad) {
        String sql = "INSERT INTO squads (name, role) VALUES (:name, :role)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql, false)
                    .bind(squad)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void update(Squad squad) {
        String sql = "UPDATE squads SET (name, role) = (:name, :role) WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql, false)
                    .bind(squad)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Squad findById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM squads WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Squad.class);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "UPDATE heroes set squad_id=NULL where squad_id=:id;DELETE from squads WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
}}
