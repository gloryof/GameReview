package jp.ne.glory.infra.db.review.entity;

import java.util.List;
import java.util.stream.Collectors;
import jp.ne.glory.domain.review.value.search.ReviewSearchCondition;
import jp.ne.glory.infra.db.common.entity.RecordLimits;
import lombok.Getter;

/**
 * レビュー検索条件.
 *
 * @author Junki Yamada
 */
public class ReviewSearchParam {

    /**
     * 検索件数制限.
     */
    @Getter
    private final RecordLimits limits;

    /**
     * レビューIDリスト.
     */
    private final List<Long> reviewIds;

    /**
     * ジャンルIDリスト.
     */
    private final List<Long> genreIds;

    /**
     * コンストラクタ.
     *
     * @param condition 検索条件
     */
    public ReviewSearchParam(final ReviewSearchCondition condition) {

        this.limits = new RecordLimits(condition);

        this.reviewIds = condition.getReviewIds().stream()
                .map(v -> v.getValue())
                .collect(Collectors.toList());

        this.genreIds = condition.getGenreIds().stream()
                .map(v -> v.getValue())
                .collect(Collectors.toList());
    }
}
