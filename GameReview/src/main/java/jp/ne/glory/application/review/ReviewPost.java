package jp.ne.glory.application.review;

import java.util.Optional;
import java.util.function.Supplier;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import jp.ne.glory.application.datetime.DateTimeCalculator;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.common.validate.ValidateRule;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.repository.GameRepository;
import jp.ne.glory.domain.game.validate.GameEditValidateRule;
import jp.ne.glory.domain.game.validate.GameModifyCommonValidateRule;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.repository.ReviewRepository;
import jp.ne.glory.domain.review.validate.ReviewEditValidateRule;
import jp.ne.glory.domain.review.validate.ReviewModifyCommonValidateRule;
import jp.ne.glory.domain.review.value.LastUpdateDateTime;
import jp.ne.glory.domain.review.value.PostDateTime;
import jp.ne.glory.domain.review.value.ReviewId;

/**
 * レビュー投稿処理.
 *
 * @author Junki Yamada
 */
@RequestScoped
public class ReviewPost {

    /**
     * レビューリポジトリ.
     */
    private final ReviewRepository reviewRepository;

    /**
     * ゲームリポジトリ.
     */
    private final GameRepository gameRepository;

    /**
     * 日付計算.
     */
    private final DateTimeCalculator calculator;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     *
     */
    @Deprecated
    ReviewPost() {
        this.reviewRepository = null;
        this.gameRepository = null;
        this.calculator = null;
    }

    /**
     * コンストラクタ.
     *
     * @param paramReviewRep レビューリポジトリ
     * @param paramGameRep ゲームリポジトリ
     * @param paramCalculator 日付計算
     */
    @Inject
    public ReviewPost(final ReviewRepository paramReviewRep, final GameRepository paramGameRep, final DateTimeCalculator paramCalculator) {

        reviewRepository = paramReviewRep;
        gameRepository = paramGameRep;
        calculator = paramCalculator;
    }

    /**
     * レビューを投稿する.
     *
     * @param review レビュー
     * @return レビュー投稿結果
     */
    public ReviewPostResult post(final Review review) {

        final Optional<Game> game = gameRepository.findBy(review.getGameId());
        final Supplier<ValidateErrors> checkFunc = () -> {

            final ValidateRule rule = crateReviewRule(review, game);
            return rule.validate();
        };

        return executePosting(review, checkFunc);
    }

    /**
     * ゲームと登録と合わせてレビューを投稿する.
     *
     * @param review レビュー
     * @param game ゲーム
     * @return レビュー登録結果
     */
    public ReviewPostResult postWithGame(final Review review, final Game game) {

        final Supplier<ValidateErrors> checkFunc = () -> {

            final Optional<Game> gameOption = Optional.ofNullable(game);
            final ValidateRule rule = crateReviewRule(review, gameOption);
            final ValidateErrors errors = rule.validate();

            gameOption.ifPresent(v -> {
                final GameModifyCommonValidateRule gameRule = new GameModifyCommonValidateRule(v);
                errors.addAll(gameRule.validate());
            });

            return errors;
        };

        return executePostingWithGame(review, game, checkFunc);
    }

    /**
     * レビューを再投稿する.
     *
     * @param review レビュー
     * @return レビュー登録結果
     */
    public ReviewPostResult repost(final Review review) {

        final Optional<Game> game = gameRepository.findBy(review.getGameId());
        final Supplier<ValidateErrors> checkFunc = () -> {

            final ValidateRule rule = crateReviewRule(review, game);
            return rule.validate();
        };

        return executePosting(review, checkFunc);
    }

    /**
     * ゲームの編集と同時にレビューを再投稿する.
     *
     * @param review レビュー
     * @param game ゲーム
     * @return レビュー登録結果
     */
    public ReviewPostResult repostWithGame(final Review review, final Game game) {

        final Supplier<ValidateErrors> checkFunc = () -> {

            final Optional<Game> gameOption = Optional.ofNullable(game);
            final ValidateRule rule = crateReviewRule(review, gameOption);
            final ValidateErrors errors = rule.validate();

            gameOption.ifPresent(v -> {
                final GameEditValidateRule gameRule = new GameEditValidateRule(v);
                errors.addAll(gameRule.validate());
            });

            return errors;
        };

        return executePostingWithGame(review, game, checkFunc);
    }

    /**
     * レビューの入力ルールオブジェクトを作成する.
     *
     * @param review レビュー
     * @param game ゲーム（Optional）
     * @return 入力ルールオブジェクト
     */
    private ValidateRule crateReviewRule(final Review review, final Optional<Game> game) {

        if (review.isRegistered()) {

            return new ReviewEditValidateRule(review, game.orElse(null));
        }

        return new ReviewModifyCommonValidateRule(review, game.orElse(null));
    }

    /**
     * レビューの投稿の保存処理を行う.
     *
     * @param review レビュー
     * @param checkFunc 入力チェック関数
     * @return 投稿結果
     */
    private ReviewPostResult executePosting(final Review review, final Supplier<ValidateErrors> checkFunc) {

        final ValidateErrors errors = checkFunc.get();

        if (errors.hasError()) {

            return new ReviewPostResult(errors, ReviewId.notNumberingValue());
        }

        final ReviewId reviewId = reviewRepository.save(createPostingReview(review));
        return new ReviewPostResult(errors, reviewId);
    }

    /**
     * *
     * レビュー/ゲームの投稿の保存処理を行う.
     *
     * @param review レビュー
     * @param game ゲーム
     * @param checkFunc 入力チェック関数
     * @return 投稿結果
     */
    private ReviewPostResult executePostingWithGame(final Review review, final Game game, final Supplier<ValidateErrors> checkFunc) {

        final ValidateErrors errors = checkFunc.get();

        if (errors.hasError()) {

            return new ReviewPostResult(errors, ReviewId.notNumberingValue());
        }

        gameRepository.save(game);

        final ReviewId reviewId = reviewRepository.save(createPostingReview(review));
        return new ReviewPostResult(errors, reviewId);
    }

    /**
     * 入力したレビューから更新用のレビューを作成する.
     *
     * @param inputReview 入力したレビュー
     * @return 投稿用のレビュー
     */
    private Review createPostingReview(final Review inputReview) {

        final Review postinReview = reviewRepository.findBy(inputReview.getId()).orElse(new Review(ReviewId.notNumberingValue()));
        postinReview.setGameId(inputReview.getGameId());
        postinReview.setScore(inputReview.getScore());
        postinReview.setGoodPoint(inputReview.getGoodPoint());
        postinReview.setBadPoint(inputReview.getBadPoint());
        postinReview.setComment(inputReview.getComment());

        final DateTimeValue currenteDateTime = calculator.getCurrentDateTime();
        postinReview.setLastUpdate(new LastUpdateDateTime(currenteDateTime));

        if (!inputReview.isRegistered()) {

            postinReview.setPostTime(new PostDateTime(currenteDateTime));
        }

        return postinReview;
    }
}
