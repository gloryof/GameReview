package jp.ne.glory.infra.db.review.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.CeroRating;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.game.value.SiteUrl;
import jp.ne.glory.domain.game.value.Title;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.genre.value.GenreId;
import jp.ne.glory.domain.genre.value.GenreName;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.repository.ReviewRepository;
import jp.ne.glory.domain.review.value.BadPoint;
import jp.ne.glory.domain.review.value.Comment;
import jp.ne.glory.domain.review.value.GoodPoint;
import jp.ne.glory.domain.review.value.LastUpdateDateTime;
import jp.ne.glory.domain.review.value.PostDateTime;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.ScorePoint;
import jp.ne.glory.domain.review.value.search.ReviewSearchCondition;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.infra.db.review.dao.ReviewBaseInfoDao;
import jp.ne.glory.infra.db.review.dao.ReviewScoreDao;
import jp.ne.glory.infra.db.review.dao.ReviewSearchDao;
import jp.ne.glory.infra.db.review.entity.ReviewBaseInfo;
import jp.ne.glory.infra.db.review.entity.ReviewInfo;
import jp.ne.glory.infra.db.review.entity.ReviewScore;
import jp.ne.glory.infra.db.review.entity.ReviewSearchParam;
import jp.ne.glory.infra.db.review.entity.ReviewSearchRow;

/**
 * レビューリポジトリ.
 *
 * @author Junki Yamada
 */
@ApplicationScoped
public class ReviewRepositoryImpl implements ReviewRepository {

    /**
     * レビュー検索DAO.
     */
    private final ReviewSearchDao searchDao;

    /**
     * レビューDAO.
     */
    private final ReviewBaseInfoDao reviewDao;

    /**
     * 点数DAO.
     */
    private final ReviewScoreDao scoreDao;

    /**
     * コンストラクタ.
     *
     * @param searchDao レビュー検索DAO
     * @param reviewDao レビューDAO
     * @param scoreDao 点数DAO
     */
    @Inject
    public ReviewRepositoryImpl(final ReviewSearchDao searchDao, final ReviewBaseInfoDao reviewDao,
            final ReviewScoreDao scoreDao) {

        this.searchDao = searchDao;
        this.reviewDao = reviewDao;
        this.scoreDao = scoreDao;
    }

    /**
     * レビューを保存する.
     *
     * @param review レビュー
     * @return 保存したレビュー情報
     */
    @Override
    public ReviewId save(final Review review) {

        final ReviewId savedId;
        if (review.isRegistered()) {

            savedId = review.getId();
            updateReveiwBaseInfo(review);
            updateReviewScore(savedId, review.getScore());
        } else {

            final long reviewIdValue = reviewDao.getSequence();
            savedId = new ReviewId(reviewIdValue);

            insertReveiwBaseInfo(savedId, review);
            insertReviewScore(savedId, review.getScore());
        }

        return savedId;
    }

    /**
     * レビューIDをキーに検索をする.
     *
     * @param reviewId レビューID
     * @return レビュー
     */
    @Override
    public Optional<Review> findBy(ReviewId reviewId) {

        return searchDao.searchInfo(reviewId.getValue())
                .map(this::convertReview);
    }

    /**
     * レビューの検索を行う.
     *
     * @param condition 検索条件
     * @return 検索結果リスト
     */
    @Override
    public List<ReviewSearchResult> search(ReviewSearchCondition condition) {

        return searchDao.search(new ReviewSearchParam(condition)).stream()
                .map(this::convertResult)
                .collect(Collectors.toList());
    }

    /**
     * 結果の件数を取得する.
     *
     * @param condition 検索条件
     * @return 件数
     */
    @Override
    public int getSearchCount(final ReviewSearchCondition condition) {

        return search(condition).size();
    }

    /**
     * レビュー情報を登録する.
     *
     * @param newId 新しいレビューID
     * @param review レビュー
     */
    private void insertReveiwBaseInfo(final ReviewId newId, final Review review) {

        final ReviewBaseInfo baseInfo = new ReviewBaseInfo();
        baseInfo.setReviewId(newId.getValue());
        baseInfo.setGameId(review.getGameId().getValue());
        baseInfo.setGoodPoint(review.getGoodPoint().getValue());
        baseInfo.setBadPoint(review.getBadPoint().getValue());
        baseInfo.setComment(review.getComment().getValue());
        baseInfo.setPostTime(review.getPostTime().getValue().getValue());
        baseInfo.setLastUpdate(review.getLastUpdate().getValue().getValue());
        baseInfo.setLockUpdateTimestamp(LocalDateTime.now());

        reviewDao.insert(baseInfo);
    }

    /**
     * レビュー情報を更新する.
     *
     * @param review レビュー
     */
    private void updateReveiwBaseInfo(final Review review) {

        final ReviewBaseInfo baseInfo = reviewDao.selectById(review.getId().getValue());
        baseInfo.setGameId(review.getGameId().getValue());
        baseInfo.setGoodPoint(review.getGoodPoint().getValue());
        baseInfo.setBadPoint(review.getBadPoint().getValue());
        baseInfo.setComment(review.getComment().getValue());
        baseInfo.setPostTime(review.getPostTime().getValue().getValue());
        baseInfo.setLastUpdate(review.getLastUpdate().getValue().getValue());
        baseInfo.setLockUpdateTimestamp(LocalDateTime.now());

        reviewDao.update(baseInfo);
    }

