package jp.ne.glory.application.review;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.review.repository.ReviewRepository;
import jp.ne.glory.domain.review.value.ReviewId;
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
     * lotPerCounttの数だけデータを取得する。<br>
     * lotNumberに全体の何ロット目を取得するかを設定する。<br>
     * 投稿日時の降順でソートされる。
     *
     * @param lotPerCount ロット内のカウント
     * @param lotNumber ロット数
     * @return レビュー検索結果
     */
    public ReviewSearchResults searchNewReviews(final int lotPerCount, final int lotNumber) {

        final ReviewSearchCondition condition = new ReviewSearchCondition();
        condition.setLotNumber(lotNumber);
        condition.setLotPerCount(lotPerCount);
        condition.setTargetCount(lotPerCount);
        condition.setOrderType(ReviewSearchOrderType.PostTimeDesc);

        final List<ReviewSearchResult> resultList = repository.search(condition);
        final int resultCount = repository.getSearchCount(condition);

        return new ReviewSearchResults(condition, resultList, resultCount);
    }

    /**
     * ジャンルIDで検索を行う.<br>
     * lotPerCounttの数だけデータを取得する。<br>
     * lotNumberに全体の何ロット目を取得するかを設定する。<br>
     * 投稿日時の降順でソートされる。
     *
     * @param genreId ジャンルID
     * @param lotPerCount ロット内のカウント
     * @param lotNumber ロット数
     * @return レビュー検索結果
     */
    public ReviewSearchResults searchReviewByGenre(final GenreId genreId, final int lotPerCount, final int lotNumber) {

        final ReviewSearchCondition condition = new ReviewSearchCondition();
        condition.setLotNumber(lotNumber);
        condition.setLotPerCount(lotPerCount);
        condition.getGenreIds().add(genreId);
        condition.setOrderType(ReviewSearchOrderType.PostTimeDesc);

        final List<ReviewSearchResult> resultList = repository.search(condition);
        final int resultCount = repository.getSearchCount(condition);

        return new ReviewSearchResults(condition, resultList, resultCount);
    }

    /**
     * レビューIDで検索を行う.
     *
     * @param reviewId レビューId
     * @return レビュー検索結果
     */
    public ReviewSearchResults searchByReviewId(final ReviewId reviewId) {

        final ReviewSearchCondition condition = new ReviewSearchCondition();
        condition.setLotNumber(1);
        condition.setLotPerCount(1);
        condition.getReviewIds().add(reviewId);
        condition.setOrderType(ReviewSearchOrderType.PostTimeDesc);

        final List<ReviewSearchResult> resultList = repository.search(condition);
        final int resultCount = resultList.size();

        return new ReviewSearchResults(condition, resultList, resultCount);
    }
}
