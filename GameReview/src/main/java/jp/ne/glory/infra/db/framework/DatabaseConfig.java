package jp.ne.glory.infra.db.framework;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * データベース設定.
 *
 * @author gloryof
 */
@ApplicationScoped
public class DatabaseConfig implements Config {

    /**
     * データソース.
     */
    private final DataSource dataSource;

    /**
     * ダイアレクト.
     */
    private final Dialect dialect;

    /**
     * コンストラクタ.
     *
     * @param dataSource データソース
     * @param dialect ダイアレクト
     */
    @Inject
    public DatabaseConfig(final DataSource dataSource, final Dialect dialect) {

        this.dataSource = dataSource;
        this.dialect = dialect;
    }

    /**
     * データソースの返却.
     *
     * @return データソース
     */
    @Override
    public DataSource getDataSource() {

        return dataSource;
    }

    /**
     * Dialectの返却.
     *
     * @return Dialectオブジェクト
     */
    @Override
    public Dialect getDialect() {

        return dialect;
    }

    /**
     * ネーミングルール.<br>
     * 小文字のスネークケースをDB側のルールとしている。
     *
     * @return Namingオブジェクト
     */
    @Override
    public Naming getNaming() {

        return Naming.LENIENT_SNAKE_LOWER_CASE;
    }

}
