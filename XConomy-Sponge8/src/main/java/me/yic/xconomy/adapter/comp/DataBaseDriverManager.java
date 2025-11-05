package me.yic.xconomy.adapter.comp;


import org.spongepowered.api.Sponge;

import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class DataBaseDriverManager {

    public static Connection getConnection(String url) {
        try {
            return Sponge.sqlManager().dataSource(url).getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}
