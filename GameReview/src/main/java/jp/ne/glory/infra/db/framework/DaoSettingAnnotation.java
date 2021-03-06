package jp.ne.glory.infra.db.framework;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;

/**
 * DAOの共通設定アノテーション.
 *
 * @author Junki Yamada
 */
@AnnotateWith(annotations = {
    @Annotation(target = AnnotationTarget.CLASS, type = Dependent.class),
    @Annotation(target = AnnotationTarget.CONSTRUCTOR, type = Inject.class)
})
public @interface DaoSettingAnnotation {

}
