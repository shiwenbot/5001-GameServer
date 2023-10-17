import Animal.Animal;

public class Square {
    private int row;
    private int col;
    private Animal animal;
    private boolean hasAnimal = false;
    //boolean visible;
    //Creature.Creature creature;

    Square(int row, int col){
        this.row = row;
        this.col = col;
    }
    /*
    * 1.һ������ֻ����һ���������һ������͹���
    * */
    boolean hasAnimal(){
        return hasAnimal;
    }
    //���Ķ����λ�ã���col��row��ֵ
    void setAnimal(Animal animal) throws CustomExceptions.AnimalExistsException {
        if(this.hasAnimal()){
            throw new CustomExceptions.AnimalExistsException("There is an aninal in this Square!");
        }
        else{
            animal.setCol(this.col);
            animal.setRow(this.row);
            this.hasAnimal = true;
        }
    }





    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}