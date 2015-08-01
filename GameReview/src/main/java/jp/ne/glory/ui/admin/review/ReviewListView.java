package jp.ne.glory.ui.admin.review;

import java.util.List;
import java.util.stream.Collectors;
import jp.ne.glory.domain.review.value.search.ReviewSearchResults;
import jp.ne.glory.web.common.PagerInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * レビュー一覧View.
 *
 * @author Junki Yamada
 */
public class ReviewListView {

    /**
     * 検索条件.
     */
    @Getter
    @Setter
    private ReviewSearchConditionBean condition;

    /**
     * レビューリスト.
     */
    @Getter
    @Setter
    private List<ReviewBean> reviews;

    /**
     * ページ情報.
     */
    @Getter
    @Setter
    private PagerInfo pager;

    /**
     * コンストラクタ.
     */
    public ReviewListView() {

        super();
    }

    /**
     * コンストラクタ.
     *
     * @param results 検索結果
     * @param condition 検索条件
     */
    public ReviewListView(final ReviewSearchResults results, final ReviewSearchConditionBean condition) {

        this.condition = condition;

        this.reviews = results.getResults().stream()
                .map(v -> new ReviewBean(v))
                .collect(Collectors.toList());

        this.pager = new PagerInfo(results);
    }
}
