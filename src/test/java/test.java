import com.ylinor.brawlator.MonsterAction;
import com.ylinor.brawlator.data.beans.MonsterBean;
import com.ylinor.brawlator.data.beans.MonsterBuilder;
import com.ylinor.brawlator.data.dao.MonsterDAO;
import com.ylinor.brawlator.data.handler.SqliteHandler;
import org.junit.Test;

import java.util.Optional;

public class test {

    @Test
  public void test(){
        SqliteHandler.testConnection();
    }
    @Test
    public void insertMonster(){

        MonsterDAO monsterDAO = new MonsterDAO();
        MonsterBean monsterBean = new MonsterBuilder().name("Archer Myope").type("skeleton")
                .effect("FIRE_RESISTANCE",99999 ,5)//.effect("INVISIBILITY",99999,1)
                .build();
        monsterDAO.insert(monsterBean);
    }
    @Test
    public void getmonster(){
        Optional<MonsterBean> monster = MonsterAction.getMonster(1);
       System.out.print(monster.get().getName());
    }
}
