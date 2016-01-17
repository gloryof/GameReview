package jp.ne.glory.web.framework.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ext.ParamConverter;

/**
 * LocalDateコンバータ.<br>
 * <br>
 * 文字列から変換する場合の許容するパターンは下記。<br>
 * ・yyyyMMdd<br>
 * ・yyyy/MM/dd<br>
 * ・yyyy-MM-dd<br>
 * <br>
 * 日付から文字列に変換した場合は下記のパターンで返却する。<br>
 * ・yyyy-MM-dd<br>
 *
 * @author Junki Yamada
 */
public class LocalDateConverter implements ParamConverter<LocalDate> {

    /**
     * 数字のみのスタイル.
     */
    private static final String NUMBER_ONLY_STYLE = "uuuuMMdd";

    /**
     * スラッシュ区切りのスタイル.
     */
    private static final String SLASH_STYLE = "uuuu/MM/dd";

    /**
     * ハイフン区切りのスタイル.
     */
    private static final String HYPHEN_STYLE = "uuuu-MM-dd";

    /**
     * デフォルトのスタイル.
     */
    private static final String DEFAULT_STYLE = HYPHEN_STYLE;

    /**
     * 数字のみの場合の正規表現.
     */
    private static final String REGEX_NUMBER_ONLY = "[0-9]{8}";

    /**
     * スラッシュ区切りの場合の正規表現.
     */
    private static final String REGEX_SLASH_STYLE = "[0-9]{4}/[01]?[0-9]/[0123]?[0-9]$";

    /**
     * ハイフン区切りの正規表現.
     */
    private static final String REGEX_HYPEHN_STYLE = "[0-9]{4}-[01][0-9]-[0123]?[0-9]$";

    /**
     * デフォルト形式の正規表現.
     */
    private static final String REGEX_DEFAULT = REGEX_HYPEHN_STYLE;

    /**
     * 文字からLocalDateに変換する.<br>
     * パターンにマッチしていない場合はBadRequestExceptionがスローされる
     *
     * @param inputValue 入力値
     * @return 日付オブジェクト
     * @throws BadRequestException 入力パターンにマッチしていない場合
     */
    @Override
    public LocalDate fromString(final String inputValue) {

        if (inputValue == null || inputValue.isEmpty()) {

            return null;
        }

        if (!isValidFormat(inputValue)) {

            throw new BadRequestException("Invalid format.Paramter value is [" + inputValue + "].");
        }

        try {

            return convert(inputValue);
        } catch (final DateTimeParseException parseException) {

            throw new BadRequestException("Invalid date.Paramter value is [" + inputValue + "].", parseException);
        }
    }

    /**
     * LocalDateからStringに変換する.
     *
     * @param inputValue 入力値
     * @return 文字列
     */
    @Override
    public String toString(final LocalDate inputValue) {

        if (inputValue == null) {

            return null;
        }

        return inputValue.format(DateTimeFormatter.ofPattern(DEFAULT_STYLE));
    }

    /**
     * 日付オブジェクトに変換する.
     *
     * @param inputValue 入力値
     * @return 日付オブジェクト
     */
    private LocalDate convert(final String inputValue) {

        final DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern(getParsePattern(inputValue))
                .withResolverStyle(ResolverStyle.STRICT);

        return LocalDate.parse(inputValue, formatter);
    }

    /**
     * 日付に変化する際のパターンを取得する.
     *
     * @param inputValue 入力値
     * @return パターン文字列
     */
    private String getParsePattern(final String inputValue) {

        if (isNumberOnly(inputValue)) {

            return NUMBER_ONLY_STYLE;
        }

        if (isSlashStyle(inputValue)) {

            return SLASH_STYLE;
        }
        return HYPHEN_STYLE;
    }


    /**
     * 数値のみかを判定する.
     *
     * @param inputValue 入力値
     * @return 数字のみの場合：true、数字以外ばある場合:false
     */
    private boolean isNumberOnly(final String inputValue) {

        return inputValue.matches(REGEX_NUMBER_ONLY);
    }

    /**
     * スラッシュ区切りかを判定する.
     *
     * @param inputValue 入力値
     * @return スラッシュ区切りの場合：true、スラッス区切り以外の場合：false
     */
    private boolean isSlashStyle(final String inputValue) {

        return inputValue.matches(REGEX_SLASH_STYLE);
    }

    /**
     * 対応しているフォーマットかを判定する.
     *
     * @param inputValue
     * @return 対応しているフォーマットの場合：true、対応していないフォーマットの場合：false
     */
    private boolean isValidFormat(final String inputValue) {

        final List<Predicate<String>> matchers = new ArrayList<>();

        matchers.add(v -> isNumberOnly(v));
        matchers.add(v -> isSlashStyle(v));
        matchers.add(v -> v.matches(REGEX_DEFAULT));

        return matchers
                .stream()
                .map(fn -> fn.test(inputValue))
                .filter(v -> v)
                .findAny()
                .orElse(false);
    }
}
