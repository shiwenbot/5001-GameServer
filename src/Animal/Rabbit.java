package Animal;

import Exceptions.ExceedMoveRangeException;
import Exceptions.NotStraightLineException;

public class Rabbit extends Animal {

    // 私有构造方法，防止外部实例化
    public Rabbit(String name) {
        super(name);
    }

    /**
     * 1.可以走一步或者跳两步，
     * 不可以跳过怪物和动物，也就是说有可能错过宝物
     * 如果跳过的格子是怪物，会强制让兔子和怪物在一个格子
     * 如果是动物的话就跳失败了，留在原地
     *
     * 2.Exception需要包括
     * 移动了超过两步，不是直线，越界？？？
     * */
    @Override
    public boolean move(int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        int rowMovement = Math.abs(oldRow - newRow);
        int colMovement = Math.abs(oldCol - newCol);
        //超出范围
        if(rowMovement > 2 || colMovement > 2){
            throw new Exception("Rabbit is trying to move to far!");
        }
        //不走直线
        else if(!isStraightLine(oldRow, oldCol, newRow, newCol)){
            throw new Exception("This animal is not moving by a straight line!");
        }
        //跳
        else if(rowMovement == 2 || colMovement == 2){
            //判断怪物在第一步或者第二步
            return true;
        }
        return true;
    }

    //判断兔子是不是走的直线
    public boolean isStraightLine(int oldRow, int oldCol, int newRow, int newCol){
        int rowMovement = Math.abs(oldRow - newRow);
        int colMovement = Math.abs(oldCol - newCol);
        if((oldRow == newRow) || (oldCol == newCol) || (rowMovement == colMovement)){
            return true;
        }
        //(2,1)(1,2)
        else if((rowMovement == 2 && colMovement == 1) ||
                (rowMovement == 1 && colMovement == 2)){
            return false;
        }
        //(2,2)
        return true;
    }

    private boolean withCreature(){
        return true;
    }
}
