package jp.ne.glory.domain.common.value;

import lombok.Getter;
import lombok.Setter;

/**
 * 検索条件.
 *
 * @author Junki Yamada
 */
public abstract class SearchCondition {

    /**
     * 対象件数.<br>
     * 1未満の場合は全件数が対象となる
     */
    @Getter
    @Setter
    private int targetCount = 0;

    /**
     * 1ロット内の件数.<br>
     * 1未満の場合は全件数が対象となる
     */
    @Getter
    @Setter
    private int lotPerCount = 0;

    /**
     * ロット番号.<br>
     * 取得対象となるロットの番号を指定する。<br>
     * デフォルトは1。
     */
    @Getter
    @Setter
    private int lotNumber = 1;
}
