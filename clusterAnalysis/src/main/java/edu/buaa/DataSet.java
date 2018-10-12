package edu.buaa;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

//数据集     作为整个聚类分析中将被使用的数据集
public class DataSet {
    private Node head;//头结点
    private int size;//链表长度
    private float distanceEx;//数据集对象间的平均距离          只在抽样数据集中有效
    private float distanceDx;//数据集对象间的平均距离的标准差  只在抽样数据集中有效
  //  private HashMap<Integer,CellData> dataSetHashMap;

    private class Node{  //私有类 结点
        private CellData data;
        private int cellDataId;    //对象标识符
        private Node next;
        public Node(CellData element,int id,Node next){
            data = element;
            cellDataId=id;
            this.next = next;
        }
        public Node(CellData element,int id){
            data = element;
            cellDataId=id;
            this.next = null;
        }
    }

    //数据集初始化
    public DataSet(){
        head=null;
        size=0;
    }


    public DataSet(int id,CellData element){
        this();
        this.addNodeInHead(id,element);
    }

    //数据集是否为空
    public boolean isEmpty(){
        return size==0;
    }

    //返回数据集数据量的多少
    public int getSize(){
        return size;
    }

    //在链表的最前端加上一个元素      添加的是一个CellData元素   需要将float数组进行包装
    public void addNodeInHead(int id,CellData element){
        Node tempNode=head;
        head = new Node(element,id,null);//每个数据的对象标识符均来着于它进入数据集的顺序
        head.next=tempNode;
        size++;
    }

    //重写方法：  在链表的前端加上一个元素
    public void addNodeInHead(Node addNode){
        Node temp=head;
        head = addNode;
        head.next=temp;
        size++;
    }

    //求取数据集的平均值      注意：单独使用之前一定要检查数据集是否为空！！！！
    private float[] getAveage() throws Exception{
        float[] aveagefloat=new float[head.data.getLen()];
         //System.out.println(aveagefloat[head.data.getLen()-1]);
         if (size==0){
             //System.out.println("Error,DataSet is empty!");
             throw new Exception("Error,DataSet is empty! Aveage is inexistence!");
        }else {
            for(Node temp=head;temp!=null;temp=temp.next){
                float[] tempfloat=temp.data.getData();
                for (int i=0;i<head.data.getLen();i++){
                    aveagefloat[i]+=tempfloat[i];
                }
            }
            for (int i=0;i<head.data.getLen();i++){
                aveagefloat[i]/=size;
            }
        }
        return aveagefloat;
    }

    //求数据集的标准差   输入变量：数据集的平均值    注意：单独使用之前一定要检查数据集是否为空！！！！
    private float[] getStandardDev ( float[] aveagefloat) throws Exception{
        float[] devfloat = new float[head.data.getLen()];
        if (size == 0) {
            //System.out.println("Error,DataSet is empty!");
            throw new Exception("Error,DataSet is empty! Strandard Dev is inexistence!");
        } else {
            for (Node temp = head; temp != null; temp = temp.next) {
                float[] tempfloat = temp.data.getData();
                for (int i = 0; i < head.data.getLen(); i++) {
                    devfloat[i] += Math.pow((tempfloat[i]-aveagefloat[i]),2);
                }
            }
            for (int i=0;i<head.data.getLen();i++) {
                devfloat[i] /= (size - 1);
                devfloat[i] = (float) Math.sqrt(devfloat[i]);
            }
        }
        return devfloat;
    }

