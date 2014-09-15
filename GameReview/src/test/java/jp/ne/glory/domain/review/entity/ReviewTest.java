package jp.ne.glory.domain.review.entity;


import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import jp.ne.glory.domain.review.value.BadPoint;
import jp.ne.glory.domain.review.value.Comment;
import jp.ne.glory.domain.review.value.GoodPoint;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.Score;
import jp.ne.glory.domain.review.value.ScorePoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class ReviewTest {

    public static class 全ての値が正常に設定されている場合 {

        private Review sut;

        private final ReviewId INIT_ID = new ReviewId(123L);

        @Before
        public void setUp() {

            sut = new Review(INIT_ID);

            sut.gooodPoint = new GoodPoint("良い点：テスト");
            sut.badPoint = new BadPoint("悪い点：テスト");
            sut.comment = new Comment("コメント：テスト");
            sut.score = new Score();
            sut.score.story = ScorePoint.Point5;
            sut.score.operability = ScorePoint.Point4;
            sut.score.addiction = ScorePoint.Point3;
            sut.score.music = ScorePoint.Point2;
            sut.score.loadTime = ScorePoint.Point1;
        }

        @Test
        public void コンストラクタで設定した値がプロパティに設定されている() {

            assertThat(sut.id.isSame(INIT_ID), is(true));
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

            assertThat(sut.id.isSame(INIT_ID), is(true));
            assertThat(sut.gooodPoint.value, is(GoodPoint.empty().value));
            assertThat(sut.badPoint.value, is(BadPoint.empty().value));
            assertThat(sut.comment.value, is(Comment.empty().value));
            assertThat(sut.score, is(not(nullValue())));
            assertThat(sut.score.addiction, is(ScorePoint.NotInput));
            assertThat(sut.score.loadTime, is(ScorePoint.NotInput));
            assertThat(sut.score.music, is(ScorePoint.NotInput));
            assertThat(sut.score.operability, is(ScorePoint.NotInput));
            assertThat(sut.score.story, is(ScorePoint.NotInput));
        }

        @Test
        public void isRegisteredにfalseが設定されている() {

            assertThat(sut.isRegistered(), is(false));
        }
    }
}
