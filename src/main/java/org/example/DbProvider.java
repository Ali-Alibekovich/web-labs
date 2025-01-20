package org.example;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DbProvider {
    private static DataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            PGSimpleDataSource ds = new PGSimpleDataSource();

            ds.setUrl("jdbc:postgresql://localhost:5432/studs");
            ds.setUser("s285317");
            ds.setPassword("");
            ds.setCurrentSchema("");

            dataSource = ds;
        }

        return dataSource;
    }
}