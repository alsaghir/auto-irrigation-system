package com.github.alsaghir.autoirrigationsystem.infrastructure.db.h2;


import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class V20230514__Base_tables extends BaseJavaMigration {

    /**
     * First create database in specific location using h2 jar
     * and command
     * java -cp .\h2-2.1.214.jar org.h2.tools.Shell
     * <p>
     * Use JDBC URL with local file mode for db creationg like
     * jdbc:h2:file:D:/Code/db
     * jdbc:h2:file:~/db
     * </p>
     * <p>
     * Use the following command to start h2 server
     * java -cp .\h2-2.1.214.jar org.h2.tools.Server
     * </p>
     * <p>
     *
     * @param context The context relevant for this migration, containing things like the JDBC connection to use and the
     *                current Flyway configuration.
     * @throws SQLException for any sql error
     */
    @Override
    public void migrate(Context context) throws SQLException {

        Connection connection = context.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("""
                CREATE TABLE plot (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(255),
                          cultivated_area DOUBLE,
                          crop_type VARCHAR(255)
                      );
                      
                      CREATE TABLE time_slot (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          plot_id INT,
                          start_time TIMESTAMP,
                          end_time TIMESTAMP,
                          amount_of_water DOUBLE,
                          status VARCHAR(50),
                          FOREIGN KEY (plot_id) REFERENCES plot(id)
                      );
                  """)) {

            statement.execute();
        }
    }

}
