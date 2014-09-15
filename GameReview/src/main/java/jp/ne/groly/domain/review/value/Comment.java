package jp.ne.groly.domain.review.value;

import jp.ne.groly.domain.common.annotation.Required;
import jp.ne.groly.domain.common.type.InputText;

/**
 * コメント.
 * @author Junki Yamada
 */
@Required(label = Comment.LABEL)
public class Comment extends InputText {

    /** ラベル. */
    public static final String LABEL = "コメント";
    
    /**
     * コンストラクタ.<br>
     * 値を設定する
     * @param paramValue 値 
     */
    public Comment(final String paramValue) {

        super(paramValue);
    }

    /**
     * 空の値オブジェクトを返却する.
     * @return コメント
     */
    public static Comment empty() {

        return new Comment("");
    }
}
