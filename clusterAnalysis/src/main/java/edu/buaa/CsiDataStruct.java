package edu.buaa;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Collectors;

//聚类结构体链表
public class CsiDataStruct {
    private CsiNode csiClusterHead;
    private float daviesBouldin;
    private float beta;
    private float r;//半径
    private int size;//CsiNode的个数
    private class CsiNode{
        private CsiData csiData;
        private CsiNode next;//指向下一个数据的指针
        public CsiNode(){
            this.csiData=null;
            this.next=null;
        }
        public CsiNode(CsiData csiData){
            this();
            this.csiData=csiData;
        }
        public CsiNode(CsiData csiData,CsiNode nextCsiNode){
            this.csiData=csiData;
            this.next=nextCsiNode;
        }
    }

    public CsiDataStruct(){
        this.beta=0;
        this.csiClusterHead=null;
        this.daviesBouldin=0;
        this.size=0;
    }

    public CsiDataStruct(CsiData csiData){
        this.beta=0;
        this.daviesBouldin=0;
        this.csiClusterHead.csiData=csiData;
        this.csiClusterHead.next=null;
        this.size=0;
    }

    /**
     * 向Csi簇中添加一个CSI结构体   输入的参数为CSI结构体
     * 已测试
     */
    public void addCsiData(CsiData csiData){
        CsiNode tempCsiNode =this.csiClusterHead;
        this.csiClusterHead=new CsiNode(csiData);
        this.csiClusterHead.next=tempCsiNode;
        this.size++;
    }

    /**
     *深克隆CsiNode链表
     * @return CsiNode的链表
     * 已测试
     */
    private CsiNode cloneCsiNodeList(CsiNode headNode){
        if (headNode.next==null){
            return new CsiNode(headNode.csiData.cloneCsiData(),null);
        }else{
            CsiNode tempCsi=cloneCsiNodeList(headNode.next);
            return new CsiNode(headNode.csiData,tempCsi);
        }
    }

    /**
     * 深克隆CsiDataStruct
     * @return CsiDataStruct的深克隆
     * 已测试
     */
    public CsiDataStruct cloneCsiCluster(){
        CsiDataStruct tempCsiDataStruct = new CsiDataStruct();
        tempCsiDataStruct.beta=this.beta;
        tempCsiDataStruct.daviesBouldin=this.daviesBouldin;
        tempCsiDataStruct.csiClusterHead=this.cloneCsiNodeList(this.csiClusterHead);
        return tempCsiDataStruct;
    }



    /**
     * 将点加入到簇内，或新创建一个簇
     * @param cellDataId 点的ID
     * @param point    点
     * @param r 簇半径
     * 已测试
     */
    public void putPointToCluster(int cellDataId,CellData point,float r){
        CsiNode head= this.csiClusterHead;
        if(this.csiClusterHead==null){
            this.addCsiData(new CsiData(cellDataId,point));
        }else {
            ArrayList<Float> distance = new ArrayList<Float>();
            for (; head != null; head = head.next) {//得到点与每个簇的距离
                distance.add(head.csiData.getDisPointToCsiData(point));
            }
            float a = findMinDistance(distance);
            if (a > r) {//如果最大簇距离大于半径，建立一个新的簇
                this.addCsiData(new CsiData(cellDataId, point));
            } else {//将加入距离最短的簇
                CsiNode newHead = this.csiClusterHead;
                for (int i = 0; i < distance.indexOf(a); i++) {
                    newHead = newHead.next;
                }
                newHead.csiData.addOneData(cellDataId, point);//已更新质心
            }
        }
    }

    /**
     * 找到ArrayList中最小的distance
     * @param distance   distance ArrayList
     * @return 最小距离
     * 已测试
     */
    private float findMinDistance(ArrayList<Float> distance){
        float min = distance.get(0);
        for(int i=0;i<distance.size();i++){
            if (min>distance.get(i)){
                min=distance.get(i);
            }
        }
        return min;
    }

