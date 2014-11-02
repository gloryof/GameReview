package jp.ne.glory.common.type;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 日時の情報を扱う.
 *
 * @author Junki
 */
public class DateTimeValue {

    /**
     * デフォルトの日付形式.
     */
    private static final String DEFAULT_FORMAT = "yyyy/MM/dd HH:mm:ss";

    /**
     * 値.
     */
    private final Optional<LocalDateTime> value;

    /**
     * コンストラクタ.
     *
     * @param paramValue 値
     */
    public DateTimeValue(final LocalDateTime paramValue) {

        value = Optional.ofNullable(paramValue);
    }

    /**
     * 空の値を返却する.
     *
     * @return 空の値が設定されたオブジェクト
     */
    public static DateTimeValue empty() {

        return new DateTimeValue(null);
    }

    /**
     * 値を返却する.<br>
     * 値がNullの場合はNullが返却される。
     *
     * @return 値
     */
    public LocalDateTime getValue() {

        if (!value.isPresent()) {

            return null;
        }

        return value.get();
    }

    /**
     * 日付形式にフォーマットする.<br>
     * フォーマット形式は「YYYY/MM/DD」。<br>
     * valueが設定されていない場合はブランクを返却する
     *
     * @return フォーマット後文字列
     */
    public String format() {

        if (!value.isPresent()) {

            return "";
        }

        return value.get().format(DateTimeFormatter.ofPattern(DEFAULT_FORMAT));
    }

}
