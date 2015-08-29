package jp.ne.glory.ui.review;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.domain.review.value.search.ReviewSearchResults;
import lombok.Getter;

/**
 * レビュー画面情報.
 *
 * @author Junki Yamada
 */
public class ReviewView {

    /**
     * レビューリスト.
     */
    @Getter
    private final List<ReviewBean> reviews;

    /**
     * コンストラクタ.
     *
     * @param results 検索結果（複数）
     */
    public ReviewView(final ReviewSearchResults results) {

        reviews = results.getResults()
                .stream()
                .map(v -> new ReviewBean(v.getReview(), v.getGame(), v.getGenre()))
                .collect(Collectors.toList());
    }

    /**
     * コンストラクタ.
     *
     * @param result 検索結果
     */
    public ReviewView(final ReviewSearchResult result) {

        reviews = new ArrayList<>();
        reviews.add(new ReviewBean(result.getReview(), result.getGame(), result.getGenre()));
    }
}
