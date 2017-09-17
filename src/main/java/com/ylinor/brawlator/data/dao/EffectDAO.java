package com.ylinor.brawlator.data.dao;

import com.ylinor.brawlator.data.beans.EffectBean;
import com.ylinor.brawlator.data.handler.SqliteHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EffectDAO {
    public static String tableCreation = "CREATE TABLE IF NOT EXISTS effect (\n" +
            " id integer PRIMARY KEY AUTOINCREMENT\n" +
            ", effect_type VARCHAR(50)\n" +
            ", duration int\n" +
            ", amplifier int\n" +
            ", monster_id integer"+
            ",FOREIGN KEY(monster_id) REFERENCES monster(id)"+
            ");";
    private final String insertStatement = "INSERT INTO effect(effect_type,duration,amplifier, monster_id)" +
            " values (?, ?, ?,?);";
    private  final String selectStatement = "SELECT * From effect ";

    private final String deleteStatement = "DELETE From effect where id = ?";

    public  Optional<List<EffectBean>> getEffectsById(int id) {
        String select = "SELECT effect_type, duration, amplifier,monster_id FROM effect WHERE monster_id = ?";
        Connection conn = SqliteHandler.getConnection();
        try{
            PreparedStatement stmt = conn.prepareStatement(select);
            stmt.setInt(1,id);
            ResultSet resultSet = stmt.executeQuery();
            List<EffectBean> effects = new ArrayList<>();

            while(resultSet.next()) {
                EffectBean effectBean = new EffectBean(resultSet.getString("effect_type"),
                        resultSet.getInt("duration"),resultSet.getInt("amplifier"));

                effectBean.setMonsterId(resultSet.getInt("monster_id"));
                effects.add(effectBean);
            }
            return Optional.of(effects);
        } catch( SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<EffectBean> getEffect(int id) {
        String select = "SELECT effect_type, duration, amplifier FROM effect WHERE id = ?";
        Connection conn = SqliteHandler.getConnection();
        try {

            PreparedStatement stmt = conn.prepareStatement(select);
            stmt.setInt(1,id);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                EffectBean effectBean = new EffectBean(resultSet.getString("effect_type")
                        ,resultSet.getInt("duration"),resultSet.getInt("amplifier"));
                effectBean.setMonsterId(resultSet.getInt("monster_id"));
                return Optional.of(effectBean);

            }
            return Optional.empty();

        } catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
        finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    public boolean insert(EffectBean effect){
        Connection conn = SqliteHandler.getConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertStatement) ;
            preparedStatement.setString(1,effect.getType());
            preparedStatement.setInt(2,effect.getDuration());
            preparedStatement.setInt(3,effect.getAmplifier());
            preparedStatement.setInt(4,effect.getMonsterId());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
