package jp.ne.groly.domain.user.value;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import jp.ne.groly.domain.common.error.ErrorInfo;
import jp.ne.groly.domain.common.error.ValidateError;
import jp.ne.groly.domain.common.error.ValidateErrors;
import jp.ne.groly.domain.common.type.Validatable;

/**
 * ユーザが保持する権限.
 * @author Junki Yamada
 */
public class Authorities implements Validatable {

    /** ラベル. */
    public static final String LABEL = "権限";

    /** 権限セット. */
    private final Set<Authority> authoritySet;

    /**
     * コンストラクタ.
     * @param paramList 権限リスト
     */
    public Authorities(final List<Authority> paramList) {

        authoritySet = paramList.stream().collect(Collectors.toSet());
    }

    /**
     * コンストラクタ.
     */
    public Authorities() {

        authoritySet = new HashSet<>();
    }

    /**
     * 権限を保持するか判定する.
     * @param paramAuthority 権限
     * @return 保持している場合：true、保持していない場合：false
     */
    public boolean hasAuthority(final Authority paramAuthority) {

        return authoritySet.contains(paramAuthority);
    }

    /**
     * 権限を追加する.
     * @param authority 権限
     */
    public void add(final Authority authority) {

        Optional<Authority> optional = Optional.ofNullable(authority);

        optional.ifPresent(v -> authoritySet.add(v));
    }

    /**
     * 入力情報の検証を行う.
     * @return 検証結果
     */
    @Override
    public ValidateErrors validate() {

        final ValidateErrors errors = new ValidateErrors();

        if (authoritySet.isEmpty()) {

            errors.add(new ValidateError(ErrorInfo.RequiredSelectOne, LABEL));
        }

        return errors;
    }
}
