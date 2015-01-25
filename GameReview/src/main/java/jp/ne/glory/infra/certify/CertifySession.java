package jp.ne.glory.infra.certify;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import jp.ne.glory.domain.user.value.UserId;
import lombok.Getter;

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
    private UserId userId = null;

    /**
     * 有効フラグ.<br>
     * 有効な場合：true、無効な場合：false
     */
    @Getter
    private boolean active = false;
}
