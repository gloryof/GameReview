package jp.ne.glory.domain.genre.flow;

import jp.ne.glory.domain.genre.flow.GenreList;
import java.util.ArrayList;
import java.util.List;
import jp.ne.glory.domain.genre.entity.Genre;

public class GenreListStub extends GenreList {

    private final List<Genre> genreList = new ArrayList<>();

    public GenreListStub(final List<Genre> initData) {

        super();
        genreList.addAll(initData);
    }

    public List<Genre> getAllGenres() {

        return genreList;
    }
}
