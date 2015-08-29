package jp.ne.glory.domain.review.entity;


import java.time.LocalDateTime;
import jp.ne.glory.common.type.DateTimeValue;
import jp.ne.glory.domain.review.value.BadPoint;
import jp.ne.glory.domain.review.value.Comment;
import jp.ne.glory.domain.review.value.GoodPoint;
import jp.ne.glory.domain.review.value.LastUpdateDateTime;
import jp.ne.glory.domain.review.value.PostDateTime;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.ScorePoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class ReviewTest {

    public static class 全ての値が正常に設定されている場合 {

        private Review sut;

        private final ReviewId INIT_ID = new ReviewId(123L);

        @Before
        public void setUp() {

            sut = new Review(INIT_ID);

            sut.setGoodPoint(new GoodPoint("良い点：テスト"));
            sut.setBadPoint(new BadPoint("悪い点：テスト"));
            sut.setComment(new Comment("コメント：テスト"));
            sut.setScore(new Score());
            sut.getScore().setStory(ScorePoint.Point5);
            sut.getScore().setOperability(ScorePoint.Point4);
            sut.getScore().setAddiction(ScorePoint.Point3);
            sut.getScore().setMusic(ScorePoint.Point2);
            sut.getScore().setLoadTime(ScorePoint.Point1);
            sut.setPostTime(new PostDateTime(new DateTimeValue(LocalDateTime.now())));
            sut.setLastUpdate(new LastUpdateDateTime(new DateTimeValue(LocalDateTime.now())));
        }

        @Test
        public void コンストラクタで設定した値がプロパティに設定されている() {

            assertThat(sut.getId().isSame(INIT_ID), is(true));
        }
        
        @Test
        public void isRegisteredにtrueが設定されている() {

            assertThat(sut.isRegistered(), is(true));
        }
    }

    
    public static class コンストラクタに初期値が設定されている場合 {

        private Review sut;

        private final ReviewId INIT_ID = ReviewId.notNumberingValue();

        @Before
        public void setUp() {

            sut = new Review(INIT_ID);
        }

        @Test
        public void 全てに初期値が設定されている() {

            assertThat(sut.getId().isSame(INIT_ID), is(true));
            assertThat(sut.getGoodPoint().getValue(), is(GoodPoint.empty().getValue()));
            assertThat(sut.getBadPoint().getValue(), is(BadPoint.empty().getValue()));
            assertThat(sut.getComment().getValue(), is(Comment.empty().getValue()));
            assertThat(sut.getScore(), is(not(nullValue())));
            assertThat(sut.getScore().getAddiction(), is(ScorePoint.NotInput));
            assertThat(sut.getScore().getLoadTime(), is(ScorePoint.NotInput));
            assertThat(sut.getScore().getMusic(), is(ScorePoint.NotInput));
            assertThat(sut.getScore().getOperability(), is(ScorePoint.NotInput));
            assertThat(sut.getScore().getStory(), is(ScorePoint.NotInput));
        }

        @Test
        public void isRegisteredにfalseが設定されている() {

            assertThat(sut.isRegistered(), is(false));
        }
    }
}
