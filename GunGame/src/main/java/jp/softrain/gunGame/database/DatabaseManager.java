package jp.softrain.gunGame.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jp.softrain.gunGame.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class DatabaseManager {

    private Main instance;
    private HikariDataSource dataSource;

    public DatabaseManager(Main instance){
        this.instance = instance;

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test");
        config.setUsername("test");
        config.setPassword("test");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        config.setMaximumPoolSize(3);

        dataSource = new HikariDataSource(config);
        testConnection();
        createTable();
    }

    private void testConnection(){
        try (Connection connection = dataSource.getConnection()){
            if (connection.isValid(2)){ // 2 Sekunden Timeout
                instance.getLogger().info(instance.getMessagesManager().get("database.connected"));
            } else {
                instance.getLogger().warning(instance.getMessagesManager().get("database.invalid"));
            }
        } catch (SQLException e){
            instance.getLogger().severe(instance.getMessagesManager().get("database.failed", Map.of("%reason%", e.getMessage())));
        }
    }

    public void createTable(){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS stats(uuid VARCHAR(36) PRIMARY KEY, kills INT DEFAULT 0, deaths INT DEFAULT 0, kd DOUBLE DEFAULT 00.00)")){

            preparedStatement.executeUpdate();
        } catch (SQLException e){
            instance.getLogger().severe(e.getMessage());
        }
    }

    public boolean existsInDatabase(UUID uuid){
        try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1 FROM stats WHERE uuid = ?")){

            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        }catch (SQLException e){
            instance.getLogger().severe(e.getMessage());
        }
        return false;
    }

    public void addKill(UUID uuid){
        try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO stats(uuid, kills, deaths, kd) VALUES (?, 0, 0, 00.00) ON DUPLICATE KEY UPDATE kills = kills + 1")){

            preparedStatement.setString(1, uuid.toString());

            preparedStatement.executeUpdate();
            calcKD(uuid);
        } catch (SQLException e){
            instance.getLogger().severe(e.getMessage());
        }
    }
    public void addDeath(UUID uuid){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO stats(uuid, kills, deaths, kd) VALUES (?, 0, 0, 00.00) ON DUPLICATE KEY UPDATE deaths = deaths + 1")){

            preparedStatement.setString(1, uuid.toString());

            preparedStatement.executeUpdate();
            calcKD(uuid);
        } catch (SQLException e){
            instance.getLogger().severe(e.getMessage());
        }
    }
    public int getKills(UUID uuid){
        try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT kills FROM stats WHERE uuid = ?")){

            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return resultSet.getInt("kills");
                //Wenn der Spieler Kills hat -> return kills
            } else {
                return 0;
                //Wenn der Spieler keine Kills hat aber z.B. Deaths
                //return -> 0 (weil keine Kills)
            }
        } catch (SQLException e){
            instance.getLogger().severe(e.getMessage());
        }
        return 0;
        //Wenn der Spieler gar nicht in der Datenbank ist
        //return -> 0 (weil weder deaths noch kills)
    }
    public int getDeaths(UUID uuid){
        try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT deaths FROM stats WHERE uuid = ?")){

            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){

                return resultSet.getInt("deaths");
            } else {
                return 0;
            }
        }catch (SQLException e){
            instance.getLogger().severe(e.getMessage());
        }
        return 0;
    }
    public void calcKD(UUID uuid){
        try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO stats(uuid, kills, deaths, kd) VALUES (?, 0, 0, 00.00) ON DUPLICATE KEY UPDATE kd = COALESCE(kills / NULLIF(deaths, 0), kills)")){

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            instance.getLogger().severe(e.getMessage());
        }
    }
    public double getKD(UUID uuid){
        try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT kd FROM stats WHERE uuid = ?")){

            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return resultSet.getDouble("kd");
            } else {
                return 00.00;
            }
        } catch (SQLException e){
            instance.getLogger().severe(e.getMessage());
        }
        return 00.00;
    }
}
