package jp.ne.glory.domain.genre.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.domain.genre.value.search.GenreSearchCondition;

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

    @Override
    public List<Genre> search(final GenreSearchCondition condition) {

        final List<Genre> allList = searchAll(condition);

        if (condition.getLotPerCount() < 1) {

            return allList;
        }

        final int tempStartIndex = (condition.getLotNumber() - 1) * condition.getLotPerCount();

        final int startIndex = tempStartIndex < 1 ? 0 : tempStartIndex - 1;
        final int tempEndIndex = startIndex + condition.getLotPerCount();

        final int listLastIndex = allList.size() - 1;
        final int endIndex = listLastIndex < tempEndIndex ? listLastIndex : tempEndIndex;

        return allList.subList(startIndex, endIndex);

    }

    @Override
    public int getSearchCount(final GenreSearchCondition condition) {
        return searchAll(condition).size();
    }

    private List<Genre> searchAll(final GenreSearchCondition condition) {

        return genreMap.entrySet()
                .stream()
                .map(e -> e.getValue())
                .filter(v -> isMatchCondition(condition, v))
                .collect(Collectors.toList());
    }

    private boolean isMatchCondition(final GenreSearchCondition condition, final Genre genre) {

        final GenreName genreName = condition.getName();

        if (genreName != null && !genreName.getValue().isEmpty()) {

            if (!genreName.getValue().equals(genre.getName().getValue())) {

                return false;
            }
        }

        return true;
    }
}
