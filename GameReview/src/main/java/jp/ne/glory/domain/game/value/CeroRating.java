package jp.ne.glory.domain.game.value;

import lombok.Getter;

/**
 * CEROレーティング.
 * 
 * @author Junki Yamada
 */
public enum CeroRating {
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    Z("Z"),
    Empty("");

   /** ラベル */
   public static final String LABEL = "CEROレーティング";

    /**
     * ラベル.
     */
    @Getter
    private final String label;

    /**
     * コンストラクタ.
     *
     * @param label ラベル
     */
    private CeroRating(final String label) {

        this.label = label;
    }
}
