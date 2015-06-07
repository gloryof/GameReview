package jp.ne.glory.web.common;

import jp.ne.glory.domain.common.value.SearchCondition;
import jp.ne.glory.domain.common.value.SearchResults;
import lombok.Getter;

/**
 * ページャ情報.
 *
 * @author Junki Yamada
 */
public class PagerInfo {

    /**
     * 1ページ内の最大ページ番号数.
     */
    private final static int MAX_VIEW_PAGES = 10;

    /**
     * 現在のページ.
     */
    @Getter
    private final int currentPage;

    /**
     * ページリスト.
     */
    @Getter
    private final int[] pages;

    /**
     * 前ページありフラグ.
     */
    @Getter
    private final boolean prevActive;

    /**
     * 次ページありフラグ
     */
    @Getter
    private final boolean nextActive;

    /**
     * コンストラクタ.
     *
     * @param results 検索結果
     * @param startPage 開始ページ
     */
    public PagerInfo(final SearchResults results, final int startPage) {

        final SearchCondition condition = results.getCondition();
        final int pagePerCount = condition.getLotPerCount();
        final int allCount = results.getAllCount();

        final int restPageCount = calculateRestPageCount(pagePerCount, allCount, startPage);
        currentPage = condition.getLotNumber();

        pages = createPageNumbers(restPageCount, startPage);

        prevActive = (1 < currentPage);

        final int viewLastPage = pages[restPageCount - 1];
        nextActive = (currentPage < viewLastPage);
    }

    /**
     * 残りの表示ページ数を計算する.
     *
     * @param pagePerCount ページ内のデータ数
     * @param allCount 全件数
     * @param startPage 表示開始ページ数
     * @return 表示ページ数
     */
    private int calculateRestPageCount(final int pagePerCount, final int allCount, final int startPage) {

        final int prevPageLast = (startPage - 1) * pagePerCount;

        final int restDataCount = allCount - prevPageLast;

        int restPageCount = (restDataCount / pagePerCount);

        if (restPageCount == 0) {

            return 1;
        }

        if (MAX_VIEW_PAGES < restPageCount) {

            return MAX_VIEW_PAGES;
        }

        if (restDataCount % pagePerCount < 1) {
            return restPageCount;
        }

        return restPageCount + 1;
    }

    /**
     * ページ数の配列を作成する.
     *
     * @param restPageCount 残りページ数
     * @param startPage 開始ページ数
     * @return ページ数配列
     */
    private int[] createPageNumbers(final int restPageCount, final int startPage) {

        final int[] pages = new int[restPageCount];
        for (int i = 0; i < restPageCount; i++) {

            pages[i] = startPage + i;
        }

        return pages;
    }
}
