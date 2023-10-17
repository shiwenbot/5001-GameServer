package Animal;

import Exceptions.ExceedMoveRangeException;
import Exceptions.NotStraightLineException;

public class Rabbit extends Animal {

    // ˽�й��췽������ֹ�ⲿʵ����
    public Rabbit(String name) {
        super(name);
    }

    /**
     * 1.������һ��������������
     * ��������������Ͷ��Ҳ����˵�п��ܴ������
     * ��������ĸ����ǹ����ǿ�������Ӻ͹�����һ������
     * ����Ƕ���Ļ�����ʧ���ˣ�����ԭ��
     *
     * 2.Exception��Ҫ����
     * �ƶ��˳�������������ֱ�ߣ�Խ�磿����
     * */
    @Override
    public boolean move(int oldRow, int oldCol, int newRow, int newCol) throws Exception {
        int rowMovement = Math.abs(oldRow - newRow);
        int colMovement = Math.abs(oldCol - newCol);
        //������Χ
        if(rowMovement > 2 || colMovement > 2){
            throw new Exception("Rabbit is trying to move to far!");
        }
        //����ֱ��
        else if(!isStraightLine(oldRow, oldCol, newRow, newCol)){
            throw new Exception("This animal is not moving by a straight line!");
        }
        //��
        else if(rowMovement == 2 || colMovement == 2){
            //�жϹ����ڵ�һ�����ߵڶ���
            return true;
        }
        return true;
    }

    //�ж������ǲ����ߵ�ֱ��
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
