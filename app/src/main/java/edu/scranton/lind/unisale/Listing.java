package edu.scranton.lind.unisale;

public class Listing {

    private int uID;
    private int listNum;
    private String title;
    private String desc;
    private double price;
    private String category;
    private String condition;
    private int finished;
    private int schoolID;

    public void setUID(int id){this.uID = id;}
    public int getUID(){return uID;}
    public void setListNum(int num){this.listNum = num;}
    public int getListNum(){return listNum;}
    public void setListTitle(String string){this.title = string;}
    public String getListTitle(){return title;}
    public void setDescription(String string){this.desc = string;}
    public String getDescription(){return desc;}
    public void setPrice(double num){this.price = num;}
    public double getPrice(){return price;}
    public void setCategory(String string){this.category = string;}
    public String getCategory(){return category;}
    public void setCondition(String string){this.condition = string;}
    public String getCondition(){return condition;}
    public void setFinished(int num){this.finished = num;}
    public int getFinished(){return finished;}
    public void setSchoolID(int id){this.schoolID = id;}
    public int getSchoolID(){return schoolID;}
}
