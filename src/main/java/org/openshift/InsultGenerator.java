package org.openshift;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class InsultGenerator {

    public String generateInsult() {
        String vowels = "AEIOU";
        String article = "an";
        String theInsult = "";

        try {
            String databaseURL = "jdbc:postgresql://";
            databaseURL += System.getenv("POSTGRESQL_SERVICE_HOST");
            databaseURL += "/" + System.getenv("POSTGRESQL_DATABASE");

            String username = System.getenv("POSTGRESQL_USER");
            String password = System.getenv("PGPASSWORD");
            Connection con = DriverManager.getConnection(databaseURL, username, password);

            if (con != null) {
                String sql = "select a.string as first, b.string as second, c.string as noun from short_adjective a, long_adjective b, noun c order by random() limit 1";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    if (vowels.indexOf(rs.getString("first").charAt(0)) == -1) {
                        article = "a";
                    }
                    theInsult = String.format("DB says: Thou art %s %s %s %s!", article,
                        rs.getString("first"),
                        rs.getString("second"),
                        rs.getString("noun"));
                }
                rs.close();
                con.close();
            }
        } catch (Exception e) {
            return "Database connection problem!";
        }
        return theInsult;
    }
}

