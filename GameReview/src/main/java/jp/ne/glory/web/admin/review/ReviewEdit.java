
package jp.ne.glory.web.admin.review;

import java.net.URI;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import jp.ne.glory.application.review.ReviewPost;
import jp.ne.glory.application.review.ReviewPostResult;
import jp.ne.glory.application.review.ReviewSearch;
import jp.ne.glory.domain.common.error.ValidateErrors;
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
import jp.ne.glory.web.common.PagePaths;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * レビュー編集.
 *
 * @author Junki Yamada
 */
@CertifyTarget
@RequestScoped
@Path("/admin/review/{reviewId}/edit")
public class ReviewEdit {

    /**
     * レビュー検索.
     */
    private final ReviewSearch search;

    /**
     * レビュー投稿.
     */
    private final ReviewPost post;

    /**
     * コンストラクタ.<br>
     * CDIの仕様（？）でRequestScopeの場合用意する必要があったため作成。<br>
     */
    @Deprecated
    protected ReviewEdit() {

        this.search = null;
        this.post = null;
    }

    /**
     * コンストラクタ.
     *
     * @param search レビュー検索
     * @param post レビュー投稿
     */
    @Inject
    public ReviewEdit(final ReviewSearch search, final ReviewPost post) {

        this.search = search;
        this.post = post;
    }

    /**
     * 編集画面表示.
     *
     * @param paramReviewId レビューID
     * @return レスポンス
     */
    @GET
    public Response view(@PathParam("reviewId") final long paramReviewId) {

        final Optional<ReviewSearchResult> results = search.searchByReviewId(new ReviewId(paramReviewId));

        if (!results.isPresent()) {

            return redirectNotFound(paramReviewId);
        }

        final ReviewSearchResult result = results.get();
        final ReviewEditView view = new ReviewEditView(result);
        final Viewable viewable = new Viewable(PagePaths.REVIEW_EDIT, view);

        return Response.ok(viewable).build();
    }

    /**
     * 編集を完了する.
     *
     * @param paramReviewId レビューID
     * @param postedReview 投稿内容
     * @return レスポンス
     */
    @POST
    public Response completeEdit(@PathParam("reviewId") final long paramReviewId, @BeanParam final ReviewBean postedReview) {

        if (postedReview.getReviewId() == null || postedReview.getReviewId() != paramReviewId) {

            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final ReviewId reviewId = new ReviewId(paramReviewId);
        final Optional<ReviewSearchResult> optResult = search.searchByReviewId(reviewId);

        if (!optResult.isPresent()) {

            return redirectNotFound(paramReviewId);
        }

        final ReviewPostResult finishResult = finishEdit(optResult.get().getReview(), postedReview);
        final ValidateErrors errors = finishResult.getErrors();

        if (errors.hasError()) {

            return buildOkToEditView(optResult.get(), postedReview, errors);
        }

        return redirectView(paramReviewId);
    }

    /**
     * 編集を終了する.
     *
     * @param baseReview ベースとなるレビュー
     * @param postReview 更新後のレビュー
     * @return 更新結果
     */
    private ReviewPostResult finishEdit(final Review baseReview, final ReviewBean postReview) {

        final Review updatedReview = new Review(baseReview.getId());
        updatedReview.setGameId(baseReview.getGameId());

        final Score updatedScore = new Score();
        final ScoreBean inputScore = postReview.getScore();
        updatedScore.setAddiction(inputScore.getAddiction());
        updatedScore.setLoadTime(inputScore.getLoadTime());
        updatedScore.setMusic(inputScore.getMusic());
        updatedScore.setOperability(inputScore.getOperability());
        updatedScore.setStory(inputScore.getStory());

        updatedReview.setScore(updatedScore);

        updatedReview.setGoodPoint(new GoodPoint(postReview.getGoodPoint()));
        updatedReview.setBadPoint(new BadPoint(postReview.getBadPoint()));
        updatedReview.setComment(new Comment(postReview.getComment()));

        return post.repost(updatedReview);
    }

    /**
     * レビューが見つからない画面にリダイレクトする.
     *
     * @param reviewId レビューID
     * @return リダイレクトレスポンス
     */
    private Response redirectNotFound(final long reviewId) {

        final String base = UriBuilder.fromResource(ReviewDetail.class).toTemplate();
        final String append = UriBuilder.fromMethod(ReviewDetail.class, "notFound").toTemplate();

        final UriBuilder builder = UriBuilder.fromUri(base + append);
        builder.resolveTemplate("reviewId", reviewId);

        final URI uri = builder.build();

        return Response.seeOther(uri).build();
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
     * 編集画面を表示する.<br>
     * ステータスはOK.
     *
     * @baseData 表示元データ
     * @param postData 投稿データ
     * @param errors エラー情報
     * @return OKレスポンス
     */
    private Response buildOkToEditView(final ReviewSearchResult baseData, final ReviewBean postData, final ValidateErrors errors) {

        final ReviewEditView view = new ReviewEditView(baseData);

        view.getErrors().addAll(errors);
        view.getReview().setScore(postData.getScore());
        view.getReview().setGoodPoint(postData.getGoodPoint());
        view.getReview().setBadPoint(postData.getBadPoint());
        view.getReview().setComment(postData.getComment());

        final Viewable viewable = new Viewable(PagePaths.REVIEW_EDIT, view);

        return Response.ok(viewable).build();
    }

}