    /**
     * 计算聚类结构体中数据的总数量
     * @return 数据总数量
     * 已测试
     */
    public int csiDataSum(){
        int SumNum = 0;
        for (CsiNode tempNode = this.csiClusterHead;tempNode!=null;tempNode=tempNode.next){
            SumNum += tempNode.csiData.getMemberNum();
        }
        return SumNum;
    }


    /**
     * 计算聚类结构体内所有簇的OF
     * 已测试
     */
    public void calculateOF(){
        int SumNum = this.csiDataSum();//得到数据总数
        for (CsiNode aNode = this.csiClusterHead;aNode!=null;aNode=aNode.next){//计算各簇的OF(离群因子)
            float outlierFactor=0;
            for (CsiNode bNode = this.csiClusterHead;bNode!=null;bNode=bNode.next){
                outlierFactor +=bNode.csiData.getMemberNum()*aNode.csiData.getDisCsiDataToCsiData(bNode.csiData);   //B簇的数目乘以AB簇之间的距离
            }
            aNode.csiData.setOutlierFactor(outlierFactor/SumNum);//除以总数目以后得到真正的OF
        }
    }

    /**
     * 按照比例判断各个簇是否为异常簇，并将异常簇置位
     * @param ratio 异常点数比例
     * 已测试
     */
    public void setCsiDataAbNormal(float ratio){//离群因子越大，则偏离数据集整体越远，越有可能是异常簇
        int abnormalSumNum = (int) (this.csiDataSum() * ratio);//按照比例得到异常数据的总数
        ArrayList<Integer> arr = new ArrayList<Integer>();//取得簇内数据的数目
        ArrayList<Float> of = new ArrayList<Float>();//离群因子
        ArrayList<Float> ofSort = new ArrayList<Float>(); //离群因子排序
        for (CsiNode aNode = this.csiClusterHead;aNode!=null;aNode=aNode.next) {//得到每个簇的离群因子
            float temp = aNode.csiData.getOutlierFactor();
            arr.add(aNode.csiData.getMemberNum());
            of.add(temp);
            ofSort.add(temp);
        }
        Comparator c = new Comparator<Float>() {  //重写Comparator   降序排列
            @Override
            public int compare(Float o1, Float o2) {
                if (o1<o2){
                    return 1;  // 1进入源码中表示o1所处的位置Ascending(向数组后部移动)
                }else
                return -1; //-1进入源码中表示o1所处的位置Descending（向数组前部移动）
            }
        };
        ofSort.sort(c);//对ofSort降序排列
        int abnormalNum=0;
        for (int i =0;
                (abnormalNum<abnormalSumNum)&&(i<ofSort.size());
                            abnormalNum+=arr.get(of.indexOf(ofSort.get(i++)))){//找到异常簇
            if(arr.get(of.indexOf(ofSort.get(i)))>abnormalSumNum*3){
                break;
            }
              findCsiNode(of.indexOf(ofSort.get(i))).csiData.setAbnormalSign(true);//将异常簇置位
                //of.indexOf(ofSort.get(i++))表示csiNode在链表中所处的位置
        }
       // System.out.println("");
    }

    /**
     * 根据csiNode在链表中的位置，找到该csiNode
     * @param index 位置     index从0开始
     * @return csiNode的引用
     * 已测试
     */
    @Contract(pure = true)
    private CsiNode findCsiNode(int index){
        CsiNode head = this.csiClusterHead;
        for (int i =0; i<index;i++){
            head=head.next;
        }
        return head;
    }



    /**
     * 计算簇内距离detaC （4.15） 簇内距离是簇内所有对象与质心的平均距离的两倍
     * @param dataSet 簇采用的数据集(根据id找到原始数据)
     * 已测试
     */
    public void setDetaCAll(DataSet dataSet){
        CsiNode tempNode = this.csiClusterHead;
        float detaC=0;
        for (;tempNode!=null;tempNode=tempNode.next){
//            int memberNum=tempNode.csiData.getMemberNum();
//            if (memberNum==0)
//                continue;
//            ArrayList<Integer> idArrayList = tempNode.csiData.getMemerIdArrayList();  //升序排列的id数组
//            if (idArrayList.size()==0){
//                continue;
//            }
//
//            float distance = 0;
//            for(int i : idArrayList){
//               CellData tempCellData = new CellData(dataSet.findData(i));
//               distance+=tempCellData.pointToPoint(tempNode.csiData.getCentroid());
//            }
//            tempNode.csiData.setDetaC(distance*2/memberNum);
            tempNode.csiData.setDetaC(dataSet);
        }
    }


