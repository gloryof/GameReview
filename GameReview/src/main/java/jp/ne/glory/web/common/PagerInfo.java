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
     * 1ページ内の件数.
     */
    private final int pagePerCount;

    /**
     * 全件数.
     */
    private final int allCount;

    /**
     * 最大ページ数.
     */
    private final int maxPageNumber;

    /**
     * 開始ページ番号.
     */
    private final int startPageNumber;

    /**
     * コンストラクタ.
     *
     * @param results 検索結果
     */
    public PagerInfo(final SearchResults results) {

        final SearchCondition condition = results.getCondition();
        pagePerCount = condition.getLotPerCount();
        allCount = results.getAllCount();

        currentPage = allCount > 0 ? condition.getLotNumber() : 0;
        maxPageNumber = calculateMaxPage();
        startPageNumber = calculateStartPage();

        pages = createPageNumbers();

        prevActive = (1 < currentPage);
        nextActive = hasNext();
    }

    /**
     * ページ数の配列を作成する.
     *
     * @return ページ数配列
     */
    private int[] createPageNumbers() {

        final int restPageCount;
        if (maxPageNumber < 1) {
            restPageCount = 0;
        } else if (maxPageNumber < (startPageNumber + MAX_VIEW_PAGES)) {

            restPageCount = maxPageNumber - (startPageNumber - 1);
        } else {

            restPageCount = MAX_VIEW_PAGES;
        }

        final int[] pages = new int[restPageCount];
        for (int i = 0; i < restPageCount; i++) {

            pages[i] = startPageNumber + i;
        }

        return pages;
    }

    /**
     * 次ページが存在するかを判定する.
     *
     * @return 存在する場合：true、存在しない場合：false
     */
    private boolean hasNext() {

        if (pages.length <= 1) {

            return false;
        }

        final int viewLastPage = pages[pages.length - 1];

        if (viewLastPage < maxPageNumber) {

            return true;
        }

        return (currentPage < viewLastPage);
    }

    /**
     * 最大ページ数を計算する.
     *
     * @return 最大ページ数.
     */
    private int calculateMaxPage() {

        if (allCount == 0) {

            return 0;
        }

        if (allCount <= pagePerCount) {

            return 1;
        }

        final int appendPage = allCount % pagePerCount > 0 ? 1 : 0;
        return (allCount / pagePerCount) + appendPage;
    }

    /**
     * 開始ページ数を計算する.
     *
     * @return 開始ページ数
     */
    private int calculateStartPage() {

        if (currentPage % MAX_VIEW_PAGES == 0) {

            return currentPage - MAX_VIEW_PAGES + 1;
        } 

        return ((currentPage / MAX_VIEW_PAGES) * MAX_VIEW_PAGES) + 1;
    }
}
