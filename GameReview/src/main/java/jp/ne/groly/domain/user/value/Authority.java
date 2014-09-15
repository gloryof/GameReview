package jp.ne.groly.domain.user.value;

import java.util.Arrays;
import java.util.Optional;

/**
 * 権限.
 * @author Junki Yamada
 */
public enum Authority {

    None(-1, "権限なし"),
    ConfigChange(1, "設定の変更"),
    ReviewPost(2, "レビュー投稿");

    /** タイプID. */
    public final int authorityId;

    /** 名前. */
    public final String name;

    /**
     * コンストラクタ
     * @param paramAuthorityId 権限ID
     * @param paramName 名前
     */
    private Authority(final int paramAuthorityId,final String paramLabel) {

        authorityId = paramAuthorityId;
        name = paramLabel;
    }

    /**
     * IDから権限を取得する.
     * @param id ID
     * @return  権限
     */
    public static Authority getByAuthorityId(final int id) {

        Optional<Authority> result =
                Arrays.stream(Authority.values()).filter(value -> value.authorityId == id).findFirst();

        return result.orElse(None);
    }
}
