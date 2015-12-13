
package jp.ne.glory.infra.db.user.entity;

import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

/**
 * ユーザ権限テーブル.
 *
 * @author Junki Yamada
 */
@Entity
public class UserAuthority {

    /**
     * ユーザID.
     */
    @Getter
    @Setter
    @Id
    private long userId;

    /**
     * 権限ID.
     */
    @Getter
    @Setter
    @Id
    private Integer authorityId;
}
