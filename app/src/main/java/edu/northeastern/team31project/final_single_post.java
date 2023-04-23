package edu.northeastern.team31project;

import java.util.Date;

public class final_single_post {
    int postType;
    String posterUID;
    String postImage;
    Date postdate;

    public final_single_post() {
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public String getPosterUID() {
        return posterUID;
    }

    public void setPosterUID(String posterUID) {
        this.posterUID = posterUID;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public Date getPostdate() {
        return postdate;
    }

    public void setPostdate(Date postdate) {
        this.postdate = postdate;
    }

    @Override
    public String toString() {
        return "final_single_post{" +
                "postType=" + postType +
                ", posterUID='" + posterUID + '\'' +
                ", postImage='" + postImage + '\'' +
                ", postdate=" + postdate +
                '}';
    }
}
