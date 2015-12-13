package jp.ne.glory.infra.db.framework;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;

/**
 * DataSourceプロデューサ.
 *
 * @author Junki Yamada
 */
@ApplicationScoped
public class DataSourceProducer {

    /**
     * データソース.
     */
    private final DataSource dataSource;

    /**
     * コンストラクタ.
     *
     * @throws NamingException JNDIに失敗した場合にスローする
     */
    public DataSourceProducer() throws NamingException {

        dataSource = (DataSource) InitialContext.doLookup("jdbc/GameReview");
    }

    /**
     * データソースを返却する.
     *
     * @return データソース.
     */
    @Produces
    public DataSource getDataSouce() {

        return dataSource;
    }

    /**
     * Diarectの返却.<br>
     * Alternativeを使えばPostgreSQL以外も返せるが、<br>
     * PostgreSQLを想定しているので固定で返却.
     *
     * @return Dialectオブジェクト
     */
    @Produces
    public Dialect getDialect() {

        return new PostgresDialect();
    }
}
