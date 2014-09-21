/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ne.glory.domain.review.repository;

import java.util.Optional;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.ReviewId;

/**
 * レビューリポジトリ.
 *
 * @author Junki Yamada
 */
public interface ReviewRepository {

    /**
     * レビューを保存する.
     *
     * @param review レビュー
     * @return 保存したレビューID
     */
    ReviewId save(final Review review);

    /**
     * レビューIDをキーにレビューを検索する.
     *
     * @param reviewId レビューID
     * @return レビュー
     */
    Optional<Review> findBy(final ReviewId reviewId);
}
