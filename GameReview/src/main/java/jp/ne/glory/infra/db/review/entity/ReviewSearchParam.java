package jp.ne.glory.infra.db.review.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import jp.ne.glory.common.type.DateValue;
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
    @Getter
    private final List<Long> reviewIds;

    /**
     * ジャンルIDリスト.
     */
    @Getter
    private final List<Long> genreIds;

    /**
     * 検索日付From.
     */
    private LocalDateTime from = null;

    /**
     * 検索日付to.
     */
    private LocalDateTime to = null;

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

        final DateValue fromDate = condition.getPostedFrom();
        if (fromDate != null && fromDate.getValue() != null) {

            this.from = fromDate.getValue().atTime(LocalTime.MIDNIGHT);
        }

        final DateValue toDate = condition.getPostedTo();
        if (toDate != null && toDate.getValue() != null) {

            this.to = toDate.getValue().plusDays(1).atTime(LocalTime.MIDNIGHT);
        }
    }
}
