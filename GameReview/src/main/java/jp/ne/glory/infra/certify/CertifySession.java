package jp.ne.glory.infra.certify;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import jp.ne.glory.domain.user.value.UserId;

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
    private UserId userId = null;

    /**
     * 有効フラグ.<br>
     * 有効な場合：true、無効な場合：false
     */
    private boolean isActive = false;
}