    //对数据集进行标准化    注意：调用之前一定要检查数据集是否为空！！！！
    public void normalizationDataSet(){
        try{
        float[] aveagefloat=this.getAveage();
        float[] devfloat=this.getStandardDev(aveagefloat);
            for (Node temp = head; temp != null; temp = temp.next) {
                float[] tempfloat = temp.data.getData();
                for (int i = 0; i < head.data.getLen(); i++) {
                    temp.data.setData((tempfloat[i]-aveagefloat[i])/devfloat[i],i);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error,DataSet is empty!");
            e.printStackTrace();
        }
    }

    //打印出链表中所有元素
    public void printDataSet(){
        System.out.println(size);//显示链表长度
        for (Node temp = head; temp != null; temp = temp.next) {
            System.out.println(temp.cellDataId);
            float[] tempfloat = temp.data.getData();
            for (int i = 0; i < head.data.getLen(); i++) {
                System.out.print(tempfloat[i]);//显示链表内各个数字
                System.out.print("  ");
            }
            System.out.println();
        }
    }

    //返回某个数据节点的对象标识符
    public int getCellDataId(Node note){
        return note.cellDataId;
    }

    //copy一个数据集，本质上数据集的各个元素并没有被重新创建，只是copy了数据集的起始位置   浅克隆
    public void copyDataSet(DataSet oldDataSet){
        this.head=oldDataSet.head;
        this.size=oldDataSet.size;
    }

    //copy一个数据集，此数据集是重新建立的新数据集    此方法的缺点是需要建立新的数据集时会出现链表的顺序相反的情况（先后顺序逆转）   深克隆
    public void copyToNewDataSet(DataSet oldDataSet){
        DataSet temp =new DataSet();    //浅克隆是有必要的  因为传进来的是指向原数据集引用的一份拷贝，对该拷贝引用进行的链表操作，依然会对原数据进行操作
        for ( temp.copyDataSet(oldDataSet);!temp.isNullHead();temp.head=temp.head.next){
            Node tempNode = new Node(new CellData(temp.head.data.getData().clone()),temp.head.cellDataId,null);
            this.addNodeInHead(tempNode);
        }
        this.distanceEx=oldDataSet.distanceEx;
        this.distanceDx=oldDataSet.distanceDx;
    }

    //迭代克隆节点链表数据
    @Nullable
    private Node cloneNodeList(Node headNode){
        if (headNode.next!=null){
            Node nextNode = cloneNodeList(headNode.next);
                return new Node(headNode.data.clone(),headNode.cellDataId,nextNode);
        }else {
                return new Node(headNode.data.clone(),headNode.cellDataId,null);
        }
    }

    /**
     * 深克隆本数据集
     * @return 返回本数据集的深克隆
     */
    public DataSet cloneDataSet(){
        DataSet tempDataSet = new DataSet();
        tempDataSet.head = this.cloneNodeList(this.head);
        tempDataSet.size = this.size;
        tempDataSet.distanceDx=this.distanceDx;
        tempDataSet.distanceEx=this.distanceEx;
        return tempDataSet;
    }

    //返回链表中首节点的数据
    public CellData readHeadNodeData(){
        return this.head.data;
    }

    //返回链表中首节点的id
    public int readHeadNodeId(){
        return this.head.cellDataId;
    }

    //将链表首指针指向下一个节点
    public void nextNode(){
        this.head=this.head.next;
        this.size--;
    }

    //判断头节点是否指向空（即数据是否已经读取完毕）
    public boolean isNullHead(){
        return this.head==null;
    }


    //从数据集中根据ID找到数据
    public float[] findData(int cellDataId){
        //copy一个数组
        DataSet tempDataSet = new DataSet();
        //根据id找到找到数据
        for ( tempDataSet.copyDataSet(this);!tempDataSet.isNullHead();tempDataSet.head=tempDataSet.head.next){
            if (tempDataSet.head.cellDataId==cellDataId)
                return tempDataSet.head.data.getData().clone();//返回一个clone
        }
        return null;
    }

    /**
     * 得到数据集对象间的平均距离以及标准差
     * 该函数将在按比例抽取数据集时调用
     * 已测试
     */
    public void getDataSetExDx(){   //暂时变为公有方法
        float ex=0;
        float dx=0;
        ArrayList<CellData> dataSet = new ArrayList<CellData>(); //数据集
        ArrayList<Float> distanceSet = new ArrayList<Float>();//两两数据间的距离
        Node tempDataSet = this.head;
        for (;tempDataSet!=null;tempDataSet=tempDataSet.next){
            dataSet.add(tempDataSet.data);
        }
        ////////////////求EX
        for (int i=0;i<dataSet.size();i++){
            for (int j=i+1;j<dataSet.size();j++){
                float distance=dataSet.get(i).pointToPoint(dataSet.get(j));//需要添加点到点的距离
                ex+=distance;
                distanceSet.add(distance);
            }
        }
        ex/=distanceSet.size();
        ////////////////求DX
        for (int i=0;i<distanceSet.size();i++){
            dx+=Math.pow((distanceSet.get(i)-ex),2);
        }
        dx=(float) Math.sqrt(dx/(distanceSet.size()-1));

        this.distanceEx=ex;
        this.distanceDx=dx;
    }

    /**
     * 根据beta值得到半径R
     * @param beta
     * @return  半径R
     */
    public float getR(float beta) throws Exception{
        if (this.distanceEx==0&&this.distanceDx==0)
        throw new Exception("该数据集没有进行DX与EX的计算");
        else {
            return this.distanceEx+beta*this.distanceDx;
        }
    }

    /**
     * 按照输入的比例对数据集进行等距采样，得到新的数据集。新的数据集已经完成了聚类分析之前所有的预处理
     * @param ratio  采样比例
     * @return  新数据集   (新数据集的链表顺序与原始数据集的链表顺序相反)
     * 注意：调用该函数前，原始数据集已经进行了标准化。
     * 未测试
     */
    public DataSet SystematicSampling(float ratio){
        int len = (int) (1/ratio);
        DataSet newDataSet = new DataSet();
        Node temp = this.head;
        for (int id =0;temp!=null;temp=temp.next,id++){
            if (id%len==0){
                newDataSet.addNodeInHead(temp.cellDataId,temp.data.clone());
            }
        }
        newDataSet.getDataSetExDx();
        return newDataSet;
    }


}
