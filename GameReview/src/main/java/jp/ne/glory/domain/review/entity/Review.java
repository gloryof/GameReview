/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ne.glory.domain.review.entity;

import jp.ne.glory.domain.common.error.ValidateErrors;
import jp.ne.glory.domain.common.type.Validatable;
import jp.ne.glory.domain.review.value.BadPoint;
import jp.ne.glory.domain.review.value.Comment;
import jp.ne.glory.domain.review.value.GoodPoint;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.Score;

/**
 * レビュー.
 * @author Junki Yamada
 */
public class Review {

    /** ラベル. */
    public static final String LABEL = "レビュー";

    /** ID. */
    public final ReviewId id;

    /** 良い点. */
    public GoodPoint gooodPoint = GoodPoint.empty();

    /** 悪い点. */
    public BadPoint badPoint = BadPoint.empty();

    /** コメント. */
    public Comment comment = Comment.empty();

    /** スコア. */
    public Score score = new Score();

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
        
        return id.isSetValue;
    }
}
