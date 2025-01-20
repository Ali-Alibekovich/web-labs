package org.example;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DbProvider {
    private static DataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            PGSimpleDataSource ds = new PGSimpleDataSource();

            ds.setUrl("jdbc:postgresql://localhost:5432/postgres");
            ds.setUser("postgres");
            ds.setPassword("postgres");

            dataSource = ds;
        }

        return dataSource;
    }
}