/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ne.glory.domain.review.repository;

import java.util.List;
import java.util.Optional;
import jp.ne.glory.domain.review.entity.Review;
import jp.ne.glory.domain.review.value.ReviewId;
import jp.ne.glory.domain.review.value.search.ReviewSearchCondition;
import jp.ne.glory.domain.review.value.search.ReviewSearchResult;

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

    /**
     * レビューを検索する.
     *
     * @param condition 検索条件
     * @return 検索結果
     */
    List<ReviewSearchResult> search(final ReviewSearchCondition condition);

    /**
     * レビュー検索の件数を取得する.
     *
     * @param condition 検索条件
     * @return 件数
     */
    int getSearchCount(final ReviewSearchCondition condition);
}
