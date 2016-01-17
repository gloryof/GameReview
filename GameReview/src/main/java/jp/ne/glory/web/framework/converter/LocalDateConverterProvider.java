
package jp.ne.glory.web.framework.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;

/**
 * 日付パラメータコンバータのプロバイダ.
 *
 * @author Junki Yamada
 */
public class LocalDateConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> type, Type type1, Annotation[] antns) {

        if (type == LocalDate.class) {

            return (ParamConverter<T>) new LocalDateConverter();
        }

        return null;
    }

}
