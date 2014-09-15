package jp.ne.groly.domain.review.value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jp.ne.groly.domain.common.error.ErrorInfo;
import jp.ne.groly.domain.common.error.ValidateError;
import jp.ne.groly.domain.common.error.ValidateErrors;
import jp.ne.groly.domain.common.type.Validatable;

/**
 * スコア.
 * @author Junki Yamada
 */
public class Score implements Validatable {

    /** ラベル. */
    public static final String LABEL = "スコア";

    /** 熱中度 */
    public ScorePoint addiction =  ScorePoint.NotInput;

    /** ストーリー. */
    public ScorePoint story = ScorePoint.NotInput;

    /** 操作性. */
    public ScorePoint operability = ScorePoint.NotInput;

    /** ロード時間. */
    public ScorePoint loadTime = ScorePoint.NotInput;

    /** 音楽 */
    public ScorePoint music =  ScorePoint.NotInput;

    /**
     * 入力情報の検証を行う.
     * @return 検証結果
     */
    @Override
    public ValidateErrors validate() {

        final ValidateErrors errors =  new ValidateErrors();
        final List<ScorePoint> pointList = toScorePointList();

        if (isAllNotInput(pointList)) {

            errors.add(new ValidateError(ErrorInfo.RequiredSelectOne, LABEL));
        }

        if (isAllExcludes(pointList)) {

            errors.add(new ValidateError(ErrorInfo.SelectedPattern, LABEL, "点数"));
        }

        return errors;
    }

    /**
     * 点数リストに変更する.
     * @return 点数リスト
     */
    private List<ScorePoint> toScorePointList() {

        final List<ScorePoint> pointList = new ArrayList<>();

        pointList.add(addiction);
        pointList.add(loadTime);
        pointList.add(music);
        pointList.add(operability);
        pointList.add(story);

        return Collections.unmodifiableList(pointList);
    }

    /**
     * 全て未入力かを判定する.
     * @param pointList 点数リスト
     * @return 全て未入力の場合：true、入力値がある場合：false
     */
    private boolean isAllNotInput(final List<ScorePoint> pointList) {

        return pointList.stream().allMatch(v -> ScorePoint.NotInput.equals(v));
    }

    /**
     * 全て評価対象外かを判定する.
     * @param pointList 点数リスト
     * @return 全て評価対象外の場合：true、評価対象外がある場合:false
     */
    private boolean isAllExcludes(final List<ScorePoint> pointList) {

        return pointList.stream().allMatch(v -> ScorePoint.Exclued.equals(v));
    }
}
