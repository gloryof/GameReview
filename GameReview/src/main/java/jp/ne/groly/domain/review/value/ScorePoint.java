package jp.ne.groly.domain.review.value;

import java.util.Arrays;
import java.util.Optional;

/**
 * スコア点数
 * @author Junki Yamada
 */
public enum ScorePoint {

    /** 0点. */
    Point0(1, 0, "最悪"),

    /** 1点. */
    Point1(2, 1, ""),

    /** 2点. */
    Point2(3, 2, ""),

    /** 3点. */
    Point3(4, 3, ""),

    /** 4点. */
    Point4(5, 4, ""),

    /** 5点. */
    Point5(6, 5, ""),

    /** 未入力. */
    NotInput(7, 0, ""),

    /** 評価対象外. */
    Exclued(8, 0, "");

    /** タイプID. */
    public final int typeId;

    /** 値. */
    public final int value;

    /** ラベル. */
    public final String label;

    /**
     * コンストラクタ
     * @param paramTypeId タイプID
     * @param paramValue 値
     * @param paramLabel ラベル
     */
    private ScorePoint(final int paramTypeId, final int paramValue, final String paramLabel) {

        typeId = paramTypeId;
        value = paramValue;
        label = paramLabel;
    }

    /**
     * IDからスコア点数を取得する.
     * @param id ID
     * @return  スコア点数
     */
    public static ScorePoint getByTypeId(final int id) {

        Optional<ScorePoint> result =
                Arrays.stream(ScorePoint.values()).filter(value -> value.typeId == id).findFirst();

        return result.orElse(NotInput);
    }
}