    /**
     * スコアを登録する.
     *
     * @param newId 新しいレビューID
     * @param score 点数
     */
    private void insertReviewScore(final ReviewId newId, final Score score) {

        final ReviewScore reviewScore = new ReviewScore();
        reviewScore.setReviewId(newId.getValue());
        reviewScore.setAddiction(score.getAddiction().typeId);
        reviewScore.setLoadTime(score.getLoadTime().typeId);
        reviewScore.setMusic(score.getMusic().typeId);
        reviewScore.setOperability(score.getOperability().typeId);
        reviewScore.setStory(score.getStory().typeId);

        scoreDao.insert(reviewScore);
    }

    /**
     * スコアを登録する.
     *
     * @param reviewId 更新対象のID
     * @param score 点数
     */
    private void updateReviewScore(final ReviewId reviewId, final Score score) {

        final ReviewScore reviewScore = scoreDao.selectById(reviewId.getValue());
        reviewScore.setAddiction(score.getAddiction().typeId);
        reviewScore.setLoadTime(score.getLoadTime().typeId);
        reviewScore.setMusic(score.getMusic().typeId);
        reviewScore.setOperability(score.getOperability().typeId);
        reviewScore.setStory(score.getStory().typeId);

        scoreDao.update(reviewScore);
    }

    /**
     * 検索結果をドメインのエンティティに変換する.
     *
     * @param row 検索結果行
     * @return エンティティ
     */
    private ReviewSearchResult convertResult(final ReviewSearchRow row) {

        return new ReviewSearchResult(convertReview(row), convertGame(row), convertGenre(row));
    }

    /**
     * レビューエンティティに変換する.
     *
     * @param row 検索結果行
     * @return エンティティ
     */
    private Review convertReview(final ReviewSearchRow row) {

        final Review review = new Review(new ReviewId(row.getReviewId()));
        review.setGameId(new GameId(row.getGameId()));
        review.setGoodPoint(new GoodPoint(row.getGoodPoint()));
        review.setBadPoint(new BadPoint(row.getBadPoint()));
        review.setComment(new Comment(row.getComment()));
        review.setScore(convertScore(row));
        review.setPostTime(new PostDateTime(new DateTimeValue(row.getPostTime())));
        review.setLastUpdate(new LastUpdateDateTime(new DateTimeValue(row.getLastUpdate())));

        return review;
    }

    /**
     * レビューエンティティに変換する.
     *
     * @param reviewInfo レビュー情報
     * @return エンティティ
     */
    private Review convertReview(final ReviewInfo reviewInfo) {

        final Review review = new Review(new ReviewId(reviewInfo.getReviewId()));
        review.setGameId(new GameId(reviewInfo.getGameId()));
        review.setGoodPoint(new GoodPoint(reviewInfo.getGoodPoint()));
        review.setBadPoint(new BadPoint(reviewInfo.getBadPoint()));
        review.setComment(new Comment(reviewInfo.getComment()));
        review.setScore(convertScore(reviewInfo));
        review.setPostTime(new PostDateTime(new DateTimeValue(reviewInfo.getPostTime())));
        review.setLastUpdate(new LastUpdateDateTime(new DateTimeValue(reviewInfo.getLastUpdate())));

        return review;
    }

    /**
     * ゲームエンティティに変換する.
     *
     * @param row 検索結果行
     * @return エンティティ
     */
    private Game convertGame(final ReviewSearchRow row) {

        final Game game = new Game(new GameId(row.getGameId()));
        game.setTitle(new Title(row.getTitle()));
        game.setUrl(new SiteUrl(row.getSiteUrl()));
        game.setCeroRating(CeroRating.fromId(row.getCeroId()));
        game.setGenreId(new GenreId(row.getGenreId()));

        return game;
    }

    /**
     * ジャンルエンティティに変換する
     *
     * @param row 結果検索行
     * @return エンティティ
     */
    private Genre convertGenre(final ReviewSearchRow row) {

        return new Genre(new GenreId(row.getGenreId()), new GenreName(row.getGenreName()));
    }

    /**
     * 点数に変換する.
     *
     * @param row 検索結果行
     * @return 点数
     */
    private Score convertScore(final ReviewSearchRow row) {

        final Score score = new Score();
        score.setAddiction(ScorePoint.getByTypeId(row.getAddiction()));
        score.setStory(ScorePoint.getByTypeId(row.getStory()));
        score.setOperability(ScorePoint.getByTypeId(row.getOperability()));
        score.setLoadTime(ScorePoint.getByTypeId(row.getLoadTime()));
        score.setMusic(ScorePoint.getByTypeId(row.getMusic()));

        return score;
    }

    /**
     * 点数に変換する.
     *
     * @param reviewInfo レビュー情報
     * @return 点数
     */
    private Score convertScore(final ReviewInfo reviewInfo) {

        final Score score = new Score();
        score.setAddiction(ScorePoint.getByTypeId(reviewInfo.getAddiction()));
        score.setStory(ScorePoint.getByTypeId(reviewInfo.getStory()));
        score.setOperability(ScorePoint.getByTypeId(reviewInfo.getOperability()));
        score.setLoadTime(ScorePoint.getByTypeId(reviewInfo.getLoadTime()));
        score.setMusic(ScorePoint.getByTypeId(reviewInfo.getMusic()));

        return score;
    }
}
