package jp.ne.glory.ui.review;

import java.util.List;
import java.util.stream.Collectors;
import jp.ne.glory.domain.review.value.search.ReviewSearchResults;

/**
 * レビュー画面情報.
 *
 * @author Junki Yamada
 */
public class ReviewView {

    /**
     * レビューリスト.
     */
    public final List<ReviewBean> reviews;

    /**
     * コンストラクタ.
     *
     * @param results 検索結果
     */
    public ReviewView(final ReviewSearchResults results) {

        reviews = results.results
                .stream()
                .map(v -> new ReviewBean(v.review, v.game))
                .collect(Collectors.toList());
    }
}
