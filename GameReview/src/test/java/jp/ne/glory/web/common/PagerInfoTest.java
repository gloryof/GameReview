
package jp.ne.glory.web.common;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jp.ne.glory.domain.common.value.SearchCondition;
import jp.ne.glory.domain.common.value.SearchResults;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PagerInfoTest {

    @Test
    public void データが1件もない場合() {

        final SearchConditionMock condition = new SearchConditionMock();
        condition.setLotNumber(1);
        condition.setLotPerCount(20);

        final int expectedStartPage = 1;

        final SearchResultsMock mock = SearchResultsMock.createResult(condition, 0, 0);
        final PagerInfo actualPageInfo = new PagerInfo(mock);

        assertThat(actualPageInfo.isPrevActive(), is(false));
        assertThat(actualPageInfo.isNextActive(), is(false));
        assertThat(actualPageInfo.getCurrentPage(), is(0));

        final int[] actualPages = actualPageInfo.getPages();
        assertThat(actualPages.length, is(0));
    }

    @Test
    public void データが1件しかない場合() {

        final SearchConditionMock condition = new SearchConditionMock();
        condition.setLotNumber(1);
        condition.setLotPerCount(20);

        final int expectedStartPage = 1;

        final SearchResultsMock mock = SearchResultsMock.createResult(condition, 1, 1);
        final PagerInfo actualPageInfo = new PagerInfo(mock);

        assertThat(actualPageInfo.isPrevActive(), is(false));
        assertThat(actualPageInfo.isNextActive(), is(false));
        assertThat(actualPageInfo.getCurrentPage(), is(1));

        final int[] actualPages = actualPageInfo.getPages();
        assertThat(actualPages.length, is(1));

        for (int i = 0; i < actualPages.length; i++) {

            final int expectedPageNumber = expectedStartPage + i;
            assertThat(actualPages[0], is(expectedPageNumber));
        }
    }

    @Test
    public void 検索結果が1ページしかない場合() {

        final SearchConditionMock condition = new SearchConditionMock();
        condition.setLotNumber(1);
        condition.setLotPerCount(20);

        final int expectedStartPage = 1;

        final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 20);
        final PagerInfo actualPageInfo = new PagerInfo(mock);

        assertThat(actualPageInfo.isPrevActive(), is(false));
        assertThat(actualPageInfo.isNextActive(), is(false));
        assertThat(actualPageInfo.getCurrentPage(), is(1));

        final int[] actualPages = actualPageInfo.getPages();
        assertThat(actualPages.length, is(1));

        for (int i = 0; i < actualPages.length; i++) {

            final int expectedPageNumber = expectedStartPage + i;
            assertThat(actualPages[0], is(expectedPageNumber));
        }
    }

    @Test
    public void 検索結果が2ページ_表示ページが1ページ目の場合() {

        final SearchConditionMock condition = new SearchConditionMock();
        condition.setLotNumber(1);
        condition.setLotPerCount(20);

        final int expectedStartPage = 1;

        final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 21);
        final PagerInfo actualPageInfo = new PagerInfo(mock);

        assertThat(actualPageInfo.isPrevActive(), is(false));
        assertThat(actualPageInfo.isNextActive(), is(true));
        assertThat(actualPageInfo.getCurrentPage(), is(1));

        final int[] actualPages = actualPageInfo.getPages();
        assertThat(actualPages.length, is(2));

        for (int i = 0; i < actualPages.length; i++) {

            final int expectedPageNumber = expectedStartPage + i;
            assertThat(actualPages[i], is(expectedPageNumber));
        }
    }

    @Test
    public void 検索結果が2ページ_表示ページが2ページ目の場合() {

        final SearchConditionMock condition = new SearchConditionMock();
        condition.setLotNumber(2);
        condition.setLotPerCount(20);

        final int expectedStartPage = 1;

        final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 21);
        final PagerInfo actualPageInfo = new PagerInfo(mock);

        assertThat(actualPageInfo.isPrevActive(), is(true));
        assertThat(actualPageInfo.isNextActive(), is(false));
        assertThat(actualPageInfo.getCurrentPage(), is(2));

        final int[] actualPages = actualPageInfo.getPages();
        assertThat(actualPages.length, is(2));

        for (int i = 0; i < actualPages.length; i++) {

            final int expectedPageNumber = expectedStartPage + i;
            assertThat(actualPages[i], is(expectedPageNumber));
        }
    }

    @Test
    public void 検索結果が10ページ_表示ページが1ページ目の場合() {

        final SearchConditionMock condition = new SearchConditionMock();
        condition.setLotNumber(1);
        condition.setLotPerCount(20);

        final int expectedStartPage = 1;

        final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 200);
        final PagerInfo actualPageInfo = new PagerInfo(mock);

        assertThat(actualPageInfo.isPrevActive(), is(false));
        assertThat(actualPageInfo.isNextActive(), is(true));
        assertThat(actualPageInfo.getCurrentPage(), is(1));

        final int[] actualPages = actualPageInfo.getPages();
        assertThat(actualPages.length, is(10));

        for (int i = 0; i < actualPages.length; i++) {

            final int expectedPageNumber = expectedStartPage + i;
            assertThat(actualPages[i], is(expectedPageNumber));
        }
    }

    @Test
    public void 検索結果が10ページ_表示ページが10ページ目の場合() {

        final SearchConditionMock condition = new SearchConditionMock();
        condition.setLotNumber(10);
        condition.setLotPerCount(20);

        final int expectedStartPage = 1;

        final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 200);
        final PagerInfo actualPageInfo = new PagerInfo(mock);

        assertThat(actualPageInfo.isPrevActive(), is(true));
        assertThat(actualPageInfo.isNextActive(), is(false));
        assertThat(actualPageInfo.getCurrentPage(), is(10));

        final int[] actualPages = actualPageInfo.getPages();
        assertThat(actualPages.length, is(10));

        for (int i = 0; i < actualPages.length; i++) {

            final int expectedPageNumber = expectedStartPage + i;
            assertThat(actualPages[i], is(expectedPageNumber));
        }
    }

    @Test
    public void 検索結果が11ページ_表示ページが11ページ目の場合() {

        final SearchConditionMock condition = new SearchConditionMock();
        condition.setLotNumber(11);
        condition.setLotPerCount(20);

        final int expectedStartPage = 11;

        final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 220);
        final PagerInfo actualPageInfo = new PagerInfo(mock);

        assertThat(actualPageInfo.isPrevActive(), is(true));
        assertThat(actualPageInfo.isNextActive(), is(false));
        assertThat(actualPageInfo.getCurrentPage(), is(11));

        final int[] actualPages = actualPageInfo.getPages();
        assertThat(actualPages.length, is(1));

        for (int i = 0; i < actualPages.length; i++) {

            final int expectedPageNumber = expectedStartPage + i;
            assertThat(actualPages[i], is(expectedPageNumber));
        }
    }

    @Test
    public void 検索結果が12ページ_表示ページが11ページ目の場合() {

        final SearchConditionMock condition = new SearchConditionMock();
        condition.setLotNumber(11);
        condition.setLotPerCount(20);

        final int expectedStartPage = 11;

        final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 240);
        final PagerInfo actualPageInfo = new PagerInfo(mock);

        assertThat(actualPageInfo.isPrevActive(), is(true));
        assertThat(actualPageInfo.isNextActive(), is(true));
        assertThat(actualPageInfo.getCurrentPage(), is(11));

        final int[] actualPages = actualPageInfo.getPages();
        assertThat(actualPages.length, is(2));

        for (int i = 0; i < actualPages.length; i++) {

            final int expectedPageNumber = expectedStartPage + i;
            assertThat(actualPages[i], is(expectedPageNumber));
        }
    }

    @Test
    public void 検索結果が12ページ_表示ページが12ページ目の場合() {

        final SearchConditionMock condition = new SearchConditionMock();
        condition.setLotNumber(12);
        condition.setLotPerCount(20);

        final int expectedStartPage = 11;

        final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 240);
        final PagerInfo actualPageInfo = new PagerInfo(mock);

        assertThat(actualPageInfo.isPrevActive(), is(true));
        assertThat(actualPageInfo.isNextActive(), is(false));
        assertThat(actualPageInfo.getCurrentPage(), is(12));

        final int[] actualPages = actualPageInfo.getPages();
        assertThat(actualPages.length, is(2));

        for (int i = 0; i < actualPages.length; i++) {

            final int expectedPageNumber = expectedStartPage + i;
            assertThat(actualPages[i], is(expectedPageNumber));
        }
    }

    @Test
    public void 検索結果が20ページ_表示ページが11ページ目の場合() {

        final SearchConditionMock condition = new SearchConditionMock();
        condition.setLotNumber(11);
        condition.setLotPerCount(20);

        final int expectedStartPage = 11;

        final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 400);
        final PagerInfo actualPageInfo = new PagerInfo(mock);

        assertThat(actualPageInfo.isPrevActive(), is(true));
        assertThat(actualPageInfo.isNextActive(), is(true));
        assertThat(actualPageInfo.getCurrentPage(), is(11));

        final int[] actualPages = actualPageInfo.getPages();
        assertThat(actualPages.length, is(10));

        for (int i = 0; i < actualPages.length; i++) {

            final int expectedPageNumber = expectedStartPage + i;
            assertThat(actualPages[i], is(expectedPageNumber));
        }
    }

    @Test
    public void 検索結果が20ページ_表示ページが20ページ目の場合() {

        final SearchConditionMock condition = new SearchConditionMock();
        condition.setLotNumber(20);
        condition.setLotPerCount(20);

        final int expectedStartPage = 11;

        final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 400);
        final PagerInfo actualPageInfo = new PagerInfo(mock);

        assertThat(actualPageInfo.isPrevActive(), is(true));
        assertThat(actualPageInfo.isNextActive(), is(false));
        assertThat(actualPageInfo.getCurrentPage(), is(20));

        final int[] actualPages = actualPageInfo.getPages();
        assertThat(actualPages.length, is(10));

        for (int i = 0; i < actualPages.length; i++) {

            final int expectedPageNumber = expectedStartPage + i;
            assertThat(actualPages[i], is(expectedPageNumber));
        }
    }

    @Test
    public void 検索結果が20ページ_表示ページが15ページ目の場合() {

        final SearchConditionMock condition = new SearchConditionMock();
        condition.setLotNumber(15);
        condition.setLotPerCount(20);

        final int expectedStartPage = 11;

        final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 400);
        final PagerInfo actualPageInfo = new PagerInfo(mock);

        assertThat(actualPageInfo.isPrevActive(), is(true));
        assertThat(actualPageInfo.isNextActive(), is(true));
        assertThat(actualPageInfo.getCurrentPage(), is(15));

        final int[] actualPages = actualPageInfo.getPages();
        assertThat(actualPages.length, is(10));

        for (int i = 0; i < actualPages.length; i++) {

            final int expectedPageNumber = expectedStartPage + i;
            assertThat(actualPages[i], is(expectedPageNumber));
        }
    }

    @Test
    public void 検索結果が20ページ_表示ページが10ページ目の場合() {

        final SearchConditionMock condition = new SearchConditionMock();
        condition.setLotNumber(10);
        condition.setLotPerCount(20);

        final int expectedStartPage = 1;

        final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 400);
        final PagerInfo actualPageInfo = new PagerInfo(mock);

        assertThat(actualPageInfo.isPrevActive(), is(true));
        assertThat(actualPageInfo.isNextActive(), is(true));
        assertThat(actualPageInfo.getCurrentPage(), is(10));

        final int[] actualPages = actualPageInfo.getPages();
        assertThat(actualPages.length, is(10));

        for (int i = 0; i < actualPages.length; i++) {

            final int expectedPageNumber = expectedStartPage + i;
            assertThat(actualPages[i], is(expectedPageNumber));
        }
    }

    private static class SearchConditionMock extends SearchCondition {

    }

    private static class DataMock {

    }

    private static class SearchResultsMock extends SearchResults<SearchConditionMock, DataMock> {

        private SearchResultsMock(final SearchConditionMock condition, final List<DataMock> results, final int allCount) {

            super(condition, results, allCount);
        }

        private static SearchResultsMock createResult(final SearchConditionMock condition, final int pageResultCount, final int allCount) {

            final List<DataMock> resultList = IntStream.rangeClosed(1, pageResultCount).mapToObj(v -> new DataMock()).collect(Collectors.toList());

            return new SearchResultsMock(condition, resultList, allCount);
        }
    }

}