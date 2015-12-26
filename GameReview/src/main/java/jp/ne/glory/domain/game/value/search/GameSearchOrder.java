package jp.ne.glory.domain.game.value.search;

/**
 * ゲーム検索の並び順.
 *
 * @author Junki Yamada
 */
public enum GameSearchOrder {

    /**
     * IDの降順.
     */
    IdDesc,
    /**
     * IDの昇順.
     */
    IdAsc;

    /**
     * 並び順のデフォルトを取得する.
     *
     * @return デフォルト値
     */
    public static GameSearchOrder defaultValue() {

        return IdDesc;
    }
}
