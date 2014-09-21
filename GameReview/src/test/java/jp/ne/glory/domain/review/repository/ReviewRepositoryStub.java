package jp.ne.glory.domain.review.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.ReviewId;

public class ReviewRepositoryStub implements ReviewRepository {

    private final Map<Long, Review> reviewMap = new HashMap<>();

    private long sequence = 1;

    @Override
    public ReviewId save(Review review) {

        final Review saveReview;
        if (review.id == null) {

            saveReview = new Review(review.id);
            saveReview.badPoint = review.badPoint;
            saveReview.comment = review.comment;
            saveReview.gooodPoint = review.gooodPoint;
            saveReview.score = review.score;

            sequence++;
        } else {

            saveReview = review;
        }
        reviewMap.put(saveReview.id.value, saveReview);

        return saveReview.id;
    }

    @Override
    public Optional<Review> findBy(ReviewId reviewId) {

        return Optional.ofNullable(reviewMap.get(reviewId.value));
    }

}
