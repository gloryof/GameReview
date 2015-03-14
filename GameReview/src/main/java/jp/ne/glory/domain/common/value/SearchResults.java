package jp.ne.glory.domain.common.value;

import java.util.List;
import lombok.Getter;

/**
 * 検索結果共通クラス.
 *
 * @author Junki Yamada
 * @param <C> 検索条件クラス
 * @param <R> 検索結果クラス
 */
public abstract class SearchResults<C extends SearchCondition, R> {

    /**
     * 検索条件.
     */
    @Getter
    private final C condition;

    /**
     * 検索結果.
     */
    @Getter
    private final List<R> results;

    /**
     * 全件数.
     */
    @Getter
    private final int allCount;

    /**
     * コンストラクタ.
     *
     * @param condition 検索条件.
     * @param results 検索結果.
     * @param allCount 全件数
     */
    protected SearchResults(final C condition, final List<R> results, final int allCount) {

        this.condition = condition;
        this.results = results;
        this.allCount = allCount;
    }

    /**
     * 次のロットがあるかを判定する.
     *
     * @return ある場合：true、ない場合：false
     */
    public boolean hasNextLot() {

        if (results == null || results.isEmpty()) {

            return false;
        }

        final int prevCount = calculatePrevCount();
        final int displayCount = prevCount + results.size();

        return displayCount < allCount;
    }

    /**
     * 前のロットがあるかを判定する.
     *
     * @return ある場合：true、ない場合：false
     */
    public boolean hasPrevLot() {

        if (results == null || results.isEmpty()) {

            return false;
        }

        final int prevCount = calculatePrevCount();

        return 0 < prevCount;
    }

    /**
     * 前ページまでの件数を計算する.
     *
     * @return
     */
    private int calculatePrevCount() {

        if (condition.getLotNumber() < 2) {

            return 0;
        }

        final int prevLotNumber = condition.getLotNumber() - 1;

        return prevLotNumber * condition.getLotPerCount();
    }
}
