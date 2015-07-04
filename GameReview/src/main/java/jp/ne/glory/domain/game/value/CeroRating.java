package jp.ne.glory.domain.game.value;

import lombok.Getter;

/**
 * CEROレーティング.
 * 
 * @author Junki Yamada
 */
public enum CeroRating {
    A(1l, "A"),
    B(2l, "B"),
    C(3l, "C"),
    D(4l, "D"),
    Z(5l, "Z"),
    Empty(null, "");

   /** ラベル */
    public static final String LABEL = "CEROレーティング";

    /**
     * ID.
     */
    @Getter
    private final Long id;

    /**
     * ラベル.
     */
    @Getter
    private final String label;

    /**
     * コンストラクタ.
     *
     * @param id ID
     * @param label ラベル
     */
    private CeroRating(final Long id, final String label) {

        this.id = id;
        this.label = label;
    }

    /**
     * 文字列からCEROレーティングに変換する.
     *
     * @param value 値
     * @return レーティング
     */
    public static CeroRating fromString(final String value) {

        if (value == null || value.isEmpty()) {

            return null;
        }

        final Long id = Long.valueOf(value);

        for (CeroRating rating : CeroRating.values()) {

            if (rating.id.equals(id)) {

                return rating;
            }
        }

        return null;
    }
}
