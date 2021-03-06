package jp.ne.glory.domain.common.validate;

import jp.ne.glory.domain.common.error.ValidateErrors;

/**
 * 入力チェックインターフェイス.
 *
 * @author Junki Yamada
 */
public interface ValidateRule {

    /**
     * 入力チェックを行う.<br>
     * 入力チェックエラーがない場合は空のValidateErrorsを返却すること
     *
     * @return 入力チェック結果
     */
    ValidateErrors validate();
}
