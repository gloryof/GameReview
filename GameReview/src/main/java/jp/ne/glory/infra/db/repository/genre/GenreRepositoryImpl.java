package jp.ne.glory.infra.db.repository.genre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.repository.GenreRepository;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;

/**
 * ジャンルリポジトリ.
 *
 * @author Junki Yamada
 */
@RequestScoped
public class GenreRepositoryImpl implements GenreRepository {

    private static final Map<Long, Genre> genreMap = new HashMap<>();

    private static long sequence = 1;

    static {

        final Genre action = new Genre(new GenreId(sequence), new GenreName("アクション"));
        genreMap.put(action.getId().getValue(), action);
        sequence++;

        final Genre rpg = new Genre(new GenreId(sequence), new GenreName("RPG"));
        genreMap.put(rpg.getId().getValue(), rpg);
        sequence++;

        final Genre simulation = new Genre(new GenreId(sequence), new GenreName("シミュレーション"));
        genreMap.put(simulation.getId().getValue(), simulation);
        sequence++;
    }

    @Override
    public GenreId save(Genre genre) {

        final Genre saveGenre;
        if (genre.getId() == null || !genre.isRegistered()) {

            saveGenre = new Genre(new GenreId(sequence), genre.getName());
            sequence++;
        } else {

            saveGenre = genre;
        }
        genreMap.put(saveGenre.getId().getValue(), saveGenre);

        return saveGenre.getId();
    }

    @Override
    public Optional<Genre> findBy(GenreId genreId) {

        return Optional.ofNullable(genreMap.get(genreId.getValue()));
    }

    @Override
    public List<Genre> getAllGenreList() {

        return genreMap.entrySet()
                .stream()
                .map(entry -> entry.getValue())
                .sorted((b, n) -> b.getId().getValue().compareTo(n.getId().getValue()))
                .collect(Collectors.toList());
    }

}
