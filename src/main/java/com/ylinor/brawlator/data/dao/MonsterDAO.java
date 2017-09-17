package com.ylinor.brawlator.data.dao;

import com.ylinor.brawlator.data.beans.EffectBean;
import com.ylinor.brawlator.data.beans.MonsterBean;
import com.ylinor.brawlator.data.beans.MonsterBuilder;
import com.ylinor.brawlator.data.handler.SqliteHandler;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class MonsterDAO {
    public static String tableCreation = "CREATE TABLE IF NOT EXISTS monster (\n" +
            " id integer PRIMARY KEY AUTOINCREMENT,\n" +
            " name VARCHAR(50),\n" +
            " entity_type VARCHAR(50),\n" +
            " hp real,\n" +
            " attack_damage real ,\n" +
            " speed real,\n" +
            " knockback_resistance int\n" +
            ");";
    private final String insertStatement = "INSERT INTO monster(name,entity_type,hp,attack_damage, speed, knockback_resistance)" +
            " values (?, ?, ?, ?, ?,?);";
    private  final String selectStatement = "SELECT * From monster ";

    private final String deleteStatement = "DELETE From monster where id = ?";

    public Optional<MonsterBean> getMonster(int id){
        String select = "SELECT * From monster where id = ?";
        Connection conn = SqliteHandler.getConnection();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(select);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){

                MonsterBuilder monsterBuilder= new MonsterBuilder();
                monsterBuilder.name(resultSet.getString("name"));
                monsterBuilder.type(resultSet.getString("entity_type"));
                monsterBuilder.hp(resultSet.getDouble("hp"));
                monsterBuilder.attackDamage(resultSet.getDouble("attack_damage"));
                monsterBuilder.speed(resultSet.getDouble("speed"));
                monsterBuilder.knockbackResistance(resultSet.getInt("knockback_resistance"));

                MonsterBean monsterBean = monsterBuilder.build();
                monsterBean.setId(resultSet.getInt("id"))
                ;
                EffectDAO effectDAO = new EffectDAO();
                Optional<List<EffectBean>> effects = effectDAO.getEffectsById(monsterBean.getId());
                if(effects.isPresent()) {
                    monsterBean.setEffectLists(effects.get());
                }

                return Optional.ofNullable(monsterBean);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Integer> getLastId(){
        String select = "select max(id) as id from Monster";
        Connection conn = SqliteHandler.getConnection();
        try{
           ResultSet resultSet = conn.createStatement().executeQuery(select);
           if (resultSet.next()) {
              return Optional.of(resultSet.getInt("id"));
           }
            return Optional.empty();
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void insert(MonsterBean monsterBean) {
        try {
            Connection conn = SqliteHandler.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(this.insertStatement);

            pstmt.setString(1,monsterBean.getName());
            pstmt.setString(2,monsterBean.getType() );
            pstmt.setDouble(3,monsterBean.getHp() );
            pstmt.setDouble(4,monsterBean.getAttackDamage() );
            pstmt.setDouble(5,monsterBean.getSpeed() );
            pstmt.setInt(6,monsterBean.getKnockbackResistance());
            pstmt.executeUpdate();

            EffectDAO effectDAO = new EffectDAO();
            for (EffectBean effect:
                monsterBean.getEffectLists()) {
                Optional<Integer> id = getLastId();
                if(id.isPresent()) {
                    effect.setMonsterId(id.get());
                    effectDAO.insert(effect);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
