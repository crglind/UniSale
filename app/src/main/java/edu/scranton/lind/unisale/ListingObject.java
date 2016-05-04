package edu.scranton.lind.unisale;

public class ListingObject {
    private int uId;
    private int listNum;
    private String title;
    private String desc;
    private double price;
    private String category;
    private String condition;
    private int finished;
    private int school;

    public int getUID(){return uId;}
    public void setUID(int id){this.uId = id;}
    public int getListNum(){return listNum;}
    public void setListNum(int num){this.listNum = num;}
    public String getListTitle(){return title;}
    public void setListTitle(String newTitle){this.title = newTitle;}
    public String getDescription(){return desc;}
    public void setDescription(String descr){this.desc = descr;}
    public double getPrice(){return price;}
    public void setPrice(double newPrice){this.price = newPrice;}
    public String getCategory(){return category;}
    public void setCategory(String cat){this.category = cat;}
    public String getCondition(){return condition;}
    public void setCondition(String cond){this.condition = cond;}
    public int getFinished(){return finished;}
    public void setFinished(int finish){this.finished = finish;}
    public int getSchool(){return school;}
    public void setSchool(int id){this.school = id;}
}
