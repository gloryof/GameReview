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

    public long getCurrentSequence() {

        return sequence;
    }
}
