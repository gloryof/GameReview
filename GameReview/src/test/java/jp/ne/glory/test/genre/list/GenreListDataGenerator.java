package jp.ne.glory.test.genre.list;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;

public class GenreListDataGenerator {

    public static List<Genre> createGenreList(final int count) {

        return IntStream.range(0, count).asLongStream()
                .mapToObj(v -> createGenre(v))
                .collect(Collectors.toList());
    }

    private static Genre createGenre(final long paramGenreId) {

        final GenreId genreId = new GenreId(paramGenreId);
        final GenreName name = new GenreName("ジャンル" + paramGenreId);

        return new Genre(genreId, name);
    }
}
