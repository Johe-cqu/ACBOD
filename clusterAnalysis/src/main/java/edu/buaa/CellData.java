package edu.buaa;

public class CellData implements Cloneable {
    private final int len=4;
    private float[] Data;
    private String time;


    public CellData(float[] Data){
        this.Data= new float[len];
        this.Data=Data;
    }
    public CellData(float[] Data,String time){
        this(Data);
        this.time=time;
    }

    //读取数据
    public float[] getData(){
        return Data;
    }

    //读取时间   时间数据不可更改
    public String getTime(){
        return this.time;
    }

    //得到数据长度
    public int getLen() {
        return len;
    }

    //更改数据   (单个float数据,不包括时间)
    public boolean setData(float data,int num){
        if (num<len) {
            this.Data[num]=data;
            return true;
        }
        return false;
    }

    public CellData clone(){
        float[] tempfloat = this.Data.clone();
        return new CellData(tempfloat,this.time);
    }

    public float pointToPoint(CellData bCellData){
        float distance=0;
        for (int i=0;i<this.getLen();i++){
            distance+=Math.pow((this.Data[i]-bCellData.getData()[i]),2);
        }
        return (float) Math.sqrt((float) (distance/this.getLen()));
    }

    //打印数据
    public void printCellData(){
        System.out.print(time+"  ");
        for (int i = 0; i <len; i++) {
            System.out.print(Data[i]);//显示链表内各个数字
            System.out.print("  ");
        }
        System.out.println("");
    }
}
