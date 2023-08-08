package hero.squad.dao;
import java.util.List;

public class Sql2oHeroDAO implements HeroDAO {
    private final Sql2o sql2o;

    public Sql2oHeroDAO(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Hero hero) {
        String sql = "INSERT INTO heroes (name, age, special_power,weakness, squad_id) VALUES (:name, :age, :specialPower, :weakness, :squadId)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql, false)
                    .bind(hero)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void update(Hero hero) {
        String sql = "UPDATE heroes SET (name, age, special_power, weakness, squad_id) = (:name, :age, :specialPower, :weakness, :squadId) WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql, false)
                    .bind(hero)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Hero> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM heroes")
                    .addColumnMapping("special_power", "specialPower")
                    .addColumnMapping("squad_id", "squadId")
                    .executeAndFetch(Hero.class);
        }
    }

    @Override
    public Hero findById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM heroes WHERE id = :id")
                    .addParameter("id", id)
                    .addColumnMapping("special_power", "specialPower")
                    .addColumnMapping("squad_id", "squadId")
                    .executeAndFetchFirst(Hero.class);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from heroes WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Hero> getHeroesBySquad(int squadId) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM heroes WHERE squad_id = :squadId")
                    .addParameter("squadId", squadId)
                    .addColumnMapping("special_power", "specialPower")
                    .addColumnMapping("squad_id", "squadId")
                    .executeAndFetch(Hero.class);
        }
    }
}
