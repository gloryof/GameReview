package jp.ne.glory.infra.certify;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.ws.rs.NameBinding;

/**
 * 認証チェックを用のアノテーション.<br>
 * 認証チェックを行う箇所に対して付与する。
 *
 * @author Junki Yamada
 */
@NameBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CertifyTarget {

}
