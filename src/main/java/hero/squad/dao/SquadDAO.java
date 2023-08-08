package hero.squad.dao;
import hero.squad.models.Squad;
import java.util.List;

public interface SquadDAO {
    List<Squad> getAll();

    // CREATE
    void add(Squad squad);

    void update(Squad squad);

    // READ
    Squad findById(int id);

    //DELETE
    void deleteById(int id);
}
