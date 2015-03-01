package jp.ne.glory.infra.certify;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import jp.ne.glory.domain.user.value.UserId;
import jp.ne.glory.domain.user.value.UserName;
import lombok.Getter;
import lombok.Setter;

/**
 * 認証のセッション情報
 *
 * @author Junki Yamada
 */
@SessionScoped
public class CertifySession implements Serializable {

    /**
     * ユーザID.
     */
    @Getter
    @Setter
    private UserId userId = UserId.notNumberingValue();

    /**
     * ユーザ名.
     */
    @Getter
    @Setter
    private UserName userName = UserName.empty();

    /**
     * 有効フラグ.<br>
     * 有効な場合：true、無効な場合：false
     */
    @Getter
    @Setter
    private boolean active = false;
}
