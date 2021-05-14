package Notifications;

public class Notification
{
    String date,submitby,title,description;

    Notification(){
    }
    public Notification(String date,String submitby,String title,String description){
        this.date=date;
        this.description=description;
        this.submitby=submitby;
        this.title=title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubmitby() {
        return submitby;
    }

    public void setSubmitby(String submitby) {
        this.submitby = submitby;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
