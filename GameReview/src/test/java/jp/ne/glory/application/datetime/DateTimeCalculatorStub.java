package jp.ne.glory.application.datetime;

import java.time.LocalDateTime;
import jp.ne.glory.common.type.DateTimeValue;

public class DateTimeCalculatorStub extends DateTimeCalculator {

    private final LocalDateTime currentTime = LocalDateTime.now();

    @Override
    public DateTimeValue getCurrentDateTime() {

        return new DateTimeValue(currentTime);
    }
}
