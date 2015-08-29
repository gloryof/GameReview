package jp.ne.glory.ui.admin.review;

import java.util.List;
import javax.ws.rs.FormParam;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.ScorePoint;
import lombok.Getter;
import lombok.Setter;

/**
 * スコアBean.
 *
 * @author Junki Yamada.
 */
public class ScoreBean {

    /**
     * 選択可能なスコア.
     */
    @Getter
    private final List<ScorePoint> selectableScorePoints = ScorePoint.getSelectableScores();

    /**
     * 熱中度
     */
    @Getter
    @Setter
    @FormParam("score.addiction")
    private ScorePoint addiction = ScorePoint.NotInput;

    /**
     * ストーリー.
     */
    @Getter
    @Setter
    @FormParam("score.story")
    private ScorePoint story = ScorePoint.NotInput;

    /**
     * 操作性.
     */
    @Getter
    @Setter
    @FormParam("score.operability")
    private ScorePoint operability = ScorePoint.NotInput;

    /**
     * ロード時間.
     */
    @Getter
    @Setter
    @FormParam("score.loadTime")
    private ScorePoint loadTime = ScorePoint.NotInput;

    /**
     * 音楽
     */
    @Getter
    @Setter
    @FormParam("score.music")
    private ScorePoint music = ScorePoint.NotInput;

    /**
     * コンストラクタ.
     */
    public ScoreBean() {

        super();
    }

    /**
     * コンストラクタ.
     *
     * @param score スコア
     */
    public ScoreBean(final Score score) {

        addiction = score.getAddiction();
        story = score.getStory();
        operability = score.getOperability();
        loadTime = score.getLoadTime();
        music = score.getMusic();
    }
}