    /**
     * 快速排序
     * @param arr 数据集的引用
     * @param head 数据集的头
     * @param tail 数据集的尾
     *未测试
     */
    private void quickSort(float[] arr,int head,int tail){//快速排序
        int i =head;
        int j =tail;
        float key =arr[i];
        while (i<j){
            while (i<j&&arr[j]>key){
                j--;
            }
            arr[i]=arr[j];//先将小于key的数据换到中间变量上去
            while(i<j&&arr[i]<key){
                i++;
            }
            arr[j]=arr[i];//然后将大于key的数据从前面换到原先找到的后面去
        }//跳出时，i=j
        arr[i]=key;//将中间变量换为key
        this.quickSort(arr,head,i);
        this.quickSort(arr,j,tail);
    }

    /**
     * 一次分簇后，聚类结构体中簇的个数
     * @return 簇的个数
     * 未测试
     */
    public int csiDataLength(){
        CsiNode head = this.csiClusterHead;
        int length;
        for (length=0;head!=null;head=head.next){
            length++;
        }
        return length;
    }


    public float getDaviesBouldin(){
        return this.daviesBouldin;
    }

    /**
     * 设置DB指数，前提的必须有各个CsiData的簇内距离(detaC)
     * 已测试
     */
    public void setDaviesBouldin(){
        int k = this.size;
        float sum =0;
        CsiNode head = this.csiClusterHead;
        ArrayList<CsiData> csiDataArrayList = new ArrayList<CsiData>();
        for (;head!=null;head=head.next){
            csiDataArrayList.add(head.csiData);
        }
        for (CsiData aCsiData:csiDataArrayList){
            float max=0;
            for (CsiData bCsiData:csiDataArrayList){
                if (bCsiData.equals(aCsiData))
                    continue;
                float ac = (aCsiData.getDetaC()+bCsiData.getDetaC())/aCsiData.getDisCsiDataToCsiData(bCsiData);
                if (ac>max)
                    max=ac;
            }
            sum+=max;
        }
        this.daviesBouldin=sum/k;
    }

    /**
     * 得到错误数据的ID   该函数在离散点检测部分被调用  异常簇与分格用0表示
     * @param dataSet  总数据集
     * @param r        半径r
     * @param ratio    错误点数比例
     * @return  错误点的id  ArrayList
     * 已测试
     */
    public ArrayList<Integer> getAbnormalDataId(DataSet dataSet,float r,float ratio){
        ArrayList<Integer> idArrayList =new ArrayList<Integer>();
        DataSet tempDataSet=dataSet.cloneDataSet();
        for (;!tempDataSet.isNullHead();tempDataSet.nextNode()){
            this.putPointToCluster(tempDataSet.readHeadNodeId(),tempDataSet.readHeadNodeData(),r);
        }
        this.setDetaCAll(dataSet.cloneDataSet());//计算簇内所有CsiData的簇内距离DetaC
        this.setDaviesBouldin();//设置簇内的DB指数
        this.calculateOF();
        this.setR(r);//设置r

        this.setCsiDataAbNormal(ratio);
        for (CsiNode tempNode = this.csiClusterHead;tempNode!=null;tempNode=tempNode.next){
            if (tempNode.csiData.getAbnomalSign()){
                CsiData tempCsiData = tempNode.csiData.cloneCsiData();
                for (;!tempCsiData.isNullMemberHead();tempCsiData.nextMemberHead()){
                    idArrayList.add(tempCsiData.getHeadMemberId());
                }
                idArrayList.add(0);
            }
        }

        return idArrayList;
    }


    public float getBeta(){
        return this.beta;
    }

    public void setBeta(float beta){
        this.beta=beta;
    }

    public void setR(float r){
        this.r=r;
    }

    public float getR(){
        return this.r;
    }

}
