package nn.dgordeev.jdbc.starter.dao;

import java.util.Collection;
import java.util.Optional;

public interface Dao<ID, E> {

    boolean delete(ID id);

    E findByIdRequired(ID id);

    Collection<E> findAll();

    Optional<E> findById(ID id);

    E update(E ticket);

    E save(E ticket);
}
