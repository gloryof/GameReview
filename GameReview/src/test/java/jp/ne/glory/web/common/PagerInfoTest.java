
package jp.ne.glory.web.common;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jp.ne.glory.domain.common.value.SearchCondition;
import jp.ne.glory.domain.common.value.SearchResults;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class PagerInfoTest {

    public static class データが1件もない場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {

            final SearchConditionMock condition = new SearchConditionMock();
            condition.setLotNumber(1);
            condition.setLotPerCount(20);

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 0, 0);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在しない() {

            assertThat(sut.isPrevActive(), is(false));
        }

        @Test
        public void 次ロットは存在しない() {

            assertThat(sut.isNextActive(), is(false));
        }

        @Test
        public void 現在のロットは0になる() {

            assertThat(sut.getCurrentPage(), is(0));
        }

        @Test
        public void 表示されるロット数は0になる() {

            assertThat(sut.getPages().length, is(0));
        }
    }

    public static class データが1件しかない場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {

            final SearchConditionMock condition = new SearchConditionMock();
            condition.setLotNumber(1);
            condition.setLotPerCount(20);

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 1, 1);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在しない() {

            assertThat(sut.isPrevActive(), is(false));
        }

        @Test
        public void 次ロットは存在しない() {

            assertThat(sut.isNextActive(), is(false));
        }

        @Test
        public void 現在のロットは1になる() {

            assertThat(sut.getCurrentPage(), is(1));
        }

        @Test
        public void 表示されるロット数は1になる() {

            assertThat(sut.getPages().length, is(1));
        }

        @Test
        public void 開始ロットは1になる() {

            assertThat(sut.getPages()[0], is(1));
        }
    }

    public static class 検索結果が1ロットしかない場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {

            final SearchConditionMock condition = new SearchConditionMock();
            condition.setLotNumber(1);
            condition.setLotPerCount(20);

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 20);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在しない() {

            assertThat(sut.isPrevActive(), is(false));
        }

        @Test
        public void 次ロットは存在しない() {

            assertThat(sut.isNextActive(), is(false));
        }

        @Test
        public void 現在のロットは1になる() {

            assertThat(sut.getCurrentPage(), is(1));
        }

        @Test
        public void 表示されるロット数は1になる() {

            assertThat(sut.getPages().length, is(1));
        }

        @Test
        public void 開始ロットは1になる() {

            assertThat(sut.getPages()[0], is(1));
        }
    }

    public static class 検索結果が2ロット_表示ロットが1ロット目の場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {

            final SearchConditionMock condition = new SearchConditionMock();
            condition.setLotNumber(1);
            condition.setLotPerCount(20);

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 21);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在しない() {

            assertThat(sut.isPrevActive(), is(false));
        }

        @Test
        public void 次ロットは存在する() {

            assertThat(sut.isNextActive(), is(true));
        }

        @Test
        public void 現在のロットは1になる() {

            assertThat(sut.getCurrentPage(), is(1));
        }

        @Test
        public void 表示されるロット数は2になる() {

            assertThat(sut.getPages().length, is(2));
        }

        @Test
        public void 開始ロットは1になる() {

            assertThat(sut.getPages()[0], is(1));
        }

        @Test
        public void ロットは1から2までの範囲が表示される() {

            final int expectedStartPage = 1;
            for (int i = 0; i < sut.getPages().length; i++) {

                final int expectedPageNumber = expectedStartPage + i;
                assertThat(sut.getPages()[i], is(expectedPageNumber));
            }
        }
    }

    public static class 検索結果が2ロット_表示ロットが2ロット目の場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {

            final SearchConditionMock condition = new SearchConditionMock();
            condition.setLotNumber(2);
            condition.setLotPerCount(20);

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 21);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在する() {

            assertThat(sut.isPrevActive(), is(true));
        }

        @Test
        public void 次ロットは存在しない() {

            assertThat(sut.isNextActive(), is(false));
        }

        @Test
        public void 現在のロットは2になる() {

            assertThat(sut.getCurrentPage(), is(2));
        }

        @Test
        public void 表示されるロット数は2になる() {

            assertThat(sut.getPages().length, is(2));
        }

        @Test
        public void 開始ロットは1になる() {

            assertThat(sut.getPages()[0], is(1));
        }

        @Test
        public void ロットは1から2までの範囲が表示される() {

            final int expectedStartPage = 1;
            for (int i = 0; i < sut.getPages().length; i++) {

                final int expectedPageNumber = expectedStartPage + i;
                assertThat(sut.getPages()[i], is(expectedPageNumber));
            }
        }
    }

    public static class 検索結果が10ロット_表示ロットが1ロット目の場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {
            final SearchConditionMock condition = new SearchConditionMock();
            condition.setLotNumber(1);
            condition.setLotPerCount(20);

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 200);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在しない() {

            assertThat(sut.isPrevActive(), is(false));
        }

        @Test
        public void 次ロットは存在する() {

            assertThat(sut.isNextActive(), is(true));
        }

        @Test
        public void 現在のロットは1になる() {

            assertThat(sut.getCurrentPage(), is(1));
        }

        @Test
        public void 表示されるロット数は10になる() {

            assertThat(sut.getPages().length, is(10));
        }

        @Test
        public void 開始ロットは1になる() {

            assertThat(sut.getPages()[0], is(1));
        }

        @Test
        public void ロットは1から10までの範囲が表示される() {

            final int expectedStartPage = 1;
            for (int i = 0; i < sut.getPages().length; i++) {

                final int expectedPageNumber = expectedStartPage + i;
                assertThat(sut.getPages()[i], is(expectedPageNumber));
            }
        }
    }

    public static class 検索結果が10ロット_表示ロットが10ロット目の場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {

            final SearchConditionMock condition = new SearchConditionMock();
            condition.setLotNumber(10);
            condition.setLotPerCount(20);

            final int expectedStartPage = 1;

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 200);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在する() {

            assertThat(sut.isPrevActive(), is(true));
        }

        @Test
        public void 次ロットは存在しない() {

            assertThat(sut.isNextActive(), is(false));
        }

        @Test
        public void 現在のロットは10になる() {

            assertThat(sut.getCurrentPage(), is(10));
        }

        @Test
        public void 表示されるロット数は10になる() {

            assertThat(sut.getPages().length, is(10));
        }

        @Test
        public void 開始ロットは1になる() {

            assertThat(sut.getPages()[0], is(1));
        }

        @Test
        public void ロットは1から10までの範囲が表示される() {

            final int expectedStartPage = 1;
            for (int i = 0; i < sut.getPages().length; i++) {

                final int expectedPageNumber = expectedStartPage + i;
                assertThat(sut.getPages()[i], is(expectedPageNumber));
            }
        }
    }

    public static class 検索結果が11ロット_表示ロットが11ロット目の場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {

            final SearchConditionMock condition = new SearchConditionMock();
            condition.setLotNumber(11);
            condition.setLotPerCount(20);

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 220);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在する() {

            assertThat(sut.isPrevActive(), is(true));
        }

        @Test
        public void 次ロットは存在しない() {

            assertThat(sut.isNextActive(), is(false));
        }

        @Test
        public void 現在のロットは11になる() {

            assertThat(sut.getCurrentPage(), is(11));
        }

        @Test
        public void 表示されるロット数は1になる() {

            assertThat(sut.getPages().length, is(1));
        }

        @Test
        public void 開始ロットは11になる() {

            assertThat(sut.getPages()[0], is(11));
        }

        @Test
        public void ロットは11のみ表示される() {

            final int expectedStartPage = 11;
            for (int i = 0; i < sut.getPages().length; i++) {

                final int expectedPageNumber = expectedStartPage + i;
                assertThat(sut.getPages()[i], is(expectedPageNumber));
            }
        }
    }

    public static class 検索結果が12ロット_表示ロットが11ロット目の場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {

            final SearchConditionMock condition = new SearchConditionMock();
            condition.setLotNumber(11);
            condition.setLotPerCount(20);

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 240);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在する() {

            assertThat(sut.isPrevActive(), is(true));
        }

        @Test
        public void 次ロットは存在する() {

            assertThat(sut.isNextActive(), is(true));
        }

        @Test
        public void 現在のロットは11になる() {

            assertThat(sut.getCurrentPage(), is(11));
        }

        @Test
        public void 表示されるロット数は2になる() {

            assertThat(sut.getPages().length, is(2));
        }

        @Test
        public void 開始ロットは11になる() {

            assertThat(sut.getPages()[0], is(11));
        }

        @Test
        public void ロットは11から12までの範囲が表示される() {

            final int expectedStartPage = 11;
            for (int i = 0; i < sut.getPages().length; i++) {

                final int expectedPageNumber = expectedStartPage + i;
                assertThat(sut.getPages()[i], is(expectedPageNumber));
            }
        }
    }

    public static class 検索結果が12ロット_表示ロットが12ロット目の場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {

            final SearchConditionMock condition = new SearchConditionMock();
            condition.setLotNumber(12);
            condition.setLotPerCount(20);

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 240);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在する() {

            assertThat(sut.isPrevActive(), is(true));
        }

        @Test
        public void 次ロットは存在しない() {

            assertThat(sut.isNextActive(), is(false));
        }

        @Test
        public void 現在のロットは12になる() {

            assertThat(sut.getCurrentPage(), is(12));
        }

        @Test
        public void 表示されるロット数は2になる() {

            assertThat(sut.getPages().length, is(2));
        }

        @Test
        public void 開始ロットは11になる() {

            assertThat(sut.getPages()[0], is(11));
        }

        @Test
        public void ロットは11から12までの範囲が表示される() {

            final int expectedStartPage = 11;
            for (int i = 0; i < sut.getPages().length; i++) {

                final int expectedPageNumber = expectedStartPage + i;
                assertThat(sut.getPages()[i], is(expectedPageNumber));
            }
        }
    }

    public static class 検索結果が20ロット_表示ロットが11ロット目の場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {

            final SearchConditionMock condition = new SearchConditionMock();
            condition.setLotNumber(11);
            condition.setLotPerCount(20);

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 400);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在する() {

            assertThat(sut.isPrevActive(), is(true));
        }

        @Test
        public void 次ロットは存在する() {

            assertThat(sut.isNextActive(), is(true));
        }

        @Test
        public void 現在のロットは11になる() {

            assertThat(sut.getCurrentPage(), is(11));
        }

        @Test
        public void 表示されるロット数は10になる() {

            assertThat(sut.getPages().length, is(10));
        }

        @Test
        public void 開始ロットは11になる() {

            assertThat(sut.getPages()[0], is(11));
        }

        @Test
        public void ロットは11から20までの範囲が表示される() {

            final int expectedStartPage = 11;
            for (int i = 0; i < sut.getPages().length; i++) {

                final int expectedPageNumber = expectedStartPage + i;
                assertThat(sut.getPages()[i], is(expectedPageNumber));
            }
        }
    }

    public static class 検索結果が20ロット_表示ロットが20ロット目の場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {

            final SearchConditionMock condition = new SearchConditionMock();
            condition.setLotNumber(20);
            condition.setLotPerCount(20);

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 400);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在する() {

            assertThat(sut.isPrevActive(), is(true));
        }

        @Test
        public void 次ロットは存在しない() {

            assertThat(sut.isNextActive(), is(false));
        }

        @Test
        public void 現在のロットは20になる() {

            assertThat(sut.getCurrentPage(), is(20));
        }

        @Test
        public void 表示されるロット数は10になる() {

            assertThat(sut.getPages().length, is(10));
        }

        @Test
        public void 開始ロットは11になる() {

            assertThat(sut.getPages()[0], is(11));
        }

        @Test
        public void ロットは11から20までの範囲が表示される() {

            final int expectedStartPage = 11;
            for (int i = 0; i < sut.getPages().length; i++) {

                final int expectedPageNumber = expectedStartPage + i;
                assertThat(sut.getPages()[i], is(expectedPageNumber));
            }
        }
    }

    public static class 検索結果が20ロット_表示ロットが15ロット目の場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {

            final SearchConditionMock condition = new SearchConditionMock();
            condition.setLotNumber(15);
            condition.setLotPerCount(20);

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 400);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在する() {

            assertThat(sut.isPrevActive(), is(true));
        }

        @Test
        public void 次ロットは存在する() {

            assertThat(sut.isNextActive(), is(true));
        }

        @Test
        public void 現在のロットは15になる() {

            assertThat(sut.getCurrentPage(), is(15));
        }

        @Test
        public void 表示されるロット数は10になる() {

            assertThat(sut.getPages().length, is(10));
        }

        @Test
        public void 開始ロットは11になる() {

            assertThat(sut.getPages()[0], is(11));
        }

        @Test
        public void ロットは11から20までの範囲が表示される() {

            final int expectedStartPage = 11;
            for (int i = 0; i < sut.getPages().length; i++) {

                final int expectedPageNumber = expectedStartPage + i;
                assertThat(sut.getPages()[i], is(expectedPageNumber));
            }
        }
    }

    public static class 検索結果が20ロット_表示ロットが10ロット目の場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {

            final SearchConditionMock condition = new SearchConditionMock();
            condition.setLotNumber(10);
            condition.setLotPerCount(20);

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 400);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在する() {

            assertThat(sut.isPrevActive(), is(true));
        }

        @Test
        public void 次ロットは存在する() {

            assertThat(sut.isNextActive(), is(true));
        }

        @Test
        public void 現在のロットは10になる() {

            assertThat(sut.getCurrentPage(), is(10));
        }

        @Test
        public void 表示されるロット数は10になる() {

            assertThat(sut.getPages().length, is(10));
        }

        @Test
        public void 開始ロットは1になる() {

            assertThat(sut.getPages()[0], is(1));
        }

        @Test
        public void ロットは1から10までの範囲が表示される() {

            final int expectedStartPage = 1;
            for (int i = 0; i < sut.getPages().length; i++) {

                final int expectedPageNumber = expectedStartPage + i;
                assertThat(sut.getPages()[i], is(expectedPageNumber));
            }
        }
    }

    public static class ページ内ロット数が指定されていない場合 {

        private PagerInfo sut = null;

        @Before
        public void setUp() {

            final SearchConditionMock condition = new SearchConditionMock();

            final SearchResultsMock mock = SearchResultsMock.createResult(condition, 20, 400);
            sut = new PagerInfo(mock);
        }

        @Test
        public void 前ロットは存在しない() {

            assertThat(sut.isPrevActive(), is(false));
        }

        @Test
        public void 次ロットは存在しない() {

            assertThat(sut.isNextActive(), is(false));
        }

        @Test
        public void 現在のロットは1になる() {

            assertThat(sut.getCurrentPage(), is(1));
        }

        @Test
        public void 表示されるロット数は1になる() {

            assertThat(sut.getPages().length, is(1));
        }

        @Test
        public void 開始ロットは1になる() {

            assertThat(sut.getPages()[0], is(1));
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