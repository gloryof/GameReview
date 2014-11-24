package jp.ne.glory.domain.genre.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;

public class GenreRepositoryStub implements GenreRepository {

    private final Map<Long, Genre> genreMap = new HashMap<>();

    private long sequence = 1;

    @Override
    public GenreId save(Genre genre) {

        final Genre saveGenre;
        if (genre.id == null) {

            saveGenre = new Genre(new GenreId(sequence), genre.name);
            sequence++;
        } else {

            saveGenre = genre;
        }
        genreMap.put(saveGenre.id.value, saveGenre);

        return saveGenre.id;
    }

    @Override
    public Optional<Genre> findBy(GenreId genreId) {

        return Optional.ofNullable(genreMap.get(genreId.value));
    }

    @Override
    public List<Genre> getAllGenreList() {

        return genreMap.entrySet()
                .stream()
                .map(entry -> entry.getValue())
                .sorted((b, n) -> b.id.value.compareTo(n.id.value))
                .collect(Collectors.toList());
    }

}
