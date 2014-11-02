package jp.ne.glory.domain.review.value.search;

/**
 * レビュー検索条件.
 *
 * @author Junki Yamada
 */
public class ReviewSearchCondition {

    /**
     * 対象件数. <br>
     * 1未満の場合は全件数が対象となる
     */
    public int targetCount = 0;

    /**
     * 1ロット内の件数.<br>
     * 1未満の場合は全件数が対象となる
     */
    public int lotPerCount = 0;

    /**
     * 検索順序タイプ.
     */
    public ReviewSearchOrderType orderType;
}
