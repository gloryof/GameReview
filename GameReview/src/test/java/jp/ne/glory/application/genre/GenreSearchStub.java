package jp.ne.glory.application.genre;

import java.util.ArrayList;
import java.util.List;
import jp.ne.glory.domain.genre.entity.Genre;

public class GenreSearchStub extends GenreSearch {

    private final List<Genre> genreList = new ArrayList<>();

    public GenreSearchStub(final List<Genre> initData) {

        super();
        genreList.addAll(initData);
    }

    public List<Genre> getAllGenres() {

        return genreList;
    }
}
