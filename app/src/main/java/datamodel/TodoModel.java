package datamodel;

public class TodoModel {

    String tid;
    String title;
    String tdate;
    String tdeadline;
    String tdesc;
    String tgb;
    String tstatus;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTdate() {
        return tdate;
    }

    public void setTdate(String tdate) {
        this.tdate = tdate;
    }

    public String getTdeadline() {
        return tdeadline;
    }

    public void setTdeadline(String tdeadline) {
        this.tdeadline = tdeadline;
    }

    public String getTdesc() {
        return tdesc;
    }

    public void setTdesc(String tdesc) {
        this.tdesc = tdesc;
    }

    public String getTgb() {
        return tgb;
    }

    public void setTgb(String tgb) {
        this.tgb = tgb;
    }

    public String getTstatus() {
        return tstatus;
    }

    public void setTstatus(String tstatus) {
        this.tstatus = tstatus;
    }
}
