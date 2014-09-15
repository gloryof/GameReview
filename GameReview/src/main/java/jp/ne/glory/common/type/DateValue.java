package jp.ne.glory.common.type;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 日付の値を扱う.
 * @author Junki Yamada
 */
public class DateValue {

    /** デフォルトの日付形式. */
    private static final String DEFAULT_FORMAT = "yyyy/MM/dd";

    /** 値. */
    private final Optional<LocalDate> value;
    
    /**
     * コンストラクタ.<br>
     * 値を設定する
     * @param paramValue 値 
     */
    public DateValue(final LocalDate paramValue) {

        value = Optional.ofNullable(paramValue);
    }

    public DateValue(final int year, final int month, final int day) {

        value = Optional.of(LocalDate.of(year, month, day));
    }

    /**
     * 空の値を返却する.
     * @return 空の値が設定されたオブジェクト
     */
    public static DateValue empty() {

        return new DateValue(null);
    }

    /**
     * 値を返却する.<br>
     * 値がNullの場合はNullが返却される。
     * 
     * @return  値
     */
    public LocalDate getValue() {

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

    /**
     * 値の比較を行う.<br>
     * Nullが渡された場合falseを返却する。
     * 
     * @param compareValue
     * @return 値が一致している場合:true、一致したいない場合:false
     */
    public boolean isSamveValue(final DateValue compareValue) {

        if  (compareValue == null) {

            return false;
        }

        if (!value.isPresent()) {

            return false;
        }

        return value.get().equals(compareValue.getValue());
    }
}
