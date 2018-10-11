package edu.buaa;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CancellationException;

/*
  CsiData是一个CSI结构体
  建立CsiData的目标是对数据进行分析
 */
public class CsiData {
    private int memberNum;//成员数目
    private MemberIdNode memberHead;//对象标识链表头
    private CellData centroid;//模拟变量质心   添加点时实时更新
    private float outlierFactor;//离群因子   选取该csiData所属的簇时更新
    private boolean abnormalSign;//异常标识  选取该csiData所属的簇时更新
    private float detaC; // 簇内距离    csiDataStrutStruct中更新
    //private int csiId;//CSI的ID

    private class MemberIdNode{//私有类——对象标识链表
        private int memberId;
        private MemberIdNode memberNext;

        public MemberIdNode(int memberId,MemberIdNode memberNext){
            this.memberId=memberId;
            this.memberNext=memberNext;
        }
    }

    //初始化一个CsiData
    //输入的参数： cellDataId为初始节点的对象标识符    cellData为初始节点的所有数据
    public CsiData(int cellDataId,CellData cellData){
        this.memberNum=1;
        this.memberHead = new MemberIdNode(cellDataId,null);
        this.centroid = cellData;
        this.outlierFactor=0;
        this.abnormalSign=false;
    }

    //重写 初始化一个CsiData
    //无输入参数
    public CsiData(){
        this.memberNum=0;
        this.outlierFactor=0;
        this.abnormalSign=false;
    }

    //向对象标识链表中加入一个对象标识
    private void addMemberIdNodeInHead(int memberId){
        MemberIdNode tempNode = this.memberHead;
        this.memberHead = new MemberIdNode(memberId,null);
        this.memberHead.memberNext=tempNode;
    }

    //更新簇质心(模拟数据)  需要在this.memberNum更新完之后进行操作
    private void freshCentroid(@NotNull float[] addData){
        float[] cloneAddData=addData.clone();
        if (this.memberNum>1){
            float[] tempFloat=this.centroid.getData();
            for(int i=0;i<this.centroid.getLen();i++){
                float temp=(tempFloat[i]*(this.memberNum-1)+cloneAddData[i])/this.memberNum;
                this.centroid.setData(temp,i);
            }
        }
        else if (this.memberNum==1){
            this.centroid = new CellData(cloneAddData);
        }
    }

    //向簇中添加一个数据，并更新簇
    //输入的参数： cellDataId为初始节点的对象标识符    cellData为初始节点的所有数据
    public void addOneData(int cellDataId,CellData addData){
        this.memberNum+=1;//成员数目
        this.addMemberIdNodeInHead(cellDataId);//增加对象标识进入对象标识链表
        this.freshCentroid(addData.getData());//更新簇质心
    }

    //克隆一个对象标识链，并返回表头   该复制出来的链表与原链表的顺序相同
    private MemberIdNode cloneMemberIdNodeList(MemberIdNode headMemberIdNode){
        if (headMemberIdNode.memberNext!=null){
            MemberIdNode nextMember = cloneMemberIdNodeList(headMemberIdNode.memberNext);
            return new MemberIdNode(headMemberIdNode.memberId,nextMember);
        }else {
            return new MemberIdNode(headMemberIdNode.memberId,null);
        }
    }

    //克隆一个CsiData    深克隆
    public CsiData cloneCsiData(){
        CsiData tempCsiData = new CsiData();
        tempCsiData.memberNum = this.memberNum;
        tempCsiData.memberHead = this.cloneMemberIdNodeList(this.memberHead);
        tempCsiData.centroid = this.centroid.clone();
        tempCsiData.outlierFactor = this.outlierFactor;
        tempCsiData.abnormalSign = this.abnormalSign;
        tempCsiData.detaC=this.detaC;
        return tempCsiData;
    }

    /**
     * 计算点与簇之间的距离
     * @param point 点
     * @return  距离
     * 已测试
     */
    public float getDisPointToCsiData(CellData point){
        float[] a = point.getData();
        float[] b = this.getCentroid().getData();
        float distance = 0;
        for(int x=0;x<a.length;x++){
            float temp =a[x]-b[x];
            distance +=(float) Math.pow( temp,2);
        }
        distance = (float) Math.sqrt(distance/a.length);   //除以数据种类的个数再开根号
        return distance;
    }

    /**
     * 计算簇与簇之间的距离
     * @param bCsiData 新的簇
     * @return 簇与簇之间的距离
     * 在没有离散数据的情况下，之间采用新簇的质心与旧簇的距离即可
     * 已测试
     */
    public float getDisCsiDataToCsiData(CsiData bCsiData){
        return this.getDisPointToCsiData(bCsiData.getCentroid());
    }


    /**
     * 获得MemberId的升序ArrayList
     * @return  升序ArrayList
     * 已测试
     */
    public ArrayList<Integer> getMemerIdArrayList(){
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (MemberIdNode tempNode = this.memberHead;tempNode!=null;tempNode=tempNode.memberNext){
            integerArrayList.add(tempNode.memberId);
        }
        Comparator c = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1<o2)
                    return -1;  //-1表示向数组前方移动
                else
                    return 1;  //1表示向数组后方移动
            }
        };
        integerArrayList.sort(c);
        return integerArrayList;
    }


    public void setAbnormalSign(boolean abnormalSign) {
        this.abnormalSign = abnormalSign;
    }

    public boolean getAbnomalSign(){
        return this.abnormalSign;
    }

    public void setOutlierFactor(float oF){
        this.outlierFactor=oF;
    }

    public float getOutlierFactor(){
        return this.outlierFactor;
    }

    public int getHeadMemberId(){
        return this.memberHead.memberId;
    }

    //确认memberHead是否为空
    public boolean isNullMemberHead(){
        return this.memberHead==null;
    }

    //将MemberHead指向下一个Member
    public void nextMemberHead(){
        this.memberHead=this.memberHead.memberNext;
    }

    //copy一个CsiData内的MemberHead
    public void copyCsiDataHead(CsiData oldCsiData){
        this.memberHead = oldCsiData.memberHead;
    }

    public CellData getCentroid(){return this.centroid;}

    public int getMemberNum(){return this.memberNum;}

    public float getDetaC(){
        return this.detaC;
    }

    public void setDetaC(float detaC){
        this.detaC=detaC;
    }

    /**
     * 根据数据集得到DetaC
     * @param dataSet 数据集
     * 已测试
     */
    public void setDetaC(DataSet dataSet){
        int memberNum=this.getMemberNum();
        ArrayList<Integer> idArrayList = this.getMemerIdArrayList();  //升序排列的id数组

        float distance = 0;
        for(int i : idArrayList){
            CellData tempCellData = new CellData(dataSet.findData(i));
            distance+=tempCellData.pointToPoint(this.getCentroid());
        }

        this.setDetaC(distance*2/memberNum);
    }

}
