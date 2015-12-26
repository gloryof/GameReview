package jp.ne.glory.web.admin.review;

import java.net.URI;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.game.GameSearch;
import jp.ne.glory.application.genre.GenreSearch;
import jp.ne.glory.application.review.ReviewPost;
import jp.ne.glory.application.review.ReviewPostResult;
import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.game.entity.Game;
import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.genre.entity.Genre;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.BadPoint;
import jp.ne.glory.domain.review.value.Comment;
import jp.ne.glory.domain.review.value.GoodPoint;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;
import jp.ne.glory.infra.certify.CertifyTarget;
import jp.ne.glory.ui.admin.review.ReviewBean;
import jp.ne.glory.ui.admin.review.ReviewEditView;
import jp.ne.glory.ui.admin.review.ScoreBean;
import jp.ne.glory.web.admin.game.GameDetail;
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * レビュー作成する.
 *
 * @author Junki Yamada
 */
@RequestScoped
@CertifyTarget
@Path("/admin/review/post/{gameId}")
public class ReviewCreate {

    /**
     * レビュー投稿.
     */
    private final ReviewPost post;

    /**
     * ゲーム検索.
     */
    private final GameSearch gameSearch;

    /**
     * ジャンル検索.
     */
    private final GenreSearch genreSearch;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    protected ReviewCreate() {

        this.post = null;
        this.gameSearch = null;
        this.genreSearch = null;
    }

    /**
     * コンストラクタ.
     *
     * @param post レビュー投稿
     * @param gameSearch ゲーム検索
     * @param genreSearch ジャンル検索
     */
    @Inject
    public ReviewCreate(final ReviewPost post, final GameSearch gameSearch, final GenreSearch genreSearch) {

        this.post = post;
        this.gameSearch = gameSearch;
        this.genreSearch = genreSearch;
    }

    /**
     * 作成画面を表示する.
     *
     * @param paramGameId ゲームID
     * @return 作成画面
     */
    @GET
    @Transactional(Transactional.TxType.REQUIRED)
    public Response view(@PathParam("gameId") final Long paramGameId) {

        final Optional<ReviewSearchResult> reviewOpt = createBaseReivew(paramGameId);

        if (!reviewOpt.isPresent()) {

            return redirectNotFoundError(paramGameId);
        }

        final ReviewEditView view = new ReviewEditView(reviewOpt.get());
        final Viewable viewable = new Viewable(PagePaths.REVIEW_CREATE, view);

        return Response.ok(viewable).build();
    }

    /**
     * 作成を完了する.
     *
     * @param paramGameId ゲームID
     * @param postData 入寮データ
     * @return 作成に成功した場合：詳細画面、入力チェックエラーの場合：レビュー投稿画面
     */
    @POST
    @Transactional(Transactional.TxType.REQUIRED)
    public Response create(@PathParam("gameId") final Long paramGameId, @BeanParam final ReviewBean postData) {

        if (!isGameIdMatched(paramGameId, postData)) {

            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final Optional<Game> optGame = gameSearch.searchBy(new GameId(paramGameId));
        if (!optGame.isPresent()) {

            return redirectNotFoundError(paramGameId);
        }

        final ReviewPostResult postResult = finishCreate(postData);
        final ValidateErrors errors = postResult.getErrors();

        if (errors.hasError()) {

            Optional<ReviewSearchResult> baseReivew = createBaseReivew(paramGameId);
            return buildOkToEditView(baseReivew.get(), postData, errors);
        }

        return redirectView(postResult.getPostedReviewId().getValue());
    }

    /**
     * ゲームが見つからないエラー画面に遷移する.
     *
     * @param gameId ゲームID
     * @return リダイレクトレスポンス
     */
    private Response redirectNotFoundError(final long gameId) {

        final String base = UriBuilder.fromResource(GameDetail.class).toTemplate();
        final String append = UriBuilder.fromMethod(GameDetail.class, "notFound").toTemplate();
        final URI uri = UriBuilder.fromUri(base + append).build(gameId);

        return Response.seeOther(uri).build();
    }

    /**
     * 作成を完了する.
     *
     * @param inputData 入力データ
     * @return 投稿結果
     */
    private ReviewPostResult finishCreate(final ReviewBean inputData) {

        final Review updatedReview = new Review(ReviewId.notNumberingValue());
        updatedReview.setGameId(new GameId(inputData.getGameId()));

        final Score updatedScore = new Score();
        final ScoreBean inputScore = inputData.getScore();
        updatedScore.setAddiction(inputScore.getAddiction());
        updatedScore.setLoadTime(inputScore.getLoadTime());
        updatedScore.setMusic(inputScore.getMusic());
        updatedScore.setOperability(inputScore.getOperability());
        updatedScore.setStory(inputScore.getStory());

        updatedReview.setScore(updatedScore);

        updatedReview.setGoodPoint(new GoodPoint(inputData.getGoodPoint()));
        updatedReview.setBadPoint(new BadPoint(inputData.getBadPoint()));
        updatedReview.setComment(new Comment(inputData.getComment()));

        return post.post(updatedReview);
    }

    /**
     * 入力画面を表示する.<br>
     * ステータスはOK.
     *
     * @baseData 表示元データ
     * @param postData 投稿データ
     * @param errors エラー情報
     * @return OKレスポンス
     */
    private Response buildOkToEditView(final ReviewSearchResult baseData,
            final ReviewBean postData, final ValidateErrors errors) {

        final ReviewEditView view = new ReviewEditView(baseData);

        view.getErrors().addAll(errors);
        view.getReview().setScore(postData.getScore());
        view.getReview().setGoodPoint(postData.getGoodPoint());
        view.getReview().setBadPoint(postData.getBadPoint());
        view.getReview().setComment(postData.getComment());

        final Viewable viewable = new Viewable(PagePaths.REVIEW_CREATE, view);

        return Response.ok(viewable).build();
    }

    /**
     * 表示画面にリダイレクトする.
     *
     * @param paramReviewId レビューID
     * @return リダイレクトレスポンス
     */
    private Response redirectView(final long paramReviewId) {

        final String urlTemplte = UriBuilder.fromResource(ReviewDetail.class).toTemplate();
        final URI uri = UriBuilder.fromUri(urlTemplte).resolveTemplate("reviewId", paramReviewId).build();

        return Response.seeOther(uri).build();
    }

    /**
     * 入力のベースとなるレビューを作成する.<br>
     * ゲームとジャンルの情報を取得する。
     *
     * @param paramGameId ゲームID
     * @return レビュー
     */
    private Optional<ReviewSearchResult> createBaseReivew(final long paramGameId) {

        final Optional<Game> gameOpt = gameSearch.searchBy(new GameId(paramGameId));

        if (!gameOpt.isPresent()) {

            return Optional.empty();
        }

        final Review newReivew = new Review(ReviewId.notNumberingValue());
        final Game game = gameOpt.get();
        final Genre genre = genreSearch.searchBy(game.getGenreId()).get();

        final ReviewSearchResult review = new ReviewSearchResult(newReivew, game, genre);

        return Optional.of(review);
    }

    /**
     * ゲームIDが一致してるかを判定する.
     *
     * @param urlGameId URLゲームID
     * @param postData 入力データ
     * @return 一致している場合：true、一致していない場合：false
     */
    private boolean isGameIdMatched(final Long urlGameId, final ReviewBean postData) {

        if (urlGameId == null || postData.getGameId() == null) {

            return false;
        }

        if (!urlGameId.equals(postData.getGameId())) {

            return false;
        }

        return true;
    }
}
