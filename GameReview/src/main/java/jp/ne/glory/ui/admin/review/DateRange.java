package jp.ne.glory.ui.admin.review;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * 日付の期間.
 *
 * @author Junki Yamada
 */
public class DateRange {

    /**
     * From.
     */
    @Getter
    @Setter

    private LocalDate from;

    /**
     * To.
     */
    @Getter
    @Setter
    private LocalDate to;
}
