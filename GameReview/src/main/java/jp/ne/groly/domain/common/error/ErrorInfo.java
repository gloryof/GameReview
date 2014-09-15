package jp.ne.groly.domain.common.error;

/**
 * エラー情報.
 * @author Junki Yamada
 */
public enum ErrorInfo {

    /** 必須入力チェックエラー. */
    Required("{0}は必須です。"),

    /** 全選択チェックエラー. */
    RequiredSelectOne("{0}は一つ選択してください。"),

    /** 文字数オーバーエラー. */
    MaxLengthOver("{0}は{1}文字以内で入力してください。"),

    /** 使用不可能文字列エラー. */
    InvalidCharacters("{0}に使用できない文字列が含まれています。"),

    /** 選択パターンエラー. */
    SelectedPattern("{0}には{1}を1つ以上選択してください。"),
    
    /** 未登録エラー. */
    NotRegister("登録されていない{0}です。"),
    ;

    /** メッセージ. */
    public final String message;

    /**
     * コンストラクタ.
     * @param paramMessage メッセージ 
     */
    private ErrorInfo(final String paramMessage) {

        message = paramMessage;
    }
}
