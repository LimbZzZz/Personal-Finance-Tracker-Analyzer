package org.example.Service;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
@Profile("test")
public class TestDataCleanupService implements DisposableBean {
    private final DataSource dataSource;

    @Autowired
    public TestDataCleanupService(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("Очистка данных до завершения");
        cleanupTestData();
    }

    private void cleanupTestData(){
        try(Connection connection = dataSource.getConnection()) {
            connection.createStatement().executeUpdate(
                    "DROP TABLE transactions"
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
