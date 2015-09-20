package jp.ne.glory.application.genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;

public class GenreSearchStub extends GenreSearch {

    private final List<Genre> genreList = new ArrayList<>();

    public GenreSearchStub(final List<Genre> initData) {

        super();
        genreList.addAll(initData);
    }

    public Optional<Genre> searchBy(final GenreId genreId) {

        return genreList.stream()
                .filter(v -> v.getId().isSame(genreId))
                .findFirst();
    }

    public List<Genre> getAllGenres() {

        return genreList;
    }
}
