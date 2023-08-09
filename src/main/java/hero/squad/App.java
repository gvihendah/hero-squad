package hero.squad;
import static Spark.Spark.*;
import hero.squad.dao.Sql2oSquadDAO;
import hero.squad.models.Squad;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import org.sql2o.Sql2o;
import hero.squad.dao.Sql2oHeroDAO;
import hero.squad.models.Hero;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class App {

    private static final String databaseUrl = System.getenv("JDBC_DATABASE_URL");
    private static final String databaseUsername = System.getenv("JDBC_DATABASE_USERNAME");
    private static final String databasePassword = System.getenv("JDBC_DATABASE_PASSWORD");
    private static final String port = System.getenv("PORT");

    public static void main(String[] args) {
        Sql2o sql2o = new Sql2o(databaseUrl, databaseUsername, databasePassword);
        Sql2oHeroDAO heroDAO = new Sql2oHeroDAO(sql2o);
        Sql2oSquadDAO squadDAO = new Sql2oSquadDAO(sql2o);
        port(port == null ? 8000 : Integer.parseInt(port));

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Squad> squads = squadDAO.getAll();
            System.out.println(req.pathInfo());
            model.put("url", "/");
            model.put("action", "Register");
            model.put("title", "Squads");
            model.put("squads", squads);
            return new ModelAndView(model, "squads.hbs");
        }, new HandlebarsTemplateEngine());

        post("/", (req, res) -> {
            String name = req.queryParams("name");
            String role = req.queryParams("role");
            Squad squad = new Squad(name, role);
            squadDAO.add(squad);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        get("/squads/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params("id"));
            Squad squad = squadDAO.findById(id);
            List<Hero> heroes = heroDAO.getHeroesBySquad(id);
            model.put("url", "/squads/" + id);
            model.put("title", squad.getName());
            model.put("action", "Update");
            model.put("squad", squad);
            model.put("heroes", heroes);
            return new ModelAndView(model, "squad.hbs");
        }, new HandlebarsTemplateEngine());

        post("/squads/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Squad squad = squadDAO.findById(id);
            squad.setName(req.queryParams("name"));
            squad.setRole(req.queryParams("role"));
            squadDAO.update(squad);
            res.redirect("/squads/" + id);
            return null;
        }, new HandlebarsTemplateEngine());

        post("/squads/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Squad squad = squadDAO.findById(id);
            squad.setName(req.queryParams("name"));
            squad.setRole(req.queryParams("role"));
            squadDAO.update(squad);
            res.redirect("/squads/" + id);
            return null;
        }, new HandlebarsTemplateEngine());

        post("/squads/:id/delete", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            squadDAO.deleteById(id);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        get("/hero", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Hero> heroes = heroDAO.getAll();
            List<Squad> squads = squadDAO.getAll();
            model.put("url", "/hero");
            model.put("action", "Register");
            model.put("title", "Heroes");
            model.put("heroes", heroes);
            model.put("squads", squads);
            return new ModelAndView(model, "heroes.hbs");
        }, new HandlebarsTemplateEngine());

        get("/hero/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params("id"));
            List<Squad> squads = squadDAO.getAll();
            Hero hero = heroDAO.findById(id);
            model.put("url", "/hero/" + id);
            model.put("title", hero.getName());
            model.put("action", "Update");
            model.put("hero", hero);
            model.put("squads", squads);
            return new ModelAndView(model, "hero.hbs");
        }, new HandlebarsTemplateEngine());

        post("/hero/:id", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            String squadId = req.queryParams("squad_id");
            Hero hero = heroDAO.findById(id);
            hero.setAge(Integer.parseInt(req.queryParams("age")));
            hero.setSpecialPower(req.queryParams("special_power"));
            hero.setWeakness(req.queryParams("weakness"));
            hero.setSquadId(squadId == null ? null : Integer.parseInt(squadId));
            hero.setName(req.queryParams("name"));
            heroDAO.update(hero);
            res.redirect("/hero/" + id);
            return null;
        }, new HandlebarsTemplateEngine());

        post("/hero/:id/delete", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            heroDAO.deleteById(id);
            res.redirect("/hero");
            return null;
        }, new HandlebarsTemplateEngine());

        post("/hero", (req, res) -> {
            String name = req.queryParams("name");
            Integer age = Integer.parseInt(req.queryParams("age"));
            String specialPower = req.queryParams("special_power");
            String weakness = req.queryParams("weakness");
            String squadId = req.queryParams("squad_id");
            Hero newHero = new Hero(name, age, specialPower, weakness, squadId == null ? null : Integer.parseInt(squadId));
            heroDAO.add(newHero);
            res.redirect("/hero");
            return null;
        }, new HandlebarsTemplateEngine());
    }
}
