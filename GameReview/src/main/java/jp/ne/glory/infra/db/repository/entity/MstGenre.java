package jp.ne.glory.infra.db.repository.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

/**
 * ジャンルマスタテーブル.
 *
 * @author Junki Yamada
 */
@Entity
public class MstGenre {

    /**
     * ジャンルID.
     */
    @Getter
    @Setter
    @Id
    private long genreId;

    /**
     * ジャンル名.
     */
    @Getter
    @Setter
    private String genreName;

    /**
     * ロック管理タイムスタンプ.
     */
    @Getter
    @Setter
    private LocalDateTime lockUpdateTimestamp;
}
