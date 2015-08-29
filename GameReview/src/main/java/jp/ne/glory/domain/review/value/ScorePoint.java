package jp.ne.glory.domain.review.value;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * スコア点数
 * @author Junki Yamada
 */
public enum ScorePoint {

    /** 0点. */
    Point0(1, 0, "最悪"),

    /** 1点. */
    Point1(2, 1, "1"),
    /** 2点. */
    Point2(3, 2, "2"),
    /** 3点. */
    Point3(4, 3, "3"),
    /** 4点. */
    Point4(5, 4, "4"),
    /** 5点. */
    Point5(6, 5, "5"),
    /** 未入力. */
    NotInput(7, 0, "未入力"),
    /** 評価対象外. */
    Exclued(8, 0, "対象外");

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

    /**
     * 選択可能なスコアの配列を返却する.
     *
     * @return スコア配列
     */
    public static List<ScorePoint> getSelectableScores() {

        return Arrays.stream(ScorePoint.values())
                .filter(v -> !v.equals(ScorePoint.NotInput))
                .collect(Collectors.toList());
    }

    /**
     * 文字列からスコア点数に変換する.
     *
     * @param value 値
     * @return スコア点数
     */
    public static ScorePoint fromString(final String value) {

        if (value == null || value.isEmpty()) {

            return ScorePoint.NotInput;
        }

        final Integer id = Integer.valueOf(value);
        for (final ScorePoint score : ScorePoint.values()) {

            if (id.equals(score.typeId)) {

                return score;
            }
        }

        return ScorePoint.NotInput;
    }
}
