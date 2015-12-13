package jp.ne.glory.infra.db.user.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

/**
 * ユーザ情報テーブル.
 *
 * @author Junki Yamada
 */
@Entity
public class UserInfo {

    /**
     * ユーザID.
     */
    @Getter
    @Setter
    @Id
    private long userId;

    /**
     * ユーザ名.
     */
    @Getter
    @Setter
    private String userName;

    /**
     * ロック管理タイムスタンプ.
     */
    @Getter
    @Setter
    private LocalDateTime lockUpdateTimestamp;
}
