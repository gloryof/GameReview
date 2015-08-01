package jp.ne.glory.domain.review.entity;

import jp.ne.glory.domain.game.value.GameId;
import jp.ne.glory.domain.review.value.BadPoint;
import jp.ne.glory.domain.review.value.Comment;
import jp.ne.glory.domain.review.value.GoodPoint;
import jp.ne.glory.domain.review.value.PostDateTime;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.Score;
import lombok.Getter;
import lombok.Setter;

/**
 * レビュー.
 * @author Junki Yamada
 */
public class Review {

    /** ラベル. */
    public static final String LABEL = "レビュー";

    /** ID. */
    @Getter
    private final ReviewId id;

    /**
     * ゲームID
     */
    @Getter
    @Setter
    private GameId gameId;

    /**
     * 良い点.
     */
    @Getter
    @Setter
    private GoodPoint goodPoint = GoodPoint.empty();

    /** 悪い点. */
    @Getter
    @Setter
    private BadPoint badPoint = BadPoint.empty();

    /** コメント. */
    @Getter
    @Setter
    private Comment comment = Comment.empty();

    /** スコア. */
    @Getter
    @Setter
    private Score score = new Score();

    /**
     * 投稿日時.
     */
    @Getter
    @Setter
    private PostDateTime postTime = PostDateTime.empty();

    /**
     * コンストラクタ.
     * @param paramId ID 
     */
    public Review(final ReviewId paramId) {

        id = paramId;
    }

    /**
     * 登録済みのレビューかを判定する.
     * @return 登録済みの場合：true、未登録の場合：false
     */
    public boolean isRegistered() {
        
        return id.isSetValue();
    }
}
