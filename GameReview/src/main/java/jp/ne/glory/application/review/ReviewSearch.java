package jp.ne.glory.application.review;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.review.repository.ReviewRepository;
import jp.ne.glory.domain.review.value.search.ReviewSearchCondition;
import jp.ne.glory.domain.review.value.search.ReviewSearchOrderType;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.domain.review.value.search.ReviewSearchResults;

/**
 * レビュー検索.
 *
 * @author Junki Yamada.
 */
@RequestScoped
public class ReviewSearch {

    /**
     * レビューリポジトリ.
     */
    private final ReviewRepository repository;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     *
     */
    @Deprecated
    ReviewSearch() {

        this.repository = null;
    }

    /**
     * コンストラクタ.
     *
     * @param repository リポジトリ
     */
    @Inject
    public ReviewSearch(final ReviewRepository repository) {

        this.repository = repository;
    }

    /**
     * 最新のレビューを取得する.<br>
     * countの数だけデータを取得する
     *
     * @param count 取得件数
     * @return レビュー検索結果
     */
    public ReviewSearchResults searchNewReviews(final int count) {

        final ReviewSearchCondition condition = new ReviewSearchCondition();
        condition.targetCount = count;
        condition.orderType = ReviewSearchOrderType.PostTimeDesc;

        final List<ReviewSearchResult> resultList = repository.search(condition);
        final int resultCount = repository.getSearchCount(condition);

        return new ReviewSearchResults(condition, resultList, resultCount);
    }

    /**
     * ジャンルIDで検索を行う.<br>
     * lotPerCounttの数だけデータを取得する。<br>
     * lotNumberに全体の何ロット目を取得するかを設定する。
     *
     * @param genreId ジャンルID
     * @param lotPerCount ロット内のカウント
     * @param lotNumber ロット数
     * @return レビュー検索結果
     */
    public ReviewSearchResults searchReviewByGenre(final GenreId genreId, final int lotPerCount, final int lotNumber) {

        return null;
    }
}
