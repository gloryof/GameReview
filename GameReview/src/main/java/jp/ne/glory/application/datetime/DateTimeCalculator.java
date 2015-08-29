package jp.ne.glory.application.datetime;

import java.time.LocalDateTime;
import javax.enterprise.context.RequestScoped;
import jp.ne.glory.common.type.DateTimeValue;

/**
 * 日付計算.
 *
 * @author Junki Yamada
 */
@RequestScoped
public class DateTimeCalculator {

    /**
     * 現在の日時を取得する.
     *
     * @return 現在の日時
     */
    public DateTimeValue getCurrentDateTime() {

        return new DateTimeValue(LocalDateTime.now());
    }
}
