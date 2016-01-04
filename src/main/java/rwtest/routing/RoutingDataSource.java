package rwtest.routing;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	protected Object determineCurrentLookupKey() {
		logger.debug(String.format(">>> determineCurrentLookupKey thread: %s", Thread.currentThread().getName()));
		logger.debug(String.format(">>> RoutingDataSource: %s", DbContextHolder.getDbType()));
        return DbContextHolder.getDbType();
	}

}
